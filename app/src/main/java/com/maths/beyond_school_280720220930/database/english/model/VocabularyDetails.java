package com.maths.beyond_school_280720220930.database.english.model;

import androidx.annotation.Keep;

@Keep
public class VocabularyDetails {
    private String word;
    private String definition;
    private String imageLink;

    public VocabularyDetails(String word, String definition, String imageLink) {
        this.word = word;
        this.definition = definition;
        this.imageLink = imageLink;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
