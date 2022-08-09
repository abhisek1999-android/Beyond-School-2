package com.maths.beyond_school_280720220930;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maths.beyond_school_280720220930.adapters.BranchAdapter;
import com.maths.beyond_school_280720220930.model.BranchModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Select_Subject extends AppCompatActivity {
    ViewPager image;
    ImageView gotonext;
    TextView name,age,grade;
    Button math,english;
    ImageView c_img;
    int val=0;
    BranchAdapter adapter;
    List<BranchModel> list;
    String name1,img,age1,grade1;
    private int NUM_PAGES=5;
    int currentPage=0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_subject);
        image=findViewById(R.id.imageView7);
        gotonext=findViewById(R.id.gotonext);
        name=findViewById(R.id.name);
        age=findViewById(R.id.age);
        grade=findViewById(R.id.grade);
        c_img=findViewById(R.id.c_img);

        name1=getIntent().getStringExtra("name");
        age1=getIntent().getStringExtra("age");
        img=getIntent().getStringExtra("image");
        grade1=getIntent().getStringExtra("grade");

        name.setText(name1);
        grade.setText(grade1);
        try {
            Glide.with(this).load(img).placeholder(R.drawable.cartoon_image_1).into(c_img);

        }catch (Exception e){
            e.printStackTrace();
        }

        list=new ArrayList<>();
        list.add(new BranchModel(R.drawable.math1));
        list.add(new BranchModel(R.drawable.eng));
        adapter=new BranchAdapter(getApplicationContext(),list);
        image.setCurrentItem(1,true);
        image.setAdapter(adapter);

        gotonext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPage == NUM_PAGES-1) {
                    currentPage = 0;
                }
                image.setCurrentItem(currentPage++, true);
               currentPage++;
            }
        });

    }
}