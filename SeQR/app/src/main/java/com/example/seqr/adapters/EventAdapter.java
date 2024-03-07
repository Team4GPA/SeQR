package com.example.seqr.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seqr.R;
import com.example.seqr.models.Event;
import java.util.List;


public class EventAdapter extends RecyclerView.Adapter<EventViewHolder>{
    private List<Event> eventList;

    public EventAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_content, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position){
        Event event = eventList.get(position);

        // REPLACE the getters with actual names of functions
        holder.imageView.setImageResource(event.getImage());
        holder.textView.setText(event.getEventName());
    }

    @Override
    public int getItemCount(){
        return eventList.size();
    }

}
