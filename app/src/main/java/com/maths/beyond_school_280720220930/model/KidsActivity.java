package com.maths.beyond_school_280720220930.model;

public class KidsActivity {

    String result;
    String status;
    long time_stamp;
    long correct;
    long wrong;
    long total_count;

    public KidsActivity(){}

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }

    public long getCorrect() {
        return correct;
    }

    public void setCorrect(long correct) {
        this.correct = correct;
    }

    public long getWrong() {
        return wrong;
    }

    public void setWrong(long wrong) {
        this.wrong = wrong;
    }

    public long getTotal_count() {
        return total_count;
    }

    public void setTotal_count(long total_count) {
        this.total_count = total_count;
    }

    public KidsActivity(String result, String status, long time_stamp, long correct, long wrong, long total_count) {
        this.result = result;
        this.status = status;
        this.time_stamp = time_stamp;
        this.correct = correct;
        this.wrong = wrong;
        this.total_count = total_count;
    }
}
