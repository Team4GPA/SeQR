package com.example.seqr.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seqr.R;
import com.example.seqr.models.QrCodePair;

import java.util.List;

public class QrCodePairAdapter extends RecyclerView.Adapter<QrCodePairViewHolder> {

    private List<QrCodePair> qrCodePairs;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(QrCodePair qrCodePair);
    }

    public QrCodePairAdapter(List<QrCodePair> qrCodePairs, OnItemClickListener listener){
        this.qrCodePairs = qrCodePairs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public QrCodePairViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reuse_qr_content,parent,false);
       return new QrCodePairViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull QrCodePairViewHolder holder, int position) {
        QrCodePair qrCodePair = qrCodePairs.get(position);
        holder.setQrImages(qrCodePair.getCheckInQR(), qrCodePair.getPromotionQR());
        holder.previousEventNameTextView.setText(qrCodePair.getPreviousEventName());
        holder.itemView.setOnClickListener(view -> listener.onItemClick(qrCodePair));
    }

    @Override
    public int getItemCount() {
        return qrCodePairs.size();
    }
}
