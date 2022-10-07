package com.maths.beyond_school_new_071020221400.database.grade_tables;

import androidx.annotation.Keep;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Keep
@Entity(tableName = "grades")
public class Grades_data {

    public boolean isIs_completed() {
        return is_completed;
    }

    public void setIs_completed(boolean is_completed) {
        this.is_completed = is_completed;
    }

    public boolean isGrade4() {
        return grade4;
    }

    public void setGrade4(boolean grade4) {
        this.grade4 = grade4;
    }

    public boolean isGrade5() {
        return grade5;
    }

    public void setGrade5(boolean grade5) {
        this.grade5 = grade5;
    }

    public Grades_data(String subject, String chapter, boolean grade1, boolean grade2, boolean grade3, boolean grade4, boolean grade5, boolean unlock, boolean is_completed, String url) {
        this.subject = subject;
        //this.subsubject = subsubject;
        this.chapter = chapter;
        this.grade1 = grade1;
        this.grade2 = grade2;
        this.grade3 = grade3;
        this.grade4 = grade4;
        this.grade5 = grade5;
        this.url=url;
        this.unlock=unlock;
        this.is_completed=is_completed;
    }



    @ColumnInfo(name="subject")
    public String subject;

    /*@ColumnInfo(name="sub_sub")
    public int  subsubject;*/

    @ColumnInfo(name="chapter")
    public String chapter;

   /* @ColumnInfo(name="grade")
    public ArrayList<String> grade;*/

    @ColumnInfo(name = "grade1")
    public boolean grade1;

    @ColumnInfo(name = "grade2")
    public boolean grade2;

    @ColumnInfo(name = "grade3")
    public boolean grade3;

    @ColumnInfo(name = "grade4")
    public boolean grade4;

    @ColumnInfo(name = "grade5")
    public boolean grade5;

    @ColumnInfo(name="url")
    public String  url;

    @ColumnInfo(name = "unlock")
    public boolean unlock;

    @ColumnInfo(name = "is_complete")
    public boolean is_completed;

    @PrimaryKey(autoGenerate = true)
    public int  progress_id;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isUnlock() {
        return unlock;
    }

    public void setUnlock(boolean unlock) {
        this.unlock = unlock;
    }
/* public int getSubsubject() {
        return subsubject;
    }

    public void setSubsubject(int subsubject) {
        this.subsubject = subsubject;
    }*/

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
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
