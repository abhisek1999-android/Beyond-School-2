// Generated by view binder compiler. Do not edit!
package com.maths.beyond_school_280720220930.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.maths.beyond_school_280720220930.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ToolBarLayoutBinding implements ViewBinding {
  @NonNull
  private final Toolbar rootView;

  @NonNull
  public final TextView employeeId;

  @NonNull
  public final ImageView imageView4;

  @NonNull
  public final CircleImageView profileImage;

  @NonNull
  public final TextView userName;

  private ToolBarLayoutBinding(@NonNull Toolbar rootView, @NonNull TextView employeeId,
      @NonNull ImageView imageView4, @NonNull CircleImageView profileImage,
      @NonNull TextView userName) {
    this.rootView = rootView;
    this.employeeId = employeeId;
    this.imageView4 = imageView4;
    this.profileImage = profileImage;
    this.userName = userName;
  }

  @Override
  @NonNull
  public Toolbar getRoot() {
    return rootView;
  }

  @NonNull
  public static ToolBarLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ToolBarLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.tool_bar_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ToolBarLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.employeeId;
      TextView employeeId = ViewBindings.findChildViewById(rootView, id);
      if (employeeId == null) {
        break missingId;
      }

      id = R.id.imageView4;
      ImageView imageView4 = ViewBindings.findChildViewById(rootView, id);
      if (imageView4 == null) {
        break missingId;
      }

      id = R.id.profileImage;
      CircleImageView profileImage = ViewBindings.findChildViewById(rootView, id);
      if (profileImage == null) {
        break missingId;
      }

      id = R.id.userName;
      TextView userName = ViewBindings.findChildViewById(rootView, id);
      if (userName == null) {
        break missingId;
      }

      return new ToolBarLayoutBinding((Toolbar) rootView, employeeId, imageView4, profileImage,
          userName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
