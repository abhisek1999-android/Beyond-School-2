package com.maths.beyond_school_280720220930.database.grade_tables;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "grade")
public class GradeData {
    @PrimaryKey(autoGenerate = false)
    private String id;
    private String chapter_name;
    private String subject;
    private String request;
    private boolean unlock;
    private boolean isFetchRequired;

    public GradeData(String id, String chapter_name, String subject, String request, boolean unlock, boolean isFetchRequired) {
        this.id = id;
        this.chapter_name = chapter_name;
        this.subject = subject;
        this.request = request;
        this.unlock = unlock;
        this.isFetchRequired = isFetchRequired;
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
}
