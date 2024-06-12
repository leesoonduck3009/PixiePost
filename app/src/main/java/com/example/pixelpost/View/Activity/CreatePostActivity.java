package com.example.pixelpost.View.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pixelpost.databinding.ActivityCreatePostBinding;
import com.example.pixelpost.databinding.ActivityMainBinding;

import java.io.File;

public class CreatePostActivity extends AppCompatActivity {
    private ActivityCreatePostBinding viewBinding;
    private Bitmap capturedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        viewBinding.cancelCreatePost.setOnClickListener(v -> cancelCreatePost());
        viewBinding.postContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                handleFocusPostContent();
            }
        });

        Intent intent = getIntent();
        String bitmapPath = intent.getStringExtra("bitmapPath");

        if (bitmapPath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(bitmapPath);
            viewBinding.capturedImage.setImageBitmap(bitmap);
            capturedImage = bitmap;
        }

//        String imagePath = getIntent().getStringExtra("image_path");
//        if (imagePath != null) {
//            File imgFile = new File(imagePath);
//            if (imgFile.exists()) {
//                viewBinding.capturedImage.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
//            }
//        }

        viewBinding.postContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(viewBinding.postContent.getText().length() > 0) {
                    viewBinding.postContent.setHint("");
                } else {
                    viewBinding.postContent.setHint("Thêm một tin nhắn");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void cancelCreatePost() {
        Intent intent = new Intent(CreatePostActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void handleFocusPostContent() {
        viewBinding.postContent.setText("");
        viewBinding.postContent.setHint("Thêm một tin nhắn");
    }
}
