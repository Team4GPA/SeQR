package com.example.seqr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

/**
 * A fragment for editing profiles in the admin dashboard.
 */
public class AEditProfileFragment extends Fragment implements DeleteItemFragment.ConfirmationDialogListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout of fragment and create buttons
        View view =  inflater.inflate(R.layout.fragment_a_edit_profile, container, false);
        Button deleteProfileButton = view.findViewById(R.id.admin_delete_profile_button);
        Button backButton = view.findViewById(R.id.admin_edit_profile_back_button);

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

    /**
     * Shows the confirmation dialog for profile deletion.
     */
    private void showConfirmationDialog() {
        DeleteItemFragment dialogFragment = new DeleteItemFragment();
        dialogFragment.show(getParentFragmentManager(), "ConfirmationDialogFragment");
    }

    @Override
    public void onYesClicked() {
        // Handle deletion
    }

    @Override
    public void onNoClicked() {
        // Dismiss dialog
        Fragment dialogFragment = getParentFragmentManager().findFragmentByTag("ConfirmationDialogFragment");
        if (dialogFragment != null) {
            getParentFragmentManager().beginTransaction().remove(dialogFragment).commit();
        }
    }
}
