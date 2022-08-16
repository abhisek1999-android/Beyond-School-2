package com.maths.beyond_school_280720220930.database.english;

import androidx.annotation.Keep;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maths.beyond_school_280720220930.database.english.model.VocabularyDetails;
import com.maths.beyond_school_280720220930.database.english.model.VocabularyModel;

import java.lang.reflect.Type;
import java.util.List;

@Keep
@Entity(tableName = "english_table")
public class EnglishModel {
    @PrimaryKey(autoGenerate = false)
    private int grade;
    private List<VocabularyModel> vocabulary;

    public EnglishModel(int grade, List<VocabularyModel> vocabulary) {
        this.grade = grade;
        this.vocabulary = vocabulary;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public List<VocabularyModel> getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(List<VocabularyModel> vocabulary) {
        this.vocabulary = vocabulary;
    }
}


class VocabularyModelConverter {
    @TypeConverter
    public static List<VocabularyModel> fromString(String value) {
        Type listType = new TypeToken<List<VocabularyModel>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<VocabularyModel> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}

class VocabularyDetailsConverter {

    @TypeConverter
    public static List<VocabularyDetails> fromString(String value) {
        Type listType = new TypeToken<List<VocabularyDetails>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(List<VocabularyDetails> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}

