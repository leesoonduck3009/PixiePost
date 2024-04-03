package com.example.pixelpost.Utils.SupportClass;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.ExecutionException;

public class CameraXViewModel extends AndroidViewModel {
    private MutableLiveData<ProcessCameraProvider> cameraProviderLiveData = null;

    public CameraXViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ProcessCameraProvider> getProcessCameraProvider() {
        if (cameraProviderLiveData == null) {
            cameraProviderLiveData = new MutableLiveData<>();
            ProcessCameraProvider.getInstance(getApplication()).addListener(
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                cameraProviderLiveData.setValue(ProcessCameraProvider.getInstance(getApplication()).get());
                            } catch (ExecutionException e) {
                                Log.e(TAG, "Unhandled exception", e);
                            } catch (InterruptedException e) {
                                Log.e(TAG, "Unhandled exception", e);
                            }
                        }
                    },
                    ContextCompat.getMainExecutor(getApplication())
            );
        }
        return cameraProviderLiveData;
    }

    private static final String TAG = "CameraXViewModel";
}

