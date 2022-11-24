package com.maths.beyond_school_new_071020221400.translation_engine;

import com.maths.beyond_school_new_071020221400.translation_engine.translator.SpeechToTextConverter;
import com.maths.beyond_school_new_071020221400.translation_engine.translator.SpeechToTextConverterEnglish;
import com.maths.beyond_school_new_071020221400.translation_engine.translator.SpeechToTextConverterSpelling;
import com.maths.beyond_school_new_071020221400.translation_engine.translator.SpeechToTextConverterVosk;

public class SpeechToTextBuilder {
    private SpeechToTextBuilder() {

    }

    public static SpeechToTextConverter builder(ConversionCallback callback) {
        return new SpeechToTextConverter(callback);
    }

    public static SpeechToTextConverterVosk builderVosk(ConversionCallback callback) {
        return new SpeechToTextConverterVosk(callback);
    }

    public static SpeechToTextConverterEnglish englishBuilder(ConversionCallback callback) {
        return new SpeechToTextConverterEnglish(callback);
    }

    public static SpeechToTextConverterSpelling spellingBuilder(ConversionCallback callback) {
        return new SpeechToTextConverterSpelling(callback);
    }
}
