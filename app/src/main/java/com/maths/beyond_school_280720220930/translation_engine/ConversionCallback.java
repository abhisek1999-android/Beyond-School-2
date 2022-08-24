package com.maths.beyond_school_280720220930.translation_engine;

import org.json.JSONException;

public interface ConversionCallback {
    default void onSuccess(String result) throws JSONException {

    }

    default void onPartialResult(String result) {

    }

    void onCompletion();

    default void getLogResult(String title) {

    }

    default void getStringResult(String title) throws JSONException {

    }

    void onErrorOccurred(String errorMessage);
}
