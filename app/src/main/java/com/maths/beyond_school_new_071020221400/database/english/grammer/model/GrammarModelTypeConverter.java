package com.maths.beyond_school_new_071020221400.database.english.grammer.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class GrammarModelTypeConverter {
    @TypeConverter
    public static List<GrammarModel> fromString(String value) {
        return new Gson().fromJson(value, TypeToken.getParameterized(List.class, GrammarModel.class).getType());
    }

    @TypeConverter
    public static String fromArrayList(List<GrammarModel> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
