package com.maths.beyond_school_280720220930.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.maths.beyond_school_280720220930.database.grade_tables.GradeData;
import com.maths.beyond_school_280720220930.databinding.RowExpandableViewBinding;
import com.maths.beyond_school_280720220930.databinding.RowLayoutLevel3Binding;

public class LevelGradeAdapter extends ListAdapter<GradeData, LevelGradeAdapter.LevelGradeViewHolder> {

    public LevelGradeAdapter() {
        super(getDiffCallback());
    }


    @NonNull
    @Override
    public LevelGradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LevelGradeViewHolder(RowLayoutLevel3Binding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LevelGradeViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static class LevelGradeViewHolder extends RecyclerView.ViewHolder {
        private final RowLayoutLevel3Binding binding;

        public LevelGradeViewHolder(@NonNull RowLayoutLevel3Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(GradeData gradeData) {
            binding.chapters.setText(gradeData.getChapter_name());
        }
    }


    @NonNull
    private static DiffUtil.ItemCallback<GradeData> getDiffCallback() {
        return new DiffUtil.ItemCallback<>() {
            @Override
            public boolean areItemsTheSame(@NonNull GradeData oldItem, @NonNull GradeData newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull GradeData oldItem, @NonNull GradeData newItem) {
                return oldItem.getId().equals(newItem.getId());
            }
        };
    }

}
