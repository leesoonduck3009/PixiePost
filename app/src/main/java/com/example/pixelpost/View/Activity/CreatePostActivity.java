package com.example.pixelpost.View.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pixelpost.Contract.Activity.ICreatePostActivityContract;
import com.example.pixelpost.Model.Post.Post;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.Presenter.Acitivity.CreatePostActivityPresenter;
import com.example.pixelpost.Utils.SupportClass.BitmapToByte;
import com.example.pixelpost.Utils.SupportClass.PreferenceManager;
import com.example.pixelpost.databinding.ActivityCreatePostBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.List;

public class CreatePostActivity extends AppCompatActivity implements ICreatePostActivityContract.View {
    private ActivityCreatePostBinding viewBinding;
    private Bitmap capturedImage;
    private ICreatePostActivityContract.Presenter presenter;
    private User currentUser;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        presenter = new CreatePostActivityPresenter(this);
        viewBinding.cancelCreatePost.setOnClickListener(v -> cancelCreatePost());
        viewBinding.postContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                handleFocusPostContent();
            }
        });
        preferenceManager = new PreferenceManager(getApplicationContext());
        currentUser = (User) preferenceManager.getSerializable(User.FIREBASE_COLLECTION_NAME);
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
    private void setListener(){
        viewBinding.createPostBtn.setOnClickListener(v->{
            List<String> exisedPeople = currentUser.getFriendList();
            exisedPeople.add(currentUser.getId());
            Post post = new Post.Builder().setTimePosted(new Date()).setText(viewBinding.postContent.getText().toString())
                    .setOwnerId(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setDisplayedUsers(exisedPeople).build();
            byte[] byteCaptureImage = BitmapToByte.convertBitmapToByteArray(capturedImage, Bitmap.CompressFormat.PNG, 100);
            presenter.uploadPost(post,byteCaptureImage);
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

    @Override
    public void uploadPostSuccess() {
        Toast.makeText(getApplicationContext(), "Create post success", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    @Override
    public void uploadPostFailed(Exception e) {
        Toast.makeText(getApplicationContext(), "Upload post failed", Toast.LENGTH_SHORT).show();
        Log.e("create-post-failed", e.getMessage());
    }
}
