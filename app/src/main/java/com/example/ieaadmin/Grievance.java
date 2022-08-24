package com.example.ieaadmin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class Grievance extends AppCompatActivity {
    RecyclerView grievanceRecyclerView;
    GrievanceAdapter grievanceAdapter;
    FirebaseRecyclerOptions<GrievanceModel> options;
    AppCompatButton grievanceBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievance);

        grievanceRecyclerView = (RecyclerView) findViewById(R.id.grievances_recycler_view);
        WrapContentLinearLayoutManager wrapContentLinearLayoutManager = new WrapContentLinearLayoutManager(this);
        wrapContentLinearLayoutManager.setReverseLayout(true);
        wrapContentLinearLayoutManager.setStackFromEnd(true);
        grievanceRecyclerView.setLayoutManager(wrapContentLinearLayoutManager);
        grievanceBackButton = findViewById(R.id.grievance_back_button);

        options = new FirebaseRecyclerOptions.Builder<GrievanceModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Unsolved Grievances"), GrievanceModel.class)
                .build();

        grievanceAdapter = new GrievanceAdapter(options);
        grievanceRecyclerView.setAdapter(grievanceAdapter);

        grievanceBackButton.setOnClickListener(view -> {
            startActivity(new Intent(this,explore_menu.class));
            finish();
        });
    }

    public static class WrapContentLinearLayoutManager extends LinearLayoutManager {
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
        grievanceAdapter.startListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Grievance.this,explore_menu.class));
        finish();
    }

}