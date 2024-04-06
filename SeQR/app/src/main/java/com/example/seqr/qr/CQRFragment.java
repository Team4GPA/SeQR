package com.example.seqr.qr;

import static androidx.core.content.FileProvider.getUriForFile;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import com.example.seqr.helpers.ShareImages;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.seqr.R;

import java.io.File;
import java.io.IOException;

/**
 * A fragment displayed check-in QR code for an event.
 */
public class CQRFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_cqr, container, false);
        Button checkinQRBack = view.findViewById(R.id.QRCheckInBackButton);

        QRCodeGenerator qrGen = new QRCodeGenerator();
        ImageView qrView = view.findViewById(R.id.ECQRQRCode);
        Button shareCQR = view.findViewById(R.id.ECQRShareButton);
        Bundle bundle = getArguments();

        assert bundle != null;
        String qrcode = bundle.getString("checkInQR");
        Bitmap bitcode = qrGen.convertToBitmap(qrcode);
        qrView.setImageBitmap(bitcode);

        //access helper methods to process QR code for sharing
        ShareImages process = new ShareImages();
        File tempFile = process.generateTempFile(getContext(), "cqr", ".png");
        try {
            process.generateImageFile(bitcode, tempFile, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Uri imageLoc = getUriForFile(getContext(), "com.example.seqr", tempFile);
        Log.d("PQRFragment", "Got the uri for image stored at " + imageLoc.toString());

        shareCQR.setOnClickListener(v -> {
            Intent shareSheet = new Intent();
            shareSheet
                    .setAction(Intent.ACTION_SEND)
                    .setClipData(ClipData.newRawUri(null, imageLoc));
            shareSheet
                    .putExtra(Intent.EXTRA_STREAM, imageLoc)
                    .setType("image/png")
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareSheet, null));
        });


        checkinQRBack.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        return view;

    }
}