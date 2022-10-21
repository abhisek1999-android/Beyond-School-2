package com.maths.beyond_school_280720220930.retrofit.model;

import com.maths.beyond_school_280720220930.retrofit.Chapter;

import java.util.List;

public class ResponseMaths {
    private List<Chapter> maths;

    public ResponseMaths(List<Chapter> maths) {
        this.maths = maths;
    }

    public List<Chapter> getMaths() {
        return maths;
    }

    public void setMaths(List<Chapter> maths) {
        this.maths = maths;
    }
}
