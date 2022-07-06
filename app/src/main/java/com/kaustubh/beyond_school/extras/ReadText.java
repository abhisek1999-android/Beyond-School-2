package com.kaustubh.beyond_school.extras;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.Locale;

import io.reactivex.subjects.BehaviorSubject;

public class ReadText implements TextToSpeech.OnInitListener {


    Context mContext;
    public TextToSpeech textToSpeech;
    public static BehaviorSubject<String> currentMethodSpc = BehaviorSubject.create();
    String method="";
    GetResultSpeech getResultSpeech;


    public ReadText(Context context,GetResultSpeech getResultSpeech){
        this.mContext=context;
        textToSpeech= new TextToSpeech(mContext, this);
        this.getResultSpeech=getResultSpeech;


    }

    @Override
    public void onInit(int i) {

        if (i==TextToSpeech.SUCCESS){
            textToSpeech.setLanguage(new Locale("en","IN"));
            textToSpeech.setSpeechRate((float) 0.8);

            Log.i("onInit","OnInit");
            textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                    Log.i("TextToSpeech","On Start");
                    method="onStart";
                    currentMethodSpc.onNext(method);
                }

                @Override
                public void onDone(String utteranceId) {
                    Log.i("TextToSpeech","On Done");
//                    method="onDone";
//                    currentMethodSpc.onNext(method);
                    getResultSpeech.gettingResultSpeech();

                }

                @Override
                public void onError(String utteranceId) {
                    Log.i("TextToSpeech","On Error");
                    method="onStart";
                    currentMethodSpc.onNext(method);
                }
            });
        }

    }

    public void read(String text){
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
    }

    public interface GetResultSpeech{

        void gettingResultSpeech();

    }

}
