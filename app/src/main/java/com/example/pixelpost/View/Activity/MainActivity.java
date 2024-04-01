package com.example.pixelpost.View.Activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.Manifest;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pixelpost.R;
import com.example.pixelpost.databinding.ActivityMainBinding;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding viewBinding;
    private ImageCapture imageCapture;
    private VideoCapture<Recorder> videoCapture;
    private Recording recording;
    private ExecutorService cameraExecutor;

    private ImageView btnMessage;

    private  ImageView profile_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        btnMessage = findViewById(R.id.btnMessage);
        profile_btn = findViewById(R.id.profile_btn);

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
                Intent intent = new Intent(MainActivity.this, FriendFunctionActivity.class);
                startActivity(intent);
            }
        });
      
        //Request camera permissions
        if(allPermissionsGranted()) {
            startCamera();
        } else {
            requestPermissions();
        }

        // Set up the listeners for take photo buttons
        viewBinding.imageCaptureButton.setOnClickListener(v -> takePhoto());

        cameraExecutor = Executors.newSingleThreadExecutor();
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                // Used to bind the lifecycle of cameras to the lifecycle owner
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                // Preview
                Preview preview = new Preview.Builder().build();

                // Select back camera as a default
                CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;

                // Unbind use cases before rebinding
                cameraProvider.unbindAll();

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(this, cameraSelector, preview);
                preview.setSurfaceProvider(viewBinding.previewView.getSurfaceProvider());
                } catch (Exception exc) {
                    Log.e(TAG, "Use case starting camera failed", exc);
                }
            }, ContextCompat.getMainExecutor(this));
    }

    private void takePhoto() {
      
    }

    private void captureVideo() {

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
                        //startCamera();
                    }
                }
            });
}