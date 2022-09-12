package com.maths.beyond_school_280720220930.database.english.spelling.model;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Keep
@Entity(tableName = "spelling_table")
public class SpellingType {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    String type;
    List<SpellingModel> spellingModels;

    public SpellingType(String type, List<SpellingModel> spellingModels) {
        this.type = type;
        this.spellingModels = spellingModels;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<SpellingModel> getSpellingModels() {
        return spellingModels;
    }

    public void setSpellingModels(List<SpellingModel> spellingModels) {
        this.spellingModels = spellingModels;
    }
}
