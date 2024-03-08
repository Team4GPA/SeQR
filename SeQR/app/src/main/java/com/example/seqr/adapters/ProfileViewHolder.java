package com.example.seqr.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.seqr.R;

/**
 *  ViewHolder class for managing profile items in a RecyclerView.
 */
public class ProfileViewHolder extends RecyclerView.ViewHolder{
    public ImageView imageView;
    public TextView textView;

    /**
     * Constructor method to build a ProfileViewHolder object
     * @param itemView the view from which you want the holder to construct a custom recyclerview
     */
    public ProfileViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.profile_image);
        textView = itemView.findViewById(R.id.profile_name);
    }
}
