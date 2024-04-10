//package com.example.pixelpost.View.Activity;
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import androidx.activity.result.ActivityResultCallback;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.camera.core.CameraSelector;
//import androidx.camera.core.ImageCapture;
//import androidx.camera.core.Preview;
//import androidx.camera.lifecycle.ProcessCameraProvider;
//import androidx.camera.video.Recorder;
//import androidx.camera.video.Recording;
//import androidx.camera.video.VideoCapture;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentActivity;
//
//import com.example.pixelpost.R;
//import com.example.pixelpost.databinding.ActivityMainBinding;
//import com.example.pixelpost.databinding.CameraActivityBinding;
//import com.google.common.util.concurrent.ListenableFuture;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Map;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//
//public class CameraActivity extends Fragment {
//    private CameraActivityBinding viewBinding;
//    private ImageCapture imageCapture;
//    private VideoCapture<Recorder> videoCapture;
//    private Recording recording;
//    private ExecutorService cameraExecutor;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        View rootView = inflater.inflate(R.layout.camera_activity,container, false);
//        return rootView;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        viewBinding = CameraActivityBinding.inflate(getLayoutInflater());
//
//        //Request camera permissions
//        if(allPermissionsGranted()) {
//            startCamera();
//        } else {
//            requestPermissions();
//        }
//
//        // Set up the listeners for take photo buttons
//        viewBinding.imageCaptureButton.setOnClickListener(v -> takePhoto());
//
//        cameraExecutor = Executors.newSingleThreadExecutor();
//    }
//
//    private void startCamera() {
//        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
//        cameraProviderFuture.addListener(() -> {
//            try {
//                // Used to bind the lifecycle of cameras to the lifecycle owner
//                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
//                // Preview
//                Preview preview = new Preview.Builder().build();
//                preview.setSurfaceProvider(viewBinding.previewView.getSurfaceProvider());
//
//                // Select back camera as a default
//                CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
//
//                // Unbind use cases before rebinding
//                cameraProvider.unbindAll();
//
//                // Bind use cases to camera
//                cameraProvider.bindToLifecycle(this, cameraSelector, preview);
//                } catch (Exception exc) {
//                    Toast.makeText(this, "Starting camera failed", Toast.LENGTH_SHORT).show();
//                }
//            }, ContextCompat.getMainExecutor(this));
//    }
//
//    private void takePhoto() {
//
//    }
//
//    private void captureVideo() {
//
//    }
//
//    private void requestPermissions() {
//        activityResultLauncher.launch(REQUIRED_PERMISSIONS);
//    }
//
//    private boolean allPermissionsGranted() {
//        for (String permissions : REQUIRED_PERMISSIONS) {
//            if (ContextCompat.checkSelfPermission(this, permissions) != PackageManager.PERMISSION_GRANTED) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        cameraExecutor.shutdown();
//    }
//
//    private static final String TAG = "CameraXApp";
//    private static final String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
//    private static final int REQUEST_CODE_PERMISSIONS = 10;
//    private static String[] REQUIRED_PERMISSIONS = new String[]{
//            Manifest.permission.CAMERA,
//            Manifest.permission.RECORD_AUDIO
//    };
//
//    // Check and add WRITE_EXTERNAL_STORAGE permission for API <= P
//    static {
//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
//            ArrayList<String> permissionList = new ArrayList<>(Arrays.asList(REQUIRED_PERMISSIONS));
//            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            REQUIRED_PERMISSIONS = permissionList.toArray(new String[0]);
//        }
//    }
//
//    private final ActivityResultLauncher<String[]> activityResultLauncher = registerForActivityResult(
//            new ActivityResultContracts.RequestMultiplePermissions(),
//            new ActivityResultCallback<Map<String, Boolean>>() {
//                @Override
//                public void onActivityResult(Map<String, Boolean> permissions) {
//                    // Handle Permission granted/rejected
//                    boolean permissionGranted = true;
//                    for (Map.Entry<String, Boolean> entry : permissions.entrySet()) {
//                        if (Arrays.asList(REQUIRED_PERMISSIONS).contains(entry.getKey()) && !entry.getValue()) {
//                            permissionGranted = false;
//                            break;
//                        }
//                    }
//                    if (!permissionGranted) {
//                        Toast.makeText(CameraActivity.this,
//                                "Permission request denied",
//                                Toast.LENGTH_SHORT).show();
//                    } else {
//                        //startCamera();
//                    }
//                }
//            });
//}
