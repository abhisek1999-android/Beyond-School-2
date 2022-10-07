package com.maths.beyond_school_new_071020221400.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.maths.beyond_school_new_071020221400.R;
import com.maths.beyond_school_new_071020221400.model.table_values;

import java.util.List;

public class NavTableAdapter extends RecyclerView.Adapter<NavTableAdapter.NavViewHolder> {
    List<table_values> list;
    Context context;

    public NavTableAdapter(List<table_values> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public NavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.table_lay, parent, false);
        return new NavViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NavViewHolder holder, int position) {
        table_values tableValues=list.get(position);
        holder.num.setText(tableValues.getName());
        holder.numbertxt.setText(String.valueOf(tableValues.getVal())+"X");
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
 
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NavViewHolder extends RecyclerView.ViewHolder {
        TextView num,numbertxt;
        CardView card;

        public NavViewHolder(@NonNull View itemView) {
            super(itemView);
            num=itemView.findViewById(R.id.num);
            numbertxt=itemView.findViewById(R.id.numberTextView);
            card=itemView.findViewById(R.id.card);
        }
    }
}
