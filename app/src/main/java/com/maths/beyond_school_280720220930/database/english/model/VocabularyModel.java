package com.maths.beyond_school_280720220930.database.english.model;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class VocabularyModel {
    private String category;
    private List<VocabularyDetails> vocabularyDetails;

    public VocabularyModel(String category, List<VocabularyDetails> vocabularyDetails) {
        this.category = category;
        this.vocabularyDetails = vocabularyDetails;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<VocabularyDetails> getVocabularyDetails() {
        return vocabularyDetails;
    }

    public void setVocabularyDetails(List<VocabularyDetails> vocabularyDetails) {
        this.vocabularyDetails = vocabularyDetails;
    }
}
