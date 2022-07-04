package com.kaustubh.beyond_school.extras;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import java.util.ArrayList;

public class RecognizeVoice implements RecognitionListener {

    SpeechRecognizer speech;
    Context mContext;
    Intent recognizerIntent;

    public RecognizeVoice(Context context){

        this.mContext=context;
        speech = SpeechRecognizer.createSpeechRecognizer(mContext);
        speech.setRecognitionListener(this);
        recognizerIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
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

    }

    @Override
    public void onRmsChanged(float v) {

        Log.i("onRmsChanged",v+"");

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int i) {

        Log.i("SpeechError",getErrorText(i));

    }

    @Override
    public void onResults(Bundle bundle) {


        Log.i("LOG_TAG", "onResults");
        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        for (String result : matches)
            text += result + "\n";
        Log.i("Recognize_Text",text);

    }

    @Override
    public void onPartialResults(Bundle bundle) {

        Log.i("LOG_TAG","onPartialResults");



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
}
