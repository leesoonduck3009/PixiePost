package com.example.pixelpost.View.Activity.QR;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import android.Manifest;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.pixelpost.R;
import com.example.pixelpost.Utils.SupportClass.CameraXViewModel;
import com.example.pixelpost.Utils.SupportClass.QRCode;
import com.example.pixelpost.Utils.SupportClass.ViewUltil;
import com.example.pixelpost.View.Activity.MainActivity;
import com.google.mlkit.vision.common.InputImage;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executors;

public class QrScannerActivity extends AppCompatActivity {
    private ProcessCameraProvider cameraProvider;
    private CameraSelector cameraSelector;
    private int lensFacing = CameraSelector.LENS_FACING_BACK;
    private Preview previewUseCase;
    private ImageAnalysis analysisUseCase;
    private PreviewView previewView;
    private ImageView imgViewScanner;
    private ImageView btnExit;
    private LinearLayout btnPutImage;
    private LinearLayout btnQRCode;
    public static final String FROM_FRIEND_REQUEST_QR = "friend_request_qr";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qr_scanner);
        previewView = findViewById(R.id.preview_view);
        imgViewScanner = findViewById(R.id.imgViewScanner);
        btnPutImage = findViewById(R.id.btnPutImage);
        btnQRCode = findViewById(R.id.btnQRCode);
        btnExit = findViewById(R.id.btnExit);
        QRCode.isFind = false;
        previewView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setupCamera();
                // Loại bỏ listener để tránh gọi nhiều lần
                previewView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        setListener();
        // Các phần khác của mã của bạn...
    }
    private void setListener()
    {
        btnQRCode.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), QRProfileActivity.class));
            finish();
        });
        btnPutImage.setOnClickListener(v->{
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });
        btnExit.setOnClickListener(v->{
            finish();
        });
    }
//region setup camera for cameraX scan
    private void setupCamera() {
        cameraSelector = new CameraSelector.Builder().requireLensFacing(lensFacing).build();
        ViewModelProvider.AndroidViewModelFactory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        CameraXViewModel cameraXViewModel = new ViewModelProvider(this, factory).get(CameraXViewModel.class);
        cameraXViewModel.getProcessCameraProvider().observe(this, provider -> {
            cameraProvider = provider;
            if (isCameraPermissionGranted()) {
                bindCameraUseCases();
            } else {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSION_CAMERA_REQUEST
                );
            }
        });
    }

    private void bindCameraUseCases() {
        bindPreviewUseCase();
        bindAnalyseUseCase();
    }

    private void bindPreviewUseCase() {
        if (cameraProvider == null) {
            return;
        }
        if (previewUseCase != null) {
            cameraProvider.unbind(previewUseCase);
        }
        if(previewView.getDisplay()==null)
        {
            return;
        }
        previewUseCase = new Preview.Builder()
                .setTargetRotation(previewView.getDisplay().getRotation())
                .build();
        previewUseCase.setSurfaceProvider(previewView.getSurfaceProvider());

        try {
            cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    previewUseCase
            );
        } catch (IllegalStateException | IllegalArgumentException e) {
            Log.e(TAG, e.getMessage() != null ? e.getMessage() : e.toString());
        }
    }

    private void bindAnalyseUseCase() {


        if (cameraProvider == null) {
            return;
        }
        if (analysisUseCase != null) {
            cameraProvider.unbind(analysisUseCase);
        }
        analysisUseCase = new ImageAnalysis.Builder()
                .setTargetRotation(imgViewScanner.getDisplay().getRotation())
                .build();

        // Initialize our background executor
        java.util.concurrent.Executor cameraExecutor = Executors.newSingleThreadExecutor();

        analysisUseCase.setAnalyzer(
                cameraExecutor,
                this::processImageProxy
        );

        try {
            cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    analysisUseCase
            );
        } catch (IllegalStateException | IllegalArgumentException e) {
            Log.e(TAG, e.getMessage() != null ? e.getMessage() : e.toString());
        }
    }

    @OptIn(markerClass = ExperimentalGetImage.class) private void processImageProxy(
            ImageProxy imageProxy) {
            if (ViewUltil.isPointInsideView(imgViewScanner, imgViewScanner.getX(), imgViewScanner.getY())) {
                InputImage inputImage = InputImage.fromMediaImage(Objects.requireNonNull(imageProxy.getImage()), imageProxy.getImageInfo().getRotationDegrees());
                checkQR(inputImage,imageProxy);
            }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_CAMERA_REQUEST) {
            if (isCameraPermissionGranted()) {
                bindCameraUseCases();
            } else {
                Log.e(TAG, "no camera permission");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean isCameraPermissionGranted() {
        return ContextCompat.checkSelfPermission(
                getBaseContext(),
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private static final String TAG = QrScannerActivity.class.getSimpleName();
    private static final int PERMISSION_CAMERA_REQUEST = 1;


//endregion
    //region set ActivityResultLauncher
ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
        registerForActivityResult(new
                ActivityResultContracts.PickVisualMedia(), uri -> {
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                try {
                    InputImage inputImage = InputImage.fromFilePath(getApplicationContext(),uri);
                    checkQR(inputImage,null);
                } catch (IOException e) {
                    Log.e("RuntimeException", e.getMessage());
                    throw new RuntimeException(e);

                }
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });
        private void checkQR(InputImage inputImage,ImageProxy imageProxy)
        {
            QRCode.scanQRImage(inputImage, (text, e) -> {
                if (e != null) {
                    Log.e("qr-code", e.getMessage());
                } else {
                    if (text.startsWith(QRCode.QRCODE_STRING_GENERATED)) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra(FROM_FRIEND_REQUEST_QR,true);
                        startActivity(intent);
                        finish();
                        QRCode.isFind = true;
                    }
                }
                if(imageProxy!=null)
                    imageProxy.close();
            });
        }
//endregion
}
