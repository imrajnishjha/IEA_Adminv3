package com.example.ieaadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.metrics.Event;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AddEventPhotosFragment extends Fragment {

    ViewPager eventImagesViewPager;
    AppCompatButton selectEventImagesBtn, pastEventFragmentDismissBtn, uploadEventImagesBtn;
    int PICK_IMAGE_CODE = 39;
    public ArrayList<Uri> imagesUri;
    ProgressDialog progressDialog;
    String EventItemKey;
    int count = 0;
    DatabaseReference eventUriRef;

    View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagesUri = new ArrayList<>();

    }

    private void pickImages() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGE_CODE);

        if(imagesUri != null) {
            imagesUri.clear();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_CODE && resultCode == -1){
            if(data != null) {
                if(data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();

                    for(int i = 0; i < count; i++){
                        imagesUri.add(data.getClipData().getItemAt(i).getUri());
                    }
                } else {
                    imagesUri.add(data.getData());
                }

                setAdapter();
            }
        }
    }

    private void setAdapter() {
        PastEventImageAdapter pastEventImageAdapter = new PastEventImageAdapter(getContext(), imagesUri);
        eventImagesViewPager.setAdapter(pastEventImageAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_add_event_photos, container, false);

        eventImagesViewPager = view.findViewById(R.id.event_images_view_pager);
        selectEventImagesBtn = view.findViewById(R.id.select_event_images_btn);
        pastEventFragmentDismissBtn = view.findViewById(R.id.past_event_fragment_dismiss_btn);
        uploadEventImagesBtn = view.findViewById(R.id.upload_event_images_btn);
        progressDialog = new ProgressDialog(getContext());


        Bundle data = getArguments();

        if(data != null){
            EventItemKey = data.getString("EventItemKey");
            Log.d("EventItemKey", EventItemKey);
        }

        selectEventImagesBtn.setOnClickListener(view -> pickImages());
        uploadEventImagesBtn.setOnClickListener(view -> compressImages(container));
        pastEventFragmentDismissBtn.setOnClickListener(view -> container.setVisibility(View.GONE));
        return view;
    }

    private void compressImages(ViewGroup container) {
        progressDialog.setMessage("Uploading Images...");
        progressDialog.show();
        eventUriRef = FirebaseDatabase.getInstance().getReference("Events/"+ EventItemKey+"/image_uris");
        if(imagesUri.size() == 0) {
            Toast.makeText(getContext(), "Please select at least one image", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

        for(int i = 0; i < imagesUri.size(); i++) {
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imagesUri.get(i));
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                byte[] imageByte = stream.toByteArray();
                uploadImages(imageByte, container);
            } catch (IOException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void uploadImages(byte[] imageByte, ViewGroup container) {
        StorageReference pastEventImagesRef = FirebaseStorage.getInstance().getReference("Past Event Images/"+EventItemKey+"/"+System.currentTimeMillis()+".jpg");
        pastEventImagesRef.putBytes(imageByte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                count += 1;
                pastEventImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        HashMap eventUri = new HashMap();
                        eventUri.put("image_uri", uri.toString());

                        eventUriRef.child(Objects.requireNonNull(eventUriRef.push().getKey())).updateChildren(eventUri);
                        if(count == imagesUri.size()) {
                            progressDialog.dismiss();
                            container.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Images Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.cancel();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}