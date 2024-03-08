package com.example.seqr;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.seqr.controllers.ProfileController;
import com.example.seqr.models.ID;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.UUID;

/**
 * Fragment for creating event check-in QR codes.
 */
public class CEventCQRFragment extends Fragment {
    private QRCodeGenerator qrCodeGenerator;
    private String checkInQR;
    private String promotionQR;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_c_event_cqr, container, false);

        Button backButton = view.findViewById(R.id.BackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
      
        Button generateQRButton = view.findViewById(R.id.generateQRButton);
        qrCodeGenerator = new QRCodeGenerator();
        String eventID = UUID.randomUUID().toString();
        Bundle bundle = getArguments();
        assert bundle != null;
        bundle.putString("eventID", eventID);

        // This code prefetches the organizer name for the next fragment so there is no delay in displaying the nane
        String organizerID = ID.getProfileId(getContext());
        Log.d("DEBUG", organizerID);
        ProfileController profileController = new ProfileController();
        profileController.getProfileUsernameByDeviceId(organizerID, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    String organizer = documentSnapshot.getString("username");
                    bundle.putString("organizerName",organizer);
                }

            }
        });

        generateQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInQR = qrCodeGenerator.generate(eventID + "_checkIn");
                promotionQR = qrCodeGenerator.generate(eventID+"_promotion");
                bundle.putString("checkInQR", checkInQR);
                bundle.putString("promotionQR",promotionQR);

                CEventPreviewFragment cEventPreviewFragment = new CEventPreviewFragment();
                cEventPreviewFragment.setArguments(bundle);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, cEventPreviewFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }
}