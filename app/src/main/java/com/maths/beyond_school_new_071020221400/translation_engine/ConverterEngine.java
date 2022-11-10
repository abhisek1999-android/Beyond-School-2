package com.maths.beyond_school_new_071020221400.translation_engine;

import android.app.Activity;


public interface ConverterEngine<T> {
    T initialize(String message, Activity appContext);

    String getErrorText(int errorCode);


}

