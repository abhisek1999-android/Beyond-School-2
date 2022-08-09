// Generated by view binder compiler. Do not edit!
package com.maths.beyond_school_280720220930.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.maths.beyond_school_280720220930.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivitySettingsBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ImageView image;

  @NonNull
  public final Switch logSwitch;

  @NonNull
  public final RelativeLayout rLayout;

  @NonNull
  public final ToolBarLayoutBackButtonBinding toolBar;

  private ActivitySettingsBinding(@NonNull LinearLayout rootView, @NonNull ImageView image,
      @NonNull Switch logSwitch, @NonNull RelativeLayout rLayout,
      @NonNull ToolBarLayoutBackButtonBinding toolBar) {
    this.rootView = rootView;
    this.image = image;
    this.logSwitch = logSwitch;
    this.rLayout = rLayout;
    this.toolBar = toolBar;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySettingsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySettingsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_settings, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySettingsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.image;
      ImageView image = ViewBindings.findChildViewById(rootView, id);
      if (image == null) {
        break missingId;
      }

      id = R.id.logSwitch;
      Switch logSwitch = ViewBindings.findChildViewById(rootView, id);
      if (logSwitch == null) {
        break missingId;
      }

      id = R.id.rLayout;
      RelativeLayout rLayout = ViewBindings.findChildViewById(rootView, id);
      if (rLayout == null) {
        break missingId;
      }

      id = R.id.toolBar;
      View toolBar = ViewBindings.findChildViewById(rootView, id);
      if (toolBar == null) {
        break missingId;
      }
      ToolBarLayoutBackButtonBinding binding_toolBar = ToolBarLayoutBackButtonBinding.bind(toolBar);

      return new ActivitySettingsBinding((LinearLayout) rootView, image, logSwitch, rLayout,
          binding_toolBar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
