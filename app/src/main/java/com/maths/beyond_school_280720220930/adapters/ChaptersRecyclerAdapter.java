package com.maths.beyond_school_280720220930.adapters;

import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_SPELLING_DETAIL;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.maths.beyond_school_280720220930.LearningActivity;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeData;
import com.maths.beyond_school_280720220930.database.grade_tables.Grades_data;
import com.maths.beyond_school_280720220930.database.process.ProgressDataBase;
import com.maths.beyond_school_280720220930.english_activity.grammar.GrammarActivity;
import com.maths.beyond_school_280720220930.english_activity.spelling.EnglishSpellingActivity;
import com.maths.beyond_school_280720220930.english_activity.spelling_objects.SpellingActivity;
import com.maths.beyond_school_280720220930.english_activity.vocabulary.EnglishActivity;
import com.maths.beyond_school_280720220930.utils.Constants;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChaptersRecyclerAdapter extends RecyclerView.Adapter<ChaptersRecyclerAdapter.SubjectViewHolder> {

    List<GradeData> list;
    Context context;
    ProgressDataBase progressDataBase;
    long timeSpend = 0;
    String subSub = "", chapter = "";


    public ChaptersRecyclerAdapter(List<GradeData> list, Context context) {
        this.list = list;
        this.context = context;

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




    }

    @Override
    public int getItemCount() {

        try {
            return list.size();
        }catch (Exception e){}

        return 0;
    }

    public interface MultiplicationOption {

        void multiplicationSelected();
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
}
