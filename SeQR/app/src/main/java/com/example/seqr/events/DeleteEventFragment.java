package com.example.seqr.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.seqr.R;
import com.example.seqr.controllers.EventController;
/**
 * A dialog fragment for confirming deletion.
 */
public class DeleteEventFragment extends DialogFragment {


    /**
     * creates a dialogue to prompt for deleting
     * @param container The container to put the fragment into
     * @param savedInstanceState The last saved instance state of the Fragment,
     *                           or null if this is a freshly created Fragment.
     * @param savedInstanceState the bundle of previous information passed to this fragment
     * @return view
     */

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_delete, container, false);

        Bundle bundle = getArguments();
        assert bundle != null;
        String eventID = bundle.getString("eventID", "");

        view.findViewById(R.id.delete_item_confirm_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                EventController eventController = new EventController();
                eventController.removeEventWithID(eventID);
                dismiss();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();

                //getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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