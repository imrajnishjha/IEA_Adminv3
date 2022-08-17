package com.example.ieaadmin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class MemberDirectoryDetail extends AppCompatActivity {
    ImageView memberProfileImage;
    TextView memberProfileName, memberMembershipId, memberMembershipDate, memberContactNumber, memberDateOfBirth, memberEmailTxtView,
            memberCompanyName, memberAddress, memberBio, memberMembershipExpiryDate, yesbtn, nobtn;
    DatabaseReference ref;
    FirebaseAuth mAuth;
    String currentUser, memberMembershipDateStr, memberMembershipIdStr, memberPhoneNumberStr, memberBioStr, memberPictureUrl, memberNameStr, memberAddressStr, memberCompanyNameStr, memberEmailStr, memberDOBStr;
    AppCompatButton removeBtn, makeMemberOfTheMonthBtn;
    Dialog confirmationDialog, memberDescriptionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_directory_detail);

        ref = FirebaseDatabase.getInstance().getReference().child("Registered Users");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getEmail();


        memberProfileImage = findViewById(R.id.member_profile_image);
        memberMembershipId = findViewById(R.id.member_membership_id);
        memberContactNumber = findViewById(R.id.member_contactNumber);
        memberDateOfBirth = findViewById(R.id.member_dateOfBirth);
        memberProfileName = findViewById(R.id.member_profile_name);
        memberMembershipDate = findViewById(R.id.member_membership_date);
        memberEmailTxtView = findViewById(R.id.member_email);
        memberCompanyName = findViewById(R.id.member_company_name);
        memberAddress = findViewById(R.id.member_address);
        memberBio = findViewById(R.id.member_bio);
        memberMembershipExpiryDate = findViewById(R.id.MembershipExpiryDate);
        removeBtn = findViewById(R.id.remove_member_btn);
        makeMemberOfTheMonthBtn = findViewById(R.id.make_member_of_month_btn);
        memberDescriptionDialog = new Dialog(this);

        String coreItemKey = getIntent().getStringExtra("MemberItemKey");

        ref.child(coreItemKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    memberMembershipDateStr = snapshot.child("date_of_membership").getValue().toString();
                    memberMembershipIdStr = snapshot.child("member_id").getValue().toString();
                    memberPhoneNumberStr = snapshot.child("phone_number").getValue().toString();
                    memberDOBStr = snapshot.child("date_of_birth").getValue().toString();
                    memberEmailStr = snapshot.child("email").getValue().toString();
                    memberCompanyNameStr = snapshot.child("company_name").getValue().toString();
                    memberAddressStr = snapshot.child("address").getValue().toString();
                    memberNameStr = snapshot.child("name").getValue().toString();
                    memberPictureUrl = snapshot.child("purl").getValue().toString();
                    memberBioStr = snapshot.child("description").getValue().toString();
                    String memberMembershipExpiryDateStr = yearincrementer(memberMembershipDateStr);

                    memberMembershipId.setText(memberMembershipIdStr);
                    memberMembershipDate.setText(memberMembershipDateStr);
                    memberContactNumber.setText(memberPhoneNumberStr);
                    memberDateOfBirth.setText(memberDOBStr);
                    memberEmailTxtView.setText(memberEmailStr);
                    memberCompanyName.setText(memberCompanyNameStr);
                    memberAddress.setText(memberAddressStr);
                    memberProfileName.setText(memberNameStr);
                    memberBio.setText(memberBioStr);
                    memberMembershipExpiryDate.setText(memberMembershipExpiryDateStr);

                    Glide.with(memberProfileImage.getContext())
                            .load(memberPictureUrl)
                            .placeholder(R.drawable.iea_logo)
                            .circleCrop()
                            .error(R.drawable.iea_logo)
                            .into(memberProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmationDialog = new Dialog(MemberDirectoryDetail.this);
                LayoutInflater inflater = getLayoutInflater();
                View confirmationView = inflater.inflate(R.layout.confirmation_popup, null);
                yesbtn  = confirmationView.findViewById(R.id.yesbtn);
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

                        memberRejection(coreItemKey, currentUser.replaceAll("\\.", "%7"));
                        Log.d("ramdom", "onClick: " + coreItemKey + currentUser);
                    }
                });
            }
        });
        makeMemberOfTheMonthBtn.setOnClickListener(view -> {

            LayoutInflater inflater = getLayoutInflater();
            View view1 = inflater.inflate(R.layout.member_of_month_popup, null);
            memberDescriptionDialog.setContentView(view1);
            memberDescriptionDialog.show();
            AppCompatButton setMemberOfMonth = view1.findViewById(R.id.set_member_of_month_btn);
            EditText memberOfTheMonthDescriptionEdtTxt = view1.findViewById(R.id.member_of_month_description_edtTxt);

            setMemberOfMonth.setOnClickListener(v -> {
                ProgressDialog progressDialog = new ProgressDialog(view.getContext());
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                DatabaseReference memberRef = FirebaseDatabase.getInstance().getReference().child("Member of Month");
                String memberOfTheMonthDescriptionStr = memberOfTheMonthDescriptionEdtTxt.getText().toString();
                MemberofMonthModel newMOM = new MemberofMonthModel(memberNameStr, memberCompanyNameStr, memberPictureUrl, memberOfTheMonthDescriptionStr);
                memberRef.setValue(newMOM).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        memberDescriptionDialog.dismiss();
                        Toasty.normal(view.getContext(), "New member of the month is set", R.drawable.notification_icon).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toasty.normal(view.getContext(), "Try again", R.drawable.notification_icon).show();
                    }
                });
            });
        });
    }

    public String yearincrementer(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 365);
        date = sdf.format(c.getTime());
        return date;
    }

    public void memberRejection(String Email, String currentUser) {
        UserRegistrationHelperClass rejectedMemberDetails = new UserRegistrationHelperClass(memberNameStr, memberEmailStr, memberPhoneNumberStr, memberCompanyNameStr
                , memberAddressStr, memberDOBStr, memberPictureUrl, memberBioStr);
        if (Email.equals(currentUser)) {
            Toasty.normal(this, "You Can't remove Yourself", R.drawable.notification_icon).show();

        } else {
            DatabaseReference memberRef = FirebaseDatabase.getInstance().getReference().child("Rejected Users");
            memberRef.child(Email.replaceAll("\\.", "%7")).child("Details")
                    .setValue(rejectedMemberDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            ref.child(Email.replaceAll("\\.", "%7")).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toasty.normal(MemberDirectoryDetail.this, "Member Removed", R.drawable.notification_icon).show();

                                    finish();
                                }
                            });
                        }
                    });
        }

    }
}