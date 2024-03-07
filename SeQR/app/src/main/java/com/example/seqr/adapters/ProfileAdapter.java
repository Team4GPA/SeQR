package com.example.seqr.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seqr.R;
import com.squareup.picasso.Picasso;
import com.example.seqr.models.Profile;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileViewHolder> {
    private List<Profile> profileList;

    public ProfileAdapter(List<Profile> profileList){
        this.profileList = profileList;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_content,parent,false);
        return new ProfileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Profile profile = profileList.get(position);
        String imageUrl = profile.getProfilePicture(); // replace with real getter
        Picasso.get().load(imageUrl).into(holder.imageView);
        holder.textView.setText(profile.getName()); // replace with real getter
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

}
