package com.maths.beyond_school_280720220930;

import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_TITLE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.adapters.ChaptersRecyclerAdapter;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeData;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.databinding.ActivityTestBinding;
import com.maths.beyond_school_280720220930.english_activity.grammar.GrammarActivity;
import com.maths.beyond_school_280720220930.retrofit.ApiClient;
import com.maths.beyond_school_280720220930.retrofit.ApiInterface;
import com.maths.beyond_school_280720220930.retrofit.model.grade.GradeModel;
import com.maths.beyond_school_280720220930.utils.Constants;
import com.maths.beyond_school_280720220930.utils.typeconverters.GradeConverter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TestActivity extends AppCompatActivity {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private String TAG = "TestActivity";

    private GradeDatabase gradeDatabase;
    private ActivityTestBinding binding;
    private ChaptersRecyclerAdapter chaptersRecyclerAdapter;
    private List<GradeData> chapterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        gradeDatabase = GradeDatabase.getDbInstance(this);
//        setUpRemoteConfig();
        getNewData();
        setRecyclerViewData();
    }

    private void setRecyclerViewData() {

        chapterList = gradeDatabase.gradesDaoUpdated().getChapter();
        binding.contentRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        chaptersRecyclerAdapter = new ChaptersRecyclerAdapter(TestActivity.this, gradeData -> {
            var intent = new Intent(TestActivity.this, GrammarActivity.class);
            intent.putExtra(Constants.EXTRA_GRAMMAR_CATEGORY, gradeData.getChapter_name());
            intent.putExtra(Constants.EXTRA_ONLINE_FLAG, true);
            intent.putExtra(EXTRA_TITLE, gradeData.getSubject());
            startActivity(intent);
        });
        binding.contentRecyclerView.setAdapter(chaptersRecyclerAdapter);
    }


    private void getNewData() {
        Retrofit retrofit = ApiClient.getClient();
        var api = retrofit.create(ApiInterface.class);
        api.getGradeData("grade1").enqueue(new retrofit2.Callback<>() {
            private Call<GradeModel> call;
            private Response<GradeModel> response;

            @Override
            public void onResponse(@NonNull retrofit2.Call<com.maths.beyond_school_280720220930.retrofit.model.grade.GradeModel> call, @NonNull retrofit2.Response<com.maths.beyond_school_280720220930.retrofit.model.grade.GradeModel> response) {
                this.call = call;
                this.response = response;
                if (response.body() != null) {
                    Log.d(TAG, "onResponse: " + response.code());
                    Log.d(TAG, "onResponse: " + response.body().getEnglish().toString());
                    var list = response.body().getEnglish();
                    mapToGradeModel(list);
                } else {
                    Toast.makeText(TestActivity.this, "Something wrong occurs", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<com.maths.beyond_school_280720220930.retrofit.model.grade.GradeModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void mapToGradeModel(List<GradeModel.EnglishModel> list) {
        list.forEach(subject -> {
            var mapper = new GradeConverter(subject.getSubject());
            var chapterList = mapper.mapToList(subject.getChapters());
            gradeDatabase.gradesDaoUpdated().insertNotes(chapterList);
        });
    }


    private void setUpRemoteConfig() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(60)
                .build();
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.data_updated_default_value);
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);


        var value = PrefConfig.readIntInPref(this,
                getResources().getString(R.string.KEY_VALUE_SAVE)
                , 0);

        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        boolean updated = task.getResult();

                        int val = (int) mFirebaseRemoteConfig.getLong("data_updated_value");
                        if (value != val) {
                            PrefConfig.writeIntInPref(
                                    TestActivity.this,
                                    val,
                                    getResources().getString(R.string.KEY_VALUE_SAVE)
                            );
//                                getData();
                        }

                        Log.d(TAG, "Config params updated: " + updated + ", val:" + val);
                        return;
                    }
                    Toast.makeText(TestActivity.this, "Fetch failed",
                            Toast.LENGTH_SHORT).show();

                });


    }


//    private void getData() {
//        Retrofit retrofit = ApiClient.getClient();
//        var api = retrofit.create(ApiInterface.class);
//        api.getTodos().enqueue(new retrofit2.Callback<>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseEnglish> call,
//                                   @NonNull Response<ResponseEnglish> response) {
//                var res = response.body();
//                if (res != null) {
//                    var english = res.getEnglish();
//                    convertData(english);
//                }else {
//                    Log.d(TAG, "onResponse: "+response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseEnglish> call, @NonNull Throwable t) {
//                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
//            }
//        });
//    }
//
//    private void convertData(List<Chapter> english) {
//        english.forEach(
//                chapter -> {
//                    var chapters = chapter.getChapters();
//                    var converter = new GradeConverter();
//                    var listOfGradeData = converter.mapToList(chapters); // Grade List similar to GradeDatabase
//                    listOfGradeData.forEach(gradeData -> {
//                        Log.d(TAG, "Subject: " + gradeData.subject);
//                        Log.d(TAG, "Chapter: " + gradeData.chapter);
//                        Log.d(TAG, "grade 1: " + gradeData.grade1);
//                        Log.d(TAG, "grade 2: " + gradeData.grade2);
//                        Log.d(TAG, "grade 3: " + gradeData.grade3);
//                        Log.d(TAG, "grade 4: " + gradeData.grade4);
//                        Log.d(TAG, "grade 5: " + gradeData.grade5);
//                        Log.d(TAG, "isCompleted: " + gradeData.is_completed);
//                        System.out.println();
//                    });
//                }
//        );
//    }


    public void buttonClick(View view) {
        finish();

    }

}