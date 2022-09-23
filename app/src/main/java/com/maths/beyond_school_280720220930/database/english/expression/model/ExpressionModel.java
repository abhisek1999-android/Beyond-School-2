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
    private List<ExpressionCategoryModel> expression;

    public ExpressionModel(int grade, List<ExpressionCategoryModel> expression) {
        this.grade = grade;
        this.expression = expression;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public List<ExpressionCategoryModel> getExpression() {
        return expression;
    }

    public void setVocabulary(List<ExpressionCategoryModel> expression) {
        this.expression = expression;
    }
}


