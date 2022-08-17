package com.example.ieaadmin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import es.dmoral.toasty.Toasty;

public class memberApprovalDetail extends AppCompatActivity {

    TextView newMemberName, newMemberEmail, newMemberCompany, newMemberTurnover, newMemberIndustry, newMemberMembership,
            newMemberAmountLeft, newMemberContact, newMemberPaymentReceiver, newMemberEmailforAuth, verificationMail, popUpHeaderText;
    ImageView newMemberPayProof;
    EditText newMemberPassforAuth, RejectionReasonText, verificationPass;
    DatabaseReference databaseReference;
    AppCompatButton approvalbackbtn, approveBtn, authCreatebtn, rejectBtn, rejectionReasonbtn, verificationBtn;
    FirebaseDatabase memberDirectoryRoot;
    DatabaseReference memberDirectoryRef, registrationDataRef, tempRegistrationData;
    StorageReference defaultProfilePicReference;
    private FirebaseAuth mAuth;

    String newName, newEmail, newCompany, newIndustry, newAmountLeft, newphoneno, newProofUrl, newTurnover, newMembership, newpaymentReceiver, newgstNo;

    String nullString, email_address, password;
    final String status = "unblocked";
    Uri imageUri;
    Dialog newMemberIdPassDialog, RejectionMailDialog, VerificationDialog;
    ProgressDialog approvingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_approval_detail);

        approvingDialog = new ProgressDialog(this);
        approvingDialog.setMessage("Approving...");


        defaultProfilePicReference = FirebaseStorage.getInstance().getReference();
        nullString = "";
        newMemberIdPassDialog = new Dialog(this);
        RejectionMailDialog = new Dialog(this);
        VerificationDialog = new Dialog(this);
        mAuth = FirebaseAuth.getInstance();


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Temp Registry");
        newMemberName = findViewById(R.id.new_member_name);
        newMemberEmail = findViewById(R.id.new_member_email);
        newMemberCompany = findViewById(R.id.new_member_company);
        newMemberTurnover = findViewById(R.id.new_member_turnover);
        newMemberIndustry = findViewById(R.id.new_member_industrytype);
        newMemberMembership = findViewById(R.id.new_member_membershiptype);
        newMemberAmountLeft = findViewById(R.id.new_member_payinglater);
        newMemberContact = findViewById(R.id.new_member_phoneno);
        newMemberPayProof = findViewById(R.id.new_member_proof_img);
        newMemberPaymentReceiver = findViewById(R.id.new_member_paymentReceiver);


        approvalbackbtn = findViewById(R.id.approvalDetail_back_button);
        approveBtn = findViewById(R.id.approval_btn);
        rejectionReasonbtn = findViewById(R.id.rejectionReason_btn);

        approvalbackbtn.setOnClickListener(view -> finish());

        String memberApprovalKey = getIntent().getStringExtra("memberApprovalKey");

        databaseReference.child(memberApprovalKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    newName = snapshot.child("fullname").getValue().toString();
                    newEmail = snapshot.child("email").getValue().toString();
                    newCompany = snapshot.child("companyName").getValue().toString();
                    newTurnover = snapshot.child("turnover").getValue().toString();
                    newIndustry = snapshot.child("department").getValue().toString();
                    newMembership = snapshot.child("memberfee").getValue().toString();
                    newAmountLeft = snapshot.child("amountLeft").getValue().toString();
                    newphoneno = snapshot.child("phoneNo").getValue().toString();
                    newProofUrl = snapshot.child("imageUrl").getValue().toString();
                    newpaymentReceiver = snapshot.child("paymentReceiverName").getValue().toString();
                    newgstNo = snapshot.child("gstno").getValue().toString();

                    newMemberName.setText(newName);
                    newMemberEmail.setText(newEmail);
                    newMemberCompany.setText(newCompany);
                    newMemberIndustry.setText(newIndustry);
                    newMemberAmountLeft.setText(newAmountLeft);
                    newMemberContact.setText(newphoneno);
                    newMemberTurnover.setText(newTurnover);
                    newMemberMembership.setText(newMembership);
                    newMemberPaymentReceiver.setText(newpaymentReceiver);

                    Glide.with(getApplicationContext())
                            .load(newProofUrl)
                            .error(R.drawable.iea_logo)
                            .into(newMemberPayProof);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        approveBtn.setOnClickListener(v -> {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.newmemberpassword_popup, null);
            Log.d("newemail", "onClick: " + newEmail);
            newMemberEmailforAuth = view.findViewById(R.id.newApprove_member_email);
            newMemberPassforAuth = view.findViewById(R.id.newApprove_member_password);
            authCreatebtn = view.findViewById(R.id.create_password_btn);
            newMemberEmailforAuth.setText(newEmail);
            String automaticPassword = autoPassowrd(newName);
            newMemberPassforAuth.setText(automaticPassword);
            newMemberIdPassDialog.setContentView(view);
            newMemberIdPassDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            newMemberIdPassDialog.show();
            String currentUseremail = mAuth.getCurrentUser().getEmail();
            Log.d("TAG0", "onSuccess: " + currentUseremail);

            authCreatebtn.setOnClickListener(v12 -> {

                if (!newMemberPassforAuth.getText().toString().isEmpty()) {
                    LayoutInflater inflater2 = getLayoutInflater();
                    View verifyView = inflater2.inflate(R.layout.newmemberpassword_popup, null);
                    verificationMail = verifyView.findViewById(R.id.newApprove_member_email);
                    verificationPass = verifyView.findViewById(R.id.newApprove_member_password);
                    verificationBtn = verifyView.findViewById(R.id.create_password_btn);
                    popUpHeaderText = verifyView.findViewById(R.id.id_pass_text);
                    popUpHeaderText.setText("Enter Your Password");
                    verificationMail.setText(currentUseremail);
                    newMemberIdPassDialog.setContentView(verifyView);
                    newMemberIdPassDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    newMemberIdPassDialog.show();

                    verificationBtn.setOnClickListener(view1 -> {
                        if (verificationPass.getText().toString().isEmpty()) {
                            verificationPass.setError("Please Enter Password");
                            verificationPass.requestFocus();
                        } else {
                            approvingDialog.show();
                            String currentUserPass = verificationPass.getText().toString();
                            assert currentUseremail != null;
                            mAuth.signInWithEmailAndPassword(currentUseremail, currentUserPass).addOnSuccessListener(authResult -> mAuth.createUserWithEmailAndPassword(newMemberEmailforAuth.getText().toString(), newMemberPassforAuth.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Log.d("TAG1", "onSuccess: " + mAuth.getCurrentUser().getEmail());
                                    memberApproval();
                                    mAuth.signInWithEmailAndPassword(currentUseremail, currentUserPass).addOnSuccessListener(authResult1 -> {
                                        approvingDialog.dismiss();
                                        newMemberIdPassDialog.dismiss();
                                        Log.d("TAG2", "onSuccess: " + mAuth.getCurrentUser().getEmail());
                                        startActivity(new Intent(getApplicationContext(), member_approval.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        sendAcceptanceEmail();
                                    }).addOnFailureListener(e -> {
                                        approvingDialog.dismiss();
                                        Toasty.normal(memberApprovalDetail.this, "User already exists", R.drawable.iea_logo).show();
                                    });
                                }
                            }).addOnFailureListener(e -> {
                                approvingDialog.dismiss();
                                Toasty.normal(memberApprovalDetail.this, e.getMessage(), R.drawable.iea_logo).show();
                            })).addOnFailureListener(e -> {
                                approvingDialog.dismiss();
                                verificationPass.setError("Enter Correct Password");
                                verificationPass.requestFocus();
                            });
                        }
                    });

                } else {
                    newMemberPassforAuth.setError("Please Enter Password");
                    newMemberPassforAuth.requestFocus();
                }
            });


        });

        rejectionReasonbtn.setOnClickListener(v -> {
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

                    memberDirectoryRoot = FirebaseDatabase.getInstance();
                    tempRegistrationData = memberDirectoryRoot.getReference("Temp Registry").child(newEmail.replaceAll("\\.", "%7"));
                    tempRegistrationData.removeValue();
                    startActivity(new Intent(getApplicationContext(), member_approval.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    sendRejectionEmail(rejectionReason);

                } else {
                    RejectionReasonText.setError("Reason can not be empty");
                    RejectionReasonText.requestFocus();
                }


            });

        });


    }

    public void memberApproval() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String approvalDate = df.format(date);

        memberDirectoryRoot = FirebaseDatabase.getInstance();
        memberDirectoryRef = memberDirectoryRoot.getReference("Registered Users");
        registrationDataRef = memberDirectoryRoot.getReference("Registration Data");
        tempRegistrationData = memberDirectoryRoot.getReference("Temp Registry").child(newEmail.replaceAll("\\.", "%7"));
        RegistrationDataModel approveRegistrationData = new RegistrationDataModel(newTurnover, newProofUrl, newEmail, newAmountLeft, newIndustry, newpaymentReceiver, newgstNo);


        StorageReference fileRef = defaultProfilePicReference.child("User Profile Pictures/" + newEmail + "ProfilePicture");
        Bitmap bitmapDefault = BitmapFactory.decodeResource(getResources(), R.drawable.iea_logo);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapDefault.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataImg = baos.toByteArray();
        fileRef.putBytes(dataImg).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            memberApprovalDetailModel approveMemberDirectoryDetailModel = new memberApprovalDetailModel(nullString, newCompany, nullString, approvalDate, newEmail, nullString, newName, newphoneno, uri.toString(), nullString, newIndustry, nullString, uri.toString(),newMembership,status);
            memberDirectoryRef.child(newEmail.replaceAll("\\.", "%7")).setValue(approveMemberDirectoryDetailModel);

            registrationDataRef.child(newEmail.replaceAll("\\.", "%7")).setValue(approveRegistrationData).addOnSuccessListener(unused -> {
                tempRegistrationData.removeValue();


                String email = mAuth.getCurrentUser().getEmail();
                Log.d("TAG", "onSuccess: " + email);
                Toast.makeText(memberApprovalDetail.this, "Member Approved", Toast.LENGTH_LONG).show();
            });

        }));
    }


    private void sendAcceptanceEmail() {

        password = newMemberPassforAuth.getText().toString();
        email_address = newMemberEmailforAuth.getText().toString();
        Log.d("email", "sendAcceptanceEmail: ");

        String[] TO = {newEmail};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "IEA Membership Approved");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Your Membership Approved,Login Credentials as follows\n\nEmail: " + email_address + "\nPassword: " + password);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(memberApprovalDetail.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendRejectionEmail(String rejectionReason) {


        Log.i("Send email", "");

        String[] TO = {newEmail};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "IEA Membership Rejected");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Your membership got rejected due to following reason \n\n" + rejectionReason);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(memberApprovalDetail.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public String autoPassowrd(String name) {
        String Alphabet = "abcdefghijaklmnopqrstuvwxyz";
        StringBuilder randomPass = new StringBuilder();
        Random random = new Random();
        int namelength = 4;
        int length = 4;
        if (name.length() < 4) {
            namelength = 1;
            length = 7;
        }

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(Alphabet.length());
            char randomChar1 = Alphabet.charAt(index);
            randomPass.append(randomChar1);
        }
        for (int i = 0; i < namelength; i++) {
            char randomChar2 = name.charAt(i);
            randomPass.append(randomChar2);
        }
        return randomPass.toString();
    }

}