package com.maths.beyond_school_280720220930.model;

public class Tables {

    String digit;
    String Decs;
    boolean is_locked;

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

    public boolean isIs_locked() {
        return is_locked;
    }

    public void setIs_locked(boolean is_locked) {
        this.is_locked = is_locked;
    }

    public Tables(String digit, String decs, boolean is_locked) {
        this.digit = digit;
        Decs = decs;
        this.is_locked=is_locked;
    }
}
