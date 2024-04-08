package com.example.seqr.adapters;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.seqr.R;
import com.example.seqr.models.QrCodePair;
import com.example.seqr.qr.QRCodeGenerator;
import com.squareup.picasso.Picasso;

/**
 * A ViewHolder to bind a QrCodePair recycler view to
 */
public class QrCodePairViewHolder extends RecyclerView.ViewHolder {
    ImageView checkInQRImageView, promotionQRImageView;
    TextView previousEventNameTextView;
    QRCodeGenerator qrGen;

    /**
     * Constructor method to set the image and text views
     *
     * @param itemView the associated itemView to fill with a QrCodePair
     */
    public QrCodePairViewHolder(View itemView){
        super(itemView);
        checkInQRImageView = itemView.findViewById(R.id.checkInQRImageView);
        promotionQRImageView = itemView.findViewById(R.id.promotionQRImageView);
        previousEventNameTextView = itemView.findViewById(R.id.previousEventNameTextView);

    }

    /**
     * Sets the image views with their associated check-in QR and promotion QR
     *
     * @param checkInQR string representing the raw data of the check in QR
     * @param promotionQR string representing the raw data of the promotion QR
     */
    public void setQrImages(String checkInQR, String promotionQR){
        qrGen = new QRCodeGenerator();
        Bitmap bitcodeCheckIn = qrGen.convertToBitmap(checkInQR);
        checkInQRImageView.setImageBitmap(bitcodeCheckIn);
        Bitmap bitcodePromotion = qrGen.convertToBitmap(promotionQR);
        promotionQRImageView.setImageBitmap(bitcodePromotion);
    }
}
