package com.maths.beyond_school_280720220930.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.process.ProgressM;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;
import com.maths.beyond_school_280720220930.model.ProgressTableWise;

import java.util.List;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder>{

    List<ProgressM> list ;
    List<ProgressTableWise>  listTableWise;
    String val="wt";
    Context context;

    public ProgressAdapter(Context context) {
        this.context = context;
    }

    public void setNotesList(List<ProgressM> list){

        this.list=list;
        notifyDataSetChanged();
    }

    public void setNotesList(List<ProgressTableWise> list,String val){

        this.listTableWise=list;
        this.val=val;
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

        if (val.equals("wt")){
            holder.tableNumber.setText(list.get(position).table);
            holder.desc1.setText(list.get(position).date);
            holder.desc2.setText("Used at: "+list.get(position).time);
            holder.desc3.setText(new UtilityFunctions().formatTime(list.get(position).time_to_complete));
            if ((list.get(position).correct+list.get(position).wrong)==0){
                holder.progressLayout.setVisibility(View.GONE);
            }
            holder.result.setText(list.get(position).correct+"/"+(list.get(position).correct+list.get(position).wrong));
            holder.resultProgress.setMax((int)(list.get(position).correct+list.get(position).wrong));
            holder.resultProgress.setProgress((int)list.get(position).correct);
        }
        else{

            holder.tableNumber.setText(listTableWise.get(position).getTable());
            holder.desc1.setText(val);
            holder.desc2.setText(listTableWise.get(position).getCount()+"  times used.");
            holder.desc3.setText(new UtilityFunctions().formatTime(listTableWise.get(position).getTotal_time()));

            if ((listTableWise.get(position).getTotal_correct()+ listTableWise.get(position).getTotal_wrong())== 0){
                holder.progressLayout.setVisibility(View.GONE);
            }

            holder.result.setText(listTableWise.get(position).getTotal_correct()+"/"+(listTableWise.get(position).getTotal_correct()+
                    listTableWise.get(position).getTotal_wrong()));
            holder.resultProgress.setMax((int)(listTableWise.get(position).getTotal_correct()+ listTableWise.get(position).getTotal_wrong()));
            holder.resultProgress.setProgress((int)listTableWise.get(position).getTotal_correct());


        }

    }

    @Override
    public int getItemCount() {

        try{
            if (val.equals("wt"))
                return list.size();
            else
                return listTableWise.size();
        }catch (Exception e){


        }
        return 0;

    }

    protected class ProgressViewHolder extends RecyclerView.ViewHolder {

        TextView tableNumber,desc1,desc2,desc3,result;
        ProgressBar resultProgress;
        RelativeLayout progressLayout;
        View mView;
        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);

            mView=itemView;

            tableNumber=mView.findViewById(R.id.numberTextView);
           desc1=mView.findViewById(R.id.descriptionTextView1);
           desc2=mView.findViewById(R.id.descriptionTextView2);
           desc3=mView.findViewById(R.id.descriptionTextView3);
           result=mView.findViewById(R.id.result);
           progressLayout=mView.findViewById(R.id.progressCircle);
           resultProgress=mView.findViewById(R.id.progressResult);



        }
    }
}
