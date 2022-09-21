package com.maths.beyond_school_280720220930.model;

import androidx.annotation.Keep;

@Keep
public class ProgressDate {
    String date;
    long total_correct;
    long total_wrong;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTotal_correct() {
        return total_correct;
    }

    public void setTotal_correct(long total_correct) {
        this.total_correct = total_correct;
    }

    public long getTotal_wrong() {
        return total_wrong;
    }

    public void setTotal_wrong(long total_wrong) {
        this.total_wrong = total_wrong;
    }

    public ProgressDate() {

    }


}
