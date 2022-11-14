package com.maths.beyond_school_280720220930;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.adapters.ChaptersRecyclerAdapter;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeData;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.databinding.ActivityTestBinding;
import com.maths.beyond_school_280720220930.firebase.CallFirebaseForInfo;
import com.maths.beyond_school_280720220930.payments.CreateSubscription;
import com.maths.beyond_school_280720220930.payments.FetchSubscriptionPlans;
import com.maths.beyond_school_280720220930.payments.FetchSubscriptionStatus;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.razorpay.Plan;
import com.razorpay.Subscription;

import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class TestActivity extends AppCompatActivity  {

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