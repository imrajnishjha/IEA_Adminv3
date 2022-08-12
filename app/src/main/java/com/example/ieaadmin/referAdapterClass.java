package com.example.ieaadmin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class referAdapterClass extends FirebaseRecyclerAdapter<ReferModelClass,referAdapterClass.ReferViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public referAdapterClass(@NonNull FirebaseRecyclerOptions<ReferModelClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ReferViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull ReferModelClass model) {

        holder.referName.setText(model.getName());
        holder.referCompanyName.setText(model.getCompanyname());
        holder.referStatus.setText(model.getStatus());

        switch (model.getStatus()){
            case "In Review":
                holder.referStatusColor.setBackgroundColor(Color.parseColor("#000000"));
                break;
            case "Reviewed":
                holder.referStatusColor.setBackgroundColor(Color.parseColor("#48A14D"));
                break;
            case "Rejected":
                holder.referStatusColor.setBackgroundColor(Color.parseColor("#96271f"));
                break;
        }

        holder.referView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ReferDetails.class);
                intent.putExtra("ReferItemKey", getRef(position).getKey());
                view.getContext().startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public ReferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewRefer = LayoutInflater.from(parent.getContext()).inflate(R.layout.grievance_item,parent,false);
        return new ReferViewHolder(viewRefer);
    }

    public class ReferViewHolder extends RecyclerView.ViewHolder {

        View referView;
        TextView referName, referCompanyName,referStatus,referStatusColor;
        public ReferViewHolder(@NonNull View itemView) {
            super(itemView);

            referName = (TextView) itemView.findViewById(R.id.grievance_email_tv);
            referCompanyName = (TextView) itemView.findViewById(R.id.grievance_department_tv);
            referStatusColor = (TextView) itemView.findViewById(R.id.grievance_status_color_tv);
            referStatus = (TextView) itemView.findViewById(R.id.grievance_status_text);
            referView = itemView;
        }
    }
}