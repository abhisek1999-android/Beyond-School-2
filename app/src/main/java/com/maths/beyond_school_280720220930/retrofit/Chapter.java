package com.maths.beyond_school_280720220930.retrofit;

import java.util.List;

public class Chapter {

    private String subject;
    private List<Chapters> chapters;

    public Chapter(){}

    public Chapter(String subject, List<Chapters> chapters) {
        this.subject = subject;
        this.chapters = chapters;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<Chapters> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapters> chapters) {
        this.chapters = chapters;
    }
}
