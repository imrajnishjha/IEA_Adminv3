package com.example.ieaadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class MembershipRenewal extends AppCompatActivity {

    RecyclerView memberRenewalRecyclerView;
    MemberRenewalAdapter renewalAdapter;
    AppCompatButton memberRenewal_babkbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_renewal);
        memberRenewal_babkbtn = findViewById(R.id.members_renewal_back_button);
        memberRenewalRecyclerView = findViewById(R.id.members_renewal_recycler_view);
        memberRenewal_babkbtn.setOnClickListener(v -> {
            startActivity(new Intent(MembershipRenewal.this,member_approval.class));
            finish();
        });

        member_approval.WrapContentLinearLayoutManager wrapContentLinearLayoutManager = new member_approval.WrapContentLinearLayoutManager(this);
        wrapContentLinearLayoutManager.setReverseLayout(true);
        wrapContentLinearLayoutManager.setStackFromEnd(true);
        memberRenewalRecyclerView.setLayoutManager(wrapContentLinearLayoutManager);

        FirebaseRecyclerOptions<MemberRenewalModel> options =
                new FirebaseRecyclerOptions.Builder<MemberRenewalModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Renewal Registry"), MemberRenewalModel.class)
                        .build();

        renewalAdapter = new MemberRenewalAdapter(options);
        memberRenewalRecyclerView.setAdapter(renewalAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        renewalAdapter.startListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MembershipRenewal.this,member_approval.class));
        finish();
    }
}