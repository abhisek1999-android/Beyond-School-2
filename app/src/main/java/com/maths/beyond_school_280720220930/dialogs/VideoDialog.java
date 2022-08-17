package com.maths.beyond_school_280720220930.dialogs;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.maths.beyond_school_280720220930.R;

public class VideoDialog extends AlertDialog {
    private DialogActionListener dialogActionListener;
    private TextView ansViewText,title;
    private ImageButton closeButton;
    private YouTubePlayerView videoView;
    private String api_key="";

    public VideoDialog(Context context) {
        super(context);
        api_key=getContext().getResources().getString(R.string.youtube_api);
        initDialog();
    }

    public void initDialog(){

            View view = LayoutInflater.from(getContext()).inflate(R.layout.video_dialog,null);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            closeButton=  view.findViewById(R.id.closeButton);
            videoView= view.findViewById(R.id.videoView);
            title=view.findViewById(R.id.title);
            setClickListener(closeButton);


//            videoView.setBackgroundColor(Color.TRANSPARENT);
//            videoView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_INSET);



            
            
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


    public void setVideoLink(String link){

        if (link!=null){

            videoView.initialize(
                    api_key,
                    new YouTubePlayer.OnInitializedListener() {
                        // Implement two methods by clicking on red
                        // error bulb inside onInitializationSuccess
                        // method add the video link or the playlist
                        // link that you want to play In here we
                        // also handle the play and pause
                        // functionality
                        @Override
                        public void onInitializationSuccess(
                                YouTubePlayer.Provider provider,
                                YouTubePlayer youTubePlayer, boolean b)
                        {
                            youTubePlayer.loadVideo(link);
                            youTubePlayer.play();
                        }

                        // Inside onInitializationFailure
                        // implement the failure functionality
                        // Here we will show toast
                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                            YouTubeInitializationResult
                                                                    youTubeInitializationResult)
                        {
                            Toast.makeText(getContext(), "Video player Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
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
