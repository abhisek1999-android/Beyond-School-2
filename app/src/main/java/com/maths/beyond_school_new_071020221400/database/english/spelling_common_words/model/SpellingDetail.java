package com.maths.beyond_school_new_071020221400.database.english.spelling_common_words.model;

import androidx.annotation.Keep;

@Keep
public class SpellingDetail {
    private String word;
    private String description;

    public SpellingDetail(String word, String description) {
        this.word = word;
        this.description = description;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
