package com.example.seqr.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.seqr.R;

import androidx.recyclerview.widget.RecyclerView;

public class EventViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView textView;

    public EventViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.event_image);
        textView = itemView.findViewById(R.id.event_name);
    }
}
