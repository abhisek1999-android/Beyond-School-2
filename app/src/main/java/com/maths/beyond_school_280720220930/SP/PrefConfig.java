package com.maths.beyond_school_280720220930.SP;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class PrefConfig {

    public static void writeIdInPref(Context context, String emailId,String LIST_KEY){

        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor= pref.edit();
        editor.putString(LIST_KEY,emailId);
        editor.apply();
    }

    public static String readIdInPref(Context context,String LIST_KEY){

        SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(context);
         String  values = pref.getString(LIST_KEY,"");
        return values;
    }

}
