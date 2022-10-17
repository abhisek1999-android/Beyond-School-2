package com.maths.beyond_school_new_071020221400;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.maths.beyond_school_new_071020221400.SP.PrefConfig;
import com.maths.beyond_school_new_071020221400.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_new_071020221400.database.english.converters.GrammarNounConvert;
import com.maths.beyond_school_new_071020221400.database.english.grammer.model.GrammarType;
import com.maths.beyond_school_new_071020221400.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_new_071020221400.database.grade_tables.GradesDao;
import com.maths.beyond_school_new_071020221400.databinding.ActivityTestBinding;
import com.maths.beyond_school_new_071020221400.retrofit.ApiClient;
import com.maths.beyond_school_new_071020221400.retrofit.ApiInterface;
import com.maths.beyond_school_new_071020221400.retrofit.Chapter;
import com.maths.beyond_school_new_071020221400.retrofit.ResponseClass;
import com.maths.beyond_school_new_071020221400.retrofit.model.ResponseSubjects;
import com.maths.beyond_school_new_071020221400.retrofit.noun.SubjectContent;
import com.maths.beyond_school_new_071020221400.utils.GradeConverter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TestActivity extends AppCompatActivity {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private String TAG = "TestActivity";

    private GradesDao dao;
    private ApiInterface api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTestBinding binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpRemoteConfig();
        var gradeDatabase = GradeDatabase.getDbInstance(this);
        dao = gradeDatabase.gradesDao();
        Retrofit retrofit = ApiClient.getClient();
        api = retrofit.create(ApiInterface.class);
        getData();
//        getGrammarData();
        getIrregularPluralNoun();
    }

    private void getIrregularPluralNoun() {
        api.getIrregularPluralNoun().enqueue(new Callback<ResponseClass<SubjectContent>>() {
            @Override
            public void onResponse(Call<ResponseClass<SubjectContent>> call, Response<ResponseClass<SubjectContent>> response) {

                var nounResponse = response.body();
                var chapter = nounResponse.getSubjectContent();
                if (chapter != null) {
                    var converter = new GrammarNounConvert();
                    var metaData = chapter.getMeta().toString();
                    Log.d(TAG, "onResponse: " + chapter.getChapter_name());
                    Log.d(TAG, "onResponse: " + metaData);
                    Log.d(TAG, "----------------------------------------");
                    Log.d(TAG, "----------------------------------------");
                    var nounList = converter.mapToList(chapter.getContent());
                    for (var noun : nounList) {
                        Log.d(TAG, "onResponse: " + noun.toString());
                    }
                    var englishDatabase = EnglishGradeDatabase.getDbInstance(TestActivity.this);
                    englishDatabase.grammarDao().insert(new GrammarType(chapter.getChapter_name(), nounList));
                } else Log.d(TAG, "onResponse: null " + response.code());
            }

            @Override
            public void onFailure(Call<ResponseClass<SubjectContent>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void getGrammarData() {
        api.getIndefiniteNoun().enqueue(new Callback<ResponseClass<SubjectContent>>() {
            @Override
            public void onResponse(Call<ResponseClass<SubjectContent>> call, Response<ResponseClass<SubjectContent>> response) {

                var nounResponse = response.body();
                var chapter = nounResponse.getSubjectContent();
                if (chapter != null) {
                    var converter = new GrammarNounConvert();
                    var metaData = chapter.getMeta().toString();
                    Log.d(TAG, "onResponse: " + chapter.getChapter_name());
                    Log.d(TAG, "onResponse: " + metaData);
                    Log.d(TAG, "----------------------------------------");
                    Log.d(TAG, "----------------------------------------");
                    var nounList = converter.mapToList(chapter.getContent());
                    for (var noun : nounList) {
                        Log.d(TAG, "onResponse: " + noun.toString());
                    }
                    var englishDatabase = EnglishGradeDatabase.getDbInstance(TestActivity.this);
                    englishDatabase.grammarDao().insert(new GrammarType(chapter.getChapter_name(), nounList));
                } else Log.d(TAG, "onResponse: null " + response.code());
            }

            @Override
            public void onFailure(Call<ResponseClass<SubjectContent>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
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
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();

                            int val = (int) mFirebaseRemoteConfig.getLong("data_updated_value");
                            if (value != val) {
                                PrefConfig.writeIntInPref(
                                        TestActivity.this,
                                        val,
                                        getResources().getString(R.string.KEY_VALUE_SAVE)
                                );
                                getData();
                            }

                            Log.d(TAG, "Config params updated: " + updated + ", val:" + val);
                            return;
                        }
                        Toast.makeText(TestActivity.this, "Fetch failed",
                                Toast.LENGTH_SHORT).show();

                    }
                });


    }


    private void getData() {

        api.getTodos().enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseSubjects> call,
                                   @NonNull Response<ResponseSubjects> response) {
                var res = response.body();
                if (res != null) {
                    var english = res.getEnglish();
                    var maths = res.getMaths();
                    convertDataEnglish(english, maths);
                } else {
                    Log.d(TAG, "onResponse: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseSubjects> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void convertDataEnglish(List<Chapter> english, List<Chapter> maths) {
        english.forEach(
                chapter -> {
                    var chapters = chapter.getChapters();
                    var converter = new GradeConverter();
                    var listOfGradeData = converter.mapToList(chapters); // Grade List similar to GradeDatabase
                    dao.insetAll(listOfGradeData);
                }
        );
        maths.forEach(chapter -> {
            var chapters = chapter.getChapters();
            var converter = new GradeConverter();
            var listOfGradeData = converter.mapToList(chapters); // Grade List similar to GradeDatabase
            dao.insetAll(listOfGradeData);
        });
    }


    public void buttonClick(View view) {
        finish();

    }

}