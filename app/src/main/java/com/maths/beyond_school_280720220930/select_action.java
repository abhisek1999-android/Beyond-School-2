package com.maths.beyond_school_280720220930;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.adapters.NavTableAdapter;
import com.maths.beyond_school_280720220930.model.KidsData;
import com.maths.beyond_school_280720220930.model.table_values;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;


import java.util.ArrayList;
import java.util.List;



public class select_action extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ImageView nav,closeButton;
    CardView resume_last,practice;
    TextView titleText;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    RecyclerView recycler;
    LinearLayout dash,remind,settings,home;
    List<table_values> list=new ArrayList<>();
    NavTableAdapter mAdapter;

    CardView TableWithHint,TableWithoutHint,RandomTable;
    TextView greetingTextView, kidsName, kidsNameTextView;
    FirebaseFirestore kidsDb = FirebaseFirestore.getInstance();
    int isHide=0;
    SharedPreferences sharedPreferences;
    ImageView image_view_profile_view;
    ImageView image_view_profile_drawer;
    SharedPreferences.Editor editor;
    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    private static final String SHARED_PREF_NAME = "beyond";
    private static final String KEY_MULTIPLICANT = "multiplicant";

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_action);
        //temporary for developing
        //tableName=findViewById(R.id.tableName);
        nav=findViewById(R.id.imageViewBack);
        TableWithHint=findViewById(R.id.button4);
        TableWithoutHint=findViewById(R.id.button);
        titleText=findViewById(R.id.titleText);
        RandomTable=findViewById(R.id.button7);
        resume_last=findViewById(R.id.resume);
        practice=findViewById(R.id.button6);
        drawerLayout=findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.navigation_view);
        //recycler=findViewById(R.id.recyler);
        dash=findViewById(R.id.dash);
        remind=findViewById(R.id.remind);
        home=findViewById(R.id.home);
        home.setVisibility(View.VISIBLE);
        settings=findViewById(R.id.settings);
        Intent intent=getIntent();
        nav.setImageResource(R.drawable.ic_nav);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();


        kidsName = findViewById(R.id.kidsName);
        image_view_profile_drawer = findViewById(R.id.imageView6);

        closeButton=findViewById(R.id.closeButton);

        /*resume_last.setTranslationX(-800);
        resume_last.setAlpha(1);*/
        TableWithoutHint.setTranslationX(-800);
        TableWithoutHint.setAlpha(1);
        RandomTable.setTranslationX(800);
        RandomTable.setAlpha(1);
        practice.setTranslationX(-800);
        practice.setAlpha(1);
        TableWithHint.setTranslationX(800);
        TableWithHint.setAlpha(1);
        //resume_last.animate().translationX(0).alpha(1).setDuration(450).setStartDelay(250).start();
        TableWithoutHint.animate().translationX(0).alpha(1).setDuration(450).setStartDelay(200).start();
        RandomTable.animate().translationX(0).alpha(1).setDuration(450).setStartDelay(200).start();
        practice.animate().translationX(0).alpha(1).setDuration(450).setStartDelay(200).start();
        TableWithHint.animate().translationX(0).alpha(1).setDuration(450).setStartDelay(200).start();

        int TableValue=intent.getIntExtra("value",0);

        titleText.setText("Table of - "+String.format("%02d", TableValue)+"");
        //titleText.setTextSize(20);

        sharedPreferences=getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.putInt(KEY_MULTIPLICANT,TableValue);
        editor.apply();

        //setting drawer
            toggle=new ActionBarDrawerToggle(this,drawerLayout,null,R.string.start,R.string.close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            navigationView.setNavigationItemSelectedListener(this);

            /*list.add(new table_values(2,"Two(02)"));
            list.add(new table_values(3,"Three(03)"));
            list.add(new table_values(4,"Four(04)"));
            list.add(new table_values(5,"Five(05)"));
            list.add(new table_values(6,"Six(06)"));
            list.add(new table_values(7,"Seven(07)"));
            list.add(new table_values(8,"Eight(08)"));
            list.add(new table_values(9,"Nine(09)"));
            list.add(new table_values(10,"Ten(10)"));
            list.add(new table_values(11,"Eleven(11)"));
            list.add(new table_values(12,"Twelve(12)"));
            list.add(new table_values(13,"Thirteen(13)"));
            list.add(new table_values(14,"Fourteen(14)"));
            list.add(new table_values(15,"Fifteen(15)"));
            list.add(new table_values(16,"Sixteen(16)"));
            list.add(new table_values(17,"Seventeen(17)"));
            list.add(new table_values(18,"Eighteen(18)"));
            list.add(new table_values(19,"Nineteen(19)"));
            list.add(new table_values(20,"Twenty(20)"));
        mAdapter = new NavTableAdapter(list,select_action.this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(mLayoutManager);
        recycler.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();*/


        isHide=getIntent().getIntExtra("isHide",0);


        if (PrefConfig.readIntInPref(getApplicationContext(),getResources().getString(R.string.multiplicand))>0 && isHide==0) {
        resume_last.setVisibility(View.VISIBLE);}
        else{
            resume_last.setVisibility(View.GONE);
        }


        dash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                drawerLayout.closeDrawer(Gravity.LEFT);
                finish();
            }
        });
        remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AlarmAtTime.class));
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });

        //tableName.setText(String.format("%02d", TableValue)+"");
        TableWithHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(select_action.this,table_with_hint.class);
                intent1.putExtra("ValueOfTable",TableValue);
                intent1.putExtra("count",1);
                intent1.putExtra("status","tableWithHint");
                startActivity(intent1);
            }
        });
        TableWithoutHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(select_action.this,table_questions.class);
                intent2.putExtra("ValueOfTable",TableValue);
                intent2.putExtra("count",1);
                intent2.putExtra("status","tableWithoutHint");
                intent2.putExtra("right",0);
                intent2.putExtra("wrong",0);
                startActivity(intent2);
            }
        });
        RandomTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3=new Intent(select_action.this,Random_questions.class);
                intent3.putExtra("ValueOfTable",TableValue);
                intent3.putExtra("status","tableRandom");
                intent3.putExtra("count",1);
                intent3.putExtra("right",0);
                intent3.putExtra("wrong",0);
                startActivity(intent3);
            }
        });
        practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3=new Intent(select_action.this,Random_questions.class);
                intent3.putExtra("ValueOfTable",TableValue);
                intent3.putExtra("status","practice");
                startActivity(intent3);
            }
        });
     resume_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    String status = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.status));
                    int multiplicant = PrefConfig.readIntInPref(getApplicationContext(), getResources().getString(R.string.multiplicand));
                    int multiplier = PrefConfig.readIntInPref(getApplicationContext(), getResources().getString(R.string.multiplier));

                    Intent intent=new Intent(getApplicationContext(), select_action.class);
                    intent.putExtra("value",multiplicant);
                    intent.putExtra("isHide",1);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

//                    if (status.equals("tableWithHint")) {
//                        Intent intent1 = new Intent(select_action.this, table_with_hint.class);
//                        intent1.putExtra("ValueOfTable", multiplicant);
//                        intent1.putExtra("count", multiplier);
//                        intent1.putExtra("status", "tableWithHint");
//                        startActivity(intent1);
//
//                    } else if (status.equals("tableWithoutHint")) {
//                        Intent intent2 = new Intent(select_action.this, table_questions.class);
//                        intent2.putExtra("ValueOfTable", multiplicant);
//                        intent2.putExtra("count", multiplier);
//                        intent2.putExtra("status", "tableWithoutHint");
//                        intent2.putExtra("right", PrefConfig.readIntInPref(getApplicationContext(), getResources().getString(R.string.right)));
//                        intent2.putExtra("wrong", PrefConfig.readIntInPref(getApplicationContext(), getResources().getString(R.string.wrong)));
//                        startActivity(intent2);
//                    } else if (status.equals("tableRandom")) {
//                        Intent intent2 = new Intent(select_action.this, table_questions.class);
//                        intent2.putExtra("ValueOfTable", multiplicant);
//                        intent2.putExtra("status", "tableRandom");
//                        intent2.putExtra("right", PrefConfig.readIntInPref(getApplicationContext(), getResources().getString(R.string.right)));
//                        intent2.putExtra("wrong", PrefConfig.readIntInPref(getApplicationContext(), getResources().getString(R.string.wrong)));
//                        startActivity(intent2);
//                    }

            }
        });
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                try {
                    drawerLayout.openDrawer(Gravity.LEFT);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(select_action.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        closeButton.setOnClickListener(v->{
            drawerLayout.closeDrawer(Gravity.LEFT);
        });

        retrieveKidsData();
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
                            kidsName.setText("Hi ," + kidsData.getName().split(" ")[0]);
                            // kidsAge.setText("You are "+kidsData.getAge()+" years old");

                            UtilityFunctions.loadImage(kidsData.getProfile_url(), image_view_profile_drawer);
                            Log.i("KidsData", kidsData.getName() + "");

                        }
                    }

                });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent3=new Intent(select_action.this,MainActivity.class);
        startActivity(intent3);
        finish();

    }
}