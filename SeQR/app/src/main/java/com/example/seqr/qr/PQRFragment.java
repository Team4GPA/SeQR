package com.example.seqr.qr;
import com.example.seqr.helpers.MyFileProvider;
import com.example.seqr.helpers.MyFileProvider.*;
import static androidx.core.content.FileProvider.getUriForFile;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.example.seqr.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A fragment to display the promotion QR code for an event, and allow for sharing using the
 * Android shareSheet built-in functionality.
 */
public class PQRFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_pqr, container, false);

        Button promoBackButton = view.findViewById(R.id.QRPromoBackButton);
        Button sharePromoCode = view.findViewById(R.id.EPQRShareButton);

        QRCodeGenerator qrGen = new QRCodeGenerator();
        ImageView qrView = view.findViewById(R.id.EPQRQRCode);
        Bundle bundle = getArguments();

        assert bundle != null;
        String qrcode = bundle.getString("promotionQR");
        Bitmap bitcode = qrGen.convertToBitmap(qrcode);
        qrView.setImageBitmap(bitcode);
        Uri pqrUri = Uri.parse("");


        //Create some temporary storage
        File tempStorage = new File(requireContext().getFilesDir(), "temp");
        tempStorage.mkdir();
        tempStorage = new File(tempStorage.getAbsolutePath()+"/pqr_temp/");
        tempStorage.mkdir();
        File tempFile;
        try {
            tempFile = File.createTempFile("pqr", ".png", tempStorage);
            tempFile.deleteOnExit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Get the bitmap byte data for storing in a temporary file
        try {
            generateImageFile(bitcode, tempFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        pqrUri = getUriForFile(getContext(), "com.example.seqr", tempFile);
        Log.d("PQRFragment", "Got the uri for image stored at " + pqrUri.toString());

        Uri finalPqrUri = pqrUri;

        //start the Android "chooser" shareSheet built-in when button is clicked
        sharePromoCode.setOnClickListener(v -> {
            Intent shareSheet = new Intent();
            shareSheet
                    .setAction(Intent.ACTION_SEND)
                    .setClipData(ClipData.newRawUri(null, finalPqrUri));
            shareSheet
                    .putExtra(Intent.EXTRA_STREAM, finalPqrUri)
                    .setType("image/png")
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareSheet, null));
        });

        promoBackButton.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        return view;
    }

    public void generateImageFile(Bitmap image, File emptyFile) throws IOException {
        ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 0, imageBytes);
        FileOutputStream writeBytes = new FileOutputStream(emptyFile);
        writeBytes.write(imageBytes.toByteArray());
        writeBytes.close();
    }
}