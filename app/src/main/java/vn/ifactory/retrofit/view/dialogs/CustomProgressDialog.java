package vn.ifactory.retrofit.view.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import vn.ifactory.retrofit.R;

/**
 * Created by PC on 10/18/2018.
 */

public class CustomProgressDialog extends DialogFragment {
    WeakReference<Context> contextReference;

    View rootView;

    String message;

    TextView textViewMessage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.custom_progress_dialog_fragment, container, false);
        initUI();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initUI() {
        textViewMessage = (TextView)rootView.findViewById(R.id.textViewMessage);
        message = getArguments().getString("message","Loading");
        textViewMessage.setText(message);

    }
}
