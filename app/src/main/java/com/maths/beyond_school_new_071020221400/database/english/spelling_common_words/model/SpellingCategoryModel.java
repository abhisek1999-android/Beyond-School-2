package com.maths.beyond_school_new_071020221400.database.english.spelling_common_words.model;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class SpellingCategoryModel {
    private String category;
    private List<SpellingDetail> details;

    public SpellingCategoryModel(String category, List<SpellingDetail> details) {
        this.category = category;
        this.details = details;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<SpellingDetail> getDetails() {
        return details;
    }

    public void setDetails(List<SpellingDetail> details) {
        this.details = details;
    }
}
