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

/**
 * A dialog fragment to get a password when entering admin portion of app
 */
public class EnterPasswordFragment extends DialogFragment {

    EditText passwordText;
    Button submitButton;
    Button cancelButton;
    String password;

    /**
     * Creates a view and associated logic for the dialog fragment to take the users password
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return returns a View type object which is the dialog fragment itself
     */
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
