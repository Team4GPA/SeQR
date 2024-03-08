package com.example.seqr.adapters;


import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.seqr.R;

/**
 * ViewHolder class for managing image items in a RecyclerView.
 */
public class ImageViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;

    /**
     * Constructs an ImageViewHolder with the given itemView.
     *
     * @param itemView The View object corresponding to a single item in the RecyclerView.
     */
    public ImageViewHolder(View itemView){
        super(itemView);
        imageView = itemView.findViewById(R.id.image_grid);
    }
}
