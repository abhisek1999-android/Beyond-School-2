package com.maths.beyond_school_new_071020221400.retrofit.noun;

import androidx.annotation.Keep;

@Keep
public class Content {
    private String word;
    private String description;
    private String image;
    private String extra;

    public Content(String word, String description, String image, String extra) {
        this.word = word;
        this.description = description;
        this.image = image;
        this.extra = extra;
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

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
