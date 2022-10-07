package com.maths.beyond_school_new_071020221400;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maths.beyond_school_new_071020221400.adapters.TablesRecyclerAdapter;
import com.maths.beyond_school_new_071020221400.databinding.ActivityAdditionBinding;
import com.maths.beyond_school_new_071020221400.databinding.ActivityMainBinding;
import com.maths.beyond_school_new_071020221400.extras.ReadText;
import com.maths.beyond_school_new_071020221400.model.Tables;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ReadText.GetResultSpeech, NavigationView.OnNavigationItemSelectedListener {



    TablesRecyclerAdapter tablesRecyclerAdapter;
    private List<Tables> tablesList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();


        tableList = getResources().getStringArray(R.array.table_name);


        binding.toolBar.titleText.setText("Select Table");


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


//        Bundle bundle = new Bundle();
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, " id");
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "name");
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

//        greetingTextView = findViewById(R.id.greetingsTime);

        tablesList = new ArrayList<>();


//        for (int i=1;i<Integer.parseInt(digit);i++){
//            tablesList.add(new Tables(i+1+"",tableList[i-1]));
//        }

//        tablesList.add(new Tables("2", "Table of Two"));
//        tablesList.add(new Tables("3", "Table of Three"));
//        tablesList.add(new Tables("4", "Table of Tour"));
//        tablesList.add(new Tables("5", "Table of Five"));
//        tablesList.add(new Tables("6", "Table of Six"));
//        tablesList.add(new Tables("7", "Table of Seven"));
//        tablesList.add(new Tables("8", "Table of Eight"));
//        tablesList.add(new Tables("9", "Table of Nine"));
//        tablesList.add(new Tables("10", "Table of Ten"));
//        tablesList.add(new Tables("11", "Table of Eleven"));
//        tablesList.add(new Tables("12", "Table of Twelve"));
//        tablesList.add(new Tables("13", "Table of Thirteen"));
//        tablesList.add(new Tables("14", "Table of Fourteen"));
//        tablesList.add(new Tables("15", "Table of Fifteen"));
//        tablesList.add(new Tables("16", "Table of Sixteen"));
//        tablesList.add(new Tables("17", "Table of Seventeen"));
//        tablesList.add(new Tables("18", "Table of Eighteen"));
//        tablesList.add(new Tables("19", "Table of Nineteen"));
//        tablesList.add(new Tables("20", "Table of Twenty"));


        binding.tablesRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));

        tablesRecyclerAdapter = new TablesRecyclerAdapter(tablesList, MainActivity.this);
        binding.tablesRecyclerView.setAdapter(tablesRecyclerAdapter);
        ViewCompat.setNestedScrollingEnabled(binding.tablesRecyclerView, false);

        //retrieveKidsData();

        binding.toolBar.imageViewBack.setOnClickListener(v->onBackPressed());

    }

//    private void retrieveKidsData() {
//
//        kidsDb.collection("users").document(mCurrentUser.getUid()).collection("kids").get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    if (queryDocumentSnapshots.isEmpty()) {
//                        //     Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
//                        Log.i("No_data", "No_data");
//                    } else {
//
//                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
//
//                            KidsData kidsData = queryDocumentSnapshot.toObject(KidsData.class);
//                            kidsData.setKids_id(queryDocumentSnapshot.getId());
//                            // Toast.makeText(this, kidsData.getKids_id()+"", Toast.LENGTH_SHORT).show();
//                            kidsName.setText("Hi, " + kidsData.getName());
//                            kidsNameTextView.setText("Hi ," + kidsData.getName().split(" ")[0]);
//                            // kidsAge.setText("You are "+kidsData.getAge()+" years old");
//                            UtilityFunctions.loadImage(kidsData.getProfile_url(), image_view_profile_view);
//                            UtilityFunctions.loadImage(kidsData.getProfile_url(), image_view_profile_drawer);
//                            Log.i("KidsData", kidsData.getName() + "");
//
//                        }
//                    }
//                });
//    }




    @Override
    public void gettingResultSpeech() {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    @Override
    public void onBackPressed() {
//        Intent a = new Intent(Intent.ACTION_MAIN);
//        a.addCategory(Intent.CATEGORY_HOME);
//        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(a);
    }
}