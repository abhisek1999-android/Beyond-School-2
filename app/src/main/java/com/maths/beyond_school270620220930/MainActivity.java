package com.maths.beyond_school270620220930;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.maths.beyond_school270620220930.adapters.TablesRecyclerAdapter;
import com.maths.beyond_school270620220930.extras.UtilityFunctions;
import com.maths.beyond_school270620220930.model.Tables;
import com.maths.beyond_school270620220930.notification.StickyNotification;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    RecyclerView tablesRecyclerView;
    TablesRecyclerAdapter tablesRecyclerAdapter;
    List<Tables> tablesList;
    TextView greetingTextView;
    private static final String CHANNEL_ID="Default Channel Beyond School";
    private static final String CHANNEL_NAME="Default Channel Beyond School";
    private static final String CHANNEL_DESC="Channel for Default Channel Beyond School";

private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //Setting notification channel................................................................................
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channel=new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);


        }

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID," id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "name");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        tablesRecyclerView=findViewById(R.id.tablesRecyclerView);
        greetingTextView=findViewById(R.id.greetingsTime);

        tablesList=new ArrayList<>();


        tablesList.add(new Tables("2","Table of two"));
        tablesList.add(new Tables("3","Table of three"));
        tablesList.add(new Tables("4","Table of four"));
        tablesList.add(new Tables("5","Table of five"));
        tablesList.add(new Tables("6","Table of six"));
        tablesList.add(new Tables("7","Table of seven"));
        tablesList.add(new Tables("8","Table of eight"));
        tablesList.add(new Tables("9","Table of nine"));
        tablesList.add(new Tables("10","Table of ten"));
        tablesList.add(new Tables("11","Table of eleven"));
        tablesList.add(new Tables("12","Table of twelve"));
        tablesList.add(new Tables("13","Table of thirteen"));
        tablesList.add(new Tables("14","Table of fourteen"));
        tablesList.add(new Tables("15","Table of fifteen"));
        tablesList.add(new Tables("16","Table of sixteen"));
        tablesList.add(new Tables("17","Table of seventeen"));
        tablesList.add(new Tables("18","Table of eighteen"));
        tablesList.add(new Tables("19","Table of nineteen"));
        tablesList.add(new Tables("20","Table of twenty"));



        tablesRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));

        tablesRecyclerAdapter=new TablesRecyclerAdapter(tablesList,getApplicationContext());
        tablesRecyclerView.setAdapter(tablesRecyclerAdapter);
        ViewCompat.setNestedScrollingEnabled(tablesRecyclerView, false);

        greetingTextView.setText(new UtilityFunctions().greeting());
    }
}