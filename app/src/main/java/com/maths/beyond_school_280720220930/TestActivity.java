package com.maths.beyond_school_280720220930;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.maths.beyond_school_280720220930.adapters.ChaptersRecyclerAdapter;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeData;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.databinding.ActivityTestBinding;
import com.maths.beyond_school_280720220930.retrofit.ApiClientNew;
import com.maths.beyond_school_280720220930.retrofit.ApiInterfaceNew;
import com.maths.beyond_school_280720220930.retrofit.model.grade.GradeModelNew;
import com.maths.beyond_school_280720220930.utils.typeconverters.GradeConverter;
import com.maths.beyond_school_280720220930.utils.typeconverters.LeveGradeConverter;
import com.razorpay.Plan;
import com.razorpay.Subscription;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TestActivity extends AppCompatActivity {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private String TAG = "TestActivity";

    private GradeDatabase gradeDatabase;
    private ActivityTestBinding binding;
    private ChaptersRecyclerAdapter chaptersRecyclerAdapter;
    private List<GradeData> chapterList;
    private List<Plan> plansList;
    private Subscription subscription;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;

    private List<GradeData> gradeModelNewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        gradeDatabase = GradeDatabase.getDbInstance(this);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        //   setUpRemoteConfigPayment();
        // setUpRemoteConfigTrial();

//        CallFirebaseForInfo.getSubscriptionStatus(firebaseFirestore,mAuth,TestActivity.this,()->{
//            Log.d(TAG, "onCreate: Saved");
//        });


        getNewData();

    }


    private void getNewData() {
        Retrofit retrofit = ApiClientNew.getClient();
        var api = retrofit.create(ApiInterfaceNew.class);
        gradeModelNewList = new ArrayList<>();
        api.getGradeData().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GradeModelNew> call, @NonNull Response<GradeModelNew> response) {
                if (response.body() != null) {

                    var s = response.body().getEnglish();
                    for (var i : s) {
                        for (var j : i.getSub_subject()) {
                            var converter = new LeveGradeConverter("English",i.getSubject());
                            var list = converter.mapToList(j.getBlocks());
                            gradeModelNewList.addAll(list);
                        }
                    }
                    for (var i : gradeModelNewList) {
                        Log.d(TAG, "onResponse: " + i);
                    }

                    mapToGradeModel(gradeModelNewList);
                } else {
                    Toast.makeText(TestActivity.this, "Something wrong occurs", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GradeModelNew> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }


    private void mapToGradeModel(List<GradeData> list) {
            gradeDatabase.gradesDaoUpdated().insertNotes(list);
    }

    private void setUpRemoteConfigPayment() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.data_updated_default_value);
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        boolean updated = task.getResult();

                        int val = (int) mFirebaseRemoteConfig.getLong("price_value");

                        Log.d(TAG, "Config params updated: payment" + updated + ", val:" + val);
                        return;
                    }
                    // Toast.makeText(SplashScreen.this, "Fetch failed", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Config params updated: " + "Fetch failed");
                });

    }

    private void setUpRemoteConfigTrial() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.data_updated_default_value);
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        boolean updated = task.getResult();

                        int val = (int) mFirebaseRemoteConfig.getLong("trial_period");

                        Log.d(TAG, "Config params updated: trial " + updated + ", val:" + val);
                        return;
                    }
                    // Toast.makeText(SplashScreen.this, "Fetch failed", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Config params updated: " + "Fetch failed");
                });

    }


}