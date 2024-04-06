package com.example.seqr.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.seqr.models.QrCodePair;

public class QrCodePairViewHolder extends RecyclerView.ViewHolder {
    ImageView checkInQRImageView, promotionQRImageView;
    TextView previousEventNameTextView;

    public QrCodePairViewHolder(View itemView){
        super(itemView);

    }
}
