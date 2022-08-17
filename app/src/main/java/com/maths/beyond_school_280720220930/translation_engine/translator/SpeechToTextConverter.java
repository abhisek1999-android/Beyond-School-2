package com.maths.beyond_school_280720220930.translation_engine.translator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.ConverterEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpeechToTextConverter implements ConverterEngine<SpeechToTextConverter> {

    private static final String TAG = SpeechToTextConverter.class.getSimpleName();

    private ConversionCallback conversionCallaBack;
    private SpeechRecognizer speechRecognizer;
    String onlyNumber = "^[0-9]*$";
    Map<String, String> stringToText = new HashMap();
    public String result = "1";

    public SpeechToTextConverter(ConversionCallback callback) {
        this.conversionCallaBack = callback;
    }


    @Override
    public SpeechToTextConverter initialize(String message, Activity appContext) {
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
        stringToText.put("To", "2");
        stringToText.put("hundred", "100");
        stringToText.put("potty", "40");
        stringToText.put("party", "40");
        stringToText.put("tatti", "30");
        stringToText.put("tati", "30");
        stringToText.put("badi stop", "buddy stop");
        stringToText.put("stop", "buddy stop");

        var intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES, true);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        var listener = new CustomRecognitionListener();
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(appContext);
        speechRecognizer.setRecognitionListener(listener);
        speechRecognizer.startListening(intent);
        return this;
    }

    class CustomRecognitionListener implements RecognitionListener {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Log.d(TAG, "onReadyForSpeech");

        }

        @Override
        public void onBeginningOfSpeech() {
            Log.d(TAG, "onBeginningOfSpeech");

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            Log.d(TAG, "onBufferReceived");
        }

        @Override
        public void onEndOfSpeech() {
            Log.d(TAG, "onEndOfSpeech");
       //     assert conversionCallaBack != null;
            conversionCallaBack.onCompletion();
        }

        @Override
        public void onError(int error) {
            Log.d(TAG, "onError");
            assert conversionCallaBack != null;
            conversionCallaBack.onErrorOccurred(getErrorText(error));
            conversionCallaBack.getLogResult("onError : " + error);
        }

        @Override
        public void onResults(Bundle results) {
            Log.i(TAG, "onResults");
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            StringBuilder text = new StringBuilder();
            for (String result : matches)
                text.append(result).append("\n");
            result = matches.get(0).trim();

            conversionCallaBack.getLogResult("onResult : " + matches.get(0).trim());
            assert conversionCallaBack != null;
            conversionCallaBack.onPartialResult("Result: " + matches.get(0).trim() + "\n");
            if (matches.get(0).trim().matches(onlyNumber)) {
                conversionCallaBack.onSuccess(matches.get(0).trim());
                conversionCallaBack.getLogResult("onResult : " + matches.get(0).trim());
            } else {
                try {
                    conversionCallaBack.onSuccess(stringToText.get(matches.get(0).trim().toLowerCase()));
                    conversionCallaBack.getLogResult("onResultFormatted : " + stringToText.get(matches.get(0).trim().toLowerCase()));
                } catch (Exception e) {
                    conversionCallaBack.onErrorOccurred("Sorry, I don't understand");
                }
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
//            Log.i("LOG_TAG", "onPertialResults" + result);
//            ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//
//            if (matches.size() != 0) {
//
//                result = matches.get(0).trim();
//                conversionCallaBack.onPartialResult("Partial: " + matches.get(0).trim() + "\n");
//                Log.i("ResultsIntP", result + "");
//                if (!result.equals("")) {
//                    try {
//                        int res = Integer.parseInt(result.replace(" ", "").trim());
//                        Log.i("ResultInt ", res + "");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            Log.d(TAG, "onEvent");
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


    public void stop(){
        if (speechRecognizer!=null){
            speechRecognizer.stopListening();
        }
    }

    // destroy Stp
    public void destroy() {
        if (speechRecognizer != null) {
            conversionCallaBack = null;
            Log.d("TAG", "destroy: destroying STT ");
            speechRecognizer.destroy();
        }
    }
}
