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

public final class LogBottomSheetBinding implements ViewBinding {
  @NonNull
  private final MaterialCardView rootView;

  @NonNull
  public final MaterialCardView logCard;

  @NonNull
  public final TextView logTextView;

  private LogBottomSheetBinding(@NonNull MaterialCardView rootView,
      @NonNull MaterialCardView logCard, @NonNull TextView logTextView) {
    this.rootView = rootView;
    this.logCard = logCard;
    this.logTextView = logTextView;
  }

  @Override
  @NonNull
  public MaterialCardView getRoot() {
    return rootView;
  }

  @NonNull
  public static LogBottomSheetBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LogBottomSheetBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.log_bottom_sheet, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LogBottomSheetBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      MaterialCardView logCard = (MaterialCardView) rootView;

      id = R.id.logTextView;
      TextView logTextView = ViewBindings.findChildViewById(rootView, id);
      if (logTextView == null) {
        break missingId;
      }

      return new LogBottomSheetBinding((MaterialCardView) rootView, logCard, logTextView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
