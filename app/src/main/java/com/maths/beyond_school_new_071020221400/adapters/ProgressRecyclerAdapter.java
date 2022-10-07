package com.maths.beyond_school_new_071020221400.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.maths.beyond_school_new_071020221400.R;
import com.maths.beyond_school_new_071020221400.Select_Sub_Activity;
import com.maths.beyond_school_new_071020221400.database.process.ProgressDataBase;
import com.maths.beyond_school_new_071020221400.model.SubSubject;

import java.util.List;

public class ProgressRecyclerAdapter extends RecyclerView.Adapter<ProgressRecyclerAdapter.SubjectViewHolder> {

    List<SubSubject> list;
    Context context;
    ProgressDataBase progressDataBase;
    long timeSpend = 0;
    private String subSub = "", chapter = "";
    private AlertDialog alertDialog;
    private String section;


    public ProgressRecyclerAdapter(List<SubSubject> list, Context context, AlertDialog alertDialog, String sectionName) {
        this.list = list;
        this.context = context;
        this.alertDialog = alertDialog;
        this.section = sectionName;

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


        holder.subSub.setText(list.get(position).getSubSubject().replace("_", " "));
        holder.status.setText("You have mastered :" + list.get(position).getCompleted() + "/" + list.get(position).getTotal());
        holder.learningProgress.setMax(list.get(position).getTotal());
        holder.learningProgress.setProgress(list.get(position).getCompleted());
        holder.image.setImageResource(list.get(position).getResource());


        holder.mView.setOnClickListener(v -> {


//            Toast.makeText(context, list.get(position).getSubSubject(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, Select_Sub_Activity.class);
            intent.putExtra("subSubject", list.get(position).getSubSubject());
            intent.putExtra("subject", section);
            context.startActivity(intent);

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
            learningProgress = mView.findViewById(R.id.learningProgress);

            image = mView.findViewById(R.id.image);

        }
    }
}
