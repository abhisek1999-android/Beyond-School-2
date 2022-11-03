package com.maths.beyond_school_280720220930;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.firebase.CallFirebaseForInfo;
import com.maths.beyond_school_280720220930.retrofit.ApiClient;
import com.maths.beyond_school_280720220930.retrofit.ApiInterface;
import com.maths.beyond_school_280720220930.retrofit.model.grade.GradeModel;
import com.maths.beyond_school_280720220930.utils.typeconverters.GradeConverter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Retrofit;

public class SplashScreen extends AppCompatActivity {


    private TextView returnedText, textGot;
    private ImageButton button;
    private ProgressBar progressBar;
    private SpeechRecognizer speech;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";
    private int REQUEST_RECORD_AUDIO = 1;
    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    private String TAG = "SplashScreen";
    FirebaseFirestore kidsDb;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    GradeDatabase database;
    private GradeDatabase gradeDatabase;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        FirebaseApp.initializeApp(this);
        kidsDb = FirebaseFirestore.getInstance();
        String myCustomUri = getIntent().getStringExtra(CalendarContract.EXTRA_CUSTOM_APP_URI);
        try {
            startService(new Intent(getBaseContext(), ClearService.class));
        } catch (Exception e) {
        }

        gradeDatabase = GradeDatabase.getDbInstance(SplashScreen.this);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (PrefConfig.readIdInPref(SplashScreen.this, getResources().getString(R.string.alter_maths)).equals("")) {
            PrefConfig.writeIdInPref(SplashScreen.this, simpleDateFormat.format(new Date()), getResources().getString(R.string.alter_maths));
        }
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

       /* ArrayList arrayList=new ArrayList();
        arrayList.add("gd1");arrayList.add("gd2");

        database= GradeDatabase.getDbInstance(this.getApplicationContext());

        Grades_data grades_data=new Grades_data();
        grades_data.subSubject="abcd";
        grades_data.chapter=1;
        grades_data.subject="esds";
        grades_data.url="oooooo";
        grades_data.grade=arrayList;

        database.gradesDao().insertNotes(grades_data);*/


        //  Toast.makeText(this, mAuth.getCurrentUser().getUid()+"", Toast.LENGTH_SHORT).show();

        if (mCurrentUser == null) {
            startActivity(new Intent(getApplicationContext(), LoginSignupActivity.class));
            finish();
        } else {
            checkUserAlreadyAvailable();
        }
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                /* Create an Intent that will start the Menu-Activity. */
//                if (mCurrentUser == null) {
//                    startActivity(new Intent(getApplicationContext(), LoginSignupActivity.class));
//                    finish();
//                } else {
//                    checkUserAlreadyAvailable();
//                }
//            }
//        }, 1000);
  //      setUpRemoteConfig();
    }

    private void checkUserAlreadyAvailable() {

        if (PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_id)).equals("")) {
            var intent = new Intent(getApplicationContext(), GradeActivity.class);
            intent.putExtra("type", "next");
            startActivity(intent);
            finish();

        } else {
            setUpRemoteConfig();

        }
        
    }


    private void setUpRemoteConfig() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.data_updated_default_value);
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);


        var value = PrefConfig.readIntInPref(this, getResources().getString(R.string.KEY_VALUE_SAVE), 0);

        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        boolean updated = task.getResult();

                        int val = (int) mFirebaseRemoteConfig.getLong("key_update_main_json");
                        String kidsGrade=PrefConfig.readIdInPref(SplashScreen.this,getResources().getString(R.string.kids_grade)).toLowerCase().replace(" ", "");
                        Log.d(TAG, "setUpRemoteConfig: Kids Grade: "+kidsGrade);
                        if (value != val) {
                            PrefConfig.writeIntInPref(SplashScreen.this, val, getResources().getString(R.string.KEY_VALUE_SAVE));

                            getNewData(kidsGrade);

                        }else {
                            startActivity(new Intent(getApplicationContext(), TabbedHomePage.class));
                            finish();
                        }

                        Log.d(TAG, "Config params updated: " + updated + ", val:" + val);
                        return;
                    }
                    Toast.makeText(SplashScreen.this, "Fetch failed", Toast.LENGTH_SHORT).show();
                });

    }

    private void getNewData(String kidsGrade) {
        Log.d(TAG, "getNewData: "+kidsGrade);
        Retrofit retrofit = ApiClient.getClient();
        var api = retrofit.create(ApiInterface.class);
        api.getGradeData(kidsGrade).enqueue(new retrofit2.Callback<>() {

            @Override
            public void onResponse(@NonNull retrofit2.Call<GradeModel> call, @NonNull retrofit2.Response<GradeModel> response) {
                if (response.body() != null) {
                    var list = response.body().getEnglish();
                    mapToGradeModel(list,kidsGrade);
                    Log.d(TAG, "onResponse: StartedUpdating");

                } else {
                    Toast.makeText(SplashScreen.this, "Something wrong occurs", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: StartedUpdatingError");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<com.maths.beyond_school_280720220930.retrofit.model.grade.GradeModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void mapToGradeModel(List<GradeModel.EnglishModel> list,String kidsGrade) {
        list.forEach(subject -> {
            var mapper = new GradeConverter(subject.getSubject());
            var chapterList = mapper.mapToList(subject.getChapters());
            gradeDatabase.gradesDaoUpdated().insertNotes(chapterList);
        });

        CallFirebaseForInfo.upDateActivities(kidsDb, mAuth, PrefConfig.readIdInPref(getApplicationContext(),getResources().getString(R.string.kids_id)),
                kidsGrade, SplashScreen.this, gradeDatabase);

        startActivity(new Intent(getApplicationContext(), TabbedHomePage.class));
        finish();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    public void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


}