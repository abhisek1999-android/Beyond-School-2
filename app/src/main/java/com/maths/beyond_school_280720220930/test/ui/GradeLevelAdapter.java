package com.maths.beyond_school_280720220930.test.ui;

import android.animation.LayoutTransition;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.databinding.RowLevelGradeBinding;
import com.maths.beyond_school_280720220930.test.data.GradeModelNew;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

public class GradeLevelAdapter extends ListAdapter<GradeModelNew.EnglishModel.Chapter, GradeLevelAdapter.GradeLevelViewHolder> {

    protected GradeLevelAdapter() {
        super(DIFF_CALLBACK);
    }


    @NonNull
    @Override
    public GradeLevelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GradeLevelViewHolder(RowLevelGradeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GradeLevelViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class GradeLevelViewHolder extends RecyclerView.ViewHolder {
        private final RowLevelGradeBinding binding;

        public GradeLevelViewHolder(RowLevelGradeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
            binding.getRoot().setOnClickListener(v -> {
                expand();
            });
        }

        private void expand() {
            int v = (binding.linearLayoutShowLesson.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
            TransitionManager.beginDelayedTransition(binding.getRoot(), new AutoTransition());
            binding.linearLayoutShowLesson.setVisibility(v);
            binding.dividerSubject.setVisibility(v);
        }

        public void bind(GradeModelNew.EnglishModel.Chapter englishModel) {
            binding.textViewSubject.setText(englishModel.getChapter_name());
            Log.d("AAA", "bind: " + englishModel.getLessons().size());
            englishModel.getLessons().forEach(lesson -> {
                UtilityFunctions.addViewToLinearLayout(R.layout.row_layout_level_3, lesson, binding.linearLayoutShowLesson, (view, lesson1) -> {
                    var text_view_lesson = (TextView) view.findViewById(R.id.text_view_lesson);
                    var image_view_lesson_lock = (ImageView) view.findViewById(R.id.image_view_lesson_lock);
                    text_view_lesson.setText(lesson1.getLesson_name());
                    UtilityFunctions.setVisibility(image_view_lesson_lock, lesson1.getUnlock() == 0);
                });
            });
        }
    }

    private static final DiffUtil.ItemCallback<GradeModelNew.EnglishModel.Chapter> DIFF_CALLBACK = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull GradeModelNew.EnglishModel.Chapter oldItem, @NonNull GradeModelNew.EnglishModel.Chapter newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull GradeModelNew.EnglishModel.Chapter oldItem, @NonNull GradeModelNew.EnglishModel.Chapter newItem) {
            return oldItem.getId().equals(newItem.getId());
        }
    };


}
