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

import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.maths.beyond_school_new_071020221400.LearningActivity;
import com.maths.beyond_school_new_071020221400.R;
import com.maths.beyond_school_new_071020221400.model.Tables;
import com.maths.beyond_school_new_071020221400.utils.UtilityFunctions;

import java.util.List;


public class TablesRecyclerAdapter extends RecyclerView.Adapter<TablesRecyclerAdapter.ContactsViewHolder>{

    List<Tables> list ;
    Context context;

  public TablesRecyclerAdapter(List<Tables> list, Context context) {
        this.list = list;
        this.context = context;


    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_layout,parent, false);
            ContactsViewHolder contactsViewHolder=new  ContactsViewHolder(view);
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


        if (list.get(position).isIs_locked())
            holder.isLocked.setVisibility(View.INVISIBLE);

        Log.i("MUL_LIST",list.get(position).isIs_locked()+"");

        holder.operation.setText(list.get(position).getDecs()+"( "+list.get(position).getDigit()+"X )");

            holder.mView.setOnClickListener(v->{
                if (list.get(position).isIs_locked()){
                Intent intent=new Intent(context, LearningActivity.class);
                intent.putExtra("selected_sub",list.get(position).getDecs()+"( "+list.get(position).getDigit()+"X )");
                intent.putExtra("max_digit",list.get(position).getDigit());
                intent.putExtra("subject","multiplication");
                intent.putExtra("video_url","default");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);}

                else
                    UtilityFunctions.displayCustomDialog(context,"Chapter Locked","Hey, Please complete previous level to unlock.");
            });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(
            RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public String timeConv(String time){

        int s=Integer.parseInt(time);
        String finalTime="";
        int sec = s % 60;
        int min = (s / 60)%60;
        int hours = (s/60)/60;

        String strHours=(hours<10)?"0"+Integer.toString(hours):Integer.toString(hours);
        if (!strHours.equals("00"))
            finalTime+=strHours+" hr";
        String strmin=(min<10)?"0"+Integer.toString(min):Integer.toString(min);
        if (!strmin.equals("00"))
            finalTime+=" "+strmin+" m";
        String strSec=(sec<10)?"0"+Integer.toString(sec):Integer.toString(sec);
        finalTime+=" "+strSec+" s";

        return finalTime;
    }



    protected class ContactsViewHolder extends RecyclerView.ViewHolder {

        TextView digit_val, digit, operation;
//        TextView tableNumber,tableDesc;
        View mView;
        ImageView isLocked;
        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);

            mView=itemView;
            operation = itemView.findViewById(R.id.operation);

//            tableNumber=mView.findViewById(R.id.numberTextView);
//            tableDesc=mView.findViewById(R.id.descriptionTextView);
            isLocked=itemView.findViewById(R.id.isLocked);
        }
    }

}
