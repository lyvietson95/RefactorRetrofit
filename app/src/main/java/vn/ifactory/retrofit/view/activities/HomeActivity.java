package vn.ifactory.retrofit.view.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.ifactory.retrofit.AppConfig;
import vn.ifactory.retrofit.R;
import vn.ifactory.retrofit.adapter.TodoItemAdapter;
import vn.ifactory.retrofit.model.ToDo;
import vn.ifactory.retrofit.utils.IntentUtils;
import vn.ifactory.retrofit.utils.Util;

public class HomeActivity extends BaseActivity implements View.OnClickListener, TodoItemAdapter.IOnItemListener {
    public static final String TAG = HomeActivity.class.getSimpleName();
    private static int sUserId = 0;
    RecyclerView rvTodo;
    private List<ToDo> mListTodo;
    TodoItemAdapter adapterTodo;

    EditText edtTodoName, edtDescription;
    Button btnTodo, btnClear;

    private int mPositionTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addControls();
    }

    private void addControls() {
        Intent intent = getIntent();
        sUserId = intent.getIntExtra(IntentUtils.USER_ID, 0);

        rvTodo = findViewById(R.id.rvToDo);
        mListTodo = new ArrayList<>();
        adapterTodo = new TodoItemAdapter(this, mListTodo);
        adapterTodo.setItemListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvTodo.setLayoutManager(layoutManager);
        rvTodo.setAdapter(adapterTodo);

        edtTodoName = findViewById(R.id.edtTodoName);
        edtDescription = findViewById(R.id.edtDescription);
        btnTodo = findViewById(R.id.btnTodo);

        btnClear = findViewById(R.id.btnClear);

        btnClear.setOnClickListener(this);
        btnTodo.setOnClickListener(this);

        getTodoByUser(sUserId);
    }

    private void getTodoByUser(int userId) {
        showBusyDialog("Loading...");
        if (Util.isAppOnLine(this)) {
            AppConfig.getApiServiceProvider().getTodoByUser(AppConfig.getInstance().getSessionToken(), userId).enqueue(new Callback<List<ToDo>>() {
                @Override
                public void onResponse(@NonNull Call<List<ToDo>> call, @NonNull Response<List<ToDo>> response) {
                    dismissBusyDialog();
                    List<ToDo> listTodo = response.body();
                    if (listTodo != null && listTodo.size() > 0) {
                        mListTodo.clear();
                        mListTodo.addAll(listTodo);
                        adapterTodo.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<ToDo>> call, @NonNull Throwable t) {
                    toastMessage(t.getMessage(), Toast.LENGTH_SHORT);
                    dismissBusyDialog();
                }
            });
        }else {
            toastMessage("Network problem. Check again", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTodo:
                if (btnTodo.getText().toString().equals("Add")) {
                    addTodo();
                } else if (btnTodo.getText().toString().equals("Modify")) {
                    modifyTodo();
                }

                break;
            case  R.id.btnClear:
                edtTodoName.setText("");
                edtDescription.setText("");
                btnTodo.setText("Add");
                break;
                default:break;
        }
    }

    private void modifyTodo() {
        final String todoName = edtTodoName.getText().toString();
        final String description = edtDescription.getText().toString();
        if (Util.isEmptyString(todoName) || Util.isEmptyString(description)){
            toastMessage("Empty text", Toast.LENGTH_SHORT);
            return;
        }

        showBusyDialog("Modifying...");
        if (Util.isAppOnLine(this)) {
            int todoId = mListTodo.get(mPositionTodo).getTodoId();
            AppConfig.getApiServiceProvider().modifyTodoItem(AppConfig.getInstance().getSessionToken(), todoId, todoName, description).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    dismissBusyDialog();
                    if (response.code() == 200) {
                        toastMessage("Successful", Toast.LENGTH_SHORT);

                        mListTodo.get(mPositionTodo).setName(todoName);
                        mListTodo.get(mPositionTodo).setDescription(description);
                        adapterTodo.notifyItemChanged(mPositionTodo);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    dismissBusyDialog();
                    toastMessage(t.getMessage(), Toast.LENGTH_SHORT);
                }
            });
        }else {
            toastMessage("Network problem. Check again", Toast.LENGTH_SHORT);
        }
    }

    private void addTodo() {
        String todoName = edtTodoName.getText().toString();
        String description = edtDescription.getText().toString();
        if (Util.isEmptyString(todoName) || Util.isEmptyString(description)){
            toastMessage("Empty text", Toast.LENGTH_SHORT);
            return;
        }

        showBusyDialog("Adding...");

        if (Util.isAppOnLine(this)) {
            AppConfig.getApiServiceProvider().addTodoItem(AppConfig.getInstance().getSessionToken(), todoName, description, sUserId).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    dismissBusyDialog();
                    if (response.code() == 200) {
                        toastMessage("Add Todo Successful", Toast.LENGTH_SHORT);

                        getTodoByUser(sUserId);
                    }else {
                        toastMessage("Add Todo problem, check again", Toast.LENGTH_SHORT);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    dismissBusyDialog();
                    toastMessage(t.getMessage(), Toast.LENGTH_SHORT);
                }
            });
        }else {
            toastMessage("Network problem. Check again", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onItemClick(int position) {
        mPositionTodo = position;
        ToDo toDo = mListTodo.get(position);
        btnTodo.setText("Modify");
        edtTodoName.setText(toDo.getName());
        edtDescription.setText(toDo.getDescription());
    }

    @Override
    public void onDeleteTodo(final int pos) {
        new AlertDialog.Builder(this)
                .setTitle("Remove Todo")
                .setMessage("Are you sure remove this Todo?")
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeTodo(pos);
                        dialog.dismiss();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void removeTodo(final int pos) {
        if (Util.isAppOnLine(this)) {
            int todoId = mListTodo.get(pos).getTodoId();
            String name = mListTodo.get(pos).getName();
            String des = mListTodo.get(pos).getDescription();

            AppConfig.getApiServiceProvider().deleteTodoItem(AppConfig.getInstance().getSessionToken(), todoId).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    dismissBusyDialog();
                    if (response.code() == 200) {
                        toastMessage("Successful", Toast.LENGTH_SHORT);

                        mListTodo.remove(pos);
                        adapterTodo.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    dismissBusyDialog();
                    toastMessage(t.getMessage(), Toast.LENGTH_SHORT);
                }
            });
        }else {
            toastMessage("Network problem. Check again", Toast.LENGTH_SHORT);
        }
    }
}
