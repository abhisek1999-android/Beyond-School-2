package com.maths.beyond_school_280720220930.database.english.spelling.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class SpellingModelConverter {
    @TypeConverter
    public static List<SpellingModel> fromString(String value) {
        return new Gson().fromJson(value, TypeToken.getParameterized(List.class, SpellingModel.class).getType());
    }

    @TypeConverter
    public static String fromArrayList(List<SpellingModel> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
