package com.maths.beyond_school_new_071020221400.database.english.spelling_common_words.model;

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
