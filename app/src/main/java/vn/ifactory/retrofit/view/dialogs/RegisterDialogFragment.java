package vn.ifactory.retrofit.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.ifactory.retrofit.AppConfig;
import vn.ifactory.retrofit.R;
import vn.ifactory.retrofit.model.Users;
import vn.ifactory.retrofit.utils.Util;

/**
 * Created by PC on 10/22/2018.
 */

public class RegisterDialogFragment extends DialogFragment implements View.OnClickListener{
    public static final String TAG = RegisterDialogFragment.class.getSimpleName();
    WeakReference<Context> contextRef;

    View rootView;
    EditText edtUsername, edtPassword, edtAddress, edtFullName;
    Button btnRegister;
    ICallbackFragment mCallback;
    IRegisterSuccess mListener;

    public void setCallBackFragment(ICallbackFragment callBack) {
        mCallback = callBack;
    }

    public interface IRegisterSuccess {
        void onRegisterSuccess(Users users);
    }

    public void setIRegisterSuccess(IRegisterSuccess registerListener) {
        mListener = registerListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.app.DialogFragment.STYLE_NORMAL,android.R.style.Theme_Holo_Light_Dialog);
        //setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Light_Dialog);
        //setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Dialog);
        //setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Light_Dialog);
        //setStyle(DialogFragment.STYLE_NORMAL,android.R.style.Theme_Holo_Light);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle("Register");
        rootView = inflater.inflate(R.layout.fragment_dialog_register, container, false);
        addControls();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contextRef = new WeakReference<Context>(getActivity());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog= super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Register");
        return dialog;
    }

    private void addControls() {
        edtUsername = rootView.findViewById(R.id.edtUserName);
        edtPassword = rootView.findViewById(R.id.edtPassword);
        edtAddress = rootView.findViewById(R.id.edtAddress);
        edtFullName = rootView.findViewById(R.id.edtFullName);
        btnRegister = rootView.findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:

                registerUsers();
                break;
                default:break;
        }
    }

    private void registerUsers() {
        String userName = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        String address = edtAddress.getText().toString();
        String fullName = edtFullName.getText().toString();

        if (Util.isEmptyString(userName) || Util.isEmptyString(password) || Util.isEmptyString(fullName)) {
            //Toast.makeText(getActivity(), "Some text box empty", Toast.LENGTH_SHORT).show();
            if (mCallback != null) {
                mCallback.showToast("Some text box empty");
            }
            return;
        }
        if (mCallback != null) {
            mCallback.showLoadingDialog(true);
        }
        final Users users = new Users();
        users.setUserName(userName);
        users.setPassword(password);
        users.setAddress(address);
        users.setFullName(fullName);

        if (Util.isAppOnLine(contextRef.get())) {
            AppConfig.getApiServiceProvider().registerUser(users).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (mCallback != null) {
                        mCallback.showLoadingDialog(false);
                    }

                    if (response.body() != null && mListener != null) {
                        mListener.onRegisterSuccess(users);
                    }

                    dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    if (mCallback != null) {
                        mCallback.showLoadingDialog(false);
                        mCallback.showToast(t.getMessage());
                    }

                }
            });
        }else {
            if (mCallback != null) {
                mCallback.showToast("Network problem. Check again");
            }
        }

    }
}
