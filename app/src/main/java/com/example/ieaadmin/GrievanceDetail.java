package com.example.ieaadmin;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import es.dmoral.toasty.Toasty;

public class GrievanceDetail extends AppCompatActivity {

    TextView grievanceEmailTv, grievanceDepartmentTv, grievanceStatusTv, grievanceSubjectTv;
    AppCompatButton grievanceSetStatusBtn, grievanceUpdateBtn,rejectBtn;
    FirebaseDatabase solvedGrievanceRoot;
    DatabaseReference solvedGrievanceRef, rejectedGrievanceRef, ref2;
    String grievanceEmailStr, grievanceDepartmentStr, grievanceDescriptionStr, grievanceStatusStr, grievanceSubjectStr,grievancePurlStr , grievanceItemKey;
    AutoCompleteTextView grievanceStatusField;
    CardView grievanceShareEmailCv, grievanceShareTwitterCv, grievanceShareFbCv, grievanceShareInstagramCv;
    EditText grievanceDescriptionEdtTxt,RejectionReasonText;
    ImageView grievanceIV;
    Dialog RejectionMailDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievance_detail);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Unsolved Grievances");

        grievanceEmailTv = findViewById(R.id.grievance_email_detail_tv);
        grievanceDepartmentTv = findViewById(R.id.grievance_department_detail_tv);
        grievanceStatusTv = findViewById(R.id.grievance_status_detail_tv);
        grievanceDescriptionEdtTxt = findViewById(R.id.grievance_description_detail_edtTxt);
        grievanceSubjectTv = findViewById(R.id.grievance_subject_detail_tv);
        grievanceStatusField = findViewById(R.id.grievance_status_field);
        grievanceSetStatusBtn = findViewById(R.id.grievance_set_status_btn);
        grievanceShareEmailCv = findViewById(R.id.grievance_share_email_cv);
        grievanceShareTwitterCv = findViewById(R.id.grievance_share_twitter_cv);
        grievanceShareFbCv = findViewById(R.id.grievance_share_fb_cv);
        grievanceShareInstagramCv = findViewById(R.id.grievance_share_instagram_cv);
        grievanceUpdateBtn = findViewById(R.id.grievance_update_btn);
        grievanceIV = findViewById(R.id.grievance_imageview);
        RejectionMailDialog = new Dialog(this);

        dropdownInit();

        grievanceItemKey = getIntent().getStringExtra("GrievanceItemKey");

        ref.child(grievanceItemKey).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    grievanceEmailStr = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                    grievanceDepartmentStr = Objects.requireNonNull(snapshot.child("department").getValue()).toString();
                    grievanceDescriptionStr = Objects.requireNonNull(snapshot.child("complain").getValue()).toString();
                    grievanceStatusStr = Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                    grievanceSubjectStr = Objects.requireNonNull(snapshot.child("subject").getValue()).toString();
                    grievancePurlStr = Objects.requireNonNull(snapshot.child("purl").getValue()).toString();


                    Log.d("Email string", grievancePurlStr);

                    ref2 = FirebaseDatabase.getInstance().getReference().child("Unresolved Grievances").child(grievanceEmailStr.replaceAll("\\.", "%7"));

                    grievanceEmailTv.setText("User: " + grievanceEmailStr);
                    grievanceDescriptionEdtTxt.setText(grievanceDescriptionStr);
                    grievanceDepartmentTv.setText("Department: " + grievanceDepartmentStr);
                    grievanceStatusTv.setText("Status: " + grievanceStatusStr);
                    grievanceSubjectTv.setText("Subject: " + grievanceSubjectStr);

                    if(!grievancePurlStr.isEmpty()){
                        grievanceIV.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext())
                                .load(grievancePurlStr)
                                .placeholder(R.drawable.iea_logo)
                                .error(R.drawable.iea_logo)
                                .into(grievanceIV);
                    }



                    switch (grievanceStatusStr) {
                        case "On Progress":
                            grievanceStatusField.setHint("On Progress");
                            break;
                        case "Solved":
                            grievanceStatusField.setHint("Solved");
                            break;
                        case "Under Review":
                            grievanceStatusField.setHint("Under Review");
                            break;
                        case "Unsolved":
                            grievanceStatusField.setHint("Unsolved");
                            break;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        grievanceSetStatusBtn.setOnClickListener(view -> {
            AtomicBoolean sendNotification = new AtomicBoolean(false);

            if (grievanceStatusField.getText().toString().equals("On Progress") && !grievanceStatusStr.equals("On Progress")) {
                HashMap grievanceStatusHash = new HashMap();
                grievanceStatusHash.put("status", "On Progress");

                ref.child(grievanceItemKey).updateChildren(grievanceStatusHash);
                ref2.child(grievanceItemKey).updateChildren(grievanceStatusHash);

                sendNotification.set(true);

                solvedGrievanceRoot = FirebaseDatabase.getInstance();
                solvedGrievanceRef = solvedGrievanceRoot.getReference("Solved Grievance").child(grievanceEmailStr.replaceAll("\\.", "%7"))
                        .child(grievanceItemKey);
                solvedGrievanceRef.removeValue();
                Log.d("On progress", "On progress working");
            } else if (grievanceStatusField.getText().toString().equals("Under Review") && !grievanceStatusStr.equals("Under Review")) {
                HashMap grievanceStatusHash = new HashMap();
                grievanceStatusHash.put("status", "Under Review");

                ref.child(grievanceItemKey).updateChildren(grievanceStatusHash);
                ref2.child(grievanceItemKey).updateChildren(grievanceStatusHash);

                sendNotification.set(true);

                solvedGrievanceRoot = FirebaseDatabase.getInstance();
                solvedGrievanceRef = solvedGrievanceRoot.getReference("Solved Grievance").child(grievanceEmailStr.replaceAll("\\.", "%7"))
                        .child(grievanceItemKey);
                solvedGrievanceRef.removeValue();
                Log.d("On progress", "Under Review working");
            } else if (grievanceStatusField.getText().toString().equals("Rejected") && !grievanceStatusStr.equals("Rejected")) {
                LayoutInflater inflater = getLayoutInflater();
                @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.rejection_reason_popup, null);
                RejectionReasonText = v.findViewById(R.id.rejectionReason_text);
                rejectBtn = v.findViewById(R.id.rejection_btn);
                RejectionMailDialog.setContentView(v);
                RejectionMailDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                RejectionMailDialog.show();
                rejectBtn.setOnClickListener(v1 -> {
                    if (!RejectionReasonText.getText().toString().isEmpty()) {
                        String rejectionReason = RejectionReasonText.getText().toString();
                        HashMap grievanceStatusHash = new HashMap();
                        grievanceStatusHash.put("status", "Rejected");

                        ref.child(grievanceItemKey).updateChildren(grievanceStatusHash);
                        ref2.child(grievanceItemKey).updateChildren(grievanceStatusHash);
                        RejectionMailDialog.dismiss();

                        finish();

                        sendNotification.set(true);

                        DatabaseReference rejectedGrievanceAddRef = FirebaseDatabase.getInstance().getReference("Rejected Grievance").child(grievanceEmailStr.replaceAll("\\.", "%7"))
                                .child(grievanceItemKey);
                        GrievanceModel solvedModel = new GrievanceModel(grievanceDescriptionStr, grievanceDepartmentStr, grievanceEmailStr, "solved");
                        rejectedGrievanceAddRef.setValue(solvedModel);

                        solvedGrievanceRoot = FirebaseDatabase.getInstance();
                        solvedGrievanceRef = solvedGrievanceRoot.getReference("Solved Grievance").child(grievanceEmailStr.replaceAll("\\.", "%7"))
                                .child(grievanceItemKey);
                        solvedGrievanceRef.removeValue();
                        Log.d("On progress", "Rejected working");

                        rejectedGrievanceRef = solvedGrievanceRoot.getReference("Unsolved Grievances").child(grievanceItemKey);
                        rejectedGrievanceRef.removeValue();
                        sendRejectionEmail(rejectionReason);

                    } else {
                        RejectionReasonText.setError("Reason can not be empty");
                        RejectionReasonText.requestFocus();
                    }


                });

//                finish();

                Log.d("Unresolved", "Unresolved removal" + rejectedGrievanceRef);
            } else if (grievanceStatusField.getText().toString().equals("Solved") && !grievanceStatusStr.equals("Solved")) {
                HashMap grievanceStatusHash = new HashMap();
                grievanceStatusHash.put("status", "Solved");

                ref.child(grievanceItemKey).updateChildren(grievanceStatusHash);
                ref2.child(grievanceItemKey).updateChildren(grievanceStatusHash);

                sendNotification.set(true);

                solvedGrievanceRoot = FirebaseDatabase.getInstance();
                solvedGrievanceRef = solvedGrievanceRoot.getReference("Solved Grievance").child(grievanceEmailStr.replaceAll("\\.", "%7"))
                        .child(grievanceItemKey);
                GrievanceModel solvedModel = new GrievanceModel(grievanceDescriptionStr, grievanceDepartmentStr, grievanceEmailStr, "solved");
                solvedGrievanceRef.setValue(solvedModel);
            }

            if (sendNotification.get()) {
                Toasty.normal(this, "Status changed", R.drawable.notification_icon).show();
                FirebaseDatabase.getInstance().getReference().child("Registered Users/" + grievanceEmailStr.replaceAll("\\.", "%7")).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String grievanceUserToken = Objects.requireNonNull(snapshot.child("user_token").getValue()).toString();
                        FcmNotificationsSender grievanceNotificationSender = new FcmNotificationsSender(grievanceUserToken, "IEA Grievance Update", "Your grievance is " + grievanceStatusField.getText().toString(), getApplicationContext(), GrievanceDetail.this);
                        grievanceNotificationSender.SendNotifications();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });

        grievanceShareEmailCv.setOnClickListener(view -> shareGrievanceEmail());

        grievanceShareTwitterCv.setOnClickListener(view -> shareGrievanceTwitter());

        grievanceShareFbCv.setOnClickListener(view -> shareGrievanceFb());

        grievanceShareInstagramCv.setOnClickListener(view -> shareGrievanceInstagram());

        grievanceUpdateBtn.setOnClickListener(view -> updateGrievance());
    }

    private void updateGrievance() {
        HashMap grievanceDescHash = new HashMap();
        grievanceDescHash.put("complain", grievanceDescriptionEdtTxt.getText().toString());
        FirebaseDatabase.getInstance().getReference("Unsolved Grievances/" + grievanceItemKey).updateChildren(grievanceDescHash).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toasty.normal(GrievanceDetail.this, "Complain updated", R.drawable.iea_logo).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.normal(GrievanceDetail.this, "Please try again", R.drawable.iea_logo).show();
            }
        });
    }

    private void shareGrievanceInstagram() {
        Intent shareIntent;
        String shareText = grievanceSubjectStr + "\n\n" + grievanceDescriptionStr;
        if (doesPackageExist("com.instagram.android")) {
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.setPackage("com.instagram.android");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(shareIntent);
        } else if (doesPackageExist("com.instagram.lite")) {
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.setPackage("com.instagram.lite");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(shareIntent);
        } else {
            Toasty.normal(this, "Please install Instagram", R.drawable.iea_logo).show();
        }
    }

    private void shareGrievanceFb() {
        Intent shareIntent;
        String shareText = grievanceSubjectStr + "\n\n" + grievanceDescriptionStr;
        if (doesPackageExist("com.facebook.katana")) {
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.setPackage("com.facebook.katana");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(shareIntent);
        } else if (doesPackageExist("com.facebook.lite")) {
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.setPackage("com.facebook.lite");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(shareIntent);
        } else {
            Toasty.normal(this, "Please install Facebook", R.drawable.notification_icon).show();
        }
    }

    private void shareGrievanceTwitter() {
        Intent shareIntent;
        String shareText = grievanceSubjectStr + "\n" + grievanceDescriptionStr;
        if (doesPackageExist("com.twitter.android")) {
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.setPackage("com.twitter.android");
            shareIntent.putExtra(Intent.EXTRA_TITLE, grievanceSubjectStr);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        } else if (doesPackageExist("com.twitter.android.lite")) {
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.setPackage("com.twitter.android.lite");
            shareIntent.putExtra(Intent.EXTRA_TITLE, grievanceSubjectStr);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        }else {
            String tweetUrl = "https://twitter.com/intent/tweet?text=" + grievanceSubjectStr + ": " + grievanceDescriptionStr;
            Uri uri = Uri.parse(tweetUrl);
            shareIntent = new Intent(Intent.ACTION_VIEW, uri);
        }
        startActivity(shareIntent);
    }

    private boolean doesPackageExist(String packageName) {
        List<ApplicationInfo> packages;
        PackageManager pm;

        pm = getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(packageName))
                return true;
        }
        return false;
    }

    private void shareGrievanceEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, grievanceSubjectStr);
        emailIntent.putExtra(Intent.EXTRA_TEXT, grievanceDescriptionStr);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toasty.normal(this, "There is no email client installed.", R.drawable.notification_icon).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        dropdownInit();
    }

    public void dropdownInit() {
        String[] grievance_departments = getResources().getStringArray(R.array.grievanceStatusArray);
        ArrayAdapter<String> arrayAdapterDepartments = new ArrayAdapter<>(getBaseContext(), R.layout.drop_down_item, grievance_departments);
        AutoCompleteTextView autoCompleteTextViewDepartments = findViewById(R.id.grievance_status_field);
        autoCompleteTextViewDepartments.setAdapter(arrayAdapterDepartments);
    }

    private void sendRejectionEmail(String rejectionReason) {


        Log.i("Send email", "");

        String[] TO = {grievanceEmailStr};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "IEA Membership Rejected");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Your membership got rejected due to following reason \n\n" + rejectionReason);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(GrievanceDetail.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}