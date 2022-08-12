package com.maths.beyond_school_280720220930.model;

public class Subject_Model {
    int chapter,digit,subsub;

    public Subject_Model(int chapter, int digit, int subsub) {
        this.chapter = chapter;
        this.digit = digit;
        this.subsub = subsub;
    }

    public int getChapter() {
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
    }

    public int getSubsub() {
        return subsub;
    }

    public void setSubsub(int subsub) {
        this.subsub = subsub;
    }
}
