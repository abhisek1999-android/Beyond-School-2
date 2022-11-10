package com.maths.beyond_school_new_071020221400.database.english.spelling_common_words.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SpellingsDetailsConverter {

    @TypeConverter
    public static List<SpellingDetail> fromString(String value) {
        Type listType = new TypeToken<List<SpellingDetail>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(List<SpellingDetail> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}

