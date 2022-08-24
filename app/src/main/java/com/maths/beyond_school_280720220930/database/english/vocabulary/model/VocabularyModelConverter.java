package com.maths.beyond_school_280720220930.database.english.vocabulary.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class VocabularyModelConverter {
    @TypeConverter
    public static List<VocabularyCategoryModel> fromString(String value) {
        Type listType = new TypeToken<List<VocabularyCategoryModel>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<VocabularyCategoryModel> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
