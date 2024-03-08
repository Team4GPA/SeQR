package com.example.seqr.adapters;

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

    /**
     * Constructs an ImageAdapter with the provided list of image URLs.
     *
     * @param imageUrlList The list of image URLs to be displayed.
     */
    public ImageAdapter(List<String> imageUrlList){
        this.imageUrlList = imageUrlList;
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
        String imageUrl = imageUrlList.get(position);
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
