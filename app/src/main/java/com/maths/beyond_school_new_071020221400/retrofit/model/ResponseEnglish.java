package com.maths.beyond_school_new_071020221400.retrofit.model;



import com.maths.beyond_school_new_071020221400.retrofit.Chapter;

import java.util.List;

public class ResponseEnglish {
    List<Chapter> english;

    public ResponseEnglish(List<Chapter> english) {
        this.english = english;
    }

    public List<Chapter> getEnglish() {
        return english;
    }
}

