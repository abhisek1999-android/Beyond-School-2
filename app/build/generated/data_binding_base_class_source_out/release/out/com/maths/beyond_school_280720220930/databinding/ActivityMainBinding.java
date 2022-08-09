// Generated by view binder compiler. Do not edit!
package com.maths.beyond_school_280720220930.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.navigation.NavigationView;
import com.maths.beyond_school_280720220930.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final DrawerLayout rootView;

  @NonNull
  public final DrawerLayout drawerLayout2;

  @NonNull
  public final TextView greetingsTime;

  @NonNull
  public final CardView imageCardView;

  @NonNull
  public final CircleImageView imageViewProfileView;

  @NonNull
  public final TextView kidsNameTextView;

  @NonNull
  public final NavigationView navigationView2;

  @NonNull
  public final NestedScrollView scrollView2;

  @NonNull
  public final RecyclerView tablesRecyclerView;

  @NonNull
  public final NavDrawer2Binding tool;

  @NonNull
  public final ToolBarLayoutBinding toolBar;

  private ActivityMainBinding(@NonNull DrawerLayout rootView, @NonNull DrawerLayout drawerLayout2,
      @NonNull TextView greetingsTime, @NonNull CardView imageCardView,
      @NonNull CircleImageView imageViewProfileView, @NonNull TextView kidsNameTextView,
      @NonNull NavigationView navigationView2, @NonNull NestedScrollView scrollView2,
      @NonNull RecyclerView tablesRecyclerView, @NonNull NavDrawer2Binding tool,
      @NonNull ToolBarLayoutBinding toolBar) {
    this.rootView = rootView;
    this.drawerLayout2 = drawerLayout2;
    this.greetingsTime = greetingsTime;
    this.imageCardView = imageCardView;
    this.imageViewProfileView = imageViewProfileView;
    this.kidsNameTextView = kidsNameTextView;
    this.navigationView2 = navigationView2;
    this.scrollView2 = scrollView2;
    this.tablesRecyclerView = tablesRecyclerView;
    this.tool = tool;
    this.toolBar = toolBar;
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
      DrawerLayout drawerLayout2 = (DrawerLayout) rootView;

      id = R.id.greetingsTime;
      TextView greetingsTime = ViewBindings.findChildViewById(rootView, id);
      if (greetingsTime == null) {
        break missingId;
      }

      id = R.id.imageCardView;
      CardView imageCardView = ViewBindings.findChildViewById(rootView, id);
      if (imageCardView == null) {
        break missingId;
      }

      id = R.id.image_view_profile_view;
      CircleImageView imageViewProfileView = ViewBindings.findChildViewById(rootView, id);
      if (imageViewProfileView == null) {
        break missingId;
      }

      id = R.id.kidsNameTextView;
      TextView kidsNameTextView = ViewBindings.findChildViewById(rootView, id);
      if (kidsNameTextView == null) {
        break missingId;
      }

      id = R.id.navigation_view2;
      NavigationView navigationView2 = ViewBindings.findChildViewById(rootView, id);
      if (navigationView2 == null) {
        break missingId;
      }

      id = R.id.scrollView2;
      NestedScrollView scrollView2 = ViewBindings.findChildViewById(rootView, id);
      if (scrollView2 == null) {
        break missingId;
      }

      id = R.id.tablesRecyclerView;
      RecyclerView tablesRecyclerView = ViewBindings.findChildViewById(rootView, id);
      if (tablesRecyclerView == null) {
        break missingId;
      }

      id = R.id.tool;
      View tool = ViewBindings.findChildViewById(rootView, id);
      if (tool == null) {
        break missingId;
      }
      NavDrawer2Binding binding_tool = NavDrawer2Binding.bind(tool);

      id = R.id.toolBar;
      View toolBar = ViewBindings.findChildViewById(rootView, id);
      if (toolBar == null) {
        break missingId;
      }
      ToolBarLayoutBinding binding_toolBar = ToolBarLayoutBinding.bind(toolBar);

      return new ActivityMainBinding((DrawerLayout) rootView, drawerLayout2, greetingsTime,
          imageCardView, imageViewProfileView, kidsNameTextView, navigationView2, scrollView2,
          tablesRecyclerView, binding_tool, binding_toolBar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
