package com.example.seqr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * A dialog fragment for confirming deletion.
 */
public class DeleteItemFragment extends DialogFragment {

    /**
     * Interface for handling dialog button clicks.
     */
    public interface ConfirmationDialogListener {
        void onYesClicked(View v);
        void onNoClicked(View v);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_confirm_delete, null);
        builder.setView(dialogView);

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ConfirmationDialogListener listener = (ConfirmationDialogListener) requireActivity();
                listener.onYesClicked(getDialog().findViewById(which));
            }
        });

        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ConfirmationDialogListener listener = (ConfirmationDialogListener) requireActivity();
                listener.onNoClicked(getDialog().findViewById(which));
            }
        });

        return builder.create();
    }

    public void onYesClicked(View v){

    }

    public void onNoClicked(View v){

    }
}