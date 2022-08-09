package com.maths.beyond_school_280720220930.database.grade_tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.maths.beyond_school_280720220930.database.Converters;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity(tableName = "grades")
public class Grades_data {

//    public Grades_data(String subject, String subsubject, int chapter, String grade,String url) {
//        this.subject = subject;
//        this.subsubject = subsubject;
//        this.chapter = chapter;
//        this.grade = Collections.singletonList(grade);
//        this.url=url;
//    }

    @ColumnInfo(name="subject")
    public String subject;

    @ColumnInfo(name="sub_sub")
    public String  subSubject;

    @ColumnInfo(name="chapter")
    public int  chapter;

    @ColumnInfo(name="grade")
    public ArrayList<String> grade;

    @ColumnInfo(name="url")
    public String  url;

    @PrimaryKey(autoGenerate = true)
    public int  progress_id;

//    public String getSubject() {
//        return subject;
//    }
//
//    public void setSubject(String subject) {
//        this.subject = subject;
//    }
//
//    public String getSubsubject() {
//        return subsubject;
//    }
//
//    public void setSubsubject(String subsubject) {
//        this.subsubject = subsubject;
//    }
//
//    public int getChapter() {
//        return chapter;
//    }
//
//    public void setChapter(int chapter) {
//        this.chapter = chapter;
//    }
//
//    public String getGrade() {
//        return grade;
//    }
//
//    public void setGrade(String grade) {
//        this.grade = grade;
//    }
//
//    public int getProgress_id() {
//        return progress_id;
//    }
//
//    public void setProgress_id(int progress_id) {
//        this.progress_id = progress_id;
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
}


