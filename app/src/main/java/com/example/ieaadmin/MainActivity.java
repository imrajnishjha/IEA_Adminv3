package com.example.ieaadmin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    int time = 4100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (getIntent().getExtras()!=null) {
            time=0;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = activityHandler();
                startActivity(i);
                finish();
            }
        },4100);
    }

    public Intent activityHandler(){
        Intent act = null;
        try{
            if(getIntent().getExtras()!=null){
                if(getIntent().getExtras().getString("activity").equals("grievance")){
                    act = new Intent(MainActivity.this,GrievanceDetail.class).putExtra("GrievanceItemKey",getIntent().getExtras().getString("chatKey"));
                    return act;
                } else if(getIntent().getExtras().getString("activity").equals("memberapproval")){
                    act = new Intent(MainActivity.this,memberApprovalDetail.class).putExtra("memberApprovalKey",getIntent().getExtras().getString("ownerKey"));
                    return act;
                } else if(getIntent().getExtras().getString("activity").equals("memberrenewal")){
                    act = new Intent(MainActivity.this,MembershipRenewal.class);
                    return act;
                } else {
                    act = new Intent(MainActivity.this, MembersNotification.class);
                    return act;
                }
            } else {
                act = new Intent(MainActivity.this, LandingPage.class);
                return act;
            }
        } catch (Exception e){
            e.printStackTrace();
            act = new Intent(MainActivity.this, LandingPage.class);
            return act;
        }

    }

}