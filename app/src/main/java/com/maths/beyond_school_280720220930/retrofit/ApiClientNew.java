package com.maths.beyond_school_280720220930.retrofit;

import static com.facebook.FacebookSdk.getCacheDir;

import java.util.Objects;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientNew {

    private static final long cacheSize = 5 * 1024 * 1024; // 5 MB

    private static final String BASE_URL = "https://beyondschoolapis-rkjbkfkqbq-el.a.run.app/apis/content/";
//    private static final String BASE_URL = "https://abhisek1999-android.github.io/";
    private static Retrofit retrofit = null;

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .cache(new Cache(Objects.requireNonNull(getCacheDir()), cacheSize))
            .addInterceptor(chain -> {
                Request request = chain.request();
                request = request.newBuilder().header("Cache-Control", "public, max-age=" + 60 * 60 * 24).build();
                return chain.proceed(request);
            })
            .build();

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
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
