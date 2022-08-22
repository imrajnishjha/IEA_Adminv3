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
    TextView eventTitleTv, eventDateTv, eventTimeTv, eventDescriptionTv, joinEventYes, joinEventNo, eventMemberTextTv;
    RecyclerView eventMembersRv, pastEventPhotosRv;
    FirebaseRecyclerOptions<EventMemberItemModel> options;
    FirebaseRecyclerOptions<PastEventPhotoModel> pastEvenPhotoOptions;
    EventMemberItemAdapter eventMemberItemAdapter;
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
        eventMembersRv = findViewById(R.id.event_members_rv);
        eventDetailImg = findViewById(R.id.event_detail_img);
        eventDetailsBackButton = findViewById(R.id.events_detail_back_btn);
        eventMemberTextTv = findViewById(R.id.event_members);
        addEventPhotosBtn = findViewById(R.id.add_event_photos_btn);
        addEventImagesFrameLayout = findViewById(R.id.add_event_images_frame_layout);
        pastEventPhotosRv = findViewById(R.id.past_event_photos_rv);

        addMyselfDialog = new Dialog(this);

        pastEventPhotosRv.setLayoutManager(new MembersDirectory.WrapContentLinearLayoutManager(PastEventDetails.this, LinearLayoutManager.HORIZONTAL, false));
        pastEvenPhotoOptions = new FirebaseRecyclerOptions.Builder<PastEventPhotoModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Events/" + EventItemKey + "/image_uris"), PastEventPhotoModel.class)
                .build();

        pastEventPhotoAdapter = new PastEventPhotoAdapter(pastEvenPhotoOptions);
        pastEventPhotosRv.setAdapter(pastEventPhotoAdapter);
        pastEventPhotoAdapter.startListening();

        eventsRef = FirebaseDatabase.getInstance().getReference().child("Events/" + EventItemKey);
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        eventMembersRv.setLayoutManager(new MembersDirectory.WrapContentLinearLayoutManager(PastEventDetails.this, LinearLayoutManager.HORIZONTAL, false));
        options = new FirebaseRecyclerOptions.Builder<EventMemberItemModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Events/" + EventItemKey + "/members"), EventMemberItemModel.class)
                .build();

        eventMemberItemAdapter = new EventMemberItemAdapter(options);
        eventMembersRv.setAdapter(eventMemberItemAdapter);

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
        eventMemberItemAdapter.startListening();
        pastEventPhotoAdapter.startListening();
    }


    @Override
    protected void onResume() {
        super.onResume();
        eventMemberItemAdapter.startListening();
        pastEventPhotoAdapter.startListening();
    }

    class EventMemberItemAdapter extends FirebaseRecyclerAdapter<EventMemberItemModel, EventMemberItemAdapter.EventMemberItemViewHolder> {
        Dialog notComingToEventDialog;

        public EventMemberItemAdapter(@NonNull FirebaseRecyclerOptions<EventMemberItemModel> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull com.example.ieaadmin.PastEventDetails.EventMemberItemAdapter.EventMemberItemViewHolder holder, int position, @NonNull EventMemberItemModel model) {
            notComingToEventDialog = new Dialog(PastEventDetails.this);

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            Glide.with(getApplicationContext())
                    .load(model.getImageUrl())
                    .circleCrop()
                    .error(R.drawable.iea_logo)
                    .placeholder(R.drawable.iea_logo)
                    .into(holder.recyclerMemberItemImg);

            holder.eventMemberItem.setOnClickListener(view -> {
                TextView coming, notComing;
                if (Objects.requireNonNull(getRef(position).getKey()).equals(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()).replaceAll("\\.", "%7"))) {
                    LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    @SuppressLint("InflateParams") View removeFromEventView = inflater.inflate(R.layout.remove_from_event_popup, null);

                    coming = removeFromEventView.findViewById(R.id.remove_from_event_no);
                    notComing = removeFromEventView.findViewById(R.id.remove_from_event_yes);

                    notComingToEventDialog.setContentView(removeFromEventView);
                    notComingToEventDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    notComingToEventDialog.show();

                    coming.setOnClickListener(v -> notComingToEventDialog.dismiss());
                    notComing.setOnClickListener(v -> {
                        FirebaseDatabase.getInstance().getReference("Events/" + EventItemKey + "/members/" + Objects.requireNonNull(mAuth.getCurrentUser().getEmail()).replaceAll("\\.", "%7")).removeValue();
                        Toast.makeText(PastEventDetails.this, "You have been removed from the list.", Toast.LENGTH_SHORT).show();
                        notComingToEventDialog.dismiss();
                    });
                } else {
                    view.getContext().startActivity(new Intent(view.getContext(), MemberDirectoryDetail.class).putExtra("MemberItemKey", model.getEmail().replaceAll("\\.", "%7")));
                }
            });
        }

        @NonNull
        @Override
        public com.example.ieaadmin.PastEventDetails.EventMemberItemAdapter.EventMemberItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View eventItemMemberView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_member_item, parent, false);
            return new com.example.ieaadmin.PastEventDetails.EventMemberItemAdapter.EventMemberItemViewHolder(eventItemMemberView);
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public class EventMemberItemViewHolder extends RecyclerView.ViewHolder {
            ImageView recyclerMemberItemImg;
            View eventMemberItem;

            public EventMemberItemViewHolder(@NonNull View itemView) {
                super(itemView);

                recyclerMemberItemImg = itemView.findViewById(R.id.event_member_item_img);
                eventMemberItem = itemView;
            }

        }
    }
}