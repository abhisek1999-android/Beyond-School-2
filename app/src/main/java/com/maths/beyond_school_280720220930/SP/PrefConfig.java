package com.maths.beyond_school_280720220930.SP;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


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
    public static int readIntInPref(Context context, String LIST_KEY,int def) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int values = pref.getInt(LIST_KEY, def);
        return values;
    }

    public static void writeIntDInPref(Context context, int value, String LIST_KEY) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(LIST_KEY, value);
        editor.apply();
    }

    public static void writeNormalListInPref(Context context, List list, String LIST_KEY){

        Gson gson=new Gson();
        String jsonString= gson.toJson(list);

        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor= pref.edit();
        editor.putString(LIST_KEY,jsonString);
        editor.apply();
    }

    public static List readNormalListInPref(Context context,String LIST_KEY){

        SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = pref.getString(LIST_KEY,"");
        Gson gson=new Gson();
        Type type=new TypeToken<ArrayList>(){}.getType();
        List list=gson.fromJson(jsonString,type);
        return list;
    }

    public static int readIntDInPref(Context context, String LIST_KEY) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int values = pref.getInt(LIST_KEY, 0);
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
