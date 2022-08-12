package com.example.ieaadmin;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class NotificationBroadcast extends AppCompatActivity {
    EditText notificationBroadcastTitleEdtTxt, notificationBroadcastContentEdtTxt;
    AppCompatButton notificationBroadcastSendBtn, notificationBroadcastBackBtn, notificationReceiverChooseBtn;
    Dialog notificationReceiverChooserPopup;
    RadioGroup notificationReceiverChooserRg;
    RadioButton notificationReceiverRb;
    AutoCompleteTextView selectIndustryAct;
    TextInputLayout industryTypeOutbox;
    String receiverOptionIDHolder, receiverOptionValueStr = "All";
    final DatabaseReference memberNotificationRef = FirebaseDatabase.getInstance().getReference("Notification");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_broadcast);

        notificationBroadcastTitleEdtTxt = findViewById(R.id.notification_broadcast_title_edtTxt);
        notificationBroadcastContentEdtTxt = findViewById(R.id.notification_broadcast_content_edtTxt);
        notificationBroadcastBackBtn = findViewById(R.id.notification_broadcast_back_icon);
        notificationBroadcastSendBtn = findViewById(R.id.notification_broadcast_send_btn);
        notificationReceiverChooseBtn = findViewById(R.id.notification_receiver_choose_btn);
        notificationReceiverChooserPopup = new Dialog(this);
        receiverOptionIDHolder = String.valueOf(R.id.broadcast_all_member_rBtn);
        selectIndustryAct = findViewById(R.id.industry_type_autocomplete);
        industryTypeOutbox = findViewById(R.id.industry_type_outbox);

        dropdownInit();

        notificationBroadcastBackBtn.setOnClickListener(view -> finish());

        notificationBroadcastSendBtn.setOnClickListener(view -> {

            if (notificationBroadcastTitleEdtTxt.getText().toString().isEmpty()) {
                notificationBroadcastTitleEdtTxt.setError("Please provide title");
                notificationBroadcastTitleEdtTxt.requestFocus();
            } else if (notificationBroadcastContentEdtTxt.getText().toString().isEmpty()) {
                notificationBroadcastContentEdtTxt.setError("Please provide content");
                notificationBroadcastContentEdtTxt.requestFocus();
            } else {
                if (receiverOptionValueStr.equals("All")) {
                    sendNotificationToAll();
                } else if (receiverOptionValueStr.equals("Core Team")) {
                    sendNotificationToCoreTeam();
                } else if (receiverOptionValueStr.equals("By Industry Type")) {
                    if(selectIndustryAct.getText().toString().isEmpty()) {
                        selectIndustryAct.setError("Select a industry type");
                        selectIndustryAct.requestFocus();
                    } else {
                        sendNotificationToIndustryType();
                    }
                }
            }
        });

        notificationReceiverChooseBtn.setOnClickListener(view -> openNotificationChooserPopup());
    }

    private void sendNotificationToIndustryType() {
        FirebaseDatabase.getInstance().getReference("Industry Notification Token/" + selectIndustryAct.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot memberToken : snapshot.getChildren()) {
                        String memberTokenStr = Objects.requireNonNull(memberToken.getValue()).toString();

                        FcmNotificationsSender notificationSender = new FcmNotificationsSender(memberTokenStr,
                                notificationBroadcastTitleEdtTxt.getText().toString(),
                                notificationBroadcastContentEdtTxt.getText().toString(),
                                getApplicationContext(),
                                NotificationBroadcast.this);

                        notificationSender.SendNotifications();

                        Toast.makeText(NotificationBroadcast.this, "Notification sent to " + selectIndustryAct.getText().toString(), Toast.LENGTH_SHORT).show();

                        Date date = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
                        String notificationDate = df.format(date);

                        for (DataSnapshot memberEmail : snapshot.getChildren()) {
                            String memberEmailStr = memberEmail.getKey();

                            HashMap memberNotification = new HashMap();
                            memberNotification.put("notificationContent", notificationBroadcastTitleEdtTxt.getText().toString());
                            memberNotification.put("notificationDate", notificationBroadcastContentEdtTxt.getText().toString());
                            memberNotification.put("notificationTitle", notificationDate);

                            FirebaseDatabase.getInstance().getReference("Notification/" + memberEmailStr)
                                    .push().updateChildren(memberNotification);
                        }
                    }
                }else {
                    Toast.makeText(NotificationBroadcast.this, "No Member exists for the selected industry type", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void dropdownInit() {
        String[] departments = getResources().getStringArray(R.array.department);
        ArrayAdapter<String> arrayAdapterDepartments = new ArrayAdapter<>(getBaseContext(), R.layout.drop_down_item, departments);
        selectIndustryAct.setAdapter(arrayAdapterDepartments);
    }

    private void sendNotificationToCoreTeam() {
        FirebaseDatabase.getInstance().getReference("Core Member Token").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot coreToken : snapshot.getChildren()) {
                    String coreTokenStr = Objects.requireNonNull(coreToken.getValue()).toString();

                    FcmNotificationsSender notificationSender = new FcmNotificationsSender(coreTokenStr,
                            notificationBroadcastTitleEdtTxt.getText().toString(),
                            notificationBroadcastContentEdtTxt.getText().toString(),
                            getApplicationContext(),
                            NotificationBroadcast.this);

                    notificationSender.SendNotifications();

                }
                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
                String notificationDate = df.format(date);

                MemberNotificationModel memberNotificationModel = new MemberNotificationModel(notificationBroadcastTitleEdtTxt.getText().toString(),
                        notificationBroadcastContentEdtTxt.getText().toString(),
                        notificationDate);

                FirebaseDatabase.getInstance().getReference("Core Team Notification")
                        .push().setValue(memberNotificationModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Toast.makeText(NotificationBroadcast.this, "Notification sent to Core Team", Toast.LENGTH_SHORT).show();
    }

    private void sendNotificationToAll() {
        FirebaseDatabase.getInstance().getReference("Member Directory Token").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot memberToken : snapshot.getChildren()) {
                    String memberTokenStr = Objects.requireNonNull(memberToken.getValue()).toString();

                    FcmNotificationsSender grievanceNotificationSender = new FcmNotificationsSender(memberTokenStr,
                            notificationBroadcastTitleEdtTxt.getText().toString(),
                            notificationBroadcastContentEdtTxt.getText().toString(),
                            getApplicationContext(),
                            NotificationBroadcast.this);

                    grievanceNotificationSender.SendNotifications();

                    Log.d("MemberToken", notificationBroadcastTitleEdtTxt.getText().toString());
                    Log.d("MemberToken", notificationBroadcastContentEdtTxt.getText().toString());
                }
                for (DataSnapshot memberEmail : snapshot.getChildren()) {
                    String memberEmailStr = memberEmail.getKey();

                    Date date = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
                    String notificationDate = df.format(date);

                    MemberNotificationModel memberNotificationModel = new MemberNotificationModel(notificationBroadcastTitleEdtTxt.getText().toString(),
                            notificationBroadcastContentEdtTxt.getText().toString(),
                            notificationDate);

                    memberNotificationRef.child(memberEmailStr).push().setValue(memberNotificationModel);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
        Toast.makeText(NotificationBroadcast.this, "Notification sent to all members.", Toast.LENGTH_SHORT).show();
    }

    private void openNotificationChooserPopup() {
        //inflating the layout
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.notification_receiver_filter_popup, null);
        notificationReceiverChooserPopup.setContentView(view);
        notificationReceiverChooserPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        notificationReceiverChooserPopup.show();

        //initializing the views
        notificationReceiverChooserRg = view.findViewById(R.id.notification_receiver_chooser_popup_rg);
        notificationReceiverRb = view.findViewById(R.id.broadcast_all_member_rBtn);
        AppCompatButton notificationReceiverOkayBtn = view.findViewById(R.id.notification_receiver_okay_btn);

        //checking the right radio button
        notificationReceiverChooserRg.check(Integer.parseInt(receiverOptionIDHolder));

        //"okay" button functionality
        notificationReceiverOkayBtn.setOnClickListener(view1 -> {
            receiverOptionIDHolder = String.valueOf(notificationReceiverChooserRg.getCheckedRadioButtonId());
            notificationReceiverChooserPopup.dismiss();
            notificationReceiverRb = view.findViewById(Integer.parseInt(receiverOptionIDHolder));
            receiverOptionValueStr = notificationReceiverRb.getText().toString();

            if (receiverOptionValueStr.equals("All")) {
                industryTypeOutbox.setVisibility(View.GONE);
                Log.d("Visibility", "Visibility working");
            } else if (receiverOptionValueStr.equals("Core Team")) {
                industryTypeOutbox.setVisibility(View.GONE);
                Log.d("Visibility", "Visibility working");
            } else if (receiverOptionValueStr.equals("By Industry Type")) {
                industryTypeOutbox.setVisibility(View.VISIBLE);
                Log.d("Visibility", "Visibility working");
                dropdownInit();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        dropdownInit();
    }
}