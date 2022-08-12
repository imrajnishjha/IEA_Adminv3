package com.example.ieaadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventList extends AppCompatActivity {

    RecyclerView scheduleEventRV;
    EventListAdapter eventListAdapter;
    CircleImageView  newEventICon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        newEventICon=findViewById(R.id.newEventIcon);

        scheduleEventRV = (RecyclerView) findViewById(R.id.scheduleEventRecycleView);
        scheduleEventRV.setLayoutManager(new WrapContentLinearLayoutManager(this));

        FirebaseRecyclerOptions<EventDetailModel> options = new  FirebaseRecyclerOptions.Builder<EventDetailModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Events"), EventDetailModel.class)
                .build();

        eventListAdapter = new EventListAdapter(options);
        scheduleEventRV.setAdapter(eventListAdapter);
        newEventICon.setOnClickListener(view -> startActivity(new Intent(EventList.this, EventDetail.class)));

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
        eventListAdapter.startListening();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        eventListAdapter.startListening();
    }
}