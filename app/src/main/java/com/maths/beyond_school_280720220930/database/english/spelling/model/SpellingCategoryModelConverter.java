package com.maths.beyond_school_280720220930.database.english.spelling.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SpellingCategoryModelConverter {
    @TypeConverter
    public static List<SpellingCategoryModel> fromString(String value) {
        Type listType = new TypeToken<List<SpellingCategoryModel>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<SpellingCategoryModel> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
