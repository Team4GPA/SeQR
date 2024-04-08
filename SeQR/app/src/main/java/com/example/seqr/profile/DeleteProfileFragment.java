package com.example.seqr.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.seqr.R;
import com.example.seqr.controllers.ProfileController;

/**
 * Fragment for confirming profile deletion.
 */
public class DeleteProfileFragment extends DialogFragment {
    /**
     * Inflates the layout for the dialog fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The View for the fragment's UI.
     */
    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_confirm_delete, container, false);

        Bundle bundle = getArguments();
        assert bundle != null;
        String profileID = bundle.getString("id","");

        view.findViewById(R.id.delete_item_confirm_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ProfileController profileController = new ProfileController();
                profileController.deleteProfile(profileID);
                dismiss();
            }
        });

        view.findViewById(R.id.cancel_password_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dismiss();
            }
        });
        return view;
    }
}
