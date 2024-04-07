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

public class QrCodePairViewHolder extends RecyclerView.ViewHolder {
    ImageView checkInQRImageView, promotionQRImageView;
    TextView previousEventNameTextView;
    QRCodeGenerator qrGen;

    public QrCodePairViewHolder(View itemView){
        super(itemView);
        checkInQRImageView = itemView.findViewById(R.id.checkInQRImageView);
        promotionQRImageView = itemView.findViewById(R.id.promotionQRImageView);
        previousEventNameTextView = itemView.findViewById(R.id.previousEventNameTextView);

    }

    public void setQrImages(String checkInQR, String promotionQR){
        qrGen = new QRCodeGenerator();
        Bitmap bitcodeCheckIn = qrGen.convertToBitmap(checkInQR);
        checkInQRImageView.setImageBitmap(bitcodeCheckIn);
        Bitmap bitcodePromotion = qrGen.convertToBitmap(promotionQR);
        promotionQRImageView.setImageBitmap(bitcodePromotion);
    }
}
