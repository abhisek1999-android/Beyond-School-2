package com.maths.beyond_school_280720220930.database.english.expression.model;

import androidx.annotation.Keep;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Keep
@Entity(tableName = "expression_table")
public class ExpressionModel {
    @PrimaryKey(autoGenerate = false)
    private int grade;
    private List<ExpressionCategoryModel> vocabulary;

    public ExpressionModel(int grade, List<ExpressionCategoryModel> vocabulary) {
        this.grade = grade;
        this.vocabulary = vocabulary;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public List<ExpressionCategoryModel> getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(List<ExpressionCategoryModel> vocabulary) {
        this.vocabulary = vocabulary;
    }
}


