package com.kaustubh.beyond_school.extras;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.Locale;

public class ReadText implements TextToSpeech.OnInitListener {


    Context mContext;
    TextToSpeech textToSpeech;

    public ReadText(Context context){
        this.mContext=context;
        textToSpeech= new TextToSpeech(mContext, this);
    }

    @Override
    public void onInit(int i) {

        if (i==TextToSpeech.SUCCESS){
            textToSpeech.setLanguage(new Locale("en","IN"));
            textToSpeech.setSpeechRate((float) 0.8);

            textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                    Log.i("TextToSpeech","On Start");
                }

                @Override
                public void onDone(String utteranceId) {
                    Log.i("TextToSpeech","On Done");

                }

                @Override
                public void onError(String utteranceId) {
                    Log.i("TextToSpeech","On Error");
                }
            });
        }

    }

    public void read(String text){
        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
    }

}
