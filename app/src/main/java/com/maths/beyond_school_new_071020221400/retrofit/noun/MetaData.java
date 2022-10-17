package com.maths.beyond_school_new_071020221400.retrofit.noun;

import androidx.annotation.Keep;

@Keep
public class MetaData {
    String practice;
    String test;

    public MetaData(String practice, String test) {
        this.practice = practice;
        this.test = test;
    }

    public String getPractice() {
        return practice;
    }

    public void setPractice(String practice) {
        this.practice = practice;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    @Override
    public String toString() {
        return "MetaData{" +
                "practice='" + practice + '\'' +
                ", test='" + test + '\'' +
                '}';
    }
}
