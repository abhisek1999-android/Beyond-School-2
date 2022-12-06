package com.maths.beyond_school_new_071020221400.adapters;

import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.maths.beyond_school_new_071020221400.LearningActivity;
import com.maths.beyond_school_new_071020221400.LearningActivityNew;
import com.maths.beyond_school_new_071020221400.R;
import com.maths.beyond_school_new_071020221400.database.grade_tables.Grades_data;
import com.maths.beyond_school_new_071020221400.database.process.ProgressDataBase;
import com.maths.beyond_school_new_071020221400.databinding.SubjectViewBinding;
import com.maths.beyond_school_new_071020221400.model.Tables;
import com.maths.beyond_school_new_071020221400.utils.UtilityFunctions;

import java.util.List;


public class TablesRecyclerAdapter extends RecyclerView.Adapter<TablesRecyclerAdapter.ContactsViewHolder> {

    List<Grades_data> list;
    Context context;
    private ItemClickListener itemClickListener;


    public TablesRecyclerAdapter(List<Grades_data> list, Context context,ItemClickListener itemClickListener) {
        this.list = list;
        this.context = context;
        this.itemClickListener=itemClickListener;


    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_view, parent, false);
        ContactsViewHolder contactsViewHolder = new ContactsViewHolder(view);
        return contactsViewHolder;


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.subjectViewBinding.subSubject.setText(list.get(position).super_subject);
        holder.subjectViewBinding.chapters.setText(list.get(position).getChapter_name());


        itemClickListener.intentAction(holder.subjectViewBinding.timeText,list.get(position).getId(),list.get(position).chapter_name);

//        ProgressDataBase.getDbInstance(context).progressDao().getTimeSpend(list.get(position).getId(),
//                list.get(position).chapter_name).observe(context,c->{
//
//        });

        holder.subjectViewBinding.timeText.setText(
                ProgressDataBase.getDbInstance(context).progressDao().getTimeSpend(list.get(position).getId(),
                list.get(position).chapter_name).getValue()+""
        );



        holder.mView.setOnClickListener(v -> {



            Intent intent = new Intent(context, LearningActivityNew.class);
            intent.putExtra("id",list.get(position).getId());
            intent.putExtra("selected_sub", list.get(position).getChapter_name());
            intent.putExtra("subject", list.get(position).subject);
            var maxDigit = UtilityFunctions.getTableNumberFromString(list.get(position).chapter_name);
            if (maxDigit.equals("0")) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                return;
            }
            intent.putExtra("max_digit", maxDigit);
            intent.putExtra("video_url", "");
            context.startActivity(intent);


        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }




    @Override
    public void onAttachedToRecyclerView(
            RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    protected class ContactsViewHolder extends RecyclerView.ViewHolder {

        TextView subSubject,chapters;
        View mView;
        ImageView isLocked;
        SubjectViewBinding subjectViewBinding;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            subjectViewBinding=SubjectViewBinding.bind(mView);
            subSubject = itemView.findViewById(R.id.subSubject);
            chapters=itemView.findViewById(R.id.chapters);
            isLocked = itemView.findViewById(R.id.isLocked);
        }
    }

    public interface ItemClickListener{
        void intentAction(TextView textView,String id,String chapterName);
    }

}
