package com.example.seqr.adapters;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.seqr.R;
import com.squareup.picasso.Picasso;

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

    public void setProfilePic(String profileID) {
        // encode it to handle special characters in the profileID
        String path = Uri.encode("ProfilePictures/" + profileID + ".jpg");
        //this is the URL that the image is stored in firebase, picasso uses this to download the image
        String imageUrl = "https://firebasestorage.googleapis.com/v0/b/seqr-177ac.appspot.com/o/" + path + "?alt=media";
        Picasso.get().load(imageUrl).into(imageView);
    }
}
