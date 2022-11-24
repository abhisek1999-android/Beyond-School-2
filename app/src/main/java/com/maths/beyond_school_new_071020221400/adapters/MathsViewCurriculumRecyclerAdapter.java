package com.maths.beyond_school_new_071020221400.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import com.maths.beyond_school_new_071020221400.utils.UtilityFunctions;

import java.util.List;


public class MathsViewCurriculumRecyclerAdapter extends RecyclerView.Adapter<MathsViewCurriculumRecyclerAdapter.ContactsViewHolder> {

    List<Grades_data> list;
    Context context;

    public MathsViewCurriculumRecyclerAdapter(List<Grades_data> list, Context context) {
        this.list = list;
        this.context = context;


    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_layout, parent, false);
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


        if (list.get(position).unlock)
            holder.isLocked.setVisibility(View.INVISIBLE);


        holder.chapterName.setText(list.get(position).getChapter_name());

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

        TextView chapterName;
        View mView;
        ImageView isLocked;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            chapterName = itemView.findViewById(R.id.chapterName);
            isLocked = itemView.findViewById(R.id.isLocked);
        }
    }

}
