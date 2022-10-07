package com.maths.beyond_school_new_071020221400;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maths.beyond_school_new_071020221400.SP.PrefConfig;
import com.maths.beyond_school_new_071020221400.database.grade_tables.GradeDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    FirebaseFirestore kidsDb;

    GradeDatabase database;

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
        try{startService(new Intent(getBaseContext(), ClearService.class));}catch (Exception e){}

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");

        if (PrefConfig.readIdInPref(SplashScreen.this,getResources().getString(R.string.alter_maths)).equals("")){
            PrefConfig.writeIdInPref(SplashScreen.this,simpleDateFormat.format(new Date()),getResources().getString(R.string.alter_maths));}
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

    }

    private void checkUserAlreadyAvailable() {

        if (PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_id)).equals("")) {
            var intent = new Intent(getApplicationContext(), KidsInfoActivity.class);
            intent.putExtra("type", "next");
            startActivity(intent);
            finish();

        } else {
            startActivity(new Intent(getApplicationContext(), HomeScreen.class));
//            startActivity(new Intent(getApplicationContext(), EnglishActivity.class));
            finish();
        }

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