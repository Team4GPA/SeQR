package com.example.seqr.adapters;

import android.view.View;
import android.widget.TextView;
import com.example.seqr.R;
import androidx.recyclerview.widget.RecyclerView;

public class AnnouncementViewHolder extends RecyclerView.ViewHolder {
    public TextView titleView;
    public TextView descView;
    public TextView timeView;

    public AnnouncementViewHolder(View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.announcementTitle);
        descView = itemView.findViewById(R.id.announcementDesc);
        timeView = itemView.findViewById(R.id.announcementTime);
    }

}
