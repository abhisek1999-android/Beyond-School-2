package com.maths.beyond_school_new_071020221400.translation_engine.translator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import com.maths.beyond_school_new_071020221400.translation_engine.ConversionCallback;
import com.maths.beyond_school_new_071020221400.translation_engine.ConverterEngine;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
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
        stringToText.put("too", "2");
        stringToText.put("To", "2");
        stringToText.put("To0", "2");
        stringToText.put("then", "10");
        stringToText.put("hundred", "100");
        stringToText.put("potty", "40");
        stringToText.put("party", "40");
        stringToText.put("tatti", "30");
        stringToText.put("tati", "30");
        stringToText.put("tati", "30");
        stringToText.put("aur", "4");
        stringToText.put("stop", "buddy stop");

        var intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en-IN");
        intent.putExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES, true);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 30);

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

          //  Log.d(TAG, "onRmsChanged: "+rmsdB);
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
            Log.d(TAG, "onError" + getErrorText(error));
            assert conversionCallaBack != null;
            conversionCallaBack.onErrorOccurred(getErrorText(error));
            conversionCallaBack.getLogResult("onError : " + getErrorText(error));
        }

        @Override
        public void onResults(Bundle results) {
            Log.i(TAG, "onResults");
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            ArrayList<String> unstableData = results.getStringArrayList("android.speech.extra.UNSTABLE_TEXT");
            Log.d(TAG, "onResults: " + matches);
            Log.d(TAG, "onResults: Unstable"+unstableData);
            StringBuilder text = new StringBuilder();
            for (String result : matches)
                text.append(result).append("\n");
            result = matches.get(0).trim();

            conversionCallaBack.getLogResult("onResult : " + matches.get(0).trim());
            assert conversionCallaBack != null;
            conversionCallaBack.onPartialResult("Result: " + matches.get(0).trim() + "\n");
            if (matches.get(0).trim().matches(onlyNumber)) {
                try {
                    conversionCallaBack.onSuccess(matches.get(0).trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                conversionCallaBack.getLogResult("onResult : " + matches.get(0).trim());
            } else {
                try {
                    conversionCallaBack.onSuccess(stringToText.get(matches.get(0).trim().toLowerCase()));
                    conversionCallaBack.getLogResult("onResultFormatted : " + stringToText.get(matches.get(0).trim().toLowerCase()));
                } catch (Exception e) {
                    try {
                        conversionCallaBack.getStringResult(matches.get(0).trim().toLowerCase());
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                    // conversionCallaBack.onErrorOccurred("Sorry, I don't understand");
                }
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            Log.i(TAG, "onPertialResults" + result);
            ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            Log.i(TAG, "onPertialResults" + matches);
            if (matches.size() != 0) {

                result = matches.get(0).trim();
                conversionCallaBack.onPartialResult("Partial: " + matches.get(0).trim() + "\n");
                Log.i(TAG, "ResultsIntP" + result + "");
                if (!result.equals("")) {
                    try {
                        int res = Integer.parseInt(result.replace(" ", "").trim());
                        Log.i(TAG, "ResultInt " + res + "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
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
            conversionCallaBack = null;
            Log.d("TAG", "destroy: destroying STT ");
            speechRecognizer.destroy();
        }
    }
}
