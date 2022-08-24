package com.maths.beyond_school_280720220930.database.english.vocabulary.model;

import androidx.annotation.Keep;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Keep
@Entity(tableName = "english_table")
public class VocabularyModel {
    @PrimaryKey(autoGenerate = false)
    private int grade;
    private List<VocabularyCategoryModel> vocabulary;

    public VocabularyModel(int grade, List<VocabularyCategoryModel> vocabulary) {
        this.grade = grade;
        this.vocabulary = vocabulary;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public List<VocabularyCategoryModel> getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(List<VocabularyCategoryModel> vocabulary) {
        this.vocabulary = vocabulary;
    }
}


