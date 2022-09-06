package com.maths.beyond_school_280720220930.model;

public class AnimData {
    String description;
    String answer;
    public AnimData(){

    }

    public AnimData(String description, String answer) {
        this.description = description;
        this.answer = answer;
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
