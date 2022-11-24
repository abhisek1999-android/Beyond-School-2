package com.maths.beyond_school_new_071020221400;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maths.beyond_school_new_071020221400.adapters.MathsViewCurriculumRecyclerAdapter;
import com.maths.beyond_school_new_071020221400.adapters.TablesRecyclerAdapter;
import com.maths.beyond_school_new_071020221400.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_new_071020221400.database.grade_tables.Grades_data;
import com.maths.beyond_school_new_071020221400.databinding.ActivityAdditionBinding;
import com.maths.beyond_school_new_071020221400.databinding.ActivityMainBinding;
import com.maths.beyond_school_new_071020221400.extras.ReadText;
import com.maths.beyond_school_new_071020221400.model.Tables;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ReadText.GetResultSpeech, NavigationView.OnNavigationItemSelectedListener {



    private MathsViewCurriculumRecyclerAdapter mathsViewCurriculumRecyclerAdapter;
    private List<Grades_data> mathsList;
    private  String[] tableList;
    private String LOG_TAG = "VoiceRecognitionActivity";
    private int REQUEST_RECORD_AUDIO = 1;
    private static final String CHANNEL_ID = "Default Channel Beyond School";
    private static final String CHANNEL_NAME = "Default Channel Beyond School";
    private static final String CHANNEL_DESC = "Channel for Default Channel Beyond School";
    ReadText readText;
    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    FirebaseFirestore kidsDb = FirebaseFirestore.getInstance();

    private String digit="";

    private String subject="";

    private String videoUrl="";

    private FirebaseAnalytics mFirebaseAnalytics;

    private ActivityMainBinding binding;

    private GradeDatabase gradeDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        binding.toolBar.titleText.setText("Beyond School");
        mathsList=new ArrayList<>();

        binding.toolBar.imageViewBack.setVisibility(View.GONE);

        gradeDatabase=GradeDatabase.getDbInstance(this);
        mathsList=gradeDatabase.gradesDao().values();


        subject=getIntent().getStringExtra("subject");
        digit=getIntent().getStringExtra("max_digit");
        videoUrl=getIntent().getStringExtra("video_url");

        //new line added
        //Setting notification channel................................................................................
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.setSound(null, null);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

            FirebaseCrashlytics.getInstance().sendUnsentReports();

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Beyond_SchoolChannel";
            String description = "Channel for Setting Alarm";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("Beyond_school", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }

        readText = new ReadText(getApplicationContext(), this);
        readText.read("");

        binding.tablesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        mathsViewCurriculumRecyclerAdapter = new MathsViewCurriculumRecyclerAdapter(mathsList, MainActivity.this);
        binding.tablesRecyclerView.setAdapter(mathsViewCurriculumRecyclerAdapter);
        ViewCompat.setNestedScrollingEnabled(binding.tablesRecyclerView, false);

        binding.toolBar.imageViewBack.setOnClickListener(v->onBackPressed());

    }





    @Override
    public void gettingResultSpeech() {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}