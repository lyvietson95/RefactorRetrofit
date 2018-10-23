package vn.ifactory.retrofit.view.activities;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.ifactory.retrofit.AppConfig;
import vn.ifactory.retrofit.R;
import vn.ifactory.retrofit.model.TokenReponse;
import vn.ifactory.retrofit.model.TokenRequest;
import vn.ifactory.retrofit.model.Users;
import vn.ifactory.retrofit.utils.IntentUtils;
import vn.ifactory.retrofit.utils.Util;
import vn.ifactory.retrofit.view.dialogs.ICallbackFragment;
import vn.ifactory.retrofit.view.dialogs.RegisterDialogFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener, ICallbackFragment, RegisterDialogFragment.IRegisterSuccess{
    public static final String GRANT_TYPE = "password";
    public static final String TAG = MainActivity.class.getSimpleName();

    EditText edtUsername, edtPassword;
    Button btnLogin;
    TextView titleRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
    }

    private void addControls() {
        edtUsername = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        titleRegister = findViewById(R.id.titleRegister);

        btnLogin.setOnClickListener(this);
        titleRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                handleLogin();
                break;
            case R.id.titleRegister:
                handleRegister();
                break;
                default:break;
        }
    }

    private void handleRegister() {
        DialogFragment dialogFragment = new RegisterDialogFragment();
        ((RegisterDialogFragment)dialogFragment).setCallBackFragment(this);
        ((RegisterDialogFragment)dialogFragment).setIRegisterSuccess(this);
        dialogFragment.show(getSupportFragmentManager(),"register");
    }

    private void handleLogin() {
        final String userName = edtUsername.getText().toString();
        final String password = edtPassword.getText().toString();
        if (userName.isEmpty() || password.isEmpty()) {
            toastMessage("Input username or password", Toast.LENGTH_SHORT);
            return;
        }

        if (Util.isAppOnLine(this)){
            showBusyDialog("Logging In");

            TokenRequest request = new TokenRequest();
            request.setUserName(userName);
            request.setPassword(password);
            request.setGrantType(GRANT_TYPE);

            AppConfig.getApiServiceProvider().getToken(userName, password, GRANT_TYPE).enqueue(new Callback<TokenReponse>() {
                @Override
                public void onResponse(@NonNull Call<TokenReponse> call, @NonNull Response<TokenReponse> response) {
                    //dismissBusyDialog();
                    TokenReponse dataResponse = response.body();
                    if (dataResponse != null) {
                        Log.d(TAG, "Token: " + dataResponse.getToken());
                        AppConfig.getInstance().saveSessionToken(dataResponse.getToken());

                        getUserInfo(AppConfig.getInstance().getSessionToken(), userName, password);
                    }else {
                        dismissBusyDialog();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<TokenReponse> call, @NonNull Throwable t) {
                    dismissBusyDialog();
                    Log.d(TAG, t.getMessage());
                }
            });
        }else {
            toastMessage("Network problem. Check again", Toast.LENGTH_SHORT);
        }
    }

    private void getUserInfo(String token, String userName, String password) {
        AppConfig.getApiServiceProvider().getUserInfo(token, userName, password).enqueue(new Callback<Users>() {
            @Override
            public void onResponse(@NonNull Call<Users> call, @NonNull Response<Users> response) {
                dismissBusyDialog();
                if (response.body() != null) {
                    AppConfig.getInstance().saveFullUser(response.body());
                    openHomeActivity(response.body().getUserId());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Users> call, @NonNull Throwable t) {
                dismissBusyDialog();
                Log.d(TAG, t.getMessage());

            }
        });
    }

    private void openHomeActivity(int userId) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(IntentUtils.USER_ID, userId);
        startActivity(intent);
    }

    @Override
    public void showLoadingDialog(boolean isShow) {
        if (isShow) {
            showBusyDialog("Loading...");
        }else {
            dismissBusyDialog();
        }

    }

    @Override
    public void showToast(String msg) {
        toastMessage(msg, Toast.LENGTH_SHORT);
    }

    @Override
    public void onRegisterSuccess(Users users) {
        Log.d(TAG, "Register User success");
        edtUsername.setText(users.getUserName());
    }
}
