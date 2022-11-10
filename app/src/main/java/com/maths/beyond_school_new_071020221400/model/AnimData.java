package com.maths.beyond_school_new_071020221400.model;

import androidx.annotation.Keep;

@Keep
public class AnimData {
    String description;
    String answer;
    String operation;
    public AnimData(){

    }

    public AnimData(String description, String answer,String operation) {
        this.description = description;
        this.answer = answer;
        this.operation=operation;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
