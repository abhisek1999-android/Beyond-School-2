package com.maths.beyond_school_280720220930.test.api;

import static com.facebook.FacebookSdk.getCacheDir;

import java.util.Objects;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class ApiClient {
    private static final long cacheSize = 5 * 1024 * 1024; // 5 MB

    private static final String BASE_URL = "https://shashuec.github.io/beyondschool_data_test/data/";
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

    private ApiClient() {
    }
}
