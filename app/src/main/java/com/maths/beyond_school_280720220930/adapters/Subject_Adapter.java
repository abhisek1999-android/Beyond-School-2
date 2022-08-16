package com.maths.beyond_school_280720220930.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.maths.beyond_school_280720220930.AdditionActivity;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.english_activity.EnglishActivity;
import com.maths.beyond_school_280720220930.model.Subject_Model;

import java.util.List;
import java.util.Locale;

public class Subject_Adapter extends RecyclerView.Adapter<Subject_Adapter.SubjectViewHolder> {

    List<Subject_Model> list;
    Context context;

    public Subject_Adapter(List<Subject_Model> list, Context context) {
        this.list = list;
        this.context = context;
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
        String val = context.getResources().getString(subject_model.getSubsub());
        holder.operation.setText(val);
        String[] res = val.split(" ");

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //needs to be intent
                if (res[0].toLowerCase(Locale.ROOT).equals("vocabulary")) {
                    Intent intent = new Intent(context, EnglishActivity.class);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, AdditionActivity.class);

                    if (!res[0].equals("Multiplication")){
                        intent.putExtra("subject", res[res.length - 1].toLowerCase());
                        intent.putExtra("max_digit", res[0]);
                    }
                    else{
                        intent.putExtra("subject", res[0].toLowerCase());
                        intent.putExtra("max_digit", res[3]);
                    }

                    context.startActivity(intent);
                }

//                Toast.makeText(context, res[0], Toast.LENGTH_SHORT).show();
//
//
//                Toast.makeText(context, res[res.length-1], Toast.LENGTH_SHORT).show();
//                //this is url
//                Toast.makeText(context, subject_model.getUrl(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView digit_val, digit, operation;
        CardView card;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);

            digit_val = itemView.findViewById(R.id.digit_val);
            digit = itemView.findViewById(R.id.digit);
            operation = itemView.findViewById(R.id.operation);
            card = itemView.findViewById(R.id.card);

        }
    }
}
