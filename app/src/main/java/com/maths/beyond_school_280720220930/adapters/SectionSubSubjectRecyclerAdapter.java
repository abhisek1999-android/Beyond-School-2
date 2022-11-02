package com.maths.beyond_school_280720220930.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.model.SectionSubSubject;
import com.maths.beyond_school_280720220930.model.SubSubject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SectionSubSubjectRecyclerAdapter extends RecyclerView.Adapter<SectionSubSubjectRecyclerAdapter.ViewHolder> {

    List<SectionSubSubject> sectionList;
    Context mContext;


    public SectionSubSubjectRecyclerAdapter(List<SectionSubSubject> sectionList, Context mContext) {
        this.sectionList = sectionList;
        this.mContext=mContext;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.section_views, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SectionSubSubject section = sectionList.get(position);
        String  sectionName = section.getSectionName();
        List<SubSubject> items = section.getSectionItems();

        holder.sectionNameTextView.setText(sectionName);

        ProgressRecyclerAdapter childRecyclerAdapter = new ProgressRecyclerAdapter(items,mContext,sectionName);
        holder.childRecyclerView.setAdapter(childRecyclerAdapter);
        holder.mView.setOnClickListener(v->{

        });

    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        TextView sectionNameTextView;
        RecyclerView childRecyclerView;
        View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView=itemView;
            sectionNameTextView = itemView.findViewById(R.id.sectionHeaderTextView);
            childRecyclerView = itemView.findViewById(R.id.sectionRecyclerView);
            childRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        }
    }
}
