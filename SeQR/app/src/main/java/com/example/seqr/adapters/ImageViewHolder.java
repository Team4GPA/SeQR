package com.example.seqr.adapters;


import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.seqr.R;

public class ImageViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public ImageViewHolder(View itemView){
        super(itemView);
        imageView = itemView.findViewById(R.id.image_grid);
    }
}
