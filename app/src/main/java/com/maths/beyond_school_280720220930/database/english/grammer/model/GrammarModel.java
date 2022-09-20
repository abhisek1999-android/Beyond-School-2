package com.maths.beyond_school_280720220930.database.english.grammer.model;

import androidx.annotation.Keep;

@Keep
public class GrammarModel {
    private String word;
    private String description;
    private String image;
    private String plural;

    public GrammarModel(String word, String description, String image) {
        this.word = word;
        this.description = description;
        this.image = image;
        plural = "";
    }

    public GrammarModel(String word, String plural, String description, String image) {
        this.word = word;
        this.description = description;
        this.image = image;
        this.plural = plural;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPlural() {
        return plural;
    }

    public void setPlural(String plural) {
        this.plural = plural;
    }
}
