package com.example.ieaadmin;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class explore_menu extends AppCompatActivity {

    FirebaseAuth mAuth;
    AppCompatButton notificationImgBtn;
    TextView exploreUsername, Memberofmonthname, memberOfMonthDescriptionText,notificationText;
    ImageView logoutImg, userImage;
    CircleImageView MemberofmonthImg;
    CardView memberDirectoryCard, grievanceCard, newMembers, notificationBroadcast, event;
    Dialog exploreIeaContactDialog;
    DatabaseReference databaseReference;
    DatabaseReference MemberOfMonthref = FirebaseDatabase.getInstance().getReference("Member of Month");
    StorageReference storageProfilePicReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_menu);

        notificationText = findViewById(R.id.menu_notification_text);
        notificationImgBtn = findViewById(R.id.menu_notification_icon);
        Memberofmonthname = findViewById(R.id.description_username);
        MemberofmonthImg = findViewById(R.id.description_img);
        logoutImg = findViewById(R.id.logout_img);
        memberDirectoryCard = findViewById(R.id.member_directory);
        grievanceCard = findViewById(R.id.grievance);
        newMembers = findViewById(R.id.new_member);
        notificationBroadcast = findViewById(R.id.notification_broadcast);
        exploreIeaContactDialog = new Dialog(this);
        userImage = findViewById(R.id.user_img);
        event = findViewById(R.id.events);
        memberOfMonthDescriptionText = findViewById(R.id.description_text);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Registered Users");
        storageProfilePicReference = FirebaseStorage.getInstance().getReference();

        String userEmail = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();

        assert userEmail != null;
        String userEmailConverted = userEmail.replaceAll("\\.", "%7");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Registered Users/" + userEmailConverted);

        StorageReference fileRef = storageProfilePicReference.child("User Profile Pictures/" + mAuth.getCurrentUser().getEmail().toString() + "ProfilePicture");
        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(userImage.getContext())
                        .load(uri)
                        .placeholder(R.drawable.iea_logo)
                        .circleCrop()
                        .error(R.drawable.iea_logo)
                        .into(userImage);
            }
        });

        MemberOfMonthref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String UserNameStr = Objects.requireNonNull(snapshot.child("name").getValue().toString());
                String purl = Objects.requireNonNull(snapshot.child("purl").getValue().toString());
                String description = Objects.requireNonNull(snapshot.child("description").getValue().toString());
                Memberofmonthname.setText(UserNameStr);
                memberOfMonthDescriptionText.setText(description);
                Glide.with(getApplicationContext())
                        .load(purl)
                        .placeholder(R.drawable.iea_logo)
                        .circleCrop()
                        .error(R.drawable.iea_logo)
                        .into(MemberofmonthImg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        logoutImg.setOnClickListener(view -> {
            mAuth.signOut();
            finish();
        });


        memberDirectoryCard.setOnClickListener(view -> startActivity(new Intent(explore_menu.this, MembersDirectory.class)));

//        MemberofmonthImg.setOnClickListener(view -> startActivity(new Intent(explore_menu.this, MemberOfMonth.class)));

        grievanceCard.setOnClickListener(view -> startActivity(new Intent(explore_menu.this, Grievance.class)));

        newMembers.setOnClickListener(view -> startActivity(new Intent(explore_menu.this, member_approval.class)));

        event.setOnClickListener(view -> startActivity(new Intent(explore_menu.this, EventList.class)));

        userImage.setOnClickListener(view -> startActivity(new Intent(explore_menu.this, UserProfile.class)));

        notificationBroadcast.setOnClickListener(view -> startActivity(new Intent(explore_menu.this, NotificationBroadcast.class)));

        notificationImgBtn.setOnClickListener(view -> startActivity(new Intent(explore_menu.this,MembersNotification.class)));

        notificationText.setOnClickListener(view -> startActivity(new Intent(explore_menu.this,MembersNotification.class)));
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        finish();
//        overridePendingTransition(0, 0);
//        startActivity(getIntent());
//        overridePendingTransition(0, 0);
//    }

}