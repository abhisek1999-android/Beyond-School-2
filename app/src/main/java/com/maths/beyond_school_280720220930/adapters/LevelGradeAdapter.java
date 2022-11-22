package com.maths.beyond_school_280720220930.adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeData;
import com.maths.beyond_school_280720220930.databinding.RowLayoutLevel3Binding;

public class LevelGradeAdapter extends ListAdapter<GradeData, LevelGradeAdapter.LevelGradeViewHolder> {

    public LevelGradeAdapter() {
        super(getDiffCallback());
    }

    private OnItemGradeClickListener onItemGradeClickListener = null;


    public void setOnItemGradeClickListener(OnItemGradeClickListener onItemGradeClickListener) {
        this.onItemGradeClickListener = onItemGradeClickListener;
    }


    @NonNull
    @Override
    public LevelGradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LevelGradeViewHolder(RowLayoutLevel3Binding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull LevelGradeViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class LevelGradeViewHolder extends RecyclerView.ViewHolder {
        private final RowLayoutLevel3Binding binding;

        public LevelGradeViewHolder(@NonNull RowLayoutLevel3Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void bind(GradeData gradeData) {
            binding.chapters.setText(gradeData.getChapter_name());

            if (gradeData.isUnlock()){
                binding.imageViewLessonLock.setVisibility(View.INVISIBLE);
                binding.imageViewLessonLockTest.setVisibility(View.INVISIBLE);
                binding.textViewEnrollTest.setText("Exercise is unlocked");
                binding.textViewEnroll.setText("Learning is unlocked");
            }

            if (gradeData.isIs_completed()){
                binding.imageViewLessonLock.setImageDrawable(binding.getRoot().getContext().getDrawable(R.drawable.verified_green_tick));
                binding.imageViewLessonLock.setVisibility(View.VISIBLE);
            }
            if (gradeData.isIs_completed_ex()){
                binding.imageViewLessonLockTest.setImageDrawable(binding.getRoot().getContext().getDrawable(R.drawable.verified_green_tick));
                binding.imageViewLessonLockTest.setVisibility(View.VISIBLE);
            }


            if (onItemGradeClickListener != null) {
                binding.relativeLayoutLearn.setOnClickListener(v -> {
                    onItemGradeClickListener.onItemGradeClick(gradeData, true);
                });
                binding.relativeLayoutExercise.setOnClickListener(v -> {
                    onItemGradeClickListener.onItemGradeClick(gradeData, false);
                });
            }
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

    public interface OnItemGradeClickListener {
        void onItemGradeClick(GradeData gradeData, Boolean isLearn);
    }
}
