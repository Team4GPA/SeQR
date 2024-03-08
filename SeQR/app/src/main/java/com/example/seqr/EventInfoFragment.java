package com.example.seqr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class EventInfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_info, container, false);

        TextView eventNameText = view.findViewById(R.id.eventInfoNameText);
        TextView eventOrganizer = view.findViewById(R.id.eventInfoOrganizer);
        TextView eventLocation = view.findViewById(R.id.eventInfoLocation);
        TextView eventTime = view.findViewById(R.id.eventInfoTime);
        TextView eventCapacity = view.findViewById(R.id.eventInfoCapacity);
        ImageView eventPhoto = view.findViewById(R.id.eventPhotoPreview);
        TextView eventDescription = view.findViewById(R.id.eventInfoDescription);

        return view;

    }
}