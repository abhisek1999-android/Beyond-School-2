package com.maths.beyond_school_280720220930.model;

public class Subject_Model {
    int chapter,digit;
    String subsub;
    String url;

    public Subject_Model (String subsub, String url) {
        //this.chapter = chapter;
        //this.digit = digit;
        this.subsub = subsub;
        this.url=url;
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
