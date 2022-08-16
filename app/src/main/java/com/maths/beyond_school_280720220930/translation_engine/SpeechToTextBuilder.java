package com.maths.beyond_school_280720220930.translation_engine;

import com.maths.beyond_school_280720220930.translation_engine.translator.SpeechToTextConverter;
import com.maths.beyond_school_280720220930.translation_engine.translator.SpeechToTextConverterEnglish;

public class SpeechToTextBuilder {
    private SpeechToTextBuilder() {

    }

    public static SpeechToTextConverter builder(ConversionCallback callback) {
        return new SpeechToTextConverter(callback);
    }

    public static SpeechToTextConverterEnglish englishBuilder(ConversionCallback callback) {
        return new SpeechToTextConverterEnglish(callback);
    }
}
