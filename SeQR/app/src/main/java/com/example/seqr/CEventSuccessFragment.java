package com.example.seqr;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class CEventSuccessFragment extends Fragment {
    private TextView eventNameTextView;
    private TextView eventOrganizerTextView;
    private TextView eventLocationTextView;
    private TextView eventTimeTextView;
    private TextView eventCapacityTextView;
    private TextView eventDescriptionTextView;
    private ImageView eventImageView;
    private Button cEventSuccessGoToButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_c_event_success, container, false);

        eventOrganizerTextView = view.findViewById(R.id.cEventSuccessOrganizer);
        eventLocationTextView = view.findViewById(R.id.cEventSuccessLocation);
        eventTimeTextView = view.findViewById(R.id.cEventSuccessTime);
        eventCapacityTextView = view.findViewById(R.id.cEventSuccessCapacity);
        eventImageView = view.findViewById(R.id.photoPreview);
        cEventSuccessGoToButton = view.findViewById(R.id.cEventSuccessGoToButton);

        Bundle bundle = getArguments();
        assert bundle != null;
      
        String organizerName = bundle.getString("organizerName","");
        String eventLocation = bundle.getString("eventLocation", "");
        String eventTime = bundle.getString("eventTime", "");
        String eventCapacity = bundle.getString("eventCapacity", "");
        String eventImageUriString = bundle.getString("imageUri", "");
        String promotionQR = bundle.getString("promotionQR","");

        eventOrganizerTextView.setText(organizerName);
        eventLocationTextView.setText(eventLocation);
        eventTimeTextView.setText(eventTime);
        eventCapacityTextView.setText(eventCapacity);


        //loads the image onto the event text
        Uri eventImageUri = Uri.parse(eventImageUriString);

        Picasso.get().load(eventImageUri).into(eventImageView);

        cEventSuccessGoToButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("promotionQR",promotionQR);
                PQRFragment pqrFragment = new PQRFragment();
                pqrFragment.setArguments(bundle1);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, pqrFragment);
                fragmentTransaction.commit();


            }
        });

        return view;
    }
}