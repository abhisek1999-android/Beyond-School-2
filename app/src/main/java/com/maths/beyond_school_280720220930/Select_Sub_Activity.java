package com.maths.beyond_school_280720220930;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.adapters.Subject_Adapter;
import com.maths.beyond_school_280720220930.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.database.grade_tables.Grades_data;
import com.maths.beyond_school_280720220930.databinding.ActivitySelectSubBinding;
import com.maths.beyond_school_280720220930.extras.ReadText;
import com.maths.beyond_school_280720220930.model.KidsData;
import com.maths.beyond_school_280720220930.model.SpinnerModel;
import com.maths.beyond_school_280720220930.model.Subject_Model;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.SpeechToTextConverter;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.ArrayList;
import java.util.List;

public class Select_Sub_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<SpinnerModel> drinkModels;
    ActivitySelectSubBinding binding;
    int count = 17, name = R.string.math, subject = R.string.math, subsub = R.string.add;
    String grade="";
    List<Subject_Model> list;
    Subject_Model subject_model;
    GradeDatabase database;
    List<Grades_data> notes;
    Subject_Adapter adapter;
    ActionBarDrawerToggle toggle;
    private TextToSpeckConverter tts;
    private SpeechToTextConverter stt;
    private int REQUEST_RECORD_AUDIO = 1;

   private FirebaseAuth mAuth;
   private FirebaseUser mCurrentUser;
   private FirebaseFirestore kidsDb = FirebaseFirestore.getInstance();
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectSubBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkAudioPermission();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        drinkModels = new ArrayList<>();


        drinkModels.add(new SpinnerModel(true, R.string.math));
        drinkModels.add(new SpinnerModel(false, R.string.add));
        drinkModels.add(new SpinnerModel(false, R.string.sub));
        drinkModels.add(new SpinnerModel(false, R.string.mul));
        drinkModels.add(new SpinnerModel(false, R.string.div));
        drinkModels.add(new SpinnerModel(true, R.string.english));
        drinkModels.add(new SpinnerModel(false, R.string.vocabulary));

       grade = PrefConfig.readIdInPref(getApplicationContext(),"KIDS_GRADE");


       binding.toolBar.userName.setText(grade);

        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, null, R.string.start, R.string.close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.toolBar.imageViewBack.setImageResource(R.drawable.ic_menu2);
        binding.navigationView.setNavigationItemSelectedListener(this);
        binding.toolBar.imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                try {
                    binding.drawerLayout.openDrawer(Gravity.LEFT);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Select_Sub_Activity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        ImageView img = findViewById(R.id.imageView6);
        img.setImageResource(R.drawable.cartoon_image_1);
        findViewById(R.id.dash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
                binding.drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
        findViewById(R.id.closeButton).setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(Gravity.LEFT);
        });
        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                binding.drawerLayout.closeDrawer(Gravity.LEFT);
                finish();
            }
        });
        findViewById(R.id.remind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AlarmAtTime.class));
                binding.drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
        findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                binding.drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });




        ArrayAdapter<SpinnerModel> spinnerAdapter = new ArrayAdapter<SpinnerModel>(this, R.layout.row, drinkModels) {

            @Override
            public boolean isEnabled(int position) {
                return !drinkModels.get(position).isHeader();
            }

            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @SuppressLint("ResourceAsColor")
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    Context mContext = this.getContext();
                    LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.row2, null);
                }

                binding.subject.setText(name);
                binding.subsub.setText(subsub);

                TextView tvName = v.findViewById(R.id.tvName);
                for (int i = position; i >= 0; i--) {
                    SpinnerModel model = drinkModels.get(i);
                    if (model.isHeader()) {
                        name = model.getName();
                        tvName.setTextColor(R.color.primary);
                        break;
                    }
                }
                SpinnerModel model = drinkModels.get(position);
                tvName.setText("Change Subjects");
                subsub = model.getName();
                binding.subject.setText(name);
                if (subsub != R.string.math && subsub != R.string.english) {
                    binding.subsub.setText(subsub);
                    recyler();
                }
                return v;
            }

            @SuppressLint({"ResourceAsColor", "ResourceType"})
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    Context mContext = this.getContext();
                    LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.row, null);
                }

                TextView tvName = v.findViewById(R.id.tvName);
                SpinnerModel model = drinkModels.get(position);
                tvName.setText(model.getName());
                if (model.isHeader()) {
                    tvName.setTextColor(R.color.primary);
                    tvName.setPadding(10, 20, 10, 20);
                    tvName.setBackground(getResources().getDrawable(R.drawable.bottom_border));
                }
                return v;
            }
        };

        binding.spinner2.setAdapter(spinnerAdapter);
        binding.spinner2.setGravity(Gravity.CENTER);
        binding.spinner2.setSelection(1);


        recyler();


    }

    @Override
    protected void onStart() {
        super.onStart();

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
                startActivity(new Intent(getApplicationContext(), Select_Sub_Activity.class));
                finish();
            } else {
                checkAudioPermission();
            }
        }

    }

    private void recyler() {


        database = GradeDatabase.getDbInstance(this);
        notes = database.gradesDao().valus();
        list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Grades_data data = notes.get(i);
            //for mathematics
            if (data.getSubject() == R.string.math) {
                for (String element : data.getGrade()) {
                    if (element.equals(grade)) {
                        String val = getResources().getString(data.getChapter());
                        String[] res = val.split(" ");
                        for (String str : res) {
                            if (str.equals(getResources().getString(subsub))) {
                                list.add(new Subject_Model(data.getChapter(), data.getUrl()));
                            }
                        }
                    } else {

                    }
                }
            }
            //for English practice
            else if (data.getSubject() == R.string.english) {
                for (String element : data.getGrade()) {
                    if (element.equals(grade)) {
                        String val = getResources().getString(data.getChapter());
                        String[] res = val.split(" ");
                        for (String str : res) {
                            if (str.equals(getResources().getString(subsub))) {
                                list.add(new Subject_Model(data.getChapter(), data.getUrl()));
                            }
                        }
                    } else {

                    }
                }
            }
        }
        binding.recylerview.setLayoutManager(new LinearLayoutManager(Select_Sub_Activity.this, LinearLayoutManager.VERTICAL, false));
        adapter = new Subject_Adapter(list, Select_Sub_Activity.this);
        binding.recylerview.setAdapter(adapter);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}