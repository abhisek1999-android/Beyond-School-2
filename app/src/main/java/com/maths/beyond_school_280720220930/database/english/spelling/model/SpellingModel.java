package com.maths.beyond_school_280720220930.database.english.spelling.model;

import androidx.annotation.Keep;
import androidx.room.Entity;

import java.util.List;

@Keep
@Entity(tableName = "spelling_table")
public class SpellingModel {
    int grade;
    List<SpellingCategoryModel> spelling;

    public SpellingModel(int grade, List<SpellingCategoryModel> spelling) {
        this.grade = grade;
        this.spelling = spelling;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public List<SpellingCategoryModel> getSpelling() {
        return spelling;
    }

    public void setSpelling(List<SpellingCategoryModel> spelling) {
        this.spelling = spelling;
    }
}

