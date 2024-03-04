package com.example.seqr;

/*
 * ScanQRFragment
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
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.Manifest;
import android.content.ContentValues;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.seqr.databinding.FragmentScanQrBinding;
import com.google.common.util.concurrent.ListenableFuture;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This fragment should pop up a video window that shows a live feed for an Android
 * camera. It is currently a work in progress based on the "Getting started with
 * CameraX" tutorial. It requires permissions from the Android OS before it can
 * access the camera.
 *
 * Mar 3 2024 - Still seeing issues with conversion from Kotlin to Java; this time,
 * having problems with Arraylist in the requestPerms() function.
 * -KZ
 */
public class ScanQRFragment extends Fragment {
    private FragmentScanQrBinding viewBinding;
    private ExecutorService cameraExecutor;
    private ArrayList<String> requiredPerms;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Camera","onCreate accessed.");
        this.viewBinding = FragmentScanQrBinding.inflate(getLayoutInflater());
        ArrayList<String> requiredPerms = new ArrayList<String>();
        this.requiredPerms = requiredPerms;
        requiredPerms.add(Manifest.permission.CAMERA);

        if (allPermissionsGranted()){
            startCamera();
        }else {
            requestPerms();
        }

        this.cameraExecutor = Executors.newSingleThreadExecutor();
    }

    /*    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
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
    }*/

    private boolean allPermissionsGranted() {
        for (String permission : this.requiredPerms) {
            if (ContextCompat.checkSelfPermission(this.getContext(), permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    private void requestPerms(){
        shouldShowRequestPermissionRationale("CAMERA");

        ActivityResultLauncher launcher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                permissions -> {
                    boolean permissionGranted = true;
                    for (Map.Entry<String, Boolean> entry : permissions.entrySet()){
                        if (this.requiredPerms.contains(entry.getKey()) && !entry.getValue()) {
                            permissionGranted = false;
                            break;
                        }
                    }
                    if (!permissionGranted){
                        Toast.makeText(getContext(), "Permission request denied.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        startCamera();
                    }
                });

        launcher.launch(this.requiredPerms);
    }

    private void startCamera() {

        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());

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
        }, ContextCompat.getMainExecutor(getContext()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }


}