package com.maths.beyond_school_new_071020221400.adapters;

import static com.maths.beyond_school_new_071020221400.utils.Constants.EXTRA_SPELLING_DETAIL;

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

import com.maths.beyond_school_new_071020221400.LearningActivity;
import com.maths.beyond_school_new_071020221400.R;
import com.maths.beyond_school_new_071020221400.database.grade_tables.Grades_data;
import com.maths.beyond_school_new_071020221400.database.process.ProgressDataBase;
import com.maths.beyond_school_new_071020221400.english_activity.spelling.EnglishSpellingActivity;
import com.maths.beyond_school_new_071020221400.english_activity.spelling_objects.SpellingActivity;
import com.maths.beyond_school_new_071020221400.english_activity.grammar.GrammarActivity;

import com.maths.beyond_school_new_071020221400.english_activity.vocabulary.EnglishActivity;
import com.maths.beyond_school_new_071020221400.utils.Constants;
import com.maths.beyond_school_new_071020221400.utils.UtilityFunctions;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SubjectRecyclerAdapter extends RecyclerView.Adapter<SubjectRecyclerAdapter.SubjectViewHolder> {

    List<Grades_data> list;
    Context context;
    ProgressDataBase progressDataBase;
    long timeSpend = 0;
    String subSub = "", chapter = "";


    public SubjectRecyclerAdapter(List<Grades_data> list, Context context) {
        this.list = list;
        this.context = context;

        progressDataBase = ProgressDataBase.getDbInstance(context);

    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.subject_view, parent, false);
        return new SubjectRecyclerAdapter.SubjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Grades_data grades_data = list.get(position);
        String val = grades_data.chapter;
        String chapter = grades_data.subject;
        String[] res = val.split(" ");

        try {
            if (grades_data.is_completed) {
                holder.status.setText("Completed");
                holder.status.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.green));
                holder.scoreText.setVisibility(View.VISIBLE);
                long correct;
                long wrong;
                if (grades_data.subject.equals("Multiplication Tables")) {
                    correct = UtilityFunctions.gettingCorrectValues(progressDataBase, "Table of " + UtilityFunctions.numberToWords(Integer.parseInt(grades_data.chapter)) + "( " + grades_data.chapter + "X )", true);
                    wrong = UtilityFunctions.gettingCorrectValues(progressDataBase, "Table of " + UtilityFunctions.numberToWords(Integer.parseInt(grades_data.chapter)) + "( " + grades_data.chapter + "X )", false);
                } else {
                    correct = UtilityFunctions.gettingCorrectValues(progressDataBase, grades_data.chapter, true);
                    wrong = UtilityFunctions.gettingCorrectValues(progressDataBase, grades_data.chapter, false);
                }
                holder.scoreText.setText("Score: " + correct + "/" + (correct + wrong));

            }
        } catch (Exception e) {
        }

        if (grades_data.subject.equals("Multiplication Tables")) {
            holder.subSub.setText(grades_data.subject);
            holder.chapters.setText("Table of " + UtilityFunctions.numberToWords(Integer.parseInt(grades_data.chapter)) + "( " + grades_data.chapter + "X )");

            try {

                timeSpend = UtilityFunctions.checkProgressAvailable(progressDataBase, "mul", "Table of " + UtilityFunctions.numberToWords(Integer.parseInt(grades_data.chapter)) + "( " + grades_data.chapter + "X )",
                        new Date(), 0, true).get(0).time_spend;
                if (timeSpend >= 8) {
                    holder.status.setText("Complete");
                    holder.status.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.green));

                }

                holder.timeText.setText(timeSpend + "/15 m");

            } catch (Exception e) {

            }

        }

        if (chapter.equals("Mathematics")) {


            subSub = grades_data.chapter.split(" ")[2];
            chapter = grades_data.chapter;

            holder.subSub.setText(subSub);
            holder.chapters.setText(chapter);
            try {
                timeSpend = UtilityFunctions.checkProgressAvailable(progressDataBase, grades_data.chapter.split(" ")[2], grades_data.chapter,
                        new Date(), 0, true).get(0).time_spend;

                if (timeSpend >= 8) {
                    holder.status.setText("Complete");
                    holder.status.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.green));

                }

                holder.timeText.setText(timeSpend + "/15 m");

            } catch (Exception e) {

            }

        }

        if (chapter.equals("English")) {

            subSub = grades_data.chapter.split(" ")[0];
            chapter = grades_data.chapter.replace(grades_data.chapter.split(" ")[0], "");

            holder.subSub.setText(subSub.replace("_", ""));
            holder.chapters.setText(chapter.replace("_", " "));

            try {
                timeSpend = UtilityFunctions.checkProgressAvailable(progressDataBase, grades_data.chapter.split(" ")[0], grades_data.chapter.replace(grades_data.chapter.split(" ")[0], ""), new Date(), 0, true).get(0).time_spend;
                holder.timeText.setText(timeSpend + "/15 m");

                Toast.makeText(context, timeSpend + "", Toast.LENGTH_SHORT).show();
                if (timeSpend >= 8) {
                    holder.status.setText("Complete");
                    holder.status.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.green));
                    holder.status.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.green));

                }
            } catch (Exception e) {

            }


        }


        holder.mView.setOnClickListener(v -> {

            if (res[0].toLowerCase(Locale.ROOT).equals("vocabulary")) {
                Intent intent = new Intent(context, EnglishActivity.class);
                Log.d("EnglishActivity", "onClick: " + res[1].toLowerCase(Locale.ROOT) + " Intent : " + UtilityFunctions.getVocabularyCategoryFromAdapter(res[1].toLowerCase(Locale.ROOT)).name());
                intent.putExtra(Constants.EXTRA_VOCABULARY_DETAIL_CATEGORY, UtilityFunctions.getVocabularyCategoryFromAdapter(val.replace("Vocabulary", "").trim().toLowerCase(Locale.ROOT)).name());
                context.startActivity(intent);
                // Toast.makeText(context, res[1], Toast.LENGTH_SHORT).show();
            } else if (res[0].toLowerCase(Locale.ROOT).equals("spelling_objects")) {
                var intent = new Intent(context, SpellingActivity.class);
                intent.putExtra(EXTRA_SPELLING_DETAIL, val);
                context.startActivity(intent);
            }
            else if (val.toLowerCase(Locale.ROOT).contains("spelling_commonwords")) {
                var intent = new Intent(context, EnglishSpellingActivity.class);
                intent.putExtra(EXTRA_SPELLING_DETAIL, val);
                context.startActivity(intent);
            }
            else if (res[0].toLowerCase(Locale.ROOT).equals("grammar")) {
                Intent intent = new Intent(context, GrammarActivity.class);
                intent.putExtra(Constants.EXTRA_GRAMMAR_CATEGORY, val);
                context.startActivity(intent);
            }

            else {
                if (grades_data.subject.equals("Multiplication Tables")) {

                    Intent intent = new Intent(context, LearningActivity.class);
                    intent.putExtra("selected_sub", "Table of " + UtilityFunctions.numberToWords(Integer.parseInt(grades_data.chapter)) + "( " + grades_data.chapter + "X )");
                    intent.putExtra("subject", "multiplication");
                    intent.putExtra("max_digit", grades_data.chapter);
                    intent.putExtra("video_url", "default");
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, LearningActivity.class);
                    intent.putExtra("selected_sub", val);
                    intent.putExtra("subject", res[res.length - 1].toLowerCase());
                    intent.putExtra("max_digit", res[0]);
                    intent.putExtra("video_url", grades_data.getUrl());
                    context.startActivity(intent);

                }


            }


        });


    }

    @Override
    public int getItemCount() {
        return list.size();
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
            subSub = mView.findViewById(R.id.subSubject);
            chapters = mView.findViewById(R.id.chapters);
            status = mView.findViewById(R.id.status);
            timeText = mView.findViewById(R.id.timeText);
            scoreText = mView.findViewById(R.id.score);


        }
    }
}
