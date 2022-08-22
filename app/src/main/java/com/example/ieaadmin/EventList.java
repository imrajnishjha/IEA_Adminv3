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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventList extends AppCompatActivity {

    RecyclerView scheduleEventRV,pastEventRV;
    EventListAdapter eventListAdapter;
    PastEventListAdapter pastEventListAdapter;
    CircleImageView  newEventICon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        newEventICon=findViewById(R.id.newEventIcon);

        scheduleEventRV = (RecyclerView) findViewById(R.id.scheduleEventRecycleView);
        scheduleEventRV.setLayoutManager(new WrapContentLinearLayoutManager(this));

        pastEventRV = (RecyclerView) findViewById(R.id.pastEventRecycleView);
        pastEventRV.setLayoutManager(new WrapContentLinearLayoutManager(this));

        FirebaseRecyclerOptions<EventDetailModel> options = new  FirebaseRecyclerOptions.Builder<EventDetailModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Events"), EventDetailModel.class)
                .build();

        FirebaseRecyclerOptions<PastEventModel> pastOptions = new  FirebaseRecyclerOptions.Builder<PastEventModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Past Events"), PastEventModel.class)
                .build();

        eventListAdapter = new EventListAdapter(options);
        scheduleEventRV.setAdapter(eventListAdapter);
        pastEventListAdapter = new PastEventListAdapter(pastOptions);
        pastEventRV.setAdapter(pastEventListAdapter);
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
        pastEventListAdapter.startListening();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        eventListAdapter.startListening();
        pastEventListAdapter.startListening();
    }

    public static int dateCompare(String date) {
        int catalog_outdated =0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        Date strDate = null;
        try {
            strDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (new Date().after(strDate)) {
            catalog_outdated = 1;
        }
        return catalog_outdated;
    }
}