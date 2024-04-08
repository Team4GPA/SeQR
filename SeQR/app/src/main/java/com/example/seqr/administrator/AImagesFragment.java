package com.example.seqr.administrator;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.example.seqr.R;
import com.example.seqr.adapters.EventAdapter;
import com.example.seqr.adapters.ImageAdapter;
import com.example.seqr.controllers.EventController;
import com.example.seqr.controllers.ImageController;
import com.example.seqr.models.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A fragment showing all images for administrator.
 */

public class AImagesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private List<String> imageList;
    private ImageController imageController;
    private int index;

    /**
     * Creates a view and associated logic for the view and returns it to whoever built the fragment
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        index=-1;
        View view = inflater.inflate(R.layout.fragment_a_images, container, false);

        Button backButton = view.findViewById(R.id.admin_images_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){getParentFragmentManager().popBackStack();}
        });
        // Create recycler view
        imageController = new ImageController(this.getContext());
        recyclerView = view.findViewById(R.id.admin_images);
        imageList = new ArrayList<>();
        imageAdapter = new ImageAdapter(imageList, this.getContext(), new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("DEBUG", "hi");
                index = position;
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(),3));
        recyclerView.setAdapter(imageAdapter);

        // Query to get all profiles
        ImageController imageController;
        imageController = new ImageController(this.getContext());
        imageController.getListOfFiles("ProfilePictures/", new ImageController.FileListCallback() {
            @Override
            public void onSuccess(List<String> fileList) {
                for (String fileName : fileList) {
                    fileName = "ProfilePictures/" + fileName;
                    imageList.add(fileName);
                }
                imageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                // Handle failure
                e.printStackTrace();
            }
        });

        imageController.getListOfFiles("EventPosters/", new ImageController.FileListCallback() {
            @Override
            public void onSuccess(List<String> fileList) {
                for (String fileName : fileList) {
                    fileName = "EventPosters/" + fileName;
                    imageList.add(fileName);
                }
                imageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                // Handle failure
                e.printStackTrace();
            }
        });

        FloatingActionButton deleteButton = view.findViewById(R.id.admin_delete_image);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG2", "INDEX: " + index);
                if (index <= -1) {
                    index =-1;
                } else {
                    String imagePath = imageList.get(index);
                    imageController.replaceImageWithPlaceHolder(imagePath);
                    index = -1;
                    imageAdapter.notifyDataSetChanged();
                }
            }
        });
        return view;
    }
}