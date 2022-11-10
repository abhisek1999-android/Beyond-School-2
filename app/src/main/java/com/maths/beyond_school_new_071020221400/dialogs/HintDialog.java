package com.maths.beyond_school_new_071020221400.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.maths.beyond_school_new_071020221400.R;

public class HintDialog extends AlertDialog {
    private DialogActionListener dialogActionListener;
    private TextView ansViewText,title;
    private ImageButton closeButton;
    private Button actionButton;

    private LottieAnimationView animationView;
    public HintDialog(Context context) {
        super(context);
        initDialog();
    }

    public void initDialog(){

            View view = LayoutInflater.from(getContext()).inflate(R.layout.hint_dialog,null);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ansViewText = view.findViewById(R.id.ansTextView);
            closeButton=  view.findViewById(R.id.closeButton);
            actionButton= view.findViewById(R.id.buttonAction);
            animationView=view.findViewById(R.id.animationView);

            title=view.findViewById(R.id.title);
            setClickListener(closeButton,actionButton);
            setView(view);

    }

    private void setClickListener(View... views){
        for (View view: views){
            view.setOnClickListener(mOnClickListener);
        }
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogActionListener.onAction(v);
        }
    };

    public void displayAnim(){
        animationView.setVisibility(View.VISIBLE);
    }

    public void actionButton(String buttonText){


        actionButton.setVisibility(View.VISIBLE);
        actionButton.setText(buttonText);
    }
   public void actionButtonBackgroundColor(int colorId){
       actionButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), colorId));

    }

    public void setAlertTitle(String titleText) {
        if(titleText != null){
            title.setVisibility(View.VISIBLE);
            title.setText(titleText);
        }
    }

    public void setAlertDesciption(String desciption) {
        ansViewText.setVisibility(View.VISIBLE);
        ansViewText.setText(desciption);
    }

    public void setOnActionListener(DialogActionListener dialogActionListener){
        this.dialogActionListener = dialogActionListener;
    }

    public interface DialogActionListener{
        void onAction(View viewId);
    }
}
