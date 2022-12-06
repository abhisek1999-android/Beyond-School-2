package com.maths.beyond_school_new_071020221400;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maths.beyond_school_new_071020221400.SP.PrefConfig;
import com.maths.beyond_school_new_071020221400.adapters.TablesRecyclerAdapter;
import com.maths.beyond_school_new_071020221400.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_new_071020221400.database.grade_tables.Grades_data;
import com.maths.beyond_school_new_071020221400.database.process.ProgressDataBase;
import com.maths.beyond_school_new_071020221400.databinding.ActivityMathsTutorialBinding;
import com.maths.beyond_school_new_071020221400.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_new_071020221400.utils.UtilityFunctions;

import java.util.List;

public class TablesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TablesRecyclerAdapter.ItemClickListener {


    private ActivityMathsTutorialBinding binding;
    private Animation slideLeftAnim,slideRightAnim,fadeIn;
    private TextToSpeckConverter tts;
    private int DELAY_ON_STARTING_STT=500;
    private TablesRecyclerAdapter tablesRecyclerAdapter;
    int i=0;
    private GradeDatabase gradeDatabase;
    private List<Grades_data> mathsList;
    private int REQUEST_RECORD_AUDIO = 1;
    private BottomSheetBehavior mBottomSheetBehavior;
    private ActionBarDrawerToggle toggle;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private static final String TAG = "TablesActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityMathsTutorialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        gradeDatabase= GradeDatabase.getDbInstance(this);
        mathsList=gradeDatabase.gradesDao().getFirstUnlockItem("Multiplication Tables");
        mAuth=FirebaseAuth.getInstance();
        mCurrentUser=mAuth.getCurrentUser();
        mBottomSheetBehavior = BottomSheetBehavior.from(binding.extLayout.permissionCard);
        binding.mathsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        tablesRecyclerAdapter = new TablesRecyclerAdapter(mathsList, TablesActivity.this,this);
        binding.mathsRecyclerView.setAdapter(tablesRecyclerAdapter);
        ViewCompat.setNestedScrollingEnabled(binding.mathsRecyclerView, false);
        checkAudioPermission();
        setUiElements();

        loadImage();
      //  startActivity(new Intent(getApplicationContext(),TestActivity.class));

    }


    private void setUiElements() {

        binding.tool.toolBar.kidsName.setText("Hi ," + PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_name)));
        binding.tool.toolBar.kidsAge.setText(UtilityFunctions.calculateAge(PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_dob))) + " years old");


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

        binding.navigationView.setNavigationItemSelectedListener(this);
        binding.toolBar.imageViewBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu2));
        binding.toolBar.imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                try {
                    binding.drawerLayout.openDrawer(Gravity.LEFT);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        binding.tool.toolBar.gotoKidsInfo.setOnClickListener(v -> {

            Intent intent = new Intent(getApplicationContext(), KidsInfoActivity.class);
            intent.putExtra("type", "update");
            startActivity(intent);
            // binding.drawerLayout.closeDrawer(Gravity.LEFT);


        });


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

        binding.gotoViewCurriculumOne.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        });

        binding.gotoViewCurriculumTwo.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        });


//        binding.tool.toolBar.imageCardView.setOnClickListener(v -> {
//            binding.drawerLayout.closeDrawer(Gravity.LEFT);
//        });

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



    private void checkAudioPermission() {
        // M = 23
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissionPadController();
        }
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
    private void loadImage() {
        UtilityFunctions.loadImage(PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_profile_url)),
                binding.tool.toolBar.imageView6);
    }

    private void completeClose() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }


    @SuppressLint("MissingSuperCall")
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (requestCode == REQUEST_RECORD_AUDIO) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(getApplicationContext(), TablesActivity.class));
                finish();
            } else {
                checkAudioPermission();
            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void intentAction(TextView textView, String id, String chapterName) {
        ProgressDataBase.getDbInstance(getApplicationContext()).progressDao().getTimeSpend(id,
                chapterName).observe(this,c->{

            if (c != null) {
                textView.setText(c + "");
            } else {
                textView.setText(0+"");
            }
            Log.d(TAG, "intentAction: "+c+","+id+","+chapterName);
        });
    }
}

