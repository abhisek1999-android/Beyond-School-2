package com.maths.beyond_school_280720220930.model;

public class ProgressTableWise {
    String table;
    long total_correct;
    long total_wrong;
    long total_time;
    long count;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public ProgressTableWise(String date, long total_correct, long total_wrong, long total_time, long count) {
        this.table = date;
        this.total_correct = total_correct;
        this.total_wrong = total_wrong;
        this.total_time = total_time;
        this.count=count;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String date) {
        this.table = date;
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

    public long getTotal_time() {
        return total_time;
    }

    public void setTotal_time(long total_time) {
        this.total_time = total_time;
    }

    public ProgressTableWise() {

    }


}
