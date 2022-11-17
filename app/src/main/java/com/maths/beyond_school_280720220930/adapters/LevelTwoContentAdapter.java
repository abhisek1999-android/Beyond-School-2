package com.maths.beyond_school_280720220930.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeData;

import java.util.List;

public class LevelTwoContentAdapter extends RecyclerView.Adapter<LevelTwoContentAdapter.ProgressViewHolder> {


    List<String> subSubjectList;
    List<List<GradeData>> chaptersData;
    Context context;

    private LevelGradeAdapter.OnItemGradeClickListener onItemGradeClickListener = null;

    public void setOnItemGradeClickListener(LevelGradeAdapter.OnItemGradeClickListener onItemGradeClickListener) {
        this.onItemGradeClickListener = onItemGradeClickListener;
    }

    public LevelTwoContentAdapter(Context context) {
        this.context = context;

    }

    public void setNotesList(List<String> subSubjectList, List<List<GradeData>> chaptersData) {

        this.subSubjectList = subSubjectList;
        this.chaptersData = chaptersData;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_expandable_view, parent, false);
        LevelTwoContentAdapter.ProgressViewHolder progressViewHolder = new LevelTwoContentAdapter.ProgressViewHolder(view);
        return progressViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ProgressViewHolder holder, int position) {

        holder.headerTitle.setText(subSubjectList.get(position));
        var adapter = new LevelGradeAdapter();
        holder.levelThreeRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.levelThreeRecyclerView.setAdapter(adapter);
        adapter.submitList(chaptersData.get(position));
        if (onItemGradeClickListener != null) {
            adapter.setOnItemGradeClickListener(onItemGradeClickListener);
        }
    }

    @Override
    public int getItemCount() {

        return subSubjectList.size();

    }

    protected class ProgressViewHolder extends RecyclerView.ViewHolder {

        TextView headerTitle;
        RecyclerView levelThreeRecyclerView;

        View mView;

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            headerTitle = mView.findViewById(R.id.header);
            levelThreeRecyclerView = mView.findViewById(R.id.content);
        }
    }
}
