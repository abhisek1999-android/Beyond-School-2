// Generated by view binder compiler. Do not edit!
package com.maths.beyond_school_280720220930.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.card.MaterialCardView;
import com.maths.beyond_school_280720220930.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class SubLayoutBinding implements ViewBinding {
  @NonNull
  private final MaterialCardView rootView;

  @NonNull
  public final MaterialCardView card;

  @NonNull
  public final MaterialCardView card2;

  @NonNull
  public final TextView digit;

  @NonNull
  public final TextView digitVal;

  @NonNull
  public final TextView operation;

  private SubLayoutBinding(@NonNull MaterialCardView rootView, @NonNull MaterialCardView card,
      @NonNull MaterialCardView card2, @NonNull TextView digit, @NonNull TextView digitVal,
      @NonNull TextView operation) {
    this.rootView = rootView;
    this.card = card;
    this.card2 = card2;
    this.digit = digit;
    this.digitVal = digitVal;
    this.operation = operation;
  }

  @Override
  @NonNull
  public MaterialCardView getRoot() {
    return rootView;
  }

  @NonNull
  public static SubLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static SubLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.sub_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static SubLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      MaterialCardView card = (MaterialCardView) rootView;

      id = R.id.card2;
      MaterialCardView card2 = ViewBindings.findChildViewById(rootView, id);
      if (card2 == null) {
        break missingId;
      }

      id = R.id.digit;
      TextView digit = ViewBindings.findChildViewById(rootView, id);
      if (digit == null) {
        break missingId;
      }

      id = R.id.digit_val;
      TextView digitVal = ViewBindings.findChildViewById(rootView, id);
      if (digitVal == null) {
        break missingId;
      }

      id = R.id.operation;
      TextView operation = ViewBindings.findChildViewById(rootView, id);
      if (operation == null) {
        break missingId;
      }

      return new SubLayoutBinding((MaterialCardView) rootView, card, card2, digit, digitVal,
          operation);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}