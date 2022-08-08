package com.maths.beyond_school_280720220930;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.maths.beyond_school_280720220930.adapters.TablesRecyclerAdapter;
import com.maths.beyond_school_280720220930.extras.ReadText;
import com.maths.beyond_school_280720220930.firebase.CallFirebaseForInfo;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;
import com.maths.beyond_school_280720220930.model.KidsData;
import com.maths.beyond_school_280720220930.model.Tables;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ReadText.GetResultSpeech, NavigationView.OnNavigationItemSelectedListener {


    RecyclerView tablesRecyclerView;
    TablesRecyclerAdapter tablesRecyclerAdapter;
    List<Tables> tablesList;
    TextView greetingTextView, kidsName, kidsNameTextView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LinearLayout logLayout, logout,privacyLayout;
    ActionBarDrawerToggle toggle;
    LinearLayout dash, remind, settings;
    private String LOG_TAG = "VoiceRecognitionActivity";
    private int REQUEST_RECORD_AUDIO = 1;
    private static final String CHANNEL_ID = "Default Channel Beyond School";
    private static final String CHANNEL_NAME = "Default Channel Beyond School";
    private static final String CHANNEL_DESC = "Channel for Default Channel Beyond School";
    ReadText readText;
    ImageView dashBoard;
    ImageView menuImageView;
    ImageView closeButton;
    ImageView image_view_profile_view;
    ImageView image_view_profile_drawer;
    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    FirebaseFirestore kidsDb = FirebaseFirestore.getInstance();


    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        privacyLayout=findViewById(R.id.privacyPolicy);

        privacyLayout.setVisibility(View.VISIBLE);
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


        menuImageView = findViewById(R.id.imageViewBack);
        closeButton = findViewById(R.id.closeButton);
        drawerLayout = findViewById(R.id.drawerLayout2);
        navigationView = findViewById(R.id.navigation_view2);
        dash = findViewById(R.id.dash);
        remind = findViewById(R.id.remind);
        settings = findViewById(R.id.settings);
        logLayout = findViewById(R.id.logoutLayout);
        logout = findViewById(R.id.logoutLayout);
        kidsNameTextView = findViewById(R.id.kidsNameTextView);
        image_view_profile_view = findViewById(R.id.image_view_profile_view);
        image_view_profile_drawer = findViewById(R.id.imageView6);

        kidsName = findViewById(R.id.kidsName);


        logLayout.setVisibility(View.VISIBLE);

        //   Toast.makeText(this, mAuth.getCurrentUser().getUid()+"", Toast.LENGTH_SHORT).show();

        toggle = new ActionBarDrawerToggle(this, drawerLayout, null, R.string.start, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        menuImageView.setImageResource(R.drawable.ic_menu2);
        menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                try {
                    drawerLayout.openDrawer(Gravity.LEFT);

                } catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText(MainActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        dash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });

        privacyLayout.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(), PrivacyPolicy.class));
            drawerLayout.closeDrawer(Gravity.LEFT);
        });
        remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AlarmAtTime.class));
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                drawerLayout.closeDrawer(Gravity.LEFT);

            }
        });

        logout.setOnClickListener(v -> {
            mAuth.signOut();
            mCurrentUser = null;
            startActivity(new Intent(getApplicationContext(), LoginSignupActivity.class));
            finish();
        });

        closeButton.setOnClickListener(v -> {
            drawerLayout.closeDrawer(Gravity.LEFT);
        });

        readText = new ReadText(getApplicationContext(), this);
        readText.read("");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        checkAudioPermission();

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, " id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "name");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        tablesRecyclerView = findViewById(R.id.tablesRecyclerView);
        greetingTextView = findViewById(R.id.greetingsTime);

        tablesList = new ArrayList<>();


        tablesList.add(new Tables("2", "Table of Two"));
        tablesList.add(new Tables("3", "Table of Three"));
        tablesList.add(new Tables("4", "Table of Tour"));
        tablesList.add(new Tables("5", "Table of Five"));
        tablesList.add(new Tables("6", "Table of Six"));
        tablesList.add(new Tables("7", "Table of Seven"));
        tablesList.add(new Tables("8", "Table of Eight"));
        tablesList.add(new Tables("9", "Table of Nine"));
        tablesList.add(new Tables("10", "Table of Ten"));
        tablesList.add(new Tables("11", "Table of Eleven"));
        tablesList.add(new Tables("12", "Table of Twelve"));
        tablesList.add(new Tables("13", "Table of Thirteen"));
        tablesList.add(new Tables("14", "Table of Fourteen"));
        tablesList.add(new Tables("15", "Table of Fifteen"));
        tablesList.add(new Tables("16", "Table of Sixteen"));
        tablesList.add(new Tables("17", "Table of Seventeen"));
        tablesList.add(new Tables("18", "Table of Eighteen"));
        tablesList.add(new Tables("19", "Table of Nineteen"));
        tablesList.add(new Tables("20", "Table of Twenty"));


        tablesRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));

        tablesRecyclerAdapter = new TablesRecyclerAdapter(tablesList, MainActivity.this);
        tablesRecyclerView.setAdapter(tablesRecyclerAdapter);
        ViewCompat.setNestedScrollingEnabled(tablesRecyclerView, false);

        greetingTextView.setText(new UtilityFunctions().greeting());


       retrieveKidsData();
        checkUser();


    }

    private void retrieveKidsData() {




        kidsDb.collection("users").document(mCurrentUser.getUid()).collection("kids").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        //     Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
                        Log.i("No_data", "No_data");
                    } else {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {

                            KidsData kidsData = queryDocumentSnapshot.toObject(KidsData.class);
                            kidsData.setKids_id(queryDocumentSnapshot.getId());
                            // Toast.makeText(this, kidsData.getKids_id()+"", Toast.LENGTH_SHORT).show();
                            kidsName.setText("Hi, " + kidsData.getName());
                            kidsNameTextView.setText("Hi ," + kidsData.getName().split(" ")[0]);
                            // kidsAge.setText("You are "+kidsData.getAge()+" years old");
                            UtilityFunctions.loadImage(kidsData.getProfile_url(), image_view_profile_view);
                            UtilityFunctions.loadImage(kidsData.getProfile_url(), image_view_profile_drawer);
                            Log.i("KidsData", kidsData.getName() + "");

                        }
                    }
                });






    }


    public void checkUser() {


        kidsDb.collection("users").get().addOnSuccessListener(queryDocumentSnapshots -> {


            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {

                Log.i("KidsData", queryDocumentSnapshot + "");
            }
        });


//        DocumentReference docRef = kidsDb.collection("users").document("f3GVk0pFdnN1ZSOB2OuHsKhVhg62");
//        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                Log.i("LOG_SNAPSHOT",value.toString()+"");
//
//                if (value.exists()){
//                    Toast.makeText(MainActivity.this, "Exist", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(MainActivity.this, "notExist", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    private void checkAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  // M = 23
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (requestCode == REQUEST_RECORD_AUDIO) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            } else {
                checkAudioPermission();
            }
        }

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
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}