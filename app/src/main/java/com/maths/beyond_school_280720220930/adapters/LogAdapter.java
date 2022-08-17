package com.maths.beyond_school_280720220930.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.log.LogEntity;

import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ProgressViewHolder> {

    List<LogEntity> list;
    Context context;

    public LogAdapter(Context context) {
        this.context = context;
    }

    public void setNotesList(List<LogEntity> list) {

        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_card_layout, parent, false);
        LogAdapter.ProgressViewHolder progressViewHolder = new LogAdapter.ProgressViewHolder(view);
        return progressViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ProgressViewHolder holder, int position) {

        holder.desc1.setText("Time Stamp" + list.get(position).getTimestamp() + "");
        holder.tableNumber.setVisibility(View.GONE);

        holder.desc2.setText(list.get(position).getLog_content());
        holder.desc3.setVisibility(View.GONE);
        holder.result.setVisibility(View.GONE);

        holder.resultProgress.setVisibility(View.GONE);



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class ProgressViewHolder extends RecyclerView.ViewHolder {

        TextView tableNumber, desc1, desc2, desc3, result, tableText, resultText;
        ProgressBar resultProgress;
        View mView;

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

            tableNumber = mView.findViewById(R.id.numberTextView);
            desc1 = mView.findViewById(R.id.descriptionTextView1);
            desc2 = mView.findViewById(R.id.descriptionTextView2);
            desc3 = mView.findViewById(R.id.descriptionTextView3);
            result = mView.findViewById(R.id.result);
            resultProgress = mView.findViewById(R.id.progressResult);
            tableText = mView.findViewById(R.id.tableText);
            resultText = mView.findViewById(R.id.resultText);

            tableText.setVisibility(View.GONE);
            resultText.setVisibility(View.GONE);

        }
    }
}
