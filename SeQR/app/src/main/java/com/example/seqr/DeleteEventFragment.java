package com.example.seqr;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * A dialog fragment for confirming deletion.
 */
public class DeleteEventFragment extends DialogFragment {


    /**
     * creates a dialogue to prompt for deleting
     *
     * @param savedInstanceState The last saved instance state of the Fragment,
     *                           or null if this is a freshly created Fragment.
     * @return view
     */

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_delete, container, false);

        Bundle bundle = getArguments();
        assert bundle != null;
        String eventID = bundle.getString("eventID", "");

        // IMPLEMENT THIS
        view.findViewById(R.id.delete_item_confirm_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("DEBUG", "hi");
            }
        });

        view.findViewById(R.id.delete_item_cancel_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dismiss();
            }
        });


        return view;
    }
}