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
public class AEditProfileFragment extends Fragment{
    /**
     * Creates a view and associated logic for the view and returns it to whoever built the fragment
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return view
     */
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
        DeleteEventFragment dialogFragment = new DeleteEventFragment();
        dialogFragment.show(getChildFragmentManager(), "ConfirmationDialogFragment");
    }

}
