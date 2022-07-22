package com.maths.beyond_school_280720220930.extras;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.table_questions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecognizeVoice implements RecognitionListener {

    public  SpeechRecognizer speech;
    Context mContext;
    Intent recognizerIntent;
    public String result="1";
    GetResult getResult;
    String method="";
    String onlyNumber="^[0-9]*$";
    Map<String, String> stringToText=new HashMap();

    int resultFromActivity=0;
   // ProgressBar progressBar;
    public RecognizeVoice(Context context,GetResult getResult){

        this.mContext=context;
        this.getResult=getResult;
       // progressBar=((Activity)mContext).findViewById(R.id.progressBar1);

       // resultFromActivity=((Activity)mContext).result;
        speech = SpeechRecognizer.createSpeechRecognizer(mContext);
        speech.setRecognitionListener(this);

        stringToText.put("sex","6");
        stringToText.put("six","6");
        stringToText.put("dirty","30");
        stringToText.put("pics","6");
        stringToText.put("three","3");
        stringToText.put("free","3");
        stringToText.put("tree","3");
        stringToText.put("tu","2");
        stringToText.put("Tu","2");
        stringToText.put("to","2");
        stringToText.put("To","2");
        stringToText.put("hundred","100");
        stringToText.put("potty","40");
        stringToText.put("party","40");
        stringToText.put("tatti","30");
        stringToText.put("tati","30");
        stringToText.put("badi stop","buddy stop");
        stringToText.put("stop","buddy stop");

        recognizerIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        //        recognizerIntent.putExtra(RecognizerIntent.ACTION_RECOGNIZE_SPEECH, RecognizerIntent.EXTRA_PREFER_OFFLINE);
        //        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 10000);
        //        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 10000);
        //        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 5000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES,true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

       //https://issuetracker.google.com/issues/36928328
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
        ///progressBar.setIndeterminate(false);
        //progressBar.setMax(10);
    }

    @Override
    public void onRmsChanged(float v) {

        Log.i("onRmsChanged",v+"");
      //  progressBar.setProgress((int) v);
    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {
        Log.i("LOG_TAG", "EndOfSpeech");
        Log.i("LOG_TAG", "onEndOfSpeech");
       // progressBar.setIndeterminate(true);
        stopListening();

    }

    @Override
    public void onError(int i) {

        Log.i("SpeechError",getErrorText(i));
        try {
            getResult.errorAction(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResults(Bundle bundle) {
       Log.i("LOG_TAG", "onResults"+result);
        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        for (String result : matches)
            text += result + "\n";
        result=matches.get(0).trim();

        if (result.matches(onlyNumber)){
        getResult.gettingResult(result);
        }
        else{

            try{
                getResult.gettingResult(stringToText.get(result.toLowerCase()));
            }catch (Exception e){
                getResult.gettingResult(result.toLowerCase());
            }
        }
       stopListening();

    }

    @Override
    public void onPartialResults(Bundle bundle) {


        Log.i("LOG_TAG", "onPertialResults"+result);
        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//        float [] confidence = bundle.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);


        String text = "";
        for (String result : matches)
            text += result + "\n";

        if (matches.size()!=0){

            result=matches.get(0).trim();
            Log.i("ResultsP",matches+"");

            Log.i("ResultFormMain",new table_questions().result+"");

            if (!result.equals("")){
                //Log.i("ConfidenceScore",confidence[0]+"");
                stopListening();
//                if (result.matches(onlyNumber)){
//                    //stopListening();
//                    getResult.gettingResult(result);
//
//                }
//                else{
//
//                    try{
//                    //    stopListening();
//                        getResult.gettingResult(stringToText.get(result.toLowerCase()));
//
//                    }catch (Exception e){
//                    //    stopListening();
//                        getResult.gettingResult(result.toLowerCase());
//
//                    }
//                }
            }

            //  stopListening();


        }



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
        void errorAction(int i) throws InterruptedException;

    }
}
