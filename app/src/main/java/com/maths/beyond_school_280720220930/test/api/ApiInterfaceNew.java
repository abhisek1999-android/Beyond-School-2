package com.maths.beyond_school_280720220930.test.api;


import com.maths.beyond_school_280720220930.test.data.GradeModelNew;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterfaceNew {

    @GET("{grade}.json")
    Call<GradeModelNew> getGradeData(@Path("grade") String grade);


}
