package com.maths.beyond_school_280720220930.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.ProgressM;
import com.maths.beyond_school_280720220930.model.Progress;
import com.maths.beyond_school_280720220930.model.Tables;

import java.util.List;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder>{

    List<ProgressM> list ;
    Context context;

    public ProgressAdapter(Context context) {
        this.context = context;
    }

    public void setNotesList(List<ProgressM> list){

        this.list=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_card_layout,parent, false);
        ProgressAdapter.ProgressViewHolder progressViewHolder=new ProgressAdapter.ProgressViewHolder(view);
        return progressViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ProgressViewHolder holder, int position) {
        holder.tableNumber.setText(list.get(position).table);
        holder.desc1.setText(list.get(position).date);
        holder.desc2.setText("Used at: "+list.get(position).time);
        holder.desc3.setText("Time taken : "+list.get(position).time_to_complete);
        holder.result.setText(list.get(position).correct+"/10");

        holder.resultProgress.setMax(10);
        holder.resultProgress.setProgress(Integer.parseInt(list.get(position).correct));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class ProgressViewHolder extends RecyclerView.ViewHolder {

        TextView tableNumber,desc1,desc2,desc3,result;
        ProgressBar resultProgress;
        View mView;
        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);

            mView=itemView;

            tableNumber=mView.findViewById(R.id.numberTextView);
           desc1=mView.findViewById(R.id.descriptionTextView1);
           desc2=mView.findViewById(R.id.descriptionTextView2);
           desc3=mView.findViewById(R.id.descriptionTextView3);
           result=mView.findViewById(R.id.result);
           resultProgress=mView.findViewById(R.id.progressResult);


        }
    }
}
