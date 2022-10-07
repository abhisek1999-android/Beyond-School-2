package com.maths.beyond_school_new_071020221400.database.english.vocabulary.model;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class VocabularyCategoryModel {
    private String category;
    private List<VocabularyDetails> vocabularyDetails;

    public VocabularyCategoryModel(String category, List<VocabularyDetails> vocabularyDetails) {
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
