package com.maths.beyond_school_280720220930.database.english.spelling.model;

import androidx.annotation.Keep;

@Keep
public class SpellingModel {
    String word;
    String imageLink;

    public SpellingModel(String word, String imageLink) {
        this.word = word;
        this.imageLink = imageLink;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
