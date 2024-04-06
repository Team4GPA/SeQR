package com.example.seqr.adapters;

import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seqr.R;

import java.util.List;
import com.squareup.picasso.Picasso;

/**
 * An adapter class for managing image items in a RecyclerView.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder>{
    private List<String> imageUrlList; // Change if we have different data type
    private int itemWidth;

    /**
     * Constructs an ImageAdapter with the provided list of image URLs.
     *
     * @param imageUrlList The list of image URLs to be displayed.
     */
    public ImageAdapter(List<String> imageUrlList, android.content.Context context){
        this.imageUrlList = imageUrlList;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        itemWidth = screenWidth / 3;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ImageViewHolder that holds the View for each image item.
     */
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_content, parent, false);

        // Set the size of each image based on the size of the screen
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.width = itemWidth;
        layoutParams.height = itemWidth;
        itemView.setLayoutParams(layoutParams);
        return new ImageViewHolder(itemView);
    }

    /**
     * Called by RecyclerView to display the image at the specified position.
     *
     * @param holder The ViewHolder which should be updated to represent the content of the item at the given position in the data set.
     * @param position The position of the image within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position){
        String path = Uri.encode(imageUrlList.get(position));
        String imageUrl = "https://firebasestorage.googleapis.com/v0/b/seqr-177ac.appspot.com/o/" + path + "?alt=media";
        Picasso.get().load(imageUrl).into(holder.imageView);
    }

    /**
     * Returns the total number of image URLs in the list held by the adapter.
     *
     * @return The total number of image URLs in the list.
     */

    @Override
    public int getItemCount() {
        return imageUrlList.size();
    }
}
