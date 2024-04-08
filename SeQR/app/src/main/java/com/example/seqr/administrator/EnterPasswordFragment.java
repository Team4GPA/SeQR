package com.example.seqr.administrator;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.seqr.R;
import com.example.seqr.controllers.EventController;

public class EnterPasswordFragment extends DialogFragment {

    EditText passwordText;
    Button submitButton;
    Button cancelButton;
    String password;
    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enter_password, container, false);

        passwordText = view.findViewById(R.id.password_edit_text);
        submitButton = view.findViewById(R.id.enter_password_button);
        cancelButton = view.findViewById(R.id.cancel_password_button);


        FragmentManager fragmentManager = getParentFragmentManager();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = passwordText.getText().toString();
                Log.d("DEBUG","The password: " +password);
                if (password.equals("1234")){
                    AdminFragment adminFragment = new AdminFragment();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment_container, adminFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    dismiss();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }
}
