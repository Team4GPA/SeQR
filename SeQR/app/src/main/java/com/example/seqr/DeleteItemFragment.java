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

public class DeleteItemFragment extends DialogFragment {

    public interface ConfirmationDialogListener {
        void onYesClicked();
        void onNoClicked();
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
                listener.onYesClicked();
            }
        });

        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ConfirmationDialogListener listener = (ConfirmationDialogListener) requireActivity();
                listener.onNoClicked();
            }
        });

        return builder.create();
    }

    public void onYesClicked(){

    }

    public void onNoClicked(){

    }
}