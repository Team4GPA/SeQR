package com.example.seqr;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A fragment for editing events in the admin dashboard.
 */
public class AEditEventFragment extends Fragment implements DeleteItemFragment.ConfirmationDialogListener {
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
    /**
     * Show confirmation dialog for deletion.
     */
    private void showConfirmationDialog() {
        DeleteItemFragment dialogFragment = new DeleteItemFragment();
        dialogFragment.show(getParentFragmentManager(), "ConfirmationDialogFragment");
    }

    /**
     * Handle when you confirm to delete an item
     *
     * @param v the view from the fragment so you can find the delete button
     */
    @Override
    public void onYesClicked(View v) {
        // Handle deletion
    }

    /**
     * Handle when you cancel deletion of an item
     *
     * @param v the view from the fragment so you can find the cancel deletion button
     */
    @Override
    public void onNoClicked(View v) {
        // Dismiss dialog
        Fragment dialogFragment = getParentFragmentManager().findFragmentByTag("ConfirmationDialogFragment");
        if (dialogFragment != null) {
            getParentFragmentManager().beginTransaction().remove(dialogFragment).commit();
        }
    }
}

