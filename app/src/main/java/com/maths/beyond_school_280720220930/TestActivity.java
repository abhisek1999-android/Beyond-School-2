package com.maths.beyond_school_280720220930;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.maths.beyond_school_280720220930.databinding.ActivityTestBinding;
import com.maths.beyond_school_280720220930.retrofit.ApiClient;
import com.maths.beyond_school_280720220930.retrofit.ApiInterface;
import com.maths.beyond_school_280720220930.retrofit.Chapter;
import com.maths.beyond_school_280720220930.retrofit.model.ResponseEnglish;
import com.maths.beyond_school_280720220930.utils.GradeConverter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.maths.beyond_school_280720220930.databinding.ActivityTestBinding binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getData();
    }

    private void getData() {
        Retrofit retrofit = ApiClient.getClient();
        var api = retrofit.create(ApiInterface.class);
        api.getTodos().enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseEnglish> call,
                                   @NonNull Response<ResponseEnglish> response) {
                var res = response.body();
                if (res != null) {
                    var english = res.getEnglish();
                    convertData(english);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseEnglish> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void convertData(List<Chapter> english) {
        english.forEach(
                chapter -> {
                    var chapters = chapter.getChapters();
                    var converter = new GradeConverter();
                    var listOfGradeData = converter.mapToList(chapters); // Grade List similar to GradeDatabase
                }
        );
    }
}