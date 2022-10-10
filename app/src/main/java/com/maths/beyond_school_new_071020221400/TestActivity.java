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
import com.maths.beyond_school_new_071020221400.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_new_071020221400.database.grade_tables.GradesDao;
import com.maths.beyond_school_new_071020221400.databinding.ActivityTestBinding;
import com.maths.beyond_school_new_071020221400.retrofit.ApiClient;
import com.maths.beyond_school_new_071020221400.retrofit.ApiInterface;
import com.maths.beyond_school_new_071020221400.retrofit.Chapter;
import com.maths.beyond_school_new_071020221400.retrofit.model.ResponseSubjects;
import com.maths.beyond_school_new_071020221400.utils.GradeConverter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TestActivity extends AppCompatActivity {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private String TAG = "TestActivity";

    private GradesDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTestBinding binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpRemoteConfig();
        var gradeDatabase = GradeDatabase.getDbInstance(this);
        dao = gradeDatabase.gradesDao();
        getData();
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
        Retrofit retrofit = ApiClient.getClient();
        var api = retrofit.create(ApiInterface.class);
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