package com.maths.beyond_school_280720220930.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeData;
import com.maths.beyond_school_280720220930.database.process.ProgressDataBase;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.ArrayDeque;

public class ChaptersRecyclerAdapter extends RecyclerView.Adapter<ChaptersRecyclerAdapter.SubjectViewHolder> {

    private OnItemClickListener mListener;
    List<GradeData> list;
    Context context;
    ProgressDataBase progressDataBase;
    long timeSpend = 0;
    String subSub = "", chapter = "";
    private int lastAnimatedPosition = -1;


    public ChaptersRecyclerAdapter(Context context,OnItemClickListener mListener) {
        this.context = context;
        this.mListener=mListener;
        progressDataBase = ProgressDataBase.getDbInstance(context);
        list = new ArrayList<>();

    }

    public void submitList(List<GradeData> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.sub_layout, parent, false);
        return new ChaptersRecyclerAdapter.SubjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            holder.mView.setAnimation(animation);
            animation.start();
        }

        GradeData gradeData = list.get(position);

        if (gradeData.isUnlock()){
            holder.isLock.setVisibility(View.INVISIBLE);

        }

        holder.subSub.setText(gradeData.getChapter_name());
        holder.mView.setOnClickListener(v -> {
            mListener.onItemClick(gradeData);
        });
    }

    @Override
    public int getItemCount() {

        try {
            return list.size();
        } catch (Exception e) {
        }

        return 0;
    }


    public class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView subSub, chapters, status, timeText, scoreText;
        View mView;

        ImageView isLock;
        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            subSub = mView.findViewById(R.id.operation);
            isLock=mView.findViewById(R.id.isLocked);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(GradeData gradeData);
    }
}
