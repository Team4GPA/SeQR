package com.example.seqr.events;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.seqr.R;
import com.squareup.picasso.Picasso;

/**
 * A fragment for selecting an image for event as poster.
 */
public class CEventImageFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView posterdisplay;

    /**
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_c_event_image, container, false);
      
        Button backButton = view.findViewById(R.id.QRCheckInBackButton);
        Button nextButton = view.findViewById(R.id.cEventImageNextButton);
        posterdisplay = view.findViewById(R.id.photoPreview);
        Button choosePhotoButton = view.findViewById(R.id.choosePhotoButton);

        // Sets an onlClickListener for the back button so you can go back to the eventDetailsPage
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
      
        //Sets an onclickListener for uploading images, this calls openFileChooser
        choosePhotoButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri == null){
                    Toast.makeText(getContext(), "Please select an Image for the event. ",Toast.LENGTH_SHORT).show();
                }else {
                Bundle bundle = getArguments();
                assert bundle != null;
                bundle.putString("imageUri", imageUri.toString());
                CEventCQRFragment cEventCQRFragment = new CEventCQRFragment();
                cEventCQRFragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, cEventCQRFragment)
                        .addToBackStack(null)
                        .commit();

            }}
        });

        return view;

    }
    /**
     * Opens a file chooser for selecting an image.
     */
    private  void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    /**
     * Called when an activity you launched exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it (new image in this case).
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            posterdisplay.setImageURI(imageUri);
            Picasso.get().load(imageUri).into(posterdisplay);

        }
    }
}