package com.maths.beyond_school_new_071020221400.database.english.spelling_objects.model;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Keep
@Entity(tableName = "spelling_object_table")
public class SpellingType {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    String type;
    List<SpellingModel> spellingModels;

    public SpellingType(@NonNull String type, List<SpellingModel> spellingModels) {
        this.type = type;
        this.spellingModels = spellingModels;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public List<SpellingModel> getSpellingModels() {
        return spellingModels;
    }

    public void setSpellingModels(List<SpellingModel> spellingModels) {
        this.spellingModels = spellingModels;
    }
}
