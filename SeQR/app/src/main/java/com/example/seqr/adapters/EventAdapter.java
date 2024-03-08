package com.example.seqr.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seqr.R;
import com.example.seqr.models.Event;
import java.util.List;

/**
 * An adapter class for managing Event items in RecyclerView.
 */
public class EventAdapter extends RecyclerView.Adapter<EventViewHolder>{

    /**
     * Constructs an EventAdapter with the provided list of events.
     *
     * @param eventList The list of events to be displayed.
     */
    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    private List<Event> eventList;
    private OnItemClickListener listener;





    public EventAdapter(List<Event> eventList, OnItemClickListener listener) {
        this.eventList = eventList;
        this.listener = listener;


    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an position in adapter .
     * @param viewType The view type of the new View.
     * @return A new EventViewHolder that holds the View for each event item.
     */
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_content, parent, false);
        return new EventViewHolder(itemView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder The ViewHolder which should be updated to represent the content of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position){
        Event event = eventList.get(position);

        // REPLACE the getters with actual names of functions
       holder.setEventImage(event.getEventID());
       holder.textView.setText(event.getEventName());

       holder.itemView.setOnClickListener(view -> listener.onItemClick(event));

    }

    public Event getEventAt(int position) {
        return eventList.get(position);
    }

    /**
     * Returns the total number of events in the list held by the adapter.
     *
     * @return The total number of events in the list.
     */
    @Override
    public int getItemCount(){
        return eventList.size();
    }

}
