package com.example.seqr.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seqr.R;
import com.example.seqr.models.Event;
import com.squareup.picasso.Picasso;
import com.example.seqr.models.Profile;
import java.util.List;

/**
 * An adapter class for managing profiles in a RecyclerView.
 */
public class ProfileAdapter extends RecyclerView.Adapter<ProfileViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Profile profile);
    }
    private final List<Profile> profileList;
    private final OnItemClickListener listener;

    /**
     * Constructs a ProfileAdapter with the provided list of profiles.
     *
     * @param profileList The list of profiles to be displayed.
     * @param listener a listener to handle clicking on a profile
     */
    public ProfileAdapter(List<Profile> profileList, OnItemClickListener listener){
        this.profileList = profileList;
        this.listener = listener;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ProfileViewHolder that holds the View for each profile item.
     */
    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_content,parent,false);
        return new ProfileViewHolder(itemView);
    }

    /**
     * Called by RecyclerView to display the profile at the specified position.
     *
     * @param holder The ViewHolder which should be updated to represent the content of the profile at the given position in the data set.
     * @param position The position of the profile within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Profile profile = profileList.get(position);

        holder.setProfilePic(profile.getId());
        holder.itemView.setOnClickListener(view -> listener.onItemClick(profile));

        holder.textView.setText(profile.getUsername());
    }

    /**
     * Returns the total number of profiles in the list held by the adapter.
     *
     * @return The total number of profiles in the list.
     */
    @Override
    public int getItemCount() {
        return profileList.size();
    }

}
