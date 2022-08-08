package com.maths.beyond_school_280720220930.subjects;

import android.content.Context;
import android.util.Log;

import com.maths.beyond_school_280720220930.extras.ReadText;
import com.maths.beyond_school_280720220930.extras.RecognizeVoice;

public class MathsHelper {

    private static final String TAG = MathsHelper.class.toString();
    private static MathsHelper instance = null;
    private final ReadText readText;
    private final RecognizeVoice recognizeVoice;

    private MathsHelper(Context context, ReadText.GetResultSpeech result, RecognizeVoice.GetResult resultSpeech) {
        readText = new ReadText(context, result);
        recognizeVoice = new RecognizeVoice(context, resultSpeech);

    }

    public static MathsHelper getInstance(Context context, ReadText.GetResultSpeech resultTTS, RecognizeVoice.GetResult resultSTT) {
        if (instance == null) {
            instance = new MathsHelper(context, resultTTS, resultSTT);
            return instance;
        }
        return instance;
    }

    public void readText(String text) {
        readText.read(text);
    }


    public void startListening() {
        recognizeVoice.startListening();
    }

    public int add(int a, int b) {
        int result = a + b;
        readText.read(a + "plus" + b + "is");
        return result;
    }

    public int sub(int a, int b) {
        int result = a - b;
        readText.read(a + "minus" + b + "is" + result);
        return result;
    }

    public void stopReading() {
        try {
            readText.textToSpeech.stop();
        } catch (Exception e) {
            Log.e(TAG, "stopReading: " + e.getMessage());
        }
    }

    public void stopListening() {
        try {
            recognizeVoice.speech.stopListening();
            recognizeVoice.speech.destroy();
        } catch (Exception e) {
            Log.e("AdditionActivity", "stopListening: " + e.getMessage());
        }
    }


}
