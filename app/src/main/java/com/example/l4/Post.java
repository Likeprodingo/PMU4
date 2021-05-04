package com.example.l4;

import android.graphics.Bitmap;
import android.net.Uri;

public class Post {
    String title = "";
    String description = "";

    Bitmap bitmap = null;

    Uri songUri = null;

    Uri imageUri = null;

    public Post(String title, String description, Bitmap bitmap, Uri songUri, Uri imageUri) {
        this.title = title;
        this.description = description;
        this.bitmap = bitmap;
        this.songUri = songUri;
        this.imageUri = imageUri;
    }
    public Post(String title, String description, Bitmap bitmap) {
        this.title = title;
        this.description = description;
        this.bitmap = bitmap;
    }
    public Post(String title, String description) {
        this.title = title;
        this.description = description;
    }
    public Post(String title) {
        this.title = title;
    }

}
