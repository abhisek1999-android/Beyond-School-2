package com.maths.beyond_school_new_071020221400.retrofit;

public class Chapters {
    String chapter;
    String chapter_name;
    int unlock;
    int grade1;
    int grade2;
    int grade3;
    int grade4;
    int grade5;
    int isFetchRequired;

    public Chapters() {
        chapter = "";
        chapter_name = "";
        unlock = 0;
        grade1 = 0;
        grade2 = 0;
        grade3 = 0;
        grade4 = 0;
        grade5 = 0;
        isFetchRequired = 0;
    }

    public Chapters(String chapter, String chapter_name, int unlock, int grade1, int grade2, int grade3, int grade4, int grade5, String path, int isFetchRequired) {
        this.chapter = chapter;
        this.chapter_name = chapter_name;
        this.unlock = unlock;
        this.grade1 = grade1;
        this.grade2 = grade2;
        this.grade3 = grade3;
        this.grade4 = grade4;
        this.grade5 = grade5;
        this.path = path;
        this.isFetchRequired = isFetchRequired;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    String path;

    public String getChapter_name() {
        return chapter_name;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }

    public int getUnlock() {
        return unlock;
    }

    public void setUnlock(int unlock) {
        this.unlock = unlock;
    }

    public int getGrade1() {
        return grade1;
    }

    public void setGrade1(int grade1) {
        this.grade1 = grade1;
    }

    public int getGrade2() {
        return grade2;
    }

    public void setGrade2(int grade2) {
        this.grade2 = grade2;
    }

    public int getGrade3() {
        return grade3;
    }

    public void setGrade3(int grade3) {
        this.grade3 = grade3;
    }

    public int getGrade4() {
        return grade4;
    }

    public void setGrade4(int grade4) {
        this.grade4 = grade4;
    }

    public int getGrade5() {
        return grade5;
    }

    public void setGrade5(int grade5) {
        this.grade5 = grade5;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getIsFetchRequired() {
        return isFetchRequired;
    }

    public void setIsFetchRequired(int isFetchRequired) {
        this.isFetchRequired = isFetchRequired;
    }
}
