package com.maths.beyond_school_280720220930.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.maths.beyond_school_280720220930.LearningActivity;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.database.grade_tables.Grades_data;
import com.maths.beyond_school_280720220930.databinding.FragmentMathsBinding;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;


@RequiresApi(api = Build.VERSION_CODES.O)
public class MathsFragment extends Fragment {


    public MathsFragment() {
        super(R.layout.fragment_maths);
    }

    private FragmentMathsBinding binding;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentMathsBinding.bind(view);

        var dao = GradeDatabase.getDbInstance(requireContext()).gradesDao();
        var val = dao.values("Multiplication Tables", "Table of Eleven");

        binding.gotoMath.cardView1.setOnClickListener(v->{
            navigateToLearn(val);
        });


    }

    private void navigateToLearn(Grades_data val) {
        Intent intent = new Intent(requireContext(), LearningActivity.class);
        intent.putExtra("selected_sub", val.getChapter_name());
        intent.putExtra("subject", val.subject);
        var maxDigit = UtilityFunctions.getTableNumberFromString(val.chapter_name);
        if (maxDigit.equals("0")) {
            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            return;
        }
        intent.putExtra("max_digit", maxDigit);
        intent.putExtra("video_url", "");
        startActivity(intent);
    }
}