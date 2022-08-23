package com.maths.beyond_school_280720220930.database.grade_tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "grades")
public class Grades_data {

    public Grades_data(int subject,int chapter, boolean grade1,boolean grade2,boolean grade3,String url) {
        this.subject = subject;
        //this.subsubject = subsubject;
        this.chapter = chapter;
        this.grade1 = grade1;
        this.grade2 = grade2;
        this.grade3 = grade3;
        this.url=url;
    }

    @ColumnInfo(name="subject")
    public int subject;

    /*@ColumnInfo(name="sub_sub")
    public int  subsubject;*/

    @ColumnInfo(name="chapter")
    public int  chapter;

   /* @ColumnInfo(name="grade")
    public ArrayList<String> grade;*/

    @ColumnInfo(name = "grade1")
    public boolean grade1;

    @ColumnInfo(name = "grade2")
    public boolean grade2;

    @ColumnInfo(name = "grade3")
    public boolean grade3;

    @ColumnInfo(name="url")
    public String  url;

    @PrimaryKey(autoGenerate = true)
    public int  progress_id;

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

   /* public int getSubsubject() {
        return subsubject;
    }

    public void setSubsubject(int subsubject) {
        this.subsubject = subsubject;
    }*/

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    /*public ArrayList<String> getGrade() {
        return grade;
    }

    public void setGrade(ArrayList<String> grade) {
        this.grade = grade;
    }*/

    public boolean isGrade1() {
        return grade1;
    }

    public void setGrade1(boolean grade1) {
        this.grade1 = grade1;
    }

    public boolean isGrade2() {
        return grade2;
    }

    public void setGrade2(boolean grade2) {
        this.grade2 = grade2;
    }

    public boolean isGrade3() {
        return grade3;
    }

    public void setGrade3(boolean grade3) {
        this.grade3 = grade3;
    }

    public int getProgress_id() {
        return progress_id;
    }

    public void setProgress_id(int progress_id) {
        this.progress_id = progress_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
