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
import com.maths.beyond_school_280720220930.database.grade_tables.GradeData;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.dialogs.HintDialog;
import com.maths.beyond_school_280720220930.firebase.CallFirebaseForInfo;
import com.maths.beyond_school_280720220930.retrofit.ApiClient;
import com.maths.beyond_school_280720220930.retrofit.ApiClientGrade;
import com.maths.beyond_school_280720220930.retrofit.ApiInterface;
import com.maths.beyond_school_280720220930.retrofit.ApiInterfaceGrade;
import com.maths.beyond_school_280720220930.retrofit.model.grade.GradeModel;
import com.maths.beyond_school_280720220930.retrofit.model.grade.GradeModelNew;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;
import com.maths.beyond_school_280720220930.utils.typeconverters.GradeConverter;
import com.maths.beyond_school_280720220930.utils.typeconverters.LeveGradeConverter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
    private List<GradeData> gradeModelNewList;

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




        if(!UtilityFunctions.checkConnection(SplashScreen.this)){
            displayNoInternetDialog();
        }

        gradeDatabase = GradeDatabase.getDbInstance(SplashScreen.this);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (PrefConfig.readIdInPref(SplashScreen.this, getResources().getString(R.string.alter_maths)).equals("")) {
            PrefConfig.writeIdInPref(SplashScreen.this, simpleDateFormat.format(new Date()), getResources().getString(R.string.alter_maths));
        }
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        if (PrefConfig.readIntInPref(SplashScreen.this,getResources().getString(R.string.build_number))!=BuildConfig.VERSION_CODE)
        {
            gradeDatabase.gradesDaoUpdated().deleteAll();
            String kidsGrade=PrefConfig.readIdInPref(SplashScreen.this,getResources().getString(R.string.kids_grade)).toLowerCase().replace(" ", "");
            if (!kidsGrade.equals(""))
                getNewData(kidsGrade);
        }


        PrefConfig.writeIntInPref(getApplicationContext(), BuildConfig.VERSION_CODE,getResources().getString(R.string.build_number));

//        gradeDatabase.gradesDaoUpdated().deleteAll();
//        String kidsGrade=PrefConfig.readIdInPref(SplashScreen.this,getResources().getString(R.string.kids_grade)).toLowerCase().replace(" ", "");
//        if (!kidsGrade.equals(""))
//            getNewData(kidsGrade);

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

//        if (mCurrentUser == null) {
//            startActivity(new Intent(getApplicationContext(), LoginSignupActivity.class));
//            finish();
//        } else {
//            checkUserAlreadyAvailable();
//        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                /* Create an Intent that will start the Menu-Activity. */

                if (mCurrentUser == null) {
                    startActivity(new Intent(getApplicationContext(), LoginSignupActivity.class));
                    finish();
                } else {
                    checkUserAlreadyAvailable();
                }
            }
        }, 1000);
  //      setUpRemoteConfig();
    }




    private void displayNoInternetDialog() {

        HintDialog hintDialog = new HintDialog(SplashScreen.this);
        hintDialog.setCancelable(false);
        hintDialog.setAlertTitle("Alert");
        hintDialog.setAlertDesciption("No internet connection!");

        hintDialog.actionButton("Close");
        hintDialog.actionButtonBackgroundColor(R.color.primary);
        hintDialog.setOnActionListener(viewId -> {

            switch (viewId.getId()) {

                case R.id.closeButton:
                    completeClose();
                    hintDialog.dismiss();
                    break;
                case R.id.buttonAction:
                    completeClose();
                    hintDialog.dismiss();
                    break;
            }
        });

        hintDialog.show();

    }

    private void completeClose() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
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

//                            startActivity(new Intent(getApplicationContext(), TabbedHomePage.class));
//                            finish();

                        }else {
                            startActivity(new Intent(getApplicationContext(), TabbedHomePage.class));
                            finish();
                        }

                        Log.d(TAG, "Config params updated: " + updated + ", val:" + val);
                        return;
                    }
                   // Toast.makeText(SplashScreen.this, "Fetch failed", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Config params updated: " + "Fetch failed");
                });

    }

    private void getNewData(String kidsGrade) {
        Log.d(TAG, "getNewData: "+kidsGrade);

        Retrofit retrofit = ApiClientGrade.getClient();
        var api = retrofit.create(ApiInterfaceGrade.class);
        gradeModelNewList = new ArrayList<>();
        api.getGradeData(kidsGrade).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GradeModelNew> call, @NonNull Response<GradeModelNew> response) {
                if (response.body() != null) {

                    var s = response.body().getEnglish();
                    for (var i : s) {
                        for (var j : i.getSub_subject()) {
                            var converter = new LeveGradeConverter("English", i.getSubject());
                            var list = converter.mapToList(j.getBlocks());
                            gradeModelNewList.addAll(list);
                        }
                    }
                    for (var i : gradeModelNewList) {
                        Log.d("TAG", "onResponse: " + i);
                    }

                    mapToGradeModel(gradeModelNewList,kidsGrade);
                } else {
                    Toast.makeText(SplashScreen.this, "Something wrong occurs", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GradeModelNew> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void mapToGradeModel(List<GradeData> list,String kidsGrade) {

        gradeDatabase.gradesDaoUpdated().insertNotes(list);

        CallFirebaseForInfo.upDateActivities(kidsDb, mAuth, PrefConfig.readIdInPref(getApplicationContext(),getResources().getString(R.string.kids_id)),
                kidsGrade, SplashScreen.this, gradeDatabase,()->{

                    if (mCurrentUser == null) {
                        startActivity(new Intent(getApplicationContext(), LoginSignupActivity.class));
                        finish();
                    }else{
                        if (PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_id)).equals("")) {
                            var intent = new Intent(getApplicationContext(), GradeActivity.class);
                            intent.putExtra("type", "next");
                            startActivity(intent);
                            finish();

                        } else {

                    startActivity(new Intent(getApplicationContext(), TabbedHomePage.class));
                    finish();

                    }

                    }
                });
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