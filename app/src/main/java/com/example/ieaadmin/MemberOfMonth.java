package com.example.ieaadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class MemberOfMonth extends AppCompatActivity {
    RecyclerView membersRV;
    AppCompatButton MOMbackBtn;
    FirebaseRecyclerOptions options;
    MemberOfMonthAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_of_month);

        membersRV = findViewById(R.id.MOM_recycler_view);
        membersRV.setLayoutManager(new WrapContentLinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        MOMbackBtn = findViewById(R.id.MOM_back_button);
        MOMbackBtn.setOnClickListener(view -> finish());

        options = new FirebaseRecyclerOptions.Builder<MembersDirectoryModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Registered Users"), MembersDirectoryModel.class)
                .build();
        adapter = new MemberOfMonthAdapter(options);
        membersRV.setAdapter(adapter);


    }

    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("TAG", "meet a IOOBE in RecyclerView");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}