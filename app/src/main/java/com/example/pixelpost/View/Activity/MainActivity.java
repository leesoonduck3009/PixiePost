package com.example.pixelpost.View.Activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.view.PreviewView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import android.Manifest;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import com.example.pixelpost.Model.Post.Post;
import com.example.pixelpost.Contract.Activity.IMainActivityContract;
import com.example.pixelpost.Model.FriendRequest.FriendRequest;
import com.example.pixelpost.Model.User.User;
import com.example.pixelpost.Presenter.Acitivity.MainActivityPresenter;
import com.example.pixelpost.R;
import com.example.pixelpost.Utils.SupportClass.PreferenceManager;
import com.example.pixelpost.View.Activity.Conversation.ConversationListActivity;
import com.example.pixelpost.View.Activity.Login.Login01Activity;
import com.example.pixelpost.View.Adapter.PostSliderAdapter;
import com.example.pixelpost.View.Activity.QR.QrScannerActivity;
import com.example.pixelpost.View.Dialog.FriendRequestDialog;
import com.example.pixelpost.databinding.ActivityMainBinding;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements IMainActivityContract.View {
    // Binding
    private ActivityMainBinding viewBinding;

    // Camera
    private PreviewView previewView;
    private ImageCapture imageCapture;
    private CameraSelector cameraSelector;
    private Bitmap capturedImage;
    private ExecutorService cameraExecutor;
    private ProcessCameraProvider cameraProvider;

    // Slider
    private ScrollView homeScrollView;
    private GestureDetector gestureDetector;
    private ViewPager2 postSlider;
    private List<Post> postList;
    private PostSliderAdapter postSliderAdapter;

    // Other
    private ImageView btnMessage;
    private PreferenceManager preferenceManager;
    private ImageView profile_btn;
    private LinearLayout friend_btn;

    private IMainActivityContract.Presenter presenter;
    private FriendRequestDialog tempFriendRequestDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        /// Slider
        // Set up home slide layout
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        LinearLayout cameraContainer = findViewById(R.id.camera_container);
        setLinearLayoutHeight(cameraContainer, displayMetrics.heightPixels);
        ConstraintLayout postSliderContainer = findViewById(R.id.post_slider_container);
        setConstrainLayoutHeight(postSliderContainer, displayMetrics.heightPixels);
        homeScrollView = findViewById(R.id.home_scrollview);
        gestureDetector = new GestureDetector(this, new SwipeGestureListener());
        homeScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {

            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                float diffY = scrollY - oldScrollY;
                postSlider.setUserInputEnabled(!(diffY < 0));
            }
        });

        homeScrollView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        preferenceManager = new PreferenceManager(getApplicationContext());
        checkLogin();
        btnMessage = findViewById(R.id.btnMessage);
        profile_btn = findViewById(R.id.profile_btn);
        friend_btn = findViewById(R.id.friend_btn);

        //Post Slider
        postSlider = findViewById(R.id.post_slider);

        postList = new ArrayList<>();

        initPost();
        postSliderAdapter = new PostSliderAdapter(postList);

        postSlider.setAdapter(postSliderAdapter);
        postSliderAdapter.notifyDataSetChanged();


        /// Other
        presenter = new MainActivityPresenter(this);

        friend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FriendFunctionActivity.class);
                startActivity(intent);
            }
        });
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ConversationListActivity.class);
                startActivity(intent);
            }
        });
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        /// Camera
        //Request camera permissions
        cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
        imageCapture = new ImageCapture.Builder().setFlashMode(ImageCapture.FLASH_MODE_OFF).build();

        if(allPermissionsGranted()) {
            startCamera();
        } else {
            requestPermissions();
        }

        //Set up the listeners for take photo buttons
        viewBinding.imageCaptureButton.setOnClickListener(v -> takePhoto());
        viewBinding.changeCameraBtn.setOnClickListener(v -> flipCam());
        viewBinding.flashBtn.setOnClickListener(v -> changeFlash());
        viewBinding.cancelCreatePost.setOnClickListener(v -> cancelCreatePost());

        cameraExecutor = Executors.newSingleThreadExecutor();
        checkFromFriendRequest();
    }


    /// Slider-related func
    private class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 0;
        private static final int SWIPE_VELOCITY_THRESHOLD = 0;

        @Override
        public boolean onFling(MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) throws NullPointerException {
            try {
                float diffY = e2.getY() - e1.getY();

                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > 0) {
                    if (diffY > 0 && velocityY > 0) {
                        // Swipe down, scroll up

                        animateScroll(-homeScrollView.getHeight());
                    } else {
                        // Swipe up, scroll down
                        animateScroll(homeScrollView.getHeight());
                    }
                    return true;
                }
                return false;
            } catch (Exception e) {
                float diffY = e1.getY();
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > 0) {
                    if (diffY > 0 && velocityY > 0) {
                        // Swipe down, scroll up
                        animateScroll(-homeScrollView.getHeight());
                    } else {
                        // Swipe up, scroll down
                        animateScroll(homeScrollView.getHeight());
                    }
                    return true;
                }
                return false;
            }
        }
    }

    private void animateScroll(final int distance) {
        homeScrollView.post(new Runnable() {
            @Override
            public void run() {
                homeScrollView.smoothScrollBy(0, distance);
            }
        });
    }

    private void setLinearLayoutHeight(LinearLayout linearLayout, int height) {
        ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
        layoutParams.height = height;
        linearLayout.setLayoutParams(layoutParams);
    }

    private void setConstrainLayoutHeight(ConstraintLayout constrainLayout, int height) {
        ViewGroup.LayoutParams layoutParams = constrainLayout.getLayoutParams();
        layoutParams.height = height;
        constrainLayout.setLayoutParams(layoutParams);
    }

    public void initPost(){
        postList.add(new Post.Builder().setText("Đây là post 1").setTimePosted(new Date()).setUrl("https://file.hstatic.net/1000159991/file/doremon-min_d7fba7f7f60a41a0af6e67dcaeb75634_grande.jpg").build());
        postList.add(new Post.Builder().setText("Đây là post 2").setTimePosted(new Date()).build());
        postList.add(new Post.Builder().setText("Đây là post 3").setTimePosted(new Date()).build());
        postList.add(new Post.Builder().setText("Đây là post 4").setTimePosted(new Date()).build());
        postList.add(new Post.Builder().setText("Đây là post 5").setTimePosted(new Date()).build());
        postList.add(new Post.Builder().setText("Đây là post 6").setTimePosted(new Date()).build());
        postList.add(new Post.Builder().setText("Đây là post 7").setTimePosted(new Date()).build());
        postList.add(new Post.Builder().setText("Đây là post 8").setTimePosted(new Date()).build());
    }

    /// Camera-related func
    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                // Used to bind the lifecycle of cameras to the lifecycle owner
                cameraProvider = cameraProviderFuture.get();
                bindCamera();
                } catch (Exception exc) {
                    Toast.makeText(this, "Starting camera failed", Toast.LENGTH_SHORT).show();
                }
            }, ContextCompat.getMainExecutor(this));
    }

    private void bindCamera() {
        // Preview
        Preview preview = new Preview.Builder().build();

        // Unbind use cases before rebinding
        cameraProvider.unbindAll();

        // Bind use cases to camera
        cameraProvider.bindToLifecycle(this, this.cameraSelector, preview, this.imageCapture);
        preview.setSurfaceProvider(viewBinding.previewView.getSurfaceProvider());
    }

    private void flipCam() {
        if (this.cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
            this.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
            startCamera();
        } else {
            this.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
            startCamera();
        }
    }

    private void changeFlash() {
        if (imageCapture.getFlashMode() == ImageCapture.FLASH_MODE_ON) {
            imageCapture.setFlashMode(ImageCapture.FLASH_MODE_OFF);
            viewBinding.flashBtn.setImageResource(R.drawable.ic_flash_on);
        } else {
            imageCapture.setFlashMode(ImageCapture.FLASH_MODE_ON);
            viewBinding.flashBtn.setImageResource(R.drawable.icon_flash);
        }
    }

    public void pauseCamera() {
        if (this.cameraProvider != null) {
            cameraProvider.unbindAll();
        }
    }

    public void resumeCamera() {
        if (this.cameraProvider != null) {
            bindCamera();
        }
    }

    private void takePhoto() {
        ImageCapture imageCapture = this.imageCapture;
        if (imageCapture == null) {
            return;
        }

        imageCapture.takePicture(
                ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageCapturedCallback() {
                    @Override
                    public void onError(ImageCaptureException exc) {
                        Log.e(TAG, "Photo capture failed: " + exc.getMessage(), exc);
                    }

                    @Override
                    public void onCaptureSuccess(ImageProxy imageProxy) {
                        ByteBuffer buffer = imageProxy.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        int rotationDegrees = imageProxy.getImageInfo().getRotationDegrees();
                        Bitmap rotatedBitmap = rotateBitmap(bitmap, rotationDegrees);
                        capturedImage = rotatedBitmap;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                viewBinding.capturedImage.setImageBitmap(capturedImage);
                                viewBinding.previewViewContainer.setVisibility(View.INVISIBLE);

                                viewBinding.changeCameraBtn.setVisibility(View.INVISIBLE);
                                viewBinding.changeCameraBtn.setClickable(false);

                                viewBinding.flashBtn.setVisibility(View.INVISIBLE);
                                viewBinding.flashBtn.setClickable(false);

                                viewBinding.imageCaptureButton.setVisibility(View.INVISIBLE);
                                viewBinding.imageCaptureButton.setClickable(false);

                                viewBinding.historyPost.setVisibility(View.INVISIBLE);
                                viewBinding.historyPost.setClickable(false);

                                viewBinding.capturedImageContainer.setVisibility(View.VISIBLE);
                                viewBinding.historyPost.setClickable(true);

                                viewBinding.cancelCreatePost.setVisibility(View.VISIBLE);
                                viewBinding.cancelCreatePost.setClickable(true);

                                viewBinding.createPostBtn.setVisibility(View.VISIBLE);
                                viewBinding.createPostBtn.setClickable(true);

                                pauseCamera();
                            }
                        });

                        imageProxy.close();

                        String msg = "Photo capture succeeded";
                        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, msg);
                    }
                }
        );
    }

    private Bitmap rotateBitmap(Bitmap bitmap, int rotationDegrees) {
        if (rotationDegrees == 0) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(rotationDegrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private void cancelCreatePost() {
        viewBinding.changeCameraBtn.setVisibility(View.VISIBLE);
        viewBinding.changeCameraBtn.setClickable(true);

        viewBinding.flashBtn.setVisibility(View.VISIBLE);
        viewBinding.flashBtn.setClickable(true);

        viewBinding.imageCaptureButton.setVisibility(View.VISIBLE);
        viewBinding.imageCaptureButton.setClickable(true);

        viewBinding.historyPost.setVisibility(View.VISIBLE);
        viewBinding.historyPost.setClickable(true);

        viewBinding.capturedImageContainer.setVisibility(View.INVISIBLE);
        viewBinding.historyPost.setClickable(false);

        viewBinding.cancelCreatePost.setVisibility(View.INVISIBLE);
        viewBinding.cancelCreatePost.setClickable(false);

        viewBinding.createPostBtn.setVisibility(View.INVISIBLE);
        viewBinding.createPostBtn.setClickable(false);

        resumeCamera();
    }

    private void requestPermissions() {
       activityResultLauncher.launch(REQUIRED_PERMISSIONS);
    }

    private boolean allPermissionsGranted() {
        for (String permissions : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permissions) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }

    /// Other func
    private void checkLogin()
    {
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            preferenceManager.removeKey(User.FIREBASE_COLLECTION_NAME);
            Intent intent = new Intent(getApplicationContext(), Login01Activity.class);
            startActivity(intent);
            finish();
        }
    }
    private static final String TAG = "CameraXApp";
    private static final String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    // Check and add WRITE_EXTERNAL_STORAGE permission for API <= P
    static {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            ArrayList<String> permissionList = new ArrayList<>(Arrays.asList(REQUIRED_PERMISSIONS));
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            REQUIRED_PERMISSIONS = permissionList.toArray(new String[0]);
        }
    }

    private final ActivityResultLauncher<String[]> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> permissions) {
                    // Handle Permission granted/rejected
                    boolean permissionGranted = true;
                    for (Map.Entry<String, Boolean> entry : permissions.entrySet()) {
                        if (Arrays.asList(REQUIRED_PERMISSIONS).contains(entry.getKey()) && !entry.getValue()) {
                            permissionGranted = false;
                            break;
                        }
                    }
                    if (!permissionGranted) {
                        Toast.makeText(MainActivity.this,
                                "Permission request denied",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        startCamera();
                    }
                }
            });


    private void checkFromFriendRequest()
    {
        Intent intent = getIntent();
        if(intent.getBooleanExtra(QrScannerActivity.FROM_FRIEND_REQUEST_QR,false))
        {

            String id = intent.getStringExtra("id");
            if(Objects.equals(id, FirebaseAuth.getInstance().getCurrentUser().getUid()))
            {
                User user = (User) preferenceManager.getSerializable(User.FIREBASE_COLLECTION_NAME);
                FriendRequestDialog.showDialog(this, user,null, FriendRequestDialog.FriendRequestDialogType.IS_FRIEND, new FriendRequestDialog.DialogClickListener() {
                    @Override
                    public void onAcceptFriendClick(FriendRequestDialog dialog) {
                    }
                });
            }
            else
                presenter.getUserFriendRequest(id);

        }
    }

    @Override
    public void getFriendRequestSuccess(User user,FriendRequest friendRequest, FriendRequestDialog.FriendRequestDialogType type) {
            FriendRequestDialog.showDialog(this, user,friendRequest, type, new FriendRequestDialog.DialogClickListener() {
                @Override
                public void onAcceptFriendClick(FriendRequestDialog dialog) {
                    if(dialog.getType() == FriendRequestDialog.FriendRequestDialogType.NOT_IS_FRIEND)
                    {
                        tempFriendRequestDialog = dialog;
                        presenter.sendFriendRequest(user.getId());
                    }
                    if(dialog.getType() == FriendRequestDialog.FriendRequestDialogType.ACCEPT)
                    {
                        tempFriendRequestDialog = dialog;
                        presenter.acceptFriendRequest(friendRequest);
                    }
                }
            });
    }

    @Override
    public void loadingFailed(Exception e) {
        tempFriendRequestDialog.failedLoading();
        Log.e("friend-request",e.getMessage());
    }

    @Override
    public void sendFriendRequestSuccess(FriendRequest friendRequest) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Thực hiện các hành động sau 1 giây
                // Ví dụ: ẩn ImageViewSuccess và đóng dialog
                tempFriendRequestDialog.successLoading();
                Toast.makeText(getApplicationContext(),"Kết bạn thành công", Toast.LENGTH_SHORT).show();
            }
        }, 1500);

    }

    @Override
    public void confirmFriendRequestSucess() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tempFriendRequestDialog.successLoading();
                Toast.makeText(getApplicationContext(),"Đã kết bạn thành công", Toast.LENGTH_SHORT).show();
            }
        }, 1500);
    }
}