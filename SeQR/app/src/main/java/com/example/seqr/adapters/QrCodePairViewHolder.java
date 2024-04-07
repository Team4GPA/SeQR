package com.example.seqr.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.seqr.R;
import com.example.seqr.models.QrCodePair;

public class QrCodePairViewHolder extends RecyclerView.ViewHolder {
    ImageView checkInQRImageView, promotionQRImageView;
    TextView previousEventNameTextView;

    public QrCodePairViewHolder(View itemView){
        super(itemView);
        checkInQRImageView = itemView.findViewById(R.id.checkInQRImageView);
        promotionQRImageView = itemView.findViewById(R.id.promotionQRImageView);
        previousEventNameTextView = itemView.findViewById(R.id.previousEventNameTextView);

    }
}
