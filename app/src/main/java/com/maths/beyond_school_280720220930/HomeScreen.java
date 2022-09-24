package com.maths.beyond_school_280720220930;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.adapters.SectionSubSubjectRecyclerAdapter;
import com.maths.beyond_school_280720220930.adapters.SubjectRecyclerAdapter;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.database.grade_tables.Grades_data;
import com.maths.beyond_school_280720220930.databinding.ActivityHomeScreenBinding;
import com.maths.beyond_school_280720220930.dialogs.HintDialog;
import com.maths.beyond_school_280720220930.extras.CustomProgressDialogue;
import com.maths.beyond_school_280720220930.model.SectionSubSubject;
import com.maths.beyond_school_280720220930.model.SubSubject;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class HomeScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityHomeScreenBinding binding;
    private List<String> mathSub;
    private List<String> engSub;
    private String kidsGrade = "";
    private String kidsName = "";
    private GradeDatabase gradeDatabase;
    private List<Grades_data> subMathsData;
    private List<Grades_data> subEngData;
    private SubjectRecyclerAdapter subjectRecyclerAdapter;
    String[] math = {"Addition", "Subtraction", "Multiplication Tables", "Division"};
    String[] eng = {"Vocabulary", "Spelling_Objects", "Spelling_CommonWords", "Grammar"};
    private List<SubSubject> subMathList;
    private List<SubSubject> subEngList;
    private List<SectionSubSubject> sectionList;
    private int[] resMath = {R.drawable.ic_addition, R.drawable.ic_sub, R.drawable.ic_mul, R.drawable.ic_division};
    private int[] resEng = {R.drawable.ic_vocab, R.drawable.ic_spell, R.drawable.ic_spell, R.drawable.ic_spell};

    private String[] tableList;
    private SectionSubSubjectRecyclerAdapter sectionSubSubjectRecyclerAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private int startIndex = 0;
    private FirebaseFirestore kidsDb = FirebaseFirestore.getInstance();
    ActionBarDrawerToggle toggle;
    private int REQUEST_RECORD_AUDIO = 1;
    private List<String> chapterListEng;
    private List<String> chapterListMath;
    private CustomProgressDialogue customProgressDialogue;
    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();


        startIndex = PrefConfig.readIntDInPref(HomeScreen.this, getResources().getString(R.string.alter_maths_value));


        customProgressDialogue = new CustomProgressDialogue(HomeScreen.this);
        mathSub = new ArrayList<>();
        engSub = new ArrayList<>();

        subMathsData = new ArrayList<>();
        subEngData = new ArrayList<>();

        subMathList = new ArrayList<>();
        subEngList = new ArrayList<>();
        sectionList = new ArrayList<>();

        tableList = getResources().getStringArray(R.array.table_name);
        subjectRecyclerAdapter = new SubjectRecyclerAdapter(subMathsData, HomeScreen.this);


        kidsGrade = PrefConfig.readIdInPref(HomeScreen.this, getResources().getString(R.string.kids_grade));

        kidsName = PrefConfig.readIdInPref(HomeScreen.this, getResources().getString(R.string.kids_name));
        gradeDatabase = GradeDatabase.getDbInstance(HomeScreen.this);

        mathSub = Arrays.asList(math);
        engSub = Arrays.asList(eng);


        mBottomSheetBehavior = BottomSheetBehavior.from(binding.extLayout.permissionCard);

        checkAudioPermission();
        binding.yourTask.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MathsTutorialActivity.class)));

        binding.yourProgressLayout.setOnClickListener(v -> {
            setSubSubjectProgress();
        });

        uiChnages();
        //    getUiData(true);

        logSent();
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }


    public void logSent() {
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("AppOpened");
    }

    private void checkAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  // M = 23
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                permissionPadController();

            }
        }
    }


    @SuppressLint("MissingSuperCall")
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (requestCode == REQUEST_RECORD_AUDIO) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                finish();
            } else {
                checkAudioPermission();
            }
        }

    }

    private void setSubSubjectProgress() {


        subMathList.clear();
        subEngList.clear();
        sectionList.clear();

        for (int i = 0; i < math.length; i++) {
            int total = UtilityFunctions.gettingSubSubjectData(gradeDatabase, kidsGrade, math[i].split(" ")[0], true).size();
            int completed = UtilityFunctions.gettingSubSubjectData(gradeDatabase, kidsGrade, math[i], false).size();
            if (!math[i].equals("Multiplication Tables")) {
                subMathList.add(new SubSubject(math[i], total, completed, resMath[i]));
            } else {
                Log.i("Mul_Data", total + "," + UtilityFunctions.gettingSubSubjectData(gradeDatabase, kidsGrade, math[i].split(" ")[0], false));
                int mulUpto = PrefConfig.readIntInPref(getApplicationContext(), getResources().getString(R.string.multiplication_upto));
                total = Integer.parseInt(UtilityFunctions.gettingSubSubjectData(gradeDatabase, kidsGrade, math[i].split(" ")[0], true).get(total - 1).chapter.split(" ")[3]);
                if (mulUpto == 1)
                    subMathList.add(new SubSubject(math[i], total, 0, resMath[i]));
                else
                    subMathList.add(new SubSubject(math[i], total, mulUpto, resMath[i]));
            }
        }

        sectionList.add(new SectionSubSubject("Mathematics", subMathList));

        for (int i = 0; i < eng.length; i++) {
            int total = UtilityFunctions.gettingSubSubjectData(gradeDatabase, kidsGrade, eng[i], true).size();
            int completed = UtilityFunctions.gettingSubSubjectData(gradeDatabase, kidsGrade, eng[i], false).size();
            subEngList.add(new SubSubject(eng[i], total, completed, resEng[i]));
        }

        sectionList.add(new SectionSubSubject("English", subEngList));


        final AlertDialog.Builder alert = new AlertDialog.Builder(HomeScreen.this);
        View mView = getLayoutInflater().inflate(R.layout.progress_report_dialog, null);

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        RecyclerView progressView = mView.findViewById(R.id.progressRecyclerView);
        ImageView closeButton = mView.findViewById(R.id.closeButton);


        progressView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        sectionSubSubjectRecyclerAdapter = new SectionSubSubjectRecyclerAdapter(sectionList, HomeScreen.this, alertDialog);
        progressView.setAdapter(sectionSubSubjectRecyclerAdapter);


        try {
            alertDialog.show();
        } catch (Exception e) {

        }

        closeButton.setOnClickListener(v -> alertDialog.dismiss());

    }


    private void uiChnages() {


        if (PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.log_check)).equals("")) {
            PrefConfig.writeIdInPref(getApplicationContext(), "true", getResources().getString(R.string.log_check));
        }
        dateChecking();


        binding.tool.toolBar.kidsName.setText("Hi ," + PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_name)));

        UtilityFunctions.loadImage(PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_profile_url)), binding.tool.toolBar.imageView6);

        Log.i("ImageUrl", PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_profile_url)));

        binding.tool.logoutLayout.setVisibility(View.VISIBLE);

        binding.tool.logout.setOnClickListener(v -> {
            mAuth.signOut();
            mCurrentUser = null;
            PrefConfig.writeIdInPref(getApplicationContext(), "", getResources().getString(R.string.kids_id));
            Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
            startActivity(intent);
            finish();
        });

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
                    Toast.makeText(HomeScreen.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
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


        binding.tool.toolBar.imageView6.setOnClickListener(v -> {


            if (!kidsName.equals("Kids Name")) {
                goToProfile("update");
            } else {
                displayAddProfileAlertDialog();
            }
            binding.drawerLayout.closeDrawer(Gravity.LEFT);


        });

        binding.tool.dash.setVisibility(View.GONE);

        findViewById(R.id.remind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AlarmAtTime.class));
                binding.drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });

        binding.tool.privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PrivacyPolicy.class));
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
    }


    private void permissionPadController() {


        if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        } else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        binding.extLayout.acceptPermission.setOnClickListener(v -> {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });
        binding.extLayout.rejectPermission.setOnClickListener(v -> {
            completeClose();
        });
        // doing some stuffs when bottom sheet is opening or closing like roatting button icon............................
        mBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {


            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {


            }
        });
    }


    private void displayAddProfileAlertDialog() {

        HintDialog hintDialog = new HintDialog(HomeScreen.this);
        hintDialog.setCancelable(true);
        hintDialog.setAlertTitle("Add Profile");
        hintDialog.setAlertDesciption("Hey, Kids profile is not added yet !!\nClick the button below to add the profile.");

        hintDialog.actionButton("Add Profile");
        hintDialog.actionButtonBackgroundColor(R.color.primary);
        hintDialog.setOnActionListener(viewId -> {

            switch (viewId.getId()) {

                case R.id.closeButton:
                    hintDialog.dismiss();
                    break;
                case R.id.buttonAction:
                    goToProfile("update_new");
                    hintDialog.dismiss();
                    break;
            }
        });

        hintDialog.show();

    }

    private void goToProfile(String type) {
        Intent intent = new Intent(getApplicationContext(), KidsInfoActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);

    }

    private void dateChecking() {


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (!PrefConfig.readIdInPref(HomeScreen.this, getResources().getString(R.string.alter_maths)).equals(simpleDateFormat.format(new Date()))) {

            // Added functionality when we got a new date

            if (kidsName.equals("Kids Name")) {
                displayAddProfileAlertDialog();
            }

            PrefConfig.writeIdInPref(HomeScreen.this, simpleDateFormat.format(new Date()), getResources().getString(R.string.alter_maths));
            if (PrefConfig.readIntDInPref(HomeScreen.this, getResources().getString(R.string.alter_maths_value)) == 0) {
                PrefConfig.writeIntDInPref(HomeScreen.this, 2, getResources().getString(R.string.alter_maths_value));
                startIndex = 2;
            } else {
                PrefConfig.writeIntDInPref(HomeScreen.this, 0, getResources().getString(R.string.alter_maths_value));
                startIndex = 0;
            }
            getUiData(true);
            customProgressDialogue.show();
        } else {

            Log.i("List_data sd", PrefConfig.readNormalListInPref(getApplicationContext(), getResources().getString(R.string.saved_english_value)) + "");

            if (PrefConfig.readNormalListInPref(getApplicationContext(), getResources().getString(R.string.saved_english_value)) == null)
                getUiData(true);
            else
                getUiData(false);
            if (startIndex == 0) {
                PrefConfig.writeIntDInPref(HomeScreen.this, 0, getResources().getString(R.string.alter_maths_value));
            } else {
                PrefConfig.writeIntDInPref(HomeScreen.this, 2, getResources().getString(R.string.alter_maths_value));
            }
        }


    }


    private void getUiData(boolean isNewCall) throws ArrayIndexOutOfBoundsException, NullPointerException {

        subMathsData.clear();
        subEngData.clear();

        chapterListEng = new ArrayList<>();
        chapterListMath = new ArrayList<>();


        chapterListMath.clear();
        chapterListEng.clear();

        subjectRecyclerAdapter.notifyDataSetChanged();


        if (isNewCall) {


//            PrefConfig.writeNormalListInPref(HomeScreen.this,chapterListMath,getResources().getString(R.string.saved_maths_value));
//            PrefConfig.writeNormalListInPref(HomeScreen.this,chapterListEng,getResources().getString(R.string.saved_english_value));

            UtilityFunctions.runOnUiThread(() -> {

                startIndex = PrefConfig.readIntDInPref(HomeScreen.this, getResources().getString(R.string.alter_maths_value));
                customProgressDialogue.dismiss();

                for (int i = startIndex; i < startIndex + 2; i++) {
                    if (!math[i].equals("Multiplication Tables")) {
                        if (UtilityFunctions.getFirstFalseData(gradeDatabase, kidsGrade, mathSub.get(i)) != null) {
                            subMathsData.add(UtilityFunctions.getFirstFalseData(gradeDatabase, kidsGrade, mathSub.get(i)));
                            chapterListMath.add(UtilityFunctions.getFirstFalseData(gradeDatabase, kidsGrade, mathSub.get(i)).chapter);

                            Log.i("DATA", UtilityFunctions.getFirstFalseData(gradeDatabase, kidsGrade, mathSub.get(i)) + "");
                        }
                    } else {

                        int mul_upto = PrefConfig.readIntInPref(getApplicationContext(), getResources().getString(R.string.multiplication_upto));
                        subMathsData.add(new Grades_data(getResources().getString(R.string.mul), mul_upto + 1 + "", false, false, false, false, false, false, false, ""));
                        chapterListMath.add("Multiplication Tables " + (mul_upto + 1));
                    }

                }

                Log.i("LIST_DATA", subMathsData + "");

                PrefConfig.writeNormalListInPref(HomeScreen.this, chapterListMath, getResources().getString(R.string.saved_maths_value));

                for (int i = 0; i < 2; i++) {
                    if (UtilityFunctions.getFirstFalseData(gradeDatabase, kidsGrade, engSub.get(i)) != null) {
                        subEngData.add(UtilityFunctions.getFirstFalseData(gradeDatabase, kidsGrade, engSub.get(i)));
                        chapterListEng.add(UtilityFunctions.getFirstFalseData(gradeDatabase, kidsGrade, engSub.get(i)).chapter);
                    }
                }

                PrefConfig.writeNormalListInPref(HomeScreen.this, chapterListEng, getResources().getString(R.string.saved_english_value));

                Log.i("LIST_DATA", subEngData + "");


                binding.mathsRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                subjectRecyclerAdapter = new SubjectRecyclerAdapter(subMathsData, HomeScreen.this);
                binding.mathsRecyclerView.setAdapter(subjectRecyclerAdapter);

                binding.englishRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                subjectRecyclerAdapter = new SubjectRecyclerAdapter(subEngData, HomeScreen.this);
                binding.englishRecyclerView.setAdapter(subjectRecyclerAdapter);

            }, 1000);


        } else {

            chapterListEng.clear();
            chapterListMath.clear();

            chapterListEng = PrefConfig.readNormalListInPref(HomeScreen.this, getResources().getString(R.string.saved_english_value));

            chapterListMath = PrefConfig.readNormalListInPref(HomeScreen.this, getResources().getString(R.string.saved_maths_value));

            Log.i("MathList", chapterListMath + "");

            int l_index = 0;

            for (int i = startIndex; i < startIndex + 2; i++) {
                //TODO:try catch needed

                try {

                    if (!chapterListMath.get(l_index).contains("Multiplication Tables")) {
                        try {
                            if (UtilityFunctions.gettingSubSubjectData(gradeDatabase, kidsGrade, chapterListMath.get(l_index), true) != null) {
                                Log.i("Data_chap", chapterListMath + "");
                                Log.i("DATA", UtilityFunctions.gettingSubSubjectData(gradeDatabase, kidsGrade, chapterListMath.get(l_index), true) + "");
                                subMathsData.add(UtilityFunctions.gettingSubSubjectData(gradeDatabase, kidsGrade, chapterListMath.get(l_index), true).get(0));

                            }

                        } catch (Exception e) {
                        }

                    } else {

                        try {
                            int mul_upto = PrefConfig.readIntInPref(getApplicationContext(), getResources().getString(R.string.multiplication_upto));
                            if (Integer.parseInt(chapterListMath.get(l_index).split(" ")[2]) == mul_upto)
                                subMathsData.add(new Grades_data(getResources().getString(R.string.mul), chapterListMath.get(l_index).split(" ")[2] + "", false, false, false, false, false, false, true, ""));
                            else
                                subMathsData.add(new Grades_data(getResources().getString(R.string.mul), chapterListMath.get(l_index).split(" ")[2] + "", false, false, false, false, false, false, false, ""));
                        } catch (Exception e) {
                        }
                    }
                    l_index++;


                } catch (Exception e) {
                }


            }

            Log.i("LIST_DATA", subMathsData + "");

            l_index = 0;
            for (int i = 0; i < 2; i++) {
                try {
                    if (UtilityFunctions.gettingSubSubjectData(gradeDatabase, kidsGrade, chapterListEng.get(l_index), true) != null) {
                        subEngData.add(UtilityFunctions.gettingSubSubjectData(gradeDatabase, kidsGrade, chapterListEng.get(l_index), true).get(0));
                    }
                } catch (Exception e) {
                }

                l_index++;
            }

            Log.i("LIST_DATA", subEngData + "");


            binding.mathsRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
            subjectRecyclerAdapter = new SubjectRecyclerAdapter(subMathsData, HomeScreen.this);
            binding.mathsRecyclerView.setAdapter(subjectRecyclerAdapter);

            binding.englishRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
            subjectRecyclerAdapter = new SubjectRecyclerAdapter(subEngData, HomeScreen.this);
            binding.englishRecyclerView.setAdapter(subjectRecyclerAdapter);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        //getUiData(false);

        kidsGrade = PrefConfig.readIdInPref(HomeScreen.this, getResources().getString(R.string.kids_grade));
        kidsName = PrefConfig.readIdInPref(HomeScreen.this, getResources().getString(R.string.kids_name));
        binding.tool.toolBar.kidsName.setText("Hi ," + PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_name)));
    }


    private void completeClose() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        completeClose();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}

