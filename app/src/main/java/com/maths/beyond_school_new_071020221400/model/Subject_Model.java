package com.maths.beyond_school_new_071020221400.model;

import androidx.annotation.Keep;

@Keep
public class Subject_Model {
    int chapter,digit;
    String subsub;
    String url;

    boolean is_locked;

    public Subject_Model (String subsub, String url) {
        //this.chapter = chapter;
        //this.digit = digit;
        this.subsub = subsub;
        this.url=url;


    }

    public boolean isIs_locked() {
        return is_locked;
    }

    public void setIs_locked(boolean is_locked) {
        this.is_locked = is_locked;
    }

    public Subject_Model (String subsub, String url, boolean is_locked) {
        //this.chapter = chapter;
        //this.digit = digit;
        this.subsub = subsub;
        this.url=url;
        this.is_locked=is_locked;


    }

    /*public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public int getDigit() {
        return digit;
    }

    public void setDigit(int digit) {
        this.digit = digit;
    }*/

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSubsub() {
        return subsub;
    }

    public void setSubsub(String subsub) {
        this.subsub = subsub;
    }
}
