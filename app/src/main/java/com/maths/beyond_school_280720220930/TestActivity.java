package com.maths.beyond_school_280720220930;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.maths.beyond_school_280720220930.extras.ReadText;
import com.maths.beyond_school_280720220930.extras.RecognizeVoice;

public class TestActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    ReadText readText;
    RecognizeVoice recognizeVoice;
    TextView textView;

    private YouTubePlayer.PlaybackEventListener playbackEventListener;
    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // Get reference to the view of Video player
        String api_key=getResources().getString(R.string.youtube_api);
        YouTubePlayerView ytPlayer = (YouTubePlayerView)findViewById(R.id.viewYouTubePlayer);

        ytPlayer.initialize(api_key,this);
        playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onLoaded(String s) {

            }

            @Override
            public void onAdStarted() {

            }

            @Override
            public void onVideoStarted() {

            }

            @Override
            public void onVideoEnded() {

            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {

            }
        };

        playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onPlaying() {

            }

            @Override
            public void onPaused() {

            }

            @Override
            public void onStopped() {

            }

            @Override
            public void onBuffering(boolean b) {

            }

            @Override
            public void onSeekTo(int i) {

            }
        };


    }

    public void buttonClick(View view) {
          finish();

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        if(!b){
            youTubePlayer.loadVideo("fzI9FNjXQ0o");//Execute a playlist
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}