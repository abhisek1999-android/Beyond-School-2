package com.maths.beyond_school_280720220930.translation_engine;

import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;

public class TextToSpeechBuilder {
    private TextToSpeechBuilder() {

    }

    public static TextToSpeckConverter builder(ConversionCallback callback) {
        return new TextToSpeckConverter(callback);
    }

}
