package com.example.ieaadmin;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Context;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.sql.Time;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class EventDetail extends AppCompatActivity {

    ImageView eventDetailImg;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    ActivityResultLauncher<String> mGetEventImage;
    Uri EventImageUri=null;
    CardView eventDetailEditCV;
    StorageReference storageProfilePicReference;
    EditText EventName,EventLocation,EventDescription;
    String date="",time="",description="",lower_casetitle="",Eventyear="";
    TextView Eventdate,Eventmonth,EventTime,EventWeekDay;
    DatePickerDialog.OnDateSetListener Datelistner;
    TimePickerDialog.OnTimeSetListener Timelistner;
    AppCompatButton EventPostBtn;
    ProgressDialog EventCreatProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        storageProfilePicReference = FirebaseStorage.getInstance().getReference();

        eventDetailImg = findViewById(R.id.EventDetailImage);
        Eventdate = findViewById(R.id.EventDetailDate);
        Eventmonth = findViewById(R.id.EventDetailMonth);
        EventTime = findViewById(R.id.EventDetailTime);
        eventDetailEditCV = findViewById(R.id.EventDetailDateCV);
        EventWeekDay = findViewById(R.id.EventDetailWeekday);
        EventPostBtn = findViewById(R.id.EventDetailPostEvent);
        EventName = findViewById(R.id.EventDetailName);
        EventLocation = findViewById(R.id.EventDetailLocation);
        EventDescription = findViewById(R.id.EventDetailDescription);


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);



        eventDetailEditCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(EventDetail.this,android.R.style.Theme_Material_Dialog_Alert,Timelistner,
                                                                           hour,minute,false);

                timePickerDialog.show();
            }
        });

        Datelistner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1 = i1+1;
                Eventmonth.setText(Monthcalculator(i1));
                Eventdate.setText(String.valueOf(i2));
                Calendar week = Calendar.getInstance();
                week.set(i,i1,i2);
                int dayOfweek = week.get(Calendar.DAY_OF_WEEK);
                Log.e("ghg", "onDateSet: "+dayOfweek );
                EventWeekDay.setText(Weekcalculator(dayOfweek));
                StringBuilder date = new StringBuilder();
                date.append(i2+"-"+ Monthcalculator(i1)+"-"+i);
                Eventyear = date.toString();


            }
        };
        Timelistner = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                String time = timeFormatter(i,i1);
                EventTime.setText(time);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EventDetail.this,
                        android.R.style.Theme_Material_Dialog_Alert, Datelistner,year,month,day);

                datePickerDialog.show();
            }
        };


        mGetEventImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                String destinationUri = new StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString();
                UCrop.of(result, Uri.fromFile(new File(getCacheDir(), destinationUri)))
                        .withAspectRatio(4, 3)
                        .start(EventDetail.this, 2);
            }
        });

        eventDetailImg.setOnClickListener(view -> {
            mGetEventImage.launch("image/*");
        });

        EventPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(EventName.getText().toString().isEmpty()){
                    EventName.setError("Enter the event name");
                    EventName.requestFocus();
                } else if(EventLocation.getText().toString().isEmpty()) {
                    EventLocation.setError("Enter the Location");
                    EventLocation.requestFocus();
                } else if(EventDescription.getText().toString().isEmpty()){
                    EventDescription.setError("Enter the Description");
                    EventDescription.requestFocus();
                } else if(Eventdate.getText().toString().isEmpty()) {
                    Toasty.normal(EventDetail.this, "Please select a Date", R.drawable.iea_logo).show();
                } else if(EventTime.getText().toString().isEmpty()) {
                    Toasty.normal(EventDetail.this, "Please select a Time", R.drawable.iea_logo).show();
                } else if(EventImageUri==null){
                    Toasty.normal(EventDetail.this, "Please select an Image", R.drawable.iea_logo).show();
                } else{
                    EventCreatProgress = new ProgressDialog(EventDetail.this);
                    EventCreatProgress.setMessage("Event Creating...");
                    EventCreatProgress.show();
                    uploadProductImage(EventImageUri,EventCreatProgress);
                }

            }
        });
    }




    private void uploadProductImage(Uri EventImageUri,ProgressDialog EventCreatProgress) {
        StorageReference productFileRef = storageProfilePicReference.child("Events Images/" + EventName.getText().toString());
        productFileRef.putFile(EventImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                productFileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DatabaseReference EventReference = FirebaseDatabase.getInstance().getReference().child("Events");
                        String key = EventReference.push().getKey();
                        String EventNameStr = EventName.getText().toString();
                        String EventLocationStr = EventLocation.getText().toString();
                        String EventDescriptionStr = EventDescription.getText().toString();
                        String EventTimeStr = EventTime.getText().toString();
                        String EventWeekStr = EventWeekDay.getText().toString();

                        EventDetailModel newEvent = new EventDetailModel(Eventyear,EventDescriptionStr,uri.toString(),EventNameStr.toLowerCase(),EventLocationStr,EventWeekStr,EventTimeStr,EventNameStr);
                        EventReference.child(key).setValue(newEvent).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toasty.normal(EventDetail.this, "Event Created", R.drawable.iea_logo).show();
                                        EventCreatProgress.dismiss();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toasty.normal(EventDetail.this, "Event Creation Failed!", R.drawable.iea_logo).show();
                                EventCreatProgress.dismiss();
                            }
                        });


                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        } else if (resultCode == RESULT_OK && requestCode == 2) {
            EventImageUri = UCrop.getOutput(data);
            eventDetailImg.setImageURI(EventImageUri);
        }
    }

    public String Monthcalculator(int i){

        switch(i){
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:

                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";

        }
        return "null";
    }

    public String Weekcalculator(int i) {

        switch (i) {
            case 1:
                return "Thursday";
            case 2:
                return "Friday";
            case 3:
                return "Saturday";
            case 4:
                return "Sunday";
            case 5:
                return "Monday";
            case 6:
                return "Tuesday";
            case 7:
                return "Wednesday";

        }
        return "null";
    }

    public String timeFormatter(int hour,int min){
        StringBuilder Time = new StringBuilder();
        if (hour<12) {
            if(hour<1){
                if(min<10){
                    Time.append("12:0"+min+"AM");
                } else {
                    Time.append("12:"+min+"AM");
                }
            } else if(hour<10){
                if(min<10){
                    Time.append("0"+hour+":0"+min+"AM");
                } else {
                    Time.append("0"+hour+":"+min+"AM");
                }
            } else {
                if(min<10){
                    Time.append(hour+":0"+min+"AM");
                } else {
                    Time.append(hour+":"+min+"AM");
                }
            }
        } else {
            if(hour==12){
                if(min<10){
                    Time.append("12:0"+min+"PM");
                } else {
                    Time.append("12:"+min+"PM");
                }
            } else {
                hour=hour-12;
                if(hour<10){
                    if(min<10){
                        Time.append("0"+hour+":0"+min+"PM");
                    } else {
                        Time.append("0"+hour+":"+min+"PM");
                    }
                } else {
                    if(min<10){
                        Time.append(hour+":0"+min+"PM");
                    } else {
                        Time.append(hour+":"+min+"PM");
                    }
                }

            }
        }
        return Time.toString();
    }
}