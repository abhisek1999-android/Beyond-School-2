package com.maths.beyond_school_new_071020221400.database.english.grammer.model;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Keep
@Entity(tableName = "grammar_table")
public class GrammarType {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String type;
    private List<GrammarModel> grammarModelList;

    public GrammarType(@NonNull String type, List<GrammarModel> grammarModelList) {
        this.type = type;
        this.grammarModelList = grammarModelList;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public List<GrammarModel> getGrammarModelList() {
        return grammarModelList;
    }

    public void setGrammarModelList(List<GrammarModel> grammarModelList) {
        this.grammarModelList = grammarModelList;
    }
}
