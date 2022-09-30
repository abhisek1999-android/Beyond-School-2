package com.maths.beyond_school_280720220930.retrofit.model;



import com.maths.beyond_school_280720220930.retrofit.Chapter;

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

