package com.maths.beyond_school_280720220930.database.english.expression.model;

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

    public String getWord() {
        return question;
    }

    public void setWord(String question) {
        this.question = question;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public List<String> getImageLink() {
        return answers;
    }

    public void setImageLink(List<String> answers) {
        this.answers = answers;
    }
}
