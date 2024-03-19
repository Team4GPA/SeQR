package com.example.seqr;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

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

        QRCodeGenerator qrGen = new QRCodeGenerator();
        ImageView qrView = view.findViewById(R.id.EPQRQRCode);
        Bundle bundle = getArguments();

        assert bundle != null;
        String qrcode = bundle.getString("promotionQR");
        Bitmap bitcode = qrGen.convertToBitmap(qrcode);
        qrView.setImageBitmap(bitcode);

        promoBackButton.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        return view;
    }
}