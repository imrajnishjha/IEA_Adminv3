package com.example.ieaadmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LandingPage extends AppCompatActivity {

    AppCompatButton requestForRegistration, existingMember,exploreUs;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_landing_page);


        existingMember = findViewById(R.id.approval_btn);
        exploreUs = findViewById(R.id.explore_btn);
        mAuth = FirebaseAuth.getInstance();




        exploreUs.setOnClickListener(view -> startActivity(new Intent(LandingPage.this, explore_us.class)));

        existingMember.setOnClickListener(view -> startActivity(new Intent(LandingPage.this, Login.class)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(LandingPage.this, explore_menu.class));
        }
    }
}