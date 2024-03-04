// Generated by view binder compiler. Do not edit!
package com.example.seqr.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.seqr.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final DrawerLayout rootView;

  @NonNull
  public final Button adminButton;

  @NonNull
  public final BottomNavigationView bottomNav;

  @NonNull
  public final Button editProfileButton;

  @NonNull
  public final CheckBox enableGeoLocationCheckbox;

  @NonNull
  public final FrameLayout fragmentContainer;

  @NonNull
  public final DrawerLayout myDrawerLayout;

  @NonNull
  public final ImageView profilePicture;

  @NonNull
  public final Toolbar toolbar;

  private ActivityMainBinding(@NonNull DrawerLayout rootView, @NonNull Button adminButton,
      @NonNull BottomNavigationView bottomNav, @NonNull Button editProfileButton,
      @NonNull CheckBox enableGeoLocationCheckbox, @NonNull FrameLayout fragmentContainer,
      @NonNull DrawerLayout myDrawerLayout, @NonNull ImageView profilePicture,
      @NonNull Toolbar toolbar) {
    this.rootView = rootView;
    this.adminButton = adminButton;
    this.bottomNav = bottomNav;
    this.editProfileButton = editProfileButton;
    this.enableGeoLocationCheckbox = enableGeoLocationCheckbox;
    this.fragmentContainer = fragmentContainer;
    this.myDrawerLayout = myDrawerLayout;
    this.profilePicture = profilePicture;
    this.toolbar = toolbar;
  }

  @Override
  @NonNull
  public DrawerLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.admin_button;
      Button adminButton = ViewBindings.findChildViewById(rootView, id);
      if (adminButton == null) {
        break missingId;
      }

      id = R.id.bottom_nav;
      BottomNavigationView bottomNav = ViewBindings.findChildViewById(rootView, id);
      if (bottomNav == null) {
        break missingId;
      }

      id = R.id.edit_profile_button;
      Button editProfileButton = ViewBindings.findChildViewById(rootView, id);
      if (editProfileButton == null) {
        break missingId;
      }

      id = R.id.enable_geo_location_checkbox;
      CheckBox enableGeoLocationCheckbox = ViewBindings.findChildViewById(rootView, id);
      if (enableGeoLocationCheckbox == null) {
        break missingId;
      }

      id = R.id.fragment_container;
      FrameLayout fragmentContainer = ViewBindings.findChildViewById(rootView, id);
      if (fragmentContainer == null) {
        break missingId;
      }

      DrawerLayout myDrawerLayout = (DrawerLayout) rootView;

      id = R.id.profile_picture;
      ImageView profilePicture = ViewBindings.findChildViewById(rootView, id);
      if (profilePicture == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      return new ActivityMainBinding((DrawerLayout) rootView, adminButton, bottomNav,
          editProfileButton, enableGeoLocationCheckbox, fragmentContainer, myDrawerLayout,
          profilePicture, toolbar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
