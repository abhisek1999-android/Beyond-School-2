package com.kaustubh.beyond_school.extras;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.ProgressBar;

import com.kaustubh.beyond_school.R;

import java.util.ArrayList;

import io.reactivex.subjects.BehaviorSubject;

public class RecognizeVoice implements RecognitionListener {

    public  SpeechRecognizer speech;
    Context mContext;
    Intent recognizerIntent;
    public String result="1";
    GetResult getResult;
    String method="";

    ProgressBar progressBar;
    public RecognizeVoice(Context context,GetResult getResult){

        this.mContext=context;
        this.getResult=getResult;
        progressBar=((Activity)mContext).findViewById(R.id.progressBar1);
        speech = SpeechRecognizer.createSpeechRecognizer(mContext);
        speech.setRecognitionListener(this);
        recognizerIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, mContext.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 3000);
    }


    public void startListening(){
        speech.startListening(recognizerIntent);
    }

    public void stopListening(){
        speech.stopListening();
    }


    @Override
    public void onReadyForSpeech(Bundle bundle) {


    }

    @Override
    public void onBeginningOfSpeech() {
        progressBar.setIndeterminate(false);
        progressBar.setMax(10);
    }

    @Override
    public void onRmsChanged(float v) {

        Log.i("onRmsChanged",v+"");
        progressBar.setProgress((int) v);
    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {
        Log.i("LOG_TAG", "EndOfSpeech");
        Log.i("LOG_TAG", "onEndOfSpeech");
        progressBar.setIndeterminate(true);
        stopListening();

    }

    @Override
    public void onError(int i) {

        Log.i("SpeechError",getErrorText(i));
        getResult.errorAction();

    }

    @Override
    public void onResults(Bundle bundle) {
        Log.i("LOG_TAG", "onResults"+result);

        getResult.gettingResult(result);
        stopListening();

    }

    @Override
    public void onPartialResults(Bundle bundle) {



        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        for (String result : matches)
            text += result + "\n";
        result=text;
        Log.i("LOG_TAG","onPartialResults"+text);
        Log.i("Recognize_Text",text);
    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

    public interface GetResult{

        void gettingResult(String title);
        void errorAction();

    }
}
