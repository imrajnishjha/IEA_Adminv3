package com.example.ieaadmin;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.proxy.ProxyApi;

import java.util.ArrayList;

public class PastEventImageAdapter extends PagerAdapter {

    Context context;
    ArrayList<Uri> imageUris;
    LayoutInflater layoutInflater;

    public PastEventImageAdapter(){}


    public PastEventImageAdapter(Context context, ArrayList<Uri> imageUris) {
        this.context = context;
        this.imageUris = imageUris;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public PastEventImageAdapter(FirebaseRecyclerOptions<PastEventPhotoModel> pastEvenPhotoOptions) {
    }

    @Override
    public int getCount() {
        return imageUris.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.event_images_item, container, false);
        ImageView pastEventImageItem = (ImageView) view.findViewById(R.id.past_event_image_item);
        pastEventImageItem.setImageURI(imageUris.get(position));

        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}