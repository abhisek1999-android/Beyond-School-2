package com.maths.beyond_school_new_071020221400.translation_engine.translator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Button;

import com.maths.beyond_school_new_071020221400.R;
import com.maths.beyond_school_new_071020221400.translation_engine.ConversionCallback;
import com.maths.beyond_school_new_071020221400.translation_engine.ConverterEngine;
import com.maths.beyond_school_new_071020221400.utils.UtilityFunctions;

import org.json.JSONException;
import org.json.JSONObject;
import org.vosk.Model;
import org.vosk.Recognizer;
import org.vosk.android.RecognitionListener;
import org.vosk.android.SpeechService;
import org.vosk.android.SpeechStreamService;
import org.vosk.android.StorageService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpeechToTextConverterVosk implements ConverterEngine<SpeechToTextConverterVosk> {

    private static final String TAG = SpeechToTextConverterVosk.class.getSimpleName();

    private ConversionCallback conversionCallaBack;
    private SpeechRecognizer speechRecognizer;
    String onlyNumber = "^[0-9]*$";
    Map<String, String> stringToText = new HashMap();
    public String result = "1";

    private Model model;
    public SpeechService speechService;
    public SpeechStreamService speechStreamService;

    public SpeechToTextConverterVosk(ConversionCallback callback) {
        this.conversionCallaBack = callback;

    }


    @Override
    public SpeechToTextConverterVosk initialize(String message, Activity appContext) {
        stringToText.put("sex", "6");
        stringToText.put("six", "6");
        stringToText.put("dirty", "30");
        stringToText.put("pics", "6");
        stringToText.put("three", "3");
        stringToText.put("free", "3");
        stringToText.put("tree", "3");
        stringToText.put("tu", "2");
        stringToText.put("Tu", "2");
        stringToText.put("to", "2");
        stringToText.put("too", "2");
        stringToText.put("do", "2");
        stringToText.put("To", "2");
        stringToText.put("To0", "2");
        stringToText.put("for", "4");
        stringToText.put("for", "4");
        stringToText.put("food", "4");
        stringToText.put("ford", "4");
        stringToText.put("hundred", "100");
        stringToText.put("potty", "40");
        stringToText.put("party", "40");
        stringToText.put("tatti", "30");
        stringToText.put("tati", "30");
        stringToText.put("tati", "30");
        stringToText.put("aur", "4");
        stringToText.put("then", "10");
        stringToText.put("been", "10");
        stringToText.put("food been", "14");

        stringToText.put("stop", "buddy stop");

        StorageService.unpack(appContext, "model-en-us", "model",
                (model) -> {
                    // init service here ...................................
                    this.model = model;
                    recVoice();

                },
                (exception) -> setErrorState("Failed to unpack the model" + exception.getMessage()));
//        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(appContext);
//        speechRecognizer.setRecognitionListener(listener);
//        speechRecognizer.startListening(intent);


        return this;
    }

    private void recVoice(){
        var listener = new CustomRecognitionListener();
        try {
            Recognizer rec = new Recognizer(model, 16000.0f);
            speechService = new SpeechService(rec, 16000.0f);
            speechService.startListening(listener);
            conversionCallaBack.successInit();
        } catch (IOException e) {
            setErrorState(e.getMessage());
        }
    }

    class CustomRecognitionListener implements RecognitionListener {

        @Override
        public void onPartialResult(String hypothesis) {



            conversionCallaBack.onPartialResult("Partial: " + hypothesis.trim() + "\n");
          //  Log.d(TAG, "onPartialResult: "+hypothesis);

//            try {
//                conversionCallaBack.onSuccess(hypothesis.trim());
//             //   pause(false);
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.d(TAG, "onPartialResult: "+e.getMessage());
//            }

        }

        @Override
        public void onResult(String hypothesis) {
            try {
                JSONObject json = new JSONObject(hypothesis);
                try {
                    conversionCallaBack.onSuccess(UtilityFunctions.wordToNumberModified(json.getString("text"))+"");
                    // pause(false);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "onResult: "+e.getMessage());
                    try {
                        conversionCallaBack.onSuccess(stringToText.get(json.getString("text")));
                    }catch (Exception eq){


                        Log.d(TAG, "onResult: eq"+eq.getMessage());
                    }
                }
                Log.d(TAG, "onResult: "+UtilityFunctions.wordToNumberModified(json.getString("text")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFinalResult(String hypothesis) {

         //   conversionCallaBack.onSuccess(stringToText.get(matches.get(0).trim().toLowerCase()));

            try {
                JSONObject json = new JSONObject(hypothesis);
                try {
                    conversionCallaBack.onSuccess(UtilityFunctions.wordToNumberModified(json.getString("text"))+"");
                    // pause(false);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "onResult: "+e.getMessage());
                    try {
                        conversionCallaBack.onSuccess(stringToText.get(json.getString("text")));
                    }catch (Exception eq){


                        Log.d(TAG, "onResult: eq"+eq.getMessage());
                    }
                }
                Log.d(TAG, "onResult: "+UtilityFunctions.wordToNumberModified(json.getString("text")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (speechStreamService != null) {
                speechStreamService = null;
            }
        }

        @Override
        public void onError(Exception exception) {
            Log.d(TAG, "onError" + exception.getMessage());
            assert conversionCallaBack != null;
            conversionCallaBack.onErrorOccurred(exception.getMessage());
            conversionCallaBack.getLogResult("onError : " + exception.getMessage());
        }

        @Override
        public void onTimeout() {

        }
    }

    @Override
    public String getErrorText(int errorCode) {
        var message = "";
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


    private String setErrorState(String message) {
        return message;
    }


    public void pause(boolean checked) {
        Log.d(TAG, "pause: ");
        if (speechService != null) {
            speechService.setPause(checked);
        }
    }

    // destroy Stp
    public void destroy() {
        if (speechService != null) {
            speechService.stop();
            speechService.shutdown();
            speechService = null;
            Log.d(TAG, "destroy: ");
        }

        if (speechStreamService != null) {
            speechStreamService.stop();
        }
    }
}
