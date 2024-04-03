package com.example.pixelpost.View.Activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import android.Manifest;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import com.example.pixelpost.R;
import com.example.pixelpost.Utils.SupportClass.CameraXViewModel;
import com.example.pixelpost.Utils.SupportClass.QRCode;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QrScannerActivity extends AppCompatActivity {
    private ProcessCameraProvider cameraProvider;
    private CameraSelector cameraSelector;
    private int lensFacing = CameraSelector.LENS_FACING_BACK;
    private Preview previewUseCase;
    private ImageAnalysis analysisUseCase;
    private PreviewView previewView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qr_scanner);
        previewView = findViewById(R.id.preview_view);
        previewView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setupCamera();
                // Loại bỏ listener để tránh gọi nhiều lần
                previewView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        // Các phần khác của mã của bạn...
    }


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
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .build();

        com.google.mlkit.vision.barcode.BarcodeScanner barcodeScanner = BarcodeScanning.getClient(options);

        if (cameraProvider == null) {
            return;
        }
        if (analysisUseCase != null) {
            cameraProvider.unbind(analysisUseCase);
        }

        analysisUseCase = new ImageAnalysis.Builder()
                .setTargetRotation(previewView.getDisplay().getRotation())
                .build();

        // Initialize our background executor
        java.util.concurrent.Executor cameraExecutor = Executors.newSingleThreadExecutor();

        analysisUseCase.setAnalyzer(
                cameraExecutor,
                imageProxy -> processImageProxy(barcodeScanner, imageProxy)
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
            com.google.mlkit.vision.barcode.BarcodeScanner barcodeScanner,
            ImageProxy imageProxy
    ) {
        InputImage inputImage = InputImage.fromMediaImage(Objects.requireNonNull(imageProxy.getImage()), imageProxy.getImageInfo().getRotationDegrees());

        barcodeScanner.process(inputImage)
                .addOnSuccessListener(barcodes -> {
                    for (Barcode barcode : barcodes) {
                        android.graphics.Rect bounds = barcode.getBoundingBox();

                        String rawValue = barcode.getRawValue();
                        if(rawValue.startsWith(QRCode.QRCODE_STRING_GENERATED))
                        {
                            Toast.makeText(getApplicationContext(),rawValue,Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, e.getMessage() != null ? e.getMessage() : e.toString()))
                .addOnCompleteListener(task -> {
                    // When the image is from CameraX analysis use case, must call image.close() on received
                    // images when finished using them. Otherwise, new images may not be received or the camera
                    // may stall.
                    imageProxy.close();
                });
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
}
