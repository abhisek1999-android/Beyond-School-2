package com.maths.beyond_school_280720220930.translation_engine.translator;

import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.ConverterEngine;

import java.util.Locale;

public class TextToSpeckConverter implements ConverterEngine<TextToSpeckConverter> {


    private ConversionCallback conversionCallaBack;


    public TextToSpeckConverter(ConversionCallback callback) {
        this.conversionCallaBack = callback;
    }

    private static final String TAG = TextToSpeckConverter.class.getSimpleName();
    private TextToSpeech textToSpeech;

    @Override
    public TextToSpeckConverter initialize(String message, Activity appContext) {
        textToSpeech = new TextToSpeech(appContext, status -> {
            if (status != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(new Locale("en", "IN"));
                textToSpeech.setPitch(0.8f);
                textToSpeech.setSpeechRate(1f);
                tts(message);
            } else {
                assert conversionCallaBack != null;
                conversionCallaBack.onErrorOccurred("Failed to initialize TTS engine");
            }
        });
        return this;
    }

    @Override
    public String getErrorText(int errorCode) {
        var message = "";
        switch (errorCode) {
            case TextToSpeech.ERROR:
                message = "ERROR";
                break;
            case TextToSpeech.ERROR_INVALID_REQUEST:
                message = "ERROR_INVALID_REQUEST";
                break;
            case TextToSpeech.ERROR_NETWORK:
                message = "ERROR_NETWORK";
                break;
            case TextToSpeech.ERROR_NETWORK_TIMEOUT:
                message = "ERROR_NETWORK_TIMEOUT";
                break;
            case TextToSpeech.ERROR_SERVICE:
                message = "ERROR_SERVICE";
                break;
            case TextToSpeech.ERROR_SYNTHESIS:
                message = "ERROR_SYNTHESIS";
                break;
            case TextToSpeech.ERROR_NOT_INSTALLED_YET:
                message = "ERROR_NOT_INSTALLED_YET";
                break;
            default:
                message = "UNKNOWN";
                break;
        }
        return message;
    }


    private void tts(String text) {
        String utteranceId = this.hashCode() + "";
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        while (textToSpeech.isSpeaking()) {
            Log.d(TAG, "Speaking");
        }
        assert conversionCallaBack != null;
        conversionCallaBack.onCompletion();
    }

    public void destroy() {
        if (textToSpeech != null) {
            Log.d("TAG", "destroy: destroying TTS");
            conversionCallaBack = null;
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
