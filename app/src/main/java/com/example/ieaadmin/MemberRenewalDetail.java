package com.example.ieaadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
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

public class MemberRenewalDetail extends AppCompatActivity {

    AppCompatButton renewalBackBtn,renewalApproveBtn,renewalRejectBtn,rejectBtn;
    TextView renewalName,renewalEmail,renewalCompanyName,renewalMembershipType,renewalAmountLeft,renewalPersonPayment,yesbtn,nobtn;
    ImageView renewalProof;
    DatabaseReference renewalDataRef = FirebaseDatabase.getInstance().getReference("Renewal Registry");
    DatabaseReference registryDataRef = FirebaseDatabase.getInstance().getReference("Registered Users");
    String renewalKey,renewalNameStr,renewalEmailStr,renewalCompanyNameStr,renewalMembershipTypeStr,renewalAmountLeftStr,renewalPersonPaymentStr;
    EditText RejectionReasonText;
    Dialog RejectionMailDialog,confirmationDialog;
    ProgressDialog rejectionProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_renewal_detail);

        renewalAmountLeft=findViewById(R.id.renewal_member_amountpayinglater);
        renewalBackBtn = findViewById(R.id.renewalDetail_back_button);
        renewalApproveBtn= findViewById(R.id.renewal_approval_btn);
        renewalRejectBtn = findViewById(R.id.renewal_rejectionReason_btn);
        renewalName = findViewById(R.id.renewal_member_name);
        renewalEmail = findViewById(R.id.renewal_member_email);
        renewalCompanyName = findViewById(R.id.renewal_member_company);
        renewalMembershipType = findViewById(R.id.renewal_member_membershiptype);
        renewalPersonPayment = findViewById(R.id.renewal_member_personpayment);
        renewalProof = findViewById(R.id.renewal_member_proof_img);
        renewalKey = getIntent().getStringExtra("memberRenewalKey");
        RejectionMailDialog = new Dialog(this);
        rejectionProgress = new ProgressDialog(this);

        renewalBackBtn.setOnClickListener(v -> finish());

        renewalDataRef.child(renewalKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    renewalNameStr = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                    renewalEmailStr =Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                    renewalCompanyNameStr = Objects.requireNonNull(snapshot.child("companyname").getValue()).toString();
                    renewalPersonPaymentStr =Objects.requireNonNull(snapshot.child("paymentReceiverName").getValue()).toString();
                    renewalAmountLeftStr= Objects.requireNonNull(snapshot.child("amountleft").getValue()).toString();
                    renewalMembershipTypeStr= Objects.requireNonNull(snapshot.child("memberfee").getValue()).toString();
                    String renewalPaymentProofStr =Objects.requireNonNull(snapshot.child("purl").getValue()).toString();

                    renewalName.setText(renewalNameStr);
                    renewalEmail.setText(renewalEmailStr);
                    renewalCompanyName.setText(renewalCompanyNameStr);
                    renewalPersonPayment.setText(renewalPersonPaymentStr);
                    renewalMembershipType.setText(renewalMembershipTypeStr);
                    renewalAmountLeft.setText(renewalAmountLeftStr);
                    Glide.with(getApplicationContext())
                            .load(renewalPaymentProofStr)
                            .error(R.drawable.iea_logo)
                            .into(renewalProof);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        renewalApproveBtn.setOnClickListener(v ->{
            confirmationDialog = new Dialog(MemberRenewalDetail.this);
            LayoutInflater inflater = getLayoutInflater();
            View confirmationView = inflater.inflate(R.layout.confirmation_popup, null);
            yesbtn = confirmationView.findViewById(R.id.yesbtn);
            nobtn = confirmationView.findViewById(R.id.nobtn);
            confirmationDialog.setContentView(confirmationView);
            confirmationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            confirmationDialog.show();
            nobtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmationDialog.dismiss();
                }
            });
            yesbtn.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View view) {
                    renewalApproval(renewalKey,rejectionProgress);
                }
            });
        });

        renewalRejectBtn.setOnClickListener(v -> {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.rejection_reason_popup, null);
            RejectionReasonText = view.findViewById(R.id.rejectionReason_text);
            rejectBtn  = view.findViewById(R.id.rejection_btn);
            RejectionMailDialog.setContentView(view);
            RejectionMailDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            RejectionMailDialog.show();
            rejectBtn.setOnClickListener(v1 -> {
                if (!RejectionReasonText.getText().toString().isEmpty()) {
                    String rejectionReason = RejectionReasonText.getText().toString();
                    renewalRejection(rejectionReason,renewalKey,rejectionProgress);
                } else {
                    RejectionReasonText.setError("Reason can not be empty");
                    RejectionReasonText.requestFocus();
                }


            });

        });
    }

    public void renewalApproval(String Key,ProgressDialog progressDialog){
        progressDialog.setMessage("Approving...");
        progressDialog.show();
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String approvalDate = df.format(date);
        HashMap<String,Object> approveMap = new HashMap<>();
        approveMap.put("status","unblocked");
        approveMap.put("date_of_membership",approvalDate);
        registryDataRef.child(Key).updateChildren(approveMap).addOnSuccessListener(s ->{
            renewalDataRef.child(Key).removeValue().addOnSuccessListener(s2 ->{
                Toast.makeText(this, "Renewal Accepted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MembershipRenewal.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                sendApprovalEmail();
            }).addOnFailureListener(f->{
                Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            });
        }).addOnFailureListener(f->{
            Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        });
    }

    public void renewalRejection(String reason, String Key, ProgressDialog progressDialog){
        progressDialog.setMessage("Rejecting...");
        progressDialog.show();
        renewalDataRef.child(Key).removeValue().addOnSuccessListener(s ->{
            HashMap<String, Object> statusMap = new HashMap<>();
            statusMap.put("status","blocked");
            registryDataRef.child(Key).updateChildren(statusMap).addOnSuccessListener(s1->{
                startActivity(new Intent(getApplicationContext(), MembershipRenewal.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                sendRejectionEmail(reason);
                progressDialog.dismiss();
            }).addOnFailureListener(f->{
                Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            });
        }).addOnFailureListener(f->{
            Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        });
    }

    @SuppressLint("IntentReset")
    private void sendRejectionEmail(String rejectionReason) {


        Log.i("Send email", "");

        String[] TO = {renewalEmailStr};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "IEA Membership Rejected");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Your membership got rejected due to following reason \n\n" + rejectionReason);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MemberRenewalDetail.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
    private void sendApprovalEmail() {


        Log.i("Send email", "");

        String[] TO = {renewalEmailStr};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "IEA Membership Renewal Acceptance");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Congrats,\nYour membership is renewed ");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MemberRenewalDetail.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}