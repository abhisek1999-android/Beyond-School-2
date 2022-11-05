package com.maths.beyond_school_280720220930.database.grade_tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "grade")
public class GradeData {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String id;
    private String chapter_name;
    private String subject;
    private String request;
    private String super_subject;
    private boolean unlock;
    private boolean isFetchRequired;
    private boolean is_completed;



    public GradeData(String id, String chapter_name, String subject, String request, String super_subject, boolean unlock, boolean isFetchRequired, boolean is_completed) {
        this.id = id;
        this.chapter_name = chapter_name;
        this.subject = subject;
        this.request = request;
        this.unlock = unlock;
        this.isFetchRequired = isFetchRequired;
        this.is_completed=is_completed;
        this.super_subject=super_subject;
    }

    public String getSuper_subject() {
        return super_subject;
    }

    public void setSuper_subject(String super_subject) {
        this.super_subject = super_subject;
    }

    public boolean is_completed() {
        return is_completed;
    }

    public void setCompleted(boolean completed) {
        is_completed = completed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChapter_name() {
        return chapter_name;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public boolean isUnlock() {
        return unlock;
    }

    public void setUnlock(boolean unlock) {
        this.unlock = unlock;
    }

    public boolean isFetchRequired() {
        return isFetchRequired;
    }

    public void setFetchRequired(boolean fetchRequired) {
        isFetchRequired = fetchRequired;
    }

    @Override
    public String toString() {
        return "GradeData{" +
                "id='" + id + '\'' +
                ", chapter_name='" + chapter_name + '\'' +
                ", subject='" + subject + '\'' +
                ", request='" + request + '\'' +
                ", unlock=" + unlock +
                ", isFetchRequired=" + isFetchRequired +
                ", is_completed=" + is_completed +
                '}';
    }
}
