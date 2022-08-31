package com.maths.beyond_school_280720220930.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.maths.beyond_school_280720220930.LearningActivity;
import com.maths.beyond_school_280720220930.MainActivity;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.Select_Sub_Activity;
import com.maths.beyond_school_280720220930.database.grade_tables.Grades_data;
import com.maths.beyond_school_280720220930.database.process.ProgressDataBase;
import com.maths.beyond_school_280720220930.english_activity.vocabulary.EnglishActivity;
import com.maths.beyond_school_280720220930.model.SubSubject;
import com.maths.beyond_school_280720220930.utils.Constants;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProgressRecyclerAdapter extends RecyclerView.Adapter<ProgressRecyclerAdapter.SubjectViewHolder> {

    List<SubSubject> list;
    Context context;
    ProgressDataBase progressDataBase;
    long timeSpend = 0;
    String subSub="",chapter="";


    public ProgressRecyclerAdapter(List<SubSubject> list, Context context) {
        this.list = list;
        this.context = context;

        progressDataBase = ProgressDataBase.getDbInstance(context);

    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.progress_layout, parent, false);
        return new ProgressRecyclerAdapter.SubjectViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {


        holder.subSub.setText(list.get(position).getSubSubject());
        holder.status.setText("You have mastered :"+ list.get(position).getCompleted()+"/"+list.get(position).getTotal());
        holder.learningProgress.setMax(list.get(position).getTotal());
        holder.learningProgress.setProgress(list.get(position).getCompleted());
        holder.image.setImageResource(list.get(position).getResource());

        holder.mView.setOnClickListener(v->{
            Intent intent=new Intent(context, Select_Sub_Activity.class);
            intent.putExtra("subSubject",list.get(position).getSubSubject());
            context.startActivity(intent );
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
        TextView subSub, status;

        ImageView image;
        ProgressBar learningProgress;
        View mView;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            subSub = mView.findViewById(R.id.subSubject);
            status = mView.findViewById(R.id.status);
            learningProgress=mView.findViewById(R.id.learningProgress);

            image=mView.findViewById(R.id.image);

        }
    }
}
