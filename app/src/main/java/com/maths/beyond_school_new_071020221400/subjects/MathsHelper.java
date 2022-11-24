package com.maths.beyond_school_new_071020221400.subjects;

import com.maths.beyond_school_new_071020221400.utils.UtilityFunctions;

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

        if (subject.equals("Addition"))
            return a+b;
        else if (subject.equals("Subtraction"))
            return Math.abs(a-b);
        else if (subject.equals("Multiplication Tables"))
            return a*b;
        else if (subject.equals("Division"))
            return a/b;
        return 0;

    }


    public static String getMathQuestion (String subject,int a, int b){

        if (subject.equals("Addition"))
            return  a+" plus "+ b+" equals";
        else if (subject.equals("Subtraction"))
            return  a+" minus "+ b+" equals";
        else if (subject.equals("Multiplication Tables"))
            return  a+" "+ UtilityFunctions.numberToWords(b) +"s are";

        else if (subject.equals("Division"))
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
