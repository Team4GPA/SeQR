package com.example.seqr;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A fragment for managing a specified event, including displaying QR codes, milestones, announcements, check-in map, attendance and users who signed up.
 */
public class EventManagementFragment extends Fragment {

    private ImageView eManagementPhoto;
    private Button cQRButton;
    private Button pQRButton;
    private Button eManagementBackButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_management, container, false);

        eManagementPhoto = view.findViewById(R.id.EManagementPhoto);
        cQRButton = view.findViewById(R.id.CQRButton);
        pQRButton = view.findViewById(R.id.PQRButton);

        Bundle bundle = getArguments();
        assert bundle != null;
        String eventName = bundle.getString("eventName", "");
        String checkInQR = bundle.getString("checkInQR", "");
        String promotionQR = bundle.getString("promotionQR", "");
        String eventId = bundle.getString("eventID","");

        TextView eManagementName = view.findViewById(R.id.EManagementName);
        eManagementName.setText(eventName);

        String path = Uri.encode("EventPosters/" + eventId + ".jpg");
        //this is the URL that the image is stored in firebase, picasso uses this to download the image
        String imageUrl = "https://firebasestorage.googleapis.com/v0/b/seqr-177ac.appspot.com/o/" + path + "?alt=media";
        Picasso.get().load(imageUrl).into(eManagementPhoto);

        setcQRButtonListener(bundle);
        setpQRButton(bundle);

        return view;
    }

    public void setcQRButtonListener(Bundle bundle){
        cQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CQRFragment cqrFragment = new CQRFragment();
                Bundle args = new Bundle();
                args.putString("checkInQR", bundle.getString("checkInQR"));
                args.putString("eventID", bundle.getString("eventID"));
                cqrFragment.setArguments(args);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, cqrFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    public void setpQRButton(Bundle bundle){
        pQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PQRFragment pqrFragment = new PQRFragment();
                Bundle args = new Bundle();
                args.putString("promotionQR",bundle.getString("promotionQR"));
                args.putString("eventID", bundle.getString("eventID"));

                pqrFragment.setArguments(args);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, pqrFragment)
                        .addToBackStack(null)
                        .commit();


            }
        });
    }


}

