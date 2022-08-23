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

import org.json.JSONException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SpeechToTextConverterEnglish implements ConverterEngine<SpeechToTextConverterEnglish> {

    private static final String TAG = SpeechToTextConverterEnglish.class.getSimpleName();

    private ConversionCallback conversionCallaBack;
    private SpeechRecognizer speechRecognizer = null;
    private Map<String, String> words = new HashMap<>();

    public SpeechToTextConverterEnglish(ConversionCallback callback) {
        this.conversionCallaBack = callback;
    }


    @Override
    public SpeechToTextConverterEnglish initialize(String message, Activity appContext) {
        words.put("com", "comb");
        words.put("komb", "comb");
        words.put("Kumbh", "comb");
        words.put("baat", "bath");
        words.put("singh", "sink");

        var intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES, true);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

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
            if (conversionCallaBack != null)
                conversionCallaBack.onCompletion();
        }

        @Override
        public void onError(int error) {
            Log.d(TAG, "onError");
            if (conversionCallaBack != null) {
                conversionCallaBack.onErrorOccurred(getErrorText(error));
                conversionCallaBack.getLogResult("onError : " + error);
            }
        }

        @Override
        public void onResults(Bundle results) {
            var translateResults = "";
            var data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            conversionCallaBack.getLogResult("onResult : " + data.get(0));
            translateResults = data.get(0);
            var s = words.get(translateResults.trim().toLowerCase(Locale.ROOT));
            if (s != null)
                translateResults = s;
            if (conversionCallaBack != null) {
                conversionCallaBack.getLogResult("onResultFormatted : " + translateResults);
                try {
                    conversionCallaBack.onSuccess(translateResults);
                } catch (JSONException e) {
                    e.printStackTrace();
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


    public void stop() {
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
        }
    }

    // destroy Stp
    public void destroy() {
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
            speechRecognizer.destroy();
            conversionCallaBack = null;
            Log.d("TAG", "destroy: destroying STT ");
        }
    }
}
