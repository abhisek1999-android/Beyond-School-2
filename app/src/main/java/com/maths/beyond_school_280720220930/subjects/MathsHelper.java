package com.maths.beyond_school_280720220930.subjects;

import android.content.Context;
import android.util.Log;

import com.maths.beyond_school_280720220930.extras.ReadText;
import com.maths.beyond_school_280720220930.extras.RecognizeVoice;

public class MathsHelper {

//    private static final String TAG = MathsHelper.class.toString();
//    private static MathsHelper instance = null;
//    private final ReadText readText;
//    private final RecognizeVoice recognizeVoice;

//    public MathsHelper(Context context, ReadText.GetResultSpeech result, RecognizeVoice.GetResult resultSpeech) {
//        readText = new ReadText(context, result);
//        recognizeVoice = new RecognizeVoice(context, resultSpeech);
//    }
//
//    public static MathsHelper getInstance(Context context, ReadText.GetResultSpeech resultTTS, RecognizeVoice.GetResult resultSTT) {
//            instance = new MathsHelper(context, resultTTS, resultSTT);
//            Log.i("Instance",instance+"");
//            return instance;
//    }

//    public void readText(String text) {
//        readText.read(text);
//    }
//
//    public void startListening() {
//        recognizeVoice.startListening();
//    }

    public static int add(int a, int b) {
        int result = a + b;
      //  readText.read(a + "plus" + b + "is");
        return result;
    }

    public static int  sub(int a, int b) {
        int result = a - b;
    //    readText.read(a + "minus" + b + "is" + result);
        return Math.abs(result);
    }



    public static int getMathResult(String subject,int a, int b){

        if (subject.equals("addition"))
            return a+b;
        else if (subject.equals("subtraction"))
            return Math.abs(a-b);
        else if (subject.equals("multiplication"))
            return a*b;
        else if (subject.equals("division"))
            return a/b;
        return 0;

    }


    public static String getMathQuestion (String subject,int a, int b){

        if (subject.equals("addition"))
            return  a+" plus "+ b+" equals";
        else if (subject.equals("subtraction"))
            return  a+" minus "+ b+" equals";
        else if (subject.equals("multiplication"))
            return  a+" "+ b+"'s are";

        else if (subject.equals("division"))
            return  a+"divided by "+ b;
        return "";

    }

    public static void swapValues(int m, int n)
    {
        // Swapping the values
        int temp = m;
        m = n;
        n = temp;
    }

//    public void stopReading() {
//        try {
//            readText.textToSpeech.stop();
//        } catch (Exception e) {
//            Log.e(TAG, "stopReading: " + e.getMessage());
//        }
//    }
//
//    public void stopListening() {
//        try {
//            recognizeVoice.speech.stopListening();
//         //   recognizeVoice.speech.destroy();
//        } catch (Exception e) {
//            Log.e("AdditionActivity", "stopListening: " + e.getMessage());
//        }
//    }


}
