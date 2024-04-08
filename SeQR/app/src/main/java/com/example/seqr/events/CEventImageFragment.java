package com.example.seqr.events;

import static android.app.Activity.RESULT_OK;

import static androidx.core.content.FileProvider.getUriForFile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.seqr.R;
import com.example.seqr.helpers.ShareImages;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A fragment for selecting an image for event as poster.
 */
public class CEventImageFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView posterdisplay;

    /**
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
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
        choosePhotoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri == null) {
                    Toast.makeText(getContext(), "Please select an Image for the event. ", Toast.LENGTH_SHORT).show();
                } else {
                    Bundle bundle = getArguments();
                    assert bundle != null;
                    bundle.putString("imageUri", imageUri.toString());
                    CEventLocationFragment locationFrag = new CEventLocationFragment();
                    locationFrag.setArguments(bundle);
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, locationFrag)
                            .addToBackStack(null)
                            .commit();

                }
            }
        });
        return view;
    }

    /**
     * Opens a file chooser for selecting an image.
     */
    private void openFileChooser() {
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

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            //start decoding and compressing input images.
            Log.d("EDIT PROFILE", "Data is " + data.getData());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                ImageDecoder.Source usrSrc = ImageDecoder.createSource(getContext().getContentResolver(), imageUri);
                try {
                    Bitmap bitmap = ImageDecoder.decodeBitmap(usrSrc, new ImageDecoder.OnHeaderDecodedListener() {
                        @Override
                        public void onHeaderDecoded(@NonNull ImageDecoder decoder, @NonNull ImageDecoder.ImageInfo info, @NonNull ImageDecoder.Source source) {
                            int targetLargestSide = 800;
                            Log.d("CREATE EVENT", "Bitmap is " + info.getMimeType());
                            int currentWidth = info.getSize().getWidth();
                            Log.d("BITMAP PROC", "Current width is " + currentWidth);
                            int currentHeight = info.getSize().getHeight();
                            Log.d("BITMAP PROC", "Current height is " + currentHeight);
                            int newHeight = 0;
                            int newWidth = 0;

                            if ((currentWidth > targetLargestSide) || (currentHeight > targetLargestSide)) {
                                //cross-multiply:
                                //knownWidth / knownHeight = targetWidth (800) / unknownHeight
                                //(knownHeight x targetWidth) / knownWidth = unknownHeight;

                                newHeight = (currentHeight * targetLargestSide) / currentWidth;
                                float scaleFactor = (float) newHeight / currentHeight;
                                float fnewWidth = (float) (scaleFactor * currentWidth);
                                newWidth = Math.round(fnewWidth);
                            }
                            else {
                                newHeight = currentHeight;
                                newWidth = currentWidth;
                            }
                            Log.d("NEW BITMAP", "New height is " + newHeight + " and width is " + newWidth);
                            decoder.setTargetSize(newWidth, newHeight);
                        }
                    });

                    if (bitmap == null) {
                        Log.d("EDIT PROFILE", "Error: null reference on the bitmap (could not convert chosen file)");
                    }

                    //write the config'd bitmap first to temp file:
                    ByteArrayOutputStream dataWrite = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 35, dataWrite);
                    ShareImages imgHelper = new ShareImages();
                    File tempFile = imgHelper.generateTempFile(getContext(), "usr_pick", ".tmp");
                    tempFile.deleteOnExit();
                    Uri newImg;
                    try {
                        FileOutputStream fileWrite = new FileOutputStream(tempFile);
                        fileWrite.write(dataWrite.toByteArray());
                        fileWrite.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    //give the new Uri to the next steps
                    newImg = getUriForFile(getContext(), "com.example.seqr", tempFile);
                    posterdisplay.setImageURI(newImg);
                    Picasso.get().load(newImg).into(posterdisplay);
                    imageUri = newImg;

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}