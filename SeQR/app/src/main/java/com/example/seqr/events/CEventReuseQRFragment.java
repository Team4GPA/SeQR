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


public class CEventReuseQRFragment extends Fragment {

    private RecyclerView qrPairsRecyclerView;
    private QrCodePairAdapter qrCodePairAdapter;
    private List<QrCodePair> qrCodePairsList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_c_event_reuse_q_r, container, false);

        qrPairsRecyclerView = view.findViewById(R.id.qrPairsRecyclerView);
        qrCodePairsList = new ArrayList<>();
        qrCodePairAdapter = new QrCodePairAdapter(qrCodePairsList, new QrCodePairAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(QrCodePair qrCodePair) {
                handleClick();
            }
        });

        qrPairsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        qrPairsRecyclerView.setAdapter(qrCodePairAdapter);

        displayQRpairs();

        return view;
    }

    public void handleClick(){

    }

    public void displayQRpairs(){
        ReusableQrController reusableQrController = new ReusableQrController();
        reusableQrController.getQRpairs(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot qrPairDoc: task.getResult()){
                        if (qrPairDoc != null && qrPairDoc.exists()){
                            QrCodePair qrCodePair = qrPairDoc.toObject(QrCodePair.class);
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