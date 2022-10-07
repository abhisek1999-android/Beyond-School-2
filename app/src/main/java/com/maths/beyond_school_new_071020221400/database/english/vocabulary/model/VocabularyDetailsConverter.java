package com.maths.beyond_school_new_071020221400.database.english.vocabulary.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class VocabularyDetailsConverter {

    @TypeConverter
    public static List<VocabularyDetails> fromString(String value) {
        return new Gson().fromJson(value, TypeToken.getParameterized(List.class, VocabularyDetails.class).getType());
    }

    @TypeConverter
    public static String fromArrayList(List<VocabularyDetails> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}

