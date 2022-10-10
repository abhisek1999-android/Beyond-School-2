package com.maths.beyond_school_new_071020221400.retrofit.model;



import com.maths.beyond_school_new_071020221400.retrofit.Chapter;

import java.util.List;

public class ResponseSubjects {
    List<Chapter> english;

    List<Chapter> maths;


    public ResponseSubjects(List<Chapter> english, List<Chapter> maths) {
        this.english = english;
        this.maths = maths;
    }

    public void setEnglish(List<Chapter> english) {
        this.english = english;
    }

    public List<Chapter> getMaths() {
        return maths;
    }

    public void setMaths(List<Chapter> maths) {
        this.maths = maths;
    }

    public List<Chapter> getEnglish() {
        return english;
    }
}

