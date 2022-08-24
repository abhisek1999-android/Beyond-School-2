package com.maths.beyond_school_280720220930.SP;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class PrefConfig {

    public static void writeIdInPref(Context context, String emailId, String LIST_KEY) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(LIST_KEY, emailId);
        editor.apply();
    }

    public static String readIdInPref(Context context, String LIST_KEY) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String values = pref.getString(LIST_KEY, "");
        return values;
    }

    public static void writeIntInPref(Context context, int value, String LIST_KEY) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(LIST_KEY, value);
        editor.apply();
    }

    public static int readIntInPref(Context context, String LIST_KEY) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int values = pref.getInt(LIST_KEY, 1);
        return values;
    }

    // function to write boolean in SharedPreferences
    public static void writeBooleanInPref(Context context, boolean value, String LIST_KEY) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(LIST_KEY, value);
        editor.apply();
    }

    // function to read boolean from SharedPreferences
    public static boolean readBooleanInPref(Context context, String LIST_KEY) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean values = pref.getBoolean(LIST_KEY, false);
        return values;
    }

}
