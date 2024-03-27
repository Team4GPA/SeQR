package com.example.seqr.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.seqr.R;

public class DeleteProfileFragment extends DialogFragment {

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_confirm_delete, container, false);

        Bundle bundle = getArguments();
        assert bundle != null;
        String profileID = bundle.getString("id","");

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
