package com.example.ieaadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Refer extends AppCompatActivity {
    RecyclerView referRecyclerView;
    referAdapterClass referAdapterclass;
    FirebaseRecyclerOptions<ReferModelClass> options;
    AppCompatButton referBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);

        referRecyclerView = (RecyclerView) findViewById(R.id.refer_recycler_view);
        referBackBtn = findViewById(R.id.refer_back_button);
        referBackBtn.setOnClickListener(view -> finish());

        MembersDirectory.WrapContentLinearLayoutManager linearLayoutManager = new MembersDirectory.WrapContentLinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        referRecyclerView.setLayoutManager(linearLayoutManager);

        options = new FirebaseRecyclerOptions.Builder<ReferModelClass>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Refer"),ReferModelClass.class).build();

        referAdapterclass = new referAdapterClass(options);
        referRecyclerView.setAdapter(referAdapterclass);


    }

    @Override
    protected void onStart() {
        super.onStart();
        referAdapterclass.startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        referAdapterclass.startListening();
    }
}