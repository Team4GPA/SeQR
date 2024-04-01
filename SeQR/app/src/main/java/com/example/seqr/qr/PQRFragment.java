package com.example.seqr.qr;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.seqr.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A fragment displayed promotion QR code for an event.
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
        File tempStorage = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        tempStorage = new File(tempStorage.getAbsolutePath() + "/.temp/");
        tempStorage.mkdir();
        File tempFile;
        try {
            tempFile = File.createTempFile("pqr", ".png", tempStorage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Get the bitmap byte data for storing in a temporary file
        ByteArrayOutputStream encodeTempFile = new ByteArrayOutputStream();
        int picSize = bitcode.getByteCount();
        ByteBuffer pqrBuffer = ByteBuffer.allocate(picSize);
        bitcode.copyPixelsToBuffer(pqrBuffer);
        byte[] pqrResult = pqrBuffer.array();

        //write the bitmap to the temp file location, and set the pqrUri to the temp file location
        try {
            FileOutputStream writePQR = new FileOutputStream(tempFile);
            writePQR.write(pqrResult);
            writePQR.flush();
            writePQR.close();
            pqrUri = Uri.fromFile(tempFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Uri finalPqrUri = pqrUri;
        sharePromoCode.setOnClickListener(v -> {
            Intent shareSheet = new Intent()
                    .setAction(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_STREAM, finalPqrUri)
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    .setType("image/png");
            Intent.createChooser(shareSheet, "Send Promo QR Code");
            startActivity(shareSheet);
        });

        promoBackButton.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });


        return view;
    }
}