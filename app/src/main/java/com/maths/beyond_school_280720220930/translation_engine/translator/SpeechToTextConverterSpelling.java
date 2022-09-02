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

public class SpeechToTextConverterSpelling implements ConverterEngine<SpeechToTextConverterSpelling> {

    private static final String TAG = SpeechToTextConverterEnglish.class.getSimpleName();

    private ConversionCallback conversionCallaBack;
    private SpeechRecognizer speechRecognizer = null;
    private Map<String, String> words = new HashMap<>();

    public SpeechToTextConverterSpelling(ConversionCallback callback) {
        this.conversionCallaBack = callback;
    }


    @Override
    public SpeechToTextConverterSpelling initialize(String message, Activity appContext) {
        words.put("ye", "a");
        words.put("yay", "a");
        words.put("bee", "b");
        words.put("see", "c");
        words.put("dee", "d");
        words.put("ee", "e");
        words.put("eff", "f");
        words.put("gee", "g");
        words.put("aitch", "h");
        words.put("eye", "i");
        words.put("jay", "j");
        words.put("kay", "k");
        words.put("ell", "l");
        words.put("em", "m");
        words.put("en", "n");
        words.put("oh", "o");
        words.put("pee", "p");
        words.put("cue", "q");
        words.put("are", "r");
        words.put("ess", "s");
        words.put("tee", "t");
        words.put("you", "u");
        words.put("vee", "v");
        words.put("double you", "w");
        words.put("ex", "x");
        words.put("why", "y");
        words.put("zed", "z");
        words.put("as", "s");
        words.put("es", "s");


        var intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES, true);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        var listener = new CustomRecognitionListener();
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(appContext);
        speechRecognizer.setRecognitionListener(listener);
        speechRecognizer.startListening(intent);
        return this;
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
                    conversionCallaBack.onSuccess(translateResults.toLowerCase(Locale.ROOT));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            var d = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            Log.d("XXX", "onPartialResults : " + d.toString());
            if (conversionCallaBack != null && d.get(0).length() > 0) {
                conversionCallaBack.getLogResult("onPartialResult : " + d.get(0));
                conversionCallaBack.onPartialResult(d.get(0).toLowerCase(Locale.ROOT));
            }


        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            Log.d(TAG, "onEvent");
        }
    }
}
