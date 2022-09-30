package com.maths.beyond_school_280720220930.retrofit;

public class Chapters {

    String chapter_name;
    int isCompleted;
    int grades;
    int grade2;
    int grade3;
    int grade4;
    int grade5;

    private Chapters(){

    }

    public Chapters(String chapter_name, int isCompleted, int grades, int grade2, int grade3, int grade4, int grade5, String path) {
        this.chapter_name = chapter_name;
        this.isCompleted = isCompleted;
        this.grades = grades;
        this.grade2 = grade2;
        this.grade3 = grade3;
        this.grade4 = grade4;
        this.grade5 = grade5;
        this.path = path;
    }

    String path;

    public String getChapter_name() {
        return chapter_name;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }

    public int getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(int isCompleted) {
        this.isCompleted = isCompleted;
    }

    public int getGrades() {
        return grades;
    }

    public void setGrades(int grades) {
        this.grades = grades;
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

}
