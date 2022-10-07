package com.maths.beyond_school_new_071020221400.adapters;

import static com.maths.beyond_school_new_071020221400.utils.Constants.EXTRA_SPELLING_DETAIL;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.maths.beyond_school_new_071020221400.LearningActivity;
import com.maths.beyond_school_new_071020221400.MainActivity;
import com.maths.beyond_school_new_071020221400.R;
import com.maths.beyond_school_new_071020221400.english_activity.grammar.GrammarActivity;
import com.maths.beyond_school_new_071020221400.english_activity.spelling.EnglishSpellingActivity;
import com.maths.beyond_school_new_071020221400.english_activity.spelling_objects.SpellingActivity;
import com.maths.beyond_school_new_071020221400.english_activity.vocabulary.EnglishActivity;
import com.maths.beyond_school_new_071020221400.model.Subject_Model;
import com.maths.beyond_school_new_071020221400.utils.Constants;
import com.maths.beyond_school_new_071020221400.utils.UtilityFunctions;

import java.util.List;
import java.util.Locale;

public class Subject_Adapter extends RecyclerView.Adapter<Subject_Adapter.SubjectViewHolder> {

    List<Subject_Model> list;
    Context context;
    MultiplicationOption multiplicationOption;

    public Subject_Adapter(List<Subject_Model> list, Context context, MultiplicationOption multiplicationOption) {
        this.list = list;
        this.context = context;
        this.multiplicationOption = multiplicationOption;

    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.sub_layout, parent, false);
        return new Subject_Adapter.SubjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject_Model subject_model = list.get(position);
        String val = subject_model.getSubsub();


        if (subject_model.isIs_locked())
            holder.isLocked.setVisibility(View.INVISIBLE);

        var finalString = "";
        if (val.contains("Vocabulary")) {
            finalString = val.replace("Vocabulary", "").replace("_", " ");
        } else if (val.contains("Spelling_Objects")) {
            finalString = val.replace("Spelling_Objects", " ");
        } else if (val.contains("Spelling_CommonWords")) {
            finalString = val.replace("Spelling_CommonWords", " ");
        } else if (val.contains("Grammar")) {
            finalString = val.replace("Grammar", "");
        } else {
            finalString = val;
        }
        holder.operation.setText(finalString);
        String[] res = val.split(" ");

        holder.card.setOnClickListener(view -> {
            if (subject_model.isIs_locked()) {
//                    needs to be intent values
                if (res[0].toLowerCase(Locale.ROOT).equals("vocabulary")) {
                    Intent intent = new Intent(context, EnglishActivity.class);
                    Log.d("EnglishActivityXXX", "onClick: " + val.replace("Vocabulary", "").trim().toLowerCase() + " Intent : " + UtilityFunctions.getVocabularyCategoryFromAdapter(res[1].toLowerCase(Locale.ROOT)).name());
                    intent.putExtra(Constants.EXTRA_VOCABULARY_DETAIL_CATEGORY, UtilityFunctions.getVocabularyCategoryFromAdapter(val.replace("Vocabulary", "").trim().toLowerCase(Locale.ROOT)).name());
                    context.startActivity(intent);
                    // Toast.makeText(context, res[1], Toast.LENGTH_SHORT).show();
                } else if (val.toLowerCase(Locale.ROOT).contains("spelling_objects")) {
                    var intent = new Intent(context, SpellingActivity.class);
                    intent.putExtra(EXTRA_SPELLING_DETAIL, val);
                    context.startActivity(intent);
                } else if (val.toLowerCase(Locale.ROOT).contains("spelling_commonwords")) {
                    var intent = new Intent(context, EnglishSpellingActivity.class);
                    intent.putExtra(EXTRA_SPELLING_DETAIL, val);
                    context.startActivity(intent);
                } else if (res[0].toLowerCase(Locale.ROOT).equals("grammar")) {
                    Intent intent = new Intent(context, GrammarActivity.class);
                    intent.putExtra(Constants.EXTRA_GRAMMAR_CATEGORY, val);
                    context.startActivity(intent);
                } else {

                    if (!res[0].equals("Multiplication")) {
                        multiplicationOption.multiplicationSelected();
                        Intent intent = new Intent(context, LearningActivity.class);
                        intent.putExtra("selected_sub", val);
                        intent.putExtra("subject", res[res.length - 1].toLowerCase());
                        intent.putExtra("max_digit", res[0]);
                        intent.putExtra("video_url", subject_model.getUrl());
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("selected_sub", val);
                        intent.putExtra("subject", res[0].toLowerCase());
                        intent.putExtra("max_digit", res[3]);
                        intent.putExtra("video_url", subject_model.getUrl());
                        context.startActivity(intent);

                    }


                }
            } else {
                UtilityFunctions.displayCustomDialog(context, "Chapter Locked", "Hey, Please complete previous level to unlock.");
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
        TextView digit_val, digit, operation;
        CardView card;
        ImageView isLocked;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);

            digit_val = itemView.findViewById(R.id.digit_val);
            digit = itemView.findViewById(R.id.digit);
            operation = itemView.findViewById(R.id.operation);
            card = itemView.findViewById(R.id.card);
            isLocked = itemView.findViewById(R.id.isLocked);

        }
    }
}
