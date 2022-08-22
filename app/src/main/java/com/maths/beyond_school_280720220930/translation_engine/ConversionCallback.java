package com.maths.beyond_school_280720220930.translation_engine;

public interface ConversionCallback {
    default void onSuccess(String result) {

    }

    default void onPartialResult(String result) {

    }

    void onCompletion();

    default void getLogResult(String title) {

    }

    default void getStringResult(String title) {

    }

    void onErrorOccurred(String errorMessage);
}
