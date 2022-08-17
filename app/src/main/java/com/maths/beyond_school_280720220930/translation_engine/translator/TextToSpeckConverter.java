package com.maths.beyond_school_280720220930.translation_engine.translator;

import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.ConverterEngine;

import java.util.Locale;

public class TextToSpeckConverter implements ConverterEngine<TextToSpeckConverter> {


    private ConversionCallback conversionCallaBack;
    private String sentence = null;


    public TextToSpeckConverter(ConversionCallback callback) {
        this.conversionCallaBack = callback;
    }

    public void setTextViewAndSentence(String sentence) {
        this.sentence = sentence;
        Log.d("XXX", "setTextViewAndSentence: Called");
    }

    private static final String TAG = TextToSpeckConverter.class.getSimpleName();
    private TextToSpeech textToSpeech;

    @Override
    public TextToSpeckConverter initialize(String message, Activity appContext) {
        textToSpeech = new TextToSpeech(appContext, status -> {
            if (status != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(new Locale("en", "IN"));
                textToSpeech.setPitch(0.8f);
                textToSpeech.setSpeechRate(0.8f);


                textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String s) {

                    }

                    @Override
                    public void onDone(String s) {
                        conversionCallaBack.onCompletion();
                    }

                    @Override
                    public void onError(String s) {

                    }

                    @Override
                    public void onRangeStart(String utteranceId, int start, int end, int frame) {
                        super.onRangeStart(utteranceId, start, end, frame);
                        var textView = (TextView) appContext.findViewById(R.id.text_view_description);
                        if (textView != null) {
                            Spannable textWithHighlights = new SpannableString(sentence);
                            textWithHighlights.setSpan(new ForegroundColorSpan(ContextCompat.getColor(appContext, R.color.primary)), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                            textView.setText(textWithHighlights);
                        }
                    }
                });


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
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);

//        while (textToSpeech.isSpeaking()) {
//            Log.d(TAG, "Speaking");
//        }

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
