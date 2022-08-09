package com.maths.beyond_school_280720220930.translation_engine;

import com.maths.beyond_school_280720220930.translation_engine.translator.SpeechToTextConverter;

public class SpeechToTextBuilder {
    private SpeechToTextBuilder() {

    }

    public static SpeechToTextConverter builder(ConversionCallback callback) {
        return new SpeechToTextConverter(callback);
    }

}
