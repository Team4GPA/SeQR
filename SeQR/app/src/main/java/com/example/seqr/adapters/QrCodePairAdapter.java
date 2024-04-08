package com.example.seqr.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seqr.R;
import com.example.seqr.models.QrCodePair;

import java.util.List;

/**
 * Class to turn an old event and its associated QR code into a recycler view to see which one to Reuse
 */
public class QrCodePairAdapter extends RecyclerView.Adapter<QrCodePairViewHolder> {

    private List<QrCodePair> qrCodePairs;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(QrCodePair qrCodePair);
    }

    /**
     * Constructor class to set the listeners and list of QrCodePair's
     *
     * @param qrCodePairs a model class that includes a deleted event, its qr code, its id, etc.
     * @param listener an on item click listener to handle clicks on the recycler view
     */
    public QrCodePairAdapter(List<QrCodePair> qrCodePairs, OnItemClickListener listener){
        this.qrCodePairs = qrCodePairs;
        this.listener = listener;
    }

    /**
     * Binds the QrCodePair to a view holder to display the old event
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return Returns a ViewHolder for the QrCodePair
     */
    @NonNull
    @Override
    public QrCodePairViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reuse_qr_content,parent,false);
       return new QrCodePairViewHolder(itemView);

    }

    /**
     * Once the view holder is attached, gets information from the qrCodePair and fills the viewHolder with it
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull QrCodePairViewHolder holder, int position) {
        QrCodePair qrCodePair = qrCodePairs.get(position);
        holder.setQrImages(qrCodePair.getCheckInQR(), qrCodePair.getPromotionQR());
        holder.previousEventNameTextView.setText(qrCodePair.getPreviousEventName());
        holder.itemView.setOnClickListener(view -> listener.onItemClick(qrCodePair));
    }

    /**
     * Getter to see how many qrCodePairs there are
     *
     * @return an integer representing the number of qrCodePair's
     */
    @Override
    public int getItemCount() {
        return qrCodePairs.size();
    }
}
