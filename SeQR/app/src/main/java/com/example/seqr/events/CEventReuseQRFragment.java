package com.example.seqr.events;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.seqr.R;
import com.example.seqr.adapters.QrCodePairAdapter;
import com.example.seqr.controllers.ReusableQrController;
import com.example.seqr.models.QrCodePair;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment where you can select a QR to reuse
 */
public class CEventReuseQRFragment extends Fragment {

    private RecyclerView qrPairsRecyclerView;
    private QrCodePairAdapter qrCodePairAdapter;
    private List<QrCodePair> qrCodePairsList;

    private Button backbutton;

    /**
     * Method to create the fragment view and handle any button clicks
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return the view associated with the fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_c_event_reuse_q_r, container, false);

        qrPairsRecyclerView = view.findViewById(R.id.qrPairsRecyclerView);
        qrCodePairsList = new ArrayList<>();
        qrCodePairAdapter = new QrCodePairAdapter(qrCodePairsList, this::handleQrPairClick);

        qrPairsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        qrPairsRecyclerView.setAdapter(qrCodePairAdapter);
        backbutton = view.findViewById(R.id.reuseQRBackbutton);
        displayQRpairs();
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        return view;
    }

    /**
     * Method when selecting a QR pair to use
     *
     * @param qrCodePair a qrPair object representing both qr's (promotional and check in)
     */
    public void handleQrPairClick(QrCodePair qrCodePair){
        Bundle newBundle = getArguments(); // Get existing bundle

        assert newBundle != null;
        newBundle.putString("eventID", qrCodePair.getEventId());
        newBundle.putString("checkInQR", qrCodePair.getCheckInQR());
        newBundle.putString("promotionQR", qrCodePair.getPromotionQR());
        CEventPreviewFragment cEventPreviewFragment = new CEventPreviewFragment();
        cEventPreviewFragment.setArguments(newBundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, cEventPreviewFragment)
                .addToBackStack(null)
                .commit();



    }

    /**
     * Method to get all the QR pairs and add them to the Adapter to display them
     */
    public void displayQRpairs(){
        ReusableQrController reusableQrController = new ReusableQrController();
        reusableQrController.getQRpairs(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot qrPairDoc: task.getResult()){
                        if (qrPairDoc != null && qrPairDoc.exists()){
                            QrCodePair qrCodePair = qrPairDoc.toObject(QrCodePair.class);
                            qrCodePair.setEventId(qrPairDoc.getId());

                            qrCodePairsList.add(qrCodePair);
                        } else{
                            Log.d("DEBUG", "there were some issues with getting the qrPairDoc it was null or didnt exist");
                        }

                    } qrCodePairAdapter.notifyDataSetChanged();


                } else {
                    Log.d("DEBUG","something went wrong getting the QRPairs");
                }
            }
        });
    }
}