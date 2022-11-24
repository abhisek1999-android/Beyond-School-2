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
import com.maths.beyond_school_new_071020221400.R;
import com.maths.beyond_school_new_071020221400.database.grade_tables.Grades_data;
import com.maths.beyond_school_new_071020221400.model.Tables;
import com.maths.beyond_school_new_071020221400.utils.UtilityFunctions;

import java.util.List;


public class TablesRecyclerAdapter extends RecyclerView.Adapter<TablesRecyclerAdapter.ContactsViewHolder> {

    List<Grades_data> list;
    Context context;

    public TablesRecyclerAdapter(List<Grades_data> list, Context context) {
        this.list = list;
        this.context = context;


    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_view, parent, false);
        ContactsViewHolder contactsViewHolder = new ContactsViewHolder(view);
        return contactsViewHolder;


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        if (position > lastAnimatedPosition) {
//            lastAnimatedPosition = position;
//            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_from_buttom);
//            animation.setInterpolator(new AccelerateDecelerateInterpolator());
//            ((RecentRecyclerAdapter.ContactsViewHolder) holder).mView.setAnimation(animation);
//            animation.start();
//        }


//        if (list.get(position).isIs_locked())
//            holder.isLocked.setVisibility(View.INVISIBLE);

        //    Log.i("MUL_LIST",list.get(position).isIs_locked()+"");


        holder.subSubject.setText(list.get(position).super_subject);
        holder.chapters.setText(list.get(position).getChapter_name());

        holder.mView.setOnClickListener(v -> {

            Intent intent = new Intent(context, LearningActivity.class);
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

//
//                if (list.get(position).isIs_locked()){
//                }
//
//                else
//                    UtilityFunctions.displayCustomDialog(context,"Chapter Locked","Hey, Please complete previous level to unlock.");
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

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            subSubject = itemView.findViewById(R.id.subSubject);
            chapters=itemView.findViewById(R.id.chapters);
//            tableNumber=mView.findViewById(R.id.numberTextView);
//            tableDesc=mView.findViewById(R.id.descriptionTextView);
            isLocked = itemView.findViewById(R.id.isLocked);
        }
    }

}
