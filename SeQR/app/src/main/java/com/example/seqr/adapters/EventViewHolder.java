package com.example.seqr.adapters;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.seqr.R;
import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.RecyclerView;

/**
 * ViewHolder class for managing Event items in a RecyclerView.
 */
public class EventViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView textView;

    /**
     * Constructs an EventViewHolder with the given itemView.
     *
     * @param itemView The View object corresponding to a single item in the RecyclerView.
     */
    public EventViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.event_image);
        textView = itemView.findViewById(R.id.event_name);
    }

    /**
     * Sets the image for the event using Picasso library.
     *
     * @param eventId The ID of the event to retrieve the image.
     */
    public void setEventImage(String eventId) {
        // encode it to handle special characters in the eventID
        String path = Uri.encode("EventPosters/" + eventId + ".jpg");
        //this is the URL that the image is stored in firebase, picasso uses this to download the image
        String imageUrl = "https://firebasestorage.googleapis.com/v0/b/seqr-177ac.appspot.com/o/" + path + "?alt=media";
        Picasso.get().load(imageUrl).into(imageView);
    }
}
