package com.example.ieaadmin;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

public class explore_us extends AppCompatActivity {

    TextView join_now;
    AppCompatButton exploreUsBackButton;
    CardView exploreUsContactUsCard;
    Dialog exploreUsContactDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_us);



        exploreUsBackButton = findViewById(R.id.exploreus_back_button);
        exploreUsContactUsCard  = findViewById(R.id.explore_us_contact_us_card);

        exploreUsContactDialog = new Dialog(this);


        exploreUsBackButton.setOnClickListener(view -> {
            finish();
        });

        exploreUsContactUsCard.setOnClickListener(view -> {
            LayoutInflater inflater = getLayoutInflater();
            View exploreUsView = inflater.inflate(R.layout.support_contact_popup, null);

            exploreUsContactDialog.setContentView(exploreUsView);
            exploreUsContactDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            exploreUsContactDialog.show();
        });

    }
}