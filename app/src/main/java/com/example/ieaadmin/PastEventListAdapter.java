package com.example.ieaadmin;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class PastEventListAdapter extends FirebaseRecyclerAdapter<PastEventModel,PastEventListAdapter.PastEventListViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PastEventListAdapter(@NonNull FirebaseRecyclerOptions<PastEventModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PastEventListViewHolder holder, int position, @NonNull PastEventModel model) {
        holder.name.setText(model.getTitle());
        holder.location.setText(model.getLocation());
        holder.time.setText(model.getTime());
        String year = model.getDate();
        if (year.length() == 10) {
            String day = year.substring(0, 1);
            String month = year.substring(2, 5);
            holder.day.setText(day);
            holder.month.setText(month);

        } else {
            String day = year.substring(0, 2);
            String month = year.substring(3, 6);
            holder.day.setText(day);
            holder.month.setText(month);
        }

        Glide.with(holder.eventImg.getContext())
                .load(model.getImgUrl())
                .error(R.drawable.iea_logo)
                .into(holder.eventImg);

        holder.EventListView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), PastEventDetails.class);
            intent.putExtra("EventItemKey", getRef(position).getKey());
            view.getContext().startActivity(intent);
        });
    }

    @NonNull
    @Override
    public PastEventListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View eventView = LayoutInflater.from(parent.getContext()).inflate(R.layout.eventlist_item,parent,false);
        return new PastEventListViewHolder(eventView);
    }

    class PastEventListViewHolder extends RecyclerView.ViewHolder {

        ImageView eventImg;
        TextView day,month,name,location,time;
        View EventListView;


        public PastEventListViewHolder(@NonNull View itemView) {
            super(itemView);

            eventImg = (ImageView) itemView.findViewById(R.id.EventListImage);
            day = (TextView) itemView.findViewById(R.id.EventListDay);
            month = (TextView)  itemView.findViewById(R.id.EventListMonth);
            name = (TextView) itemView.findViewById(R.id.EventListName);
            location = (TextView) itemView.findViewById(R.id.EventListLocation);
            time = (TextView) itemView.findViewById(R.id.EventListTime);
            EventListView= itemView;
        }
    }
}
