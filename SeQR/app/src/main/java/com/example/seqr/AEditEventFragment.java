package com.example.seqr;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;


public class AEditEventFragment extends Fragment implements DeleteItemFragment.ConfirmationDialogListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout of fragment and create buttons
        View view =  inflater.inflate(R.layout.fragment_a_edit_event, container, false);
        Button deleteProfileButton = view.findViewById(R.id.admin_event_edit_delete_button);
        Button backButton = view.findViewById(R.id.admin_edit_event_back_button);

        // Handle pressing back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        // Handle pressing delete button

        deleteProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });



        return view;
    }
    private void showConfirmationDialog() {
        DeleteItemFragment dialogFragment = new DeleteItemFragment();
        dialogFragment.show(getParentFragmentManager(), "ConfirmationDialogFragment");
    }

    @Override
    public void onYesClicked(View v) {
        // Handle deletion
    }

    @Override
    public void onNoClicked(View v) {
        // Dismiss dialog
        Fragment dialogFragment = getParentFragmentManager().findFragmentByTag("ConfirmationDialogFragment");
        if (dialogFragment != null) {
            getParentFragmentManager().beginTransaction().remove(dialogFragment).commit();
        }
    }
}

