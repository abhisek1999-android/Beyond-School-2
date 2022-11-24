package com.maths.beyond_school_new_071020221400.database.grade_tables;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Keep
@Entity(tableName = "grades")
public class Grades_data {


    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String id;

    @ColumnInfo(name="subject")
    public String subject;

    @ColumnInfo(name="chapter_name")
    public String chapter_name;

    @ColumnInfo(name="super_subject")
    public String super_subject;

    @ColumnInfo(name = "unlock")
    public boolean unlock;

    @ColumnInfo(name = "is_completed")
    public boolean is_completed;

    public Grades_data(@NonNull String id, String subject, String chapter_name, String super_subject, boolean unlock, boolean is_completed) {
        this.id = id;
        this.subject = subject;
        this.chapter_name = chapter_name;
        this.super_subject = super_subject;
        this.unlock = unlock;
        this.is_completed = is_completed;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getChapter_name() {
        return chapter_name;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }

    public String getSuper_subject() {
        return super_subject;
    }

    public void setSuper_subject(String super_subject) {
        this.super_subject = super_subject;
    }

    public boolean isUnlock() {
        return unlock;
    }

    public void setUnlock(boolean unlock) {
        this.unlock = unlock;
    }

    public boolean isIs_completed() {
        return is_completed;
    }

    public void setIs_completed(boolean is_completed) {
        this.is_completed = is_completed;
    }
}
