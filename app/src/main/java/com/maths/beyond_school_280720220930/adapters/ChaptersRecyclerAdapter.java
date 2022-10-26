package com.maths.beyond_school_280720220930.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeData;
import com.maths.beyond_school_280720220930.database.process.ProgressDataBase;

import java.util.List;

public class ChaptersRecyclerAdapter extends RecyclerView.Adapter<ChaptersRecyclerAdapter.SubjectViewHolder> {

    private OnItemClickListener mListener;
    List<GradeData> list;
    Context context;
    ProgressDataBase progressDataBase;
    long timeSpend = 0;
    String subSub = "", chapter = "";


    public ChaptersRecyclerAdapter(List<GradeData> list, Context context,OnItemClickListener mListener) {
        this.list = list;
        this.context = context;
        this.mListener=mListener;
        progressDataBase = ProgressDataBase.getDbInstance(context);

    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.sub_layout, parent, false);
        return new ChaptersRecyclerAdapter.SubjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        GradeData gradeData = list.get(position);
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

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            subSub = mView.findViewById(R.id.operation);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(GradeData gradeData);
    }
}
