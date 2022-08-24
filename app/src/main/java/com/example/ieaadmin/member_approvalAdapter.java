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

public class member_approvalAdapter extends FirebaseRecyclerAdapter<UserRegistrationHelperClass,member_approvalAdapter.MembersDirectoryViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public member_approvalAdapter(@NonNull FirebaseRecyclerOptions<UserRegistrationHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MembersDirectoryViewHolder holder, int position, @NonNull UserRegistrationHelperClass model) {

        holder.name.setText(model.getFullname());
        holder.company_name.setText(model.getCompanyName());
        holder.department.setText(model.getDepartment());

        holder.memberApprovalView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), memberApprovalDetail.class);
            intent.putExtra("memberApprovalKey", getRef(position).getKey());
            view.getContext().startActivity(intent);
        });


    }

    @NonNull
    @Override
    public MembersDirectoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_approval_item,parent,false);
        return new MembersDirectoryViewHolder(view3);
    }

    static class MembersDirectoryViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView name, company_name, department;
        View memberApprovalView;

        public MembersDirectoryViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.members_approval_profile_picture);
            name = (TextView) itemView.findViewById(R.id.members_approval_name);
            company_name = (TextView) itemView.findViewById(R.id.members_approval_company_name);
            department = (TextView) itemView.findViewById(R.id.members_approval_department_name);
            memberApprovalView = itemView;
        }
    }
}