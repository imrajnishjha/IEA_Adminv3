package com.example.ieaadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class ReferDetails extends AppCompatActivity {

    TextView referName,referEmail,referCompanyName,referPhoneno,referalEmail;
    AppCompatButton removerRefer,setReferStatusBtn;
    String NameStr,ContactnoStr,Emailstr,CompanyNameStr,StatusStr,UserEmailStr;
    AutoCompleteTextView referStatusField;
    DatabaseReference DRefer,MRefer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_details);

        DRefer = FirebaseDatabase.getInstance().getReference().child("Refer");

        referName = findViewById(R.id.refer_name_Text);
        referEmail= findViewById(R.id.refer_email_address_Text);
        referCompanyName = findViewById(R.id.refer_company_name_Text);
        referPhoneno = findViewById(R.id.refer_contact_number_Text);
        referalEmail = findViewById(R.id.referal_email_address_Text);
        removerRefer = findViewById(R.id.remove_refer_btn);
        referStatusField = findViewById(R.id.refer_status_field);
        setReferStatusBtn = findViewById(R.id.refer_set_status_btn);

        dropdownInit();

        String referKey = getIntent().getStringExtra("ReferItemKey");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Refer");

        ref.child(referKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try {
                    NameStr= Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                    Emailstr= Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                    ContactnoStr = Objects.requireNonNull(snapshot.child("contactno").getValue()).toString();
                    CompanyNameStr =Objects.requireNonNull(snapshot.child("companyname").getValue()).toString();
                    UserEmailStr = Objects.requireNonNull(snapshot.child("useremail").getValue()).toString();
                    StatusStr = Objects.requireNonNull(snapshot.child("status").getValue()).toString();

                    MRefer = FirebaseDatabase.getInstance().getReference().child("Refer by Member").child(UserEmailStr.replaceAll("\\.","%7"));

                    referName.setText(NameStr);
                    referEmail.setText(Emailstr);
                    referCompanyName.setText(CompanyNameStr);
                    referPhoneno.setText(ContactnoStr);
                    referalEmail.setText(UserEmailStr);

                    switch (StatusStr) {
                        case "In Review":
                            referStatusField.setHint("In Review");
                            break;
                        case "Reviewed":
                            referStatusField.setHint("Reviewed");
                            break;
                        case "Rejected":
                            referStatusField.setHint("Rejected");
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setReferStatusBtn.setOnClickListener(view -> {
            boolean sendNotification = false;
            if(referStatusField.getText().toString().equals("In Review")&&!StatusStr.equals("In Review")){
                HashMap referStatusHash = new HashMap();
                referStatusHash.put("status", "In Review");

                DRefer.child(referKey).updateChildren(referStatusHash);
                MRefer.child(referKey).updateChildren(referStatusHash);

                sendNotification= true;

            } else if(referStatusField.getText().toString().equals("Reviewed")&&!StatusStr.equals("Reviewed")){
                HashMap referStatusHash = new HashMap();
                referStatusHash.put("status", "Reviewed");

                DRefer.child(referKey).updateChildren(referStatusHash);
                MRefer.child(referKey).updateChildren(referStatusHash);

                sendNotification= true;
            } else if(referStatusField.getText().toString().equals("Rejected")&&!StatusStr.equals("Rejected")){
                HashMap referStatusHash = new HashMap();
                referStatusHash.put("status", "Rejected");

                DRefer.child(referKey).updateChildren(referStatusHash);
                MRefer.child(referKey).updateChildren(referStatusHash);

                sendNotification= true;
            }

            if (sendNotification) {
                Toasty.normal(this, "Status changed", R.drawable.notification_icon).show();
                FirebaseDatabase.getInstance().getReference().child("Registered Users/" + UserEmailStr.replaceAll("\\.", "%7")).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String grievanceUserToken = Objects.requireNonNull(snapshot.child("user_token").getValue()).toString();
                        FcmNotificationsSender grievanceNotificationSender = new FcmNotificationsSender(grievanceUserToken, "IEA Refer Update", "Your Refer is " + referStatusField.getText().toString(), getApplicationContext(), ReferDetails.this);
                        grievanceNotificationSender.SendNotifications();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });


        removerRefer.setOnClickListener(view -> {
            ref.child(referKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
//                    startActivity(new Intent(ReferDetails.this,Refer.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    Toast.makeText(ReferDetails.this, "Refer has been removed.", Toast.LENGTH_SHORT).show();
                    HashMap referStatusHash = new HashMap();
                    referStatusHash.put("status", "Rejected");
                    MRefer.child(referKey).updateChildren(referStatusHash);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ReferDetails.this, "Some error occured", Toast.LENGTH_SHORT).show();
                }
            });

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        dropdownInit();
    }

    public void dropdownInit() {
        String[] grievance_departments = getResources().getStringArray(R.array.referStatusArray);
        ArrayAdapter<String> arrayAdapterDepartments = new ArrayAdapter<>(getBaseContext(), R.layout.drop_down_item, grievance_departments);
        AutoCompleteTextView autoCompleteTextViewDepartments = findViewById(R.id.refer_status_field);
        autoCompleteTextViewDepartments.setAdapter(arrayAdapterDepartments);
    }
}