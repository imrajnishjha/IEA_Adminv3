package com.example.ieaadmin;

public class PastEventPhotoModel {
    String imageUri;

    public PastEventPhotoModel() {
    }

    public PastEventPhotoModel(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}