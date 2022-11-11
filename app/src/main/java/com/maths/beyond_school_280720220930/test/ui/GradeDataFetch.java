package com.maths.beyond_school_280720220930.test.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.maths.beyond_school_280720220930.databinding.ActivityGradeDataFetchBinding;
import com.maths.beyond_school_280720220930.test.api.ApiClient;
import com.maths.beyond_school_280720220930.test.api.ApiInterfaceNew;
import com.maths.beyond_school_280720220930.test.data.GradeModelNew;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GradeDataFetch extends AppCompatActivity {

    private static final String TAG = "GradeDataFetch";
    private ActivityGradeDataFetchBinding binding;
    private GradeLevelAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGradeDataFetchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setView();
        fetchData();
    }

    private void fetchData() {
        var apiClient = ApiClient.getClient().create(ApiInterfaceNew.class);
        apiClient.getGradeData("grade1").enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GradeModelNew> call, @NonNull Response<GradeModelNew> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        adapter.submitList(response.body().getEnglish().get(0).getChapters());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GradeModelNew> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void setView() {
        adapter = new GradeLevelAdapter();
        binding.gradeDataRecyclerView.
                setLayoutManager(new LinearLayoutManager(this));
        binding.gradeDataRecyclerView.setAdapter(adapter);
    }
}