package com.maths.beyond_school_new_071020221400.translation_engine;

import org.json.JSONException;

import java.util.List;

public interface ConversionCallback {
    default void onSuccess(String result) throws JSONException {

    }

    default void onPartialResult(String result) {

    }

    void onCompletion();

    default void getLogResult(String title) {

    }
    default void getArrayResult(List<String[]> list) {

    }

    default void getStringResult(String title) throws JSONException {

    }

    void onErrorOccurred(String errorMessage);
}
