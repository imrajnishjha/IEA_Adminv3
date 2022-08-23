package com.example.ieaadmin;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.metrics.Event;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class PastEventDetails extends AppCompatActivity {
    String EventItemKey;
    TextView eventTitleTv, eventDateTv, eventTimeTv, eventDescriptionTv, joinEventYes, joinEventNo;
    RecyclerView  pastEventPhotosRv;
    FirebaseRecyclerOptions<EventMemberItemModel> options;
    FirebaseRecyclerOptions<PastEventPhotoModel> pastEvenPhotoOptions;
    DatabaseReference eventsRef;
    ImageView eventDetailImg;
    AppCompatButton eventDetailsBackButton, addEventPhotosBtn;
    Dialog addMyselfDialog;
    FirebaseAuth mAuth;
    FrameLayout addEventImagesFrameLayout;
    PastEventPhotoAdapter pastEventPhotoAdapter;

    String[] userEmail = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_event_details);

        EventItemKey = getIntent().getStringExtra("EventItemKey");

        eventTitleTv = findViewById(R.id.event_title_txt);
        eventDateTv = findViewById(R.id.event_date_txt);
        eventTimeTv = findViewById(R.id.event_time_txt);
        eventDescriptionTv = findViewById(R.id.event_description_txt);
        eventDetailImg = findViewById(R.id.event_detail_img);
        eventDetailsBackButton = findViewById(R.id.events_detail_back_btn);
        addEventPhotosBtn = findViewById(R.id.add_event_photos_btn);
        addEventImagesFrameLayout = findViewById(R.id.add_event_images_frame_layout);
        pastEventPhotosRv = findViewById(R.id.past_event_photos_rv);

        addMyselfDialog = new Dialog(this);

        pastEventPhotosRv.setLayoutManager(new MembersDirectory.WrapContentLinearLayoutManager(PastEventDetails.this, LinearLayoutManager.HORIZONTAL, false));
        pastEvenPhotoOptions = new FirebaseRecyclerOptions.Builder<PastEventPhotoModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Past Events/" + EventItemKey + "/image_uris"), PastEventPhotoModel.class)
                .build();

        pastEventPhotoAdapter = new PastEventPhotoAdapter(pastEvenPhotoOptions);
        pastEventPhotosRv.setAdapter(pastEventPhotoAdapter);
        pastEventPhotoAdapter.startListening();

        eventsRef = FirebaseDatabase.getInstance().getReference().child("Past Events/" + EventItemKey);
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    eventTitleTv.setText(Objects.requireNonNull(snapshot.child("title").getValue()).toString());
                    eventTimeTv.setText(Objects.requireNonNull(snapshot.child("time").getValue()).toString());
                    eventDateTv.setText(Objects.requireNonNull(snapshot.child("date").getValue()).toString());
                    eventDescriptionTv.setText(Objects.requireNonNull(snapshot.child("description").getValue()).toString());

                    Glide.with(getApplicationContext())
                            .load(Objects.requireNonNull(snapshot.child("imgUrl").getValue()).toString())
                            .error(R.drawable.iea_logo)
                            .placeholder(R.drawable.iea_logo)
                            .into(eventDetailImg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        eventDetailsBackButton.setOnClickListener(view -> finish());

        addEventPhotosBtn.setOnClickListener(view -> openAddEventPhotosFragment());


    }

    private void openAddEventPhotosFragment() {
        addEventImagesFrameLayout.setVisibility(View.VISIBLE);
        AddEventPhotosFragment addEventPhotosFragment = new AddEventPhotosFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle data = new Bundle();
        data.putString("EventItemKey", EventItemKey);

        addEventPhotosFragment.setArguments(data);
        fragmentTransaction.replace(R.id.add_event_images_frame_layout, addEventPhotosFragment).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        pastEventPhotoAdapter.startListening();
    }


    @Override
    protected void onResume() {
        super.onResume();
        pastEventPhotoAdapter.startListening();
    }

}