package com.maths.beyond_school_280720220930.retrofit;

import android.util.Log;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "https://aiyu-ayaan.github.io/Data-Share/data/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

// ----------------------------------------Getting data using retrofit-------------------------

//    private void getData() {
//        Call<ResponseEnglish> call = apiInterface.getTodos();
//
//        Log.i("BASE_URL",call.toString());
//        call.enqueue(new Callback<ResponseEnglish>() {
//            @Override
//            public void onResponse(Call<ResponseEnglish> call, Response<ResponseEnglish> response) {
//                List<Chapter> res = Objects.requireNonNull(response.body()).getEnglish();
//                if(res  != null)
//                    for(Chapter s : res)
//                        Log.d(TAG, "onResponse: "+s.getSubject());
//                Log.d(TAG, "onResponse: "+response.code());
//            }
//
//            @Override
//            public void onFailure(Call<ResponseEnglish> call, Throwable t) {
//                Log.e(TAG, "onFailure: " + t.getLocalizedMessage() );
//            }
//        });
//    }
