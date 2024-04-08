package com.example.seqr.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seqr.R;
import com.example.seqr.models.Announcement;

import java.util.List;

/**
 * Adapter class to turn our announcments into a recycler view
 */
public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(Announcement announcement);
    }

    private final List<Announcement> announcementList;
    private final AnnouncementAdapter.OnItemClickListener listener;

    /**
     * Constructor to build an announcement adapter
     *
     * @param announcementList the list of Announcement objects
     * @param listener an onitem click listener to handle clicking an announcement
     */
    public AnnouncementAdapter(List<Announcement> announcementList, OnItemClickListener listener) {
        this.announcementList = announcementList;
        this.listener = listener;
    }

    /**
     * Method to construct and get the associated View Holder for the announcement recycler view
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return returns the Announcment View Holder
     */
    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_content, parent, false);
        return new AnnouncementViewHolder(itemView);
    }

    /**
     * Binds the announcements to the view holder
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
        Announcement announcement = announcementList.get(position);

        holder.titleView.setText(announcement.getTitle());
        holder.descView.setText(announcement.getDescription());
        holder.timeView.setText(announcement.getTime().toDate().toString());

        holder.itemView.setOnClickListener(view -> listener.onItemClick(announcement));
    }

    /**
     * Getter function to get an announcement at a certain position in the recycler view
     *
     * @param position
     * @return returns the Announcement at a certain index
     */
    public Announcement getAnnouncementAt(int position) {
        return announcementList.get(position);
    }

    /**
     * Getter to get the number of announcements in the recycler view
     *
     * @return an int representing the number of announcements
     */
    @Override
    public int getItemCount(){
        return announcementList.size();
    }
}
