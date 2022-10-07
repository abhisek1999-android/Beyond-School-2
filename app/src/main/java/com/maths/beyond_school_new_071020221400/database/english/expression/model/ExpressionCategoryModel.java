package com.maths.beyond_school_new_071020221400.database.english.expression.model;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class ExpressionCategoryModel {
    private String category;
    private List<ExpressionDetails> vocabularyDetails;

    public ExpressionCategoryModel(String category, List<ExpressionDetails> vocabularyDetails) {
        this.category = category;
        this.vocabularyDetails = vocabularyDetails;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<ExpressionDetails> getExpressionDetails() {
        return vocabularyDetails;
    }

    public void setExpressionDetails(List<ExpressionDetails> vocabularyDetails) {
        this.vocabularyDetails = vocabularyDetails;
    }
}
