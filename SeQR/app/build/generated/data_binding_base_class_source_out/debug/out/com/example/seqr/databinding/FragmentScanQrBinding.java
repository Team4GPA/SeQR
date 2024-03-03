// Generated by view binder compiler. Do not edit!
package com.example.seqr.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.view.PreviewView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.seqr.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentScanQrBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView cameraLabel;

  @NonNull
  public final PreviewView viewFinder;

  private FragmentScanQrBinding(@NonNull ConstraintLayout rootView, @NonNull TextView cameraLabel,
      @NonNull PreviewView viewFinder) {
    this.rootView = rootView;
    this.cameraLabel = cameraLabel;
    this.viewFinder = viewFinder;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentScanQrBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentScanQrBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_scan_qr, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentScanQrBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.camera_label;
      TextView cameraLabel = ViewBindings.findChildViewById(rootView, id);
      if (cameraLabel == null) {
        break missingId;
      }

      id = R.id.viewFinder;
      PreviewView viewFinder = ViewBindings.findChildViewById(rootView, id);
      if (viewFinder == null) {
        break missingId;
      }

      return new FragmentScanQrBinding((ConstraintLayout) rootView, cameraLabel, viewFinder);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
