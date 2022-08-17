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

public class MemberRenewalAdapter extends FirebaseRecyclerAdapter<MemberRenewalModel,MemberRenewalAdapter.MembersDirectoryViewHolder2> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MemberRenewalAdapter(@NonNull FirebaseRecyclerOptions<MemberRenewalModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MembersDirectoryViewHolder2 holder, int position, @NonNull MemberRenewalModel model) {
        holder.name.setText(model.getName());
        holder.company_name.setText(model.getCompanyname());
        holder.department.setVisibility(View.GONE);
        String Purl="https://firebasestorage.googleapis.com/v0/b/iea-app.appspot.com/o/default_profile_picture.jpg?alt=media&token=af41ca91-9929-46b5-b9a3-e8ff6c258495";
        Glide.with(holder.img.getContext()).load(Purl).into(holder.img);

        holder.memberRenewalView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), MemberRenewalDetail.class);
            intent.putExtra("memberRenewalKey", getRef(position).getKey());
            view.getContext().startActivity(intent);
        });

    }

    @NonNull
    @Override
    public MembersDirectoryViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_approval_item,parent,false);
        return new MembersDirectoryViewHolder2(view);
    }

    static class MembersDirectoryViewHolder2 extends RecyclerView.ViewHolder{

        ImageView img;
        TextView name, company_name, department;
        View memberRenewalView;

        public MembersDirectoryViewHolder2(@NonNull View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.members_approval_profile_picture);
            name = (TextView) itemView.findViewById(R.id.members_approval_name);
            company_name = (TextView) itemView.findViewById(R.id.members_approval_company_name);
            department = (TextView) itemView.findViewById(R.id.members_approval_department_name);
            memberRenewalView = itemView;
        }
    }
}
