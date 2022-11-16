package com.maths.beyond_school_280720220930.test.api;


import com.maths.beyond_school_280720220930.test.data.GradeModelNew;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterfaceNew {

    @GET("subjects/grade1/english/vocabulary/test.json")
    Call<List<GradeModelNew>> getGradeData();


}
