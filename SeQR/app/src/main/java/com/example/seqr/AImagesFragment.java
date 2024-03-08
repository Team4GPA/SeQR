package com.example.seqr;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.seqr.adapters.EventAdapter;
import com.example.seqr.adapters.ImageAdapter;
import com.example.seqr.controllers.EventController;
import com.example.seqr.controllers.ImageController;
import com.example.seqr.models.Event;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AImagesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private List<String> imageList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a_images, container, false);

        Button backButton = view.findViewById(R.id.admin_images_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){getParentFragmentManager().popBackStack();}
        });

        // Create recycler view
        recyclerView = view.findViewById(R.id.admin_images);
        imageList = new ArrayList<>();
        imageAdapter = new ImageAdapter(imageList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(imageAdapter);

        // Query to get all profiles
        ImageController imageController = new ImageController();
        imageController.getListOfFiles("ProfilePictures/", new ImageController.FileListCallback() {
            @Override
            public void onSuccess(List<String> fileList) {
                for (String fileName : fileList) {
                    fileName = "ProfilePictures/" + fileName;
                    imageList.add(fileName);
                    imageAdapter.notifyDataSetChanged();
                }
                Log.d("DEBUGGER", Integer.toString(imageList.size()));

            }

            @Override
            public void onFailure(Exception e) {
                // Handle failure
                e.printStackTrace();
            }
        });
        Log.d("DEBUGGER2", Integer.toString(imageList.size()));
        imageController.getListOfFiles("EventPosters/", new ImageController.FileListCallback() {
            @Override
            public void onSuccess(List<String> fileList) {
                for (String fileName : fileList) {
                    fileName = "EventPoster/" + fileName;
                    imageList.add(fileName);
                    imageAdapter.notifyDataSetChanged();
                }
                Log.d("DEBUGGER3", Integer.toString(imageList.size()));
            }

            @Override
            public void onFailure(Exception e) {
                // Handle failure
                e.printStackTrace();
            }
        });
        Log.d("DEBUGGER4", Integer.toString(imageList.size()));

        


        return view;
    }
}