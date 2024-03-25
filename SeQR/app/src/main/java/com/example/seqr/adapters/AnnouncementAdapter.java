package com.example.seqr.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seqr.R;
import com.example.seqr.models.Announcement;

import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(Announcement announcement);
    }

    private List<Announcement> announcementList;
    private AnnouncementAdapter.OnItemClickListener listener;

    public AnnouncementAdapter(List<Announcement> announcementList, OnItemClickListener listener) {
        this.announcementList = announcementList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_content, parent, false);
        return new AnnouncementViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
        Announcement announcement = announcementList.get(position);

        holder.titleView.setText(announcement.getTitle());
        holder.descView.setText(announcement.getDescription());
        holder.timeView.setText(announcement.getTime().toDate().toString());

        holder.itemView.setOnClickListener(view -> listener.onItemClick(announcement));
    }

    public Announcement getAnnouncementAt(int position) {
        return announcementList.get(position);
    }

    @Override
    public int getItemCount(){
        return announcementList.size();
    }
}
