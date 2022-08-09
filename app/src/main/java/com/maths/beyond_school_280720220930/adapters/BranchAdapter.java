package com.maths.beyond_school_280720220930.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.card.MaterialCardView;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.TopicsActivity;
import com.maths.beyond_school_280720220930.model.BranchModel;

import java.util.List;

public class BranchAdapter extends PagerAdapter {
    Context context;
    List<BranchModel> list;
    //Drawable[] imgs={};

    public BranchAdapter(Context context, List<BranchModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.branch_item_layout, container, false);
        ImageView img;
        img = view.findViewById(R.id.img);

        img.setImageResource(list.get(position).getImg());
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position==0){
                        Intent intent=new Intent(context, TopicsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                }else if (position==1){
                    Toast.makeText(context, "You have selected English", Toast.LENGTH_SHORT).show();
                }
            }
        });
        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}