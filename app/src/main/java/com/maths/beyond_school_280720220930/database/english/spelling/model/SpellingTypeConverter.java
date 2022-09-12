package com.maths.beyond_school_280720220930.database.english.spelling.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SpellingTypeConverter {
    @TypeConverter
    public static List<SpellingType> fromString(String value) {
        Type listType = new TypeToken<List<SpellingType>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<SpellingType> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

}
