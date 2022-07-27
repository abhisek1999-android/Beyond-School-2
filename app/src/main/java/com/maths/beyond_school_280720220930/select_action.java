package com.maths.beyond_school_280720220930;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.maths.beyond_school_280720220930.adapters.NavTableAdapter;
import com.maths.beyond_school_280720220930.model.table_values;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class select_action extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ImageView nav;
    CardView resume_last,practice;
    TextView titleText;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    RecyclerView recycler;
    List<table_values> list=new ArrayList<>();
    NavTableAdapter mAdapter;

    CardView TableWithHint,TableWithoutHint,RandomTable;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String SHARED_PREF_NAME = "beyond";
    private static final String KEY_MULTIPLICANT = "multiplicant";
    private static final String KEY_MULTIPLIER = "multiplier";
    private static final String KEY_STATUS = "status";
    private static final String KEY_RIGHT = "right";
    private static final String KEY_WRONG = "wrong";

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_action);
        //temporary for developing
        //tableName=findViewById(R.id.tableName);
        nav=findViewById(R.id.imageView4);
        TableWithHint=findViewById(R.id.button4);
        TableWithoutHint=findViewById(R.id.button);
        titleText=findViewById(R.id.titleText);
        RandomTable=findViewById(R.id.button7);
        //resume_last=findViewById(R.id.button5);
        practice=findViewById(R.id.button6);
        drawerLayout=findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.navigation_view);
        recycler=findViewById(R.id.recyler);
        Intent intent=getIntent();
        nav.setImageResource(R.drawable.ic_nav);

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

        titleText.setText("You Have Selected Table of - "+String.format("%02d", TableValue)+"");
        titleText.setTextSize(20);

        sharedPreferences=getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //setting drawer
            toggle=new ActionBarDrawerToggle(this,drawerLayout,null,R.string.start,R.string.close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            navigationView.setNavigationItemSelectedListener(this);

            list.add(new table_values(2,"Two(02)"));
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
        mAdapter.notifyDataSetChanged();


        //tableName.setText(String.format("%02d", TableValue)+"");
        TableWithHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(select_action.this,table_with_hint.class);
                intent1.putExtra("ValueOfTable",TableValue);
                intent1.putExtra("count",1);
                intent1.putExtra("status","tablewithhint");
                startActivity(intent1);
            }
        });
        TableWithoutHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(select_action.this,table_questions.class);
                intent2.putExtra("ValueOfTable",TableValue);
                intent2.putExtra("status","tablewithouthint");
                startActivity(intent2);
            }
        });
        RandomTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3=new Intent(select_action.this,Random_questions.class);
                intent3.putExtra("ValueOfTable",TableValue);
                intent3.putExtra("visibility","gone");
                startActivity(intent3);
            }
        });
        practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3=new Intent(select_action.this,Random_questions.class);
                intent3.putExtra("ValueOfTable",TableValue);
                intent3.putExtra("visibility","");
                startActivity(intent3);
            }
        });
        /*resume_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.contains(KEY_STATUS)) {
                    String status=sharedPreferences.getString(KEY_STATUS,null);
                    int multiplicant=sharedPreferences.getInt(KEY_MULTIPLICANT,1);
                    int multiplier = sharedPreferences.getInt(KEY_MULTIPLIER,1);
                    if (TableValue==multiplicant) {
                        if (status.equals("tablewithhint")){
                            Intent intent1=new Intent(select_action.this,table_with_hint.class);
                            intent1.putExtra("ValueOfTable",multiplicant);
                            intent1.putExtra("count",multiplier);
                            intent1.putExtra("status","tablewithhint");
                            startActivity(intent1);

                        }else if (status.equals("tablewithouthint")){
                            Intent intent2=new Intent(select_action.this,table_questions.class);
                            intent2.putExtra("ValueOfTable",TableValue);
                            intent2.putExtra("count",multiplier);
                            intent2.putExtra("status","tablewithouthint");
                            intent2.putExtra("right",sharedPreferences.getInt(KEY_RIGHT,0));
                            intent2.putExtra("wrong",sharedPreferences.getInt(KEY_WRONG,0));
                            startActivity(intent2);
                        }
                    }
                }else {
                    //resume_last.setVisibility(View.GONE);
                    Toast.makeText(select_action.this, "No Operations to Resume", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}