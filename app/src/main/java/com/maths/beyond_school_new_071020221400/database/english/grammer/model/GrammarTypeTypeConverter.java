package com.maths.beyond_school_new_071020221400.database.english.grammer.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class GrammarTypeTypeConverter {
    @TypeConverter
    public static List<GrammarType> fromString(String value) {
        return new Gson().fromJson(value, TypeToken.getParameterized(List.class, GrammarType.class).getType());
    }

    @TypeConverter
    public static String fromArrayList(List<GrammarType> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
