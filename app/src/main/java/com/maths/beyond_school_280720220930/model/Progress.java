package com.maths.beyond_school_280720220930.model;

public class Progress {

    String date,table,time_to_complete,correct,wrong,is_completed,progress_id;


    public Progress(){

    }

    public Progress(String date, String table, String time_to_complete, String correct, String wrong, String is_completed, String progress_id) {
        this.date = date;
        this.table = table;
        this.time_to_complete = time_to_complete;
        this.correct = correct;
        this.wrong = wrong;
        this.is_completed = is_completed;
        this.progress_id = progress_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getTime_to_complete() {
        return time_to_complete;
    }

    public void setTime_to_complete(String time_to_complete) {
        this.time_to_complete = time_to_complete;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public String getWrong() {
        return wrong;
    }

    public void setWrong(String wrong) {
        this.wrong = wrong;
    }

    public String getIs_completed() {
        return is_completed;
    }

    public void setIs_completed(String is_completed) {
        this.is_completed = is_completed;
    }

    public String getProgress_id() {
        return progress_id;
    }

    public void setProgress_id(String progress_id) {
        this.progress_id = progress_id;
    }
}
