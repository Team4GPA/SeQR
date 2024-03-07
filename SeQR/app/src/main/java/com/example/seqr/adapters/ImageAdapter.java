package com.example.seqr.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seqr.R;

import java.util.List;
import com.squareup.picasso.Picasso;

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder>{
    private List<String> imageUrlList; // Change if we have different data type

    public ImageAdapter(List<String> imageUrlList){
        this.imageUrlList = imageUrlList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_content, parent, false);
        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position){
        String imageUrl = imageUrlList.get(position);
        Picasso.get().load(imageUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUrlList.size();
    }
}
