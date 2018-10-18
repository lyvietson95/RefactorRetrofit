package vn.ifactory.retrofit.view.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import vn.ifactory.retrofit.view.dialogs.CustomProgressDialog;

/**
 * Created by PC on 10/18/2018.
 */

public class BaseActivity extends AppCompatActivity {
    DialogFragment dialogFragment;

    protected void showBusyDialog(String message){
        if(dialogFragment!=null && dialogFragment.isVisible()){
            dialogFragment.dismiss();
        }
        dialogFragment = new CustomProgressDialog();
        Bundle bundle = new Bundle();
        bundle.putString("message",message);
        dialogFragment.setArguments(bundle);
        dialogFragment.setCancelable(false);
        dialogFragment.show(getSupportFragmentManager(),"busyDialog");
    }

    protected  void dismissBusyDialog(){
        if(dialogFragment.isVisible()){
            dialogFragment.dismiss();
        }
    }

    protected void toastMessage(String message, int toastTiming){
        Toast.makeText(this,message,toastTiming).show();
    }
}
