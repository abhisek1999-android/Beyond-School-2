package com.maths.beyond_school_280720220930.translation_engine;

import android.app.Activity;


public interface ConverterEngine<T> {
    T initialize(String message, Activity appContext);

    String getErrorText(int errorCode);


}

