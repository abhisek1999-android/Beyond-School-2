package com.maths.beyond_school_new_071020221400.retrofit;



import com.maths.beyond_school_new_071020221400.retrofit.model.ResponseSubjects;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("english.json")
    Call<ResponseSubjects> getTodos();
}
