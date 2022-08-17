package com.maths.beyond_school_280720220930.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.maths.beyond_school_280720220930.R;

public class HintDialog extends AlertDialog {
    private DialogActionListener dialogActionListener;
    private TextView ansViewText,title;
    private ImageButton closeButton;

    public HintDialog(Context context) {
        super(context);
        initDialog();
    }

    public void initDialog(){

            View view = LayoutInflater.from(getContext()).inflate(R.layout.hint_dialog,null);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ansViewText = view.findViewById(R.id.ansTextView);
            closeButton=  view.findViewById(R.id.closeButton);
            title=view.findViewById(R.id.title);
            setClickListener(closeButton);
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