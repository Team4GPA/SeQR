package com.example.seqr.helpers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.seqr.R;

/**
 * A Test fragment to create when need to test promotional QR's
 */
public class A_Test_Fragment extends Fragment {
    /**
     * function that sets up the view and buttons when building the fragment
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return view of the fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.a_test_layout, container, false);

        Button backButton = view.findViewById(R.id.a_test_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){getParentFragmentManager().popBackStack();}
        });
        return view;
    }
}
