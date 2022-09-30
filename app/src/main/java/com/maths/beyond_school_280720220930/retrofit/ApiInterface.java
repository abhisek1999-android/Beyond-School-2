package com.maths.beyond_school_280720220930.retrofit;



import com.maths.beyond_school_280720220930.retrofit.model.ResponseEnglish;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("level_0.json")
    Call<ResponseEnglish> getTodos();
}
