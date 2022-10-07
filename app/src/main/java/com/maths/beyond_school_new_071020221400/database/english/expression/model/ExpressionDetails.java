package com.maths.beyond_school_new_071020221400.database.english.expression.model;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class ExpressionDetails {
    private String question;
    private String definition;
    private List<String> answers;

    public ExpressionDetails(String question, String definition, List<String> answer) {
        this.question = question;
        this.definition = definition;
        this.answers = answer;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

}
