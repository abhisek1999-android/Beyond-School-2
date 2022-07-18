package com.maths.beyond_school270620220930.model;

public class Tables {

    String digit;
    String Decs;

    public Tables(){

    }

    public String getDigit() {
        return digit;
    }

    public void setDigit(String digit) {
        this.digit = digit;
    }

    public String getDecs() {
        return Decs;
    }

    public void setDecs(String decs) {
        Decs = decs;
    }

    public Tables(String digit, String decs) {
        this.digit = digit;
        Decs = decs;
    }
}
