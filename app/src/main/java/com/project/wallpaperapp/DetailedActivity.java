package com.project.wallpaperapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DetailedActivity extends AppCompatActivity {
    public ImageView imageView;
    public Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);

        String url = getIntent().getStringExtra("url");
        Glide.with(this).load(url).into(imageView);

        button.setOnClickListener(v -> {
            ProgressDialog dialog = new ProgressDialog(DetailedActivity.this);
            dialog.setTitle("Setting wallpaper...");
            dialog.setMessage("Just a second");
            dialog.show();

            Glide.with(this).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    try {
                        WallpaperManager.getInstance(DetailedActivity.this).setBitmap(resource);
                        dialog.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
    }
}
