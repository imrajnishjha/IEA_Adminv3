package com.example.ieaadmin;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class MembersDirectory extends AppCompatActivity {

    RecyclerView memberDirectoryRecyclerView;
    AppCompatButton memberDirectoryBackButton;
    MembersDirectoryAdapter memberDirectoryAdapter;
    FirebaseRecyclerOptions<MembersDirectoryModel> options;
    AutoCompleteTextView memberSearchTextView;
    TextView noMemberTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_directory);

        memberDirectoryRecyclerView = findViewById(R.id.members_directory_recycler_view);
        memberDirectoryRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));
        memberDirectoryBackButton = findViewById(R.id.members_directory_back_button);
        memberSearchTextView = findViewById(R.id.member_search_autocomplete);
        noMemberTv = findViewById(R.id.no_member_tv);

        options = new FirebaseRecyclerOptions.Builder<MembersDirectoryModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Registered Users"), MembersDirectoryModel.class)
                .build();

        memberDirectoryBackButton.setOnClickListener(view -> finish());

        memberDirectoryAdapter = new MembersDirectoryAdapter(options);
        memberDirectoryRecyclerView.setAdapter(memberDirectoryAdapter);

        memberSearchTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (memberSearchTextView.getText().toString().equals("Clear Selection")) {
                    memberSearchTextView.setText("");
                    options = new FirebaseRecyclerOptions.Builder<MembersDirectoryModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Registered Users"), MembersDirectoryModel.class)
                            .build();

                } else {
                    options = new FirebaseRecyclerOptions.Builder<MembersDirectoryModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Registered Users").orderByChild("industry_type").equalTo(memberSearchTextView.getText().toString()), MembersDirectoryModel.class)
                            .build();
                }
                memberDirectoryAdapter = new MembersDirectoryAdapter(options);
                memberDirectoryRecyclerView.setAdapter(memberDirectoryAdapter);
                memberDirectoryAdapter.startListening();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void dropdownInit() {
        String[] departments = getResources().getStringArray(R.array.department_search);
        ArrayAdapter<String> arrayAdapterDepartments = new ArrayAdapter<>(getBaseContext(), R.layout.drop_down_item, departments);
        memberSearchTextView.setAdapter(arrayAdapterDepartments);
    }

    @Override
    public void onResume() {
        super.onResume();
        dropdownInit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        memberDirectoryAdapter.startListening();
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        memberDirectoryAdapter.stopListening();
//    }

    public static class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }
        public WrapContentLinearLayoutManager(Context context,int orientation,boolean reverseLayout) {
            super(context,orientation,reverseLayout);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("TAG", "Recycler view error");
            }
        }

        @Override
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }
    }


}