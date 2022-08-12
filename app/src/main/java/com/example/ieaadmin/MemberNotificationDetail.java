package com.example.ieaadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MemberNotificationDetail extends AppCompatActivity {


    TextView notificationTitle,notificationDescription;
    DatabaseReference databaseReference;
    AppCompatButton notificationDetailBackBtn;
    String ownerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_notification_detail);
        notificationDescription=findViewById(R.id.notification_descripiton_editText);
        notificationTitle= findViewById(R.id.notification_title_editText);
        notificationDetailBackBtn = findViewById(R.id.notification_details_back_button);
        String notificationKey = getIntent().getStringExtra("NotificationItemKey");

        ownerEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString().replaceAll("\\.","%7");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Core Team Notification").child(notificationKey);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Titlestr = Objects.requireNonNull(snapshot.child("notificationTitle").getValue()).toString();
                String Descriptionstr = Objects.requireNonNull(snapshot.child("notificationContent").getValue()).toString();

                notificationDescription.setText(Descriptionstr);
                notificationTitle.setText(Titlestr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        notificationDetailBackBtn.setOnClickListener(view -> finish());
    }
}