package com.example.seqr;

/**
 * ScanQRFragment
 *
 * This class is used to initialize the camera.
 * It follows the steps from the "Getting Started with CameraX" developer
 * tutorial at https://developer.android.com/codelabs/camerax-getting-started
 * <p>
 * -Kyle Zwarich
 *
 */

//
// imports
//
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.Recording;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

//import com.example.seqr.databinding.ActivityMainBinding;
import com.example.seqr.databinding.FragmentScanQrBinding;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanQRFragment extends AppCompatActivity {
    private FragmentScanQrBinding viewBinding;
    private ExecutorService cameraExecutor;
    private ArrayList<String> requiredPerms;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        viewBinding = FragmentScanQrBinding.inflate(getLayoutInflater());
        requiredPerms.add(Manifest.permission.CAMERA);
        //setContentView(R.layout.fragment_scan_qr);

        //deal with permissions
        if (allPermissionsGranted()){
            startCamera();
        }
        else {
            requestPerms();
        }

        cameraExecutor = Executors.newSingleThreadExecutor();
    }

    private boolean allPermissionsGranted() {
        for (String permission : requiredPerms) {
            if (ContextCompat.checkSelfPermission(getBaseContext(), permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    private void requestPerms(){
        shouldShowRequestPermissionRationale("CAMERA");

        ActivityResultLauncher mylaunch = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                permissions -> {
                    boolean permissionGranted = true;
                    for (Map.Entry<String, Boolean> entry : permissions.entrySet()){
                        if (requiredPerms.contains(entry.getKey()) && !entry.getValue()) {
                            permissionGranted = false;
                            break;
                        }
                    }
                    if (!permissionGranted){
                        Toast.makeText(getApplicationContext(), "Permission request denied.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        startCamera();
                    }
                });

        mylaunch.launch(requiredPerms);
    }
    private void takePhoto(){

    }

    private void captureVideo(){

    }

    private void startCamera() {

        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        //set up the listener to watch the camera feed;
        cameraProviderFuture.addListener(() -> {

            //wrap this in a try/catch because .get() needs it for errors if the camera kakks out
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                //builder for a preview window
                Preview preview = new Preview.Builder().build();
                //camera selected;
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
                Camera camera = cameraProvider.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview);
                preview.setSurfaceProvider(viewBinding.viewFinder.getSurfaceProvider());
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }


}