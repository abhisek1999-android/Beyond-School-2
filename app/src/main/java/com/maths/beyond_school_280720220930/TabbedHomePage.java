package com.maths.beyond_school_280720220930;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.databinding.ActivityTabbedHomePageBinding;
import com.maths.beyond_school_280720220930.databinding.RowBinding;
import com.maths.beyond_school_280720220930.extras.CustomProgressDialogue;
import com.maths.beyond_school_280720220930.firebase.CallFirebaseForInfo;
import com.maths.beyond_school_280720220930.fragments.MyAdapter;
import com.maths.beyond_school_280720220930.payments.FetchSubscriptionStatus;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;
import com.razorpay.Subscription;

import java.util.Date;
import java.util.concurrent.ExecutionException;

public class TabbedHomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private ActivityTabbedHomePageBinding binding;
    ActionBarDrawerToggle toggle;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private BottomSheetBehavior mBottomSheetBehavior;
    private FirebaseFirestore firebaseFirestore;
    private int REQUEST_RECORD_AUDIO = 1;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private final static String TAG = "TabbedHomeScreen";
    private Subscription subscription;
    private String subscriptionId = "";
    private String createAt;
    private CustomProgressDialogue customProgressDialogue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTabbedHomePageBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        customProgressDialogue = new CustomProgressDialogue(this);

        subscriptionId = PrefConfig.readIdInPref(TabbedHomePage.this, getResources().getString(R.string.subscription_id));
        createAt = PrefConfig.readIdInPref(this, getResources().getString(R.string.created_at));

        if (PrefConfig.readIdInPref(TabbedHomePage.this, getResources().getString(R.string.plan_id)).equals(""))
            setUpRemoteConfigPayment();


        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("English"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Math"));
        mBottomSheetBehavior = BottomSheetBehavior.from(binding.extLayout.permissionCard);
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final MyAdapter adapter = new MyAdapter(this, getSupportFragmentManager(), binding.tabLayout.getTabCount());
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        checkAudioPermission();

        setUiElements();
        loadImage();

    }

    private void loadImage() {
        UtilityFunctions.loadImage(PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_profile_url)),
                binding.tool.toolBar.imageView6);
    }


    private void setUpRemoteConfigPayment() {
        customProgressDialogue.show();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.data_updated_default_value);
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        boolean updated = task.getResult();

                        int val = (int) mFirebaseRemoteConfig.getLong("price_value");
                        PrefConfig.writeIntInPref(TabbedHomePage.this, val, getResources().getString(R.string.plan_value));
                        PrefConfig.writeIdInPref(TabbedHomePage.this, UtilityFunctions.getPlanIds(val), getResources().getString(R.string.plan_id));

                        if (PrefConfig.readIntInPref(TabbedHomePage.this, getResources().getString(R.string.trial_period), 0) == 0)
                            setUpRemoteConfigTrial(val);
                        else
                            customProgressDialogue.dismiss();

                        Log.d(TAG, "Config params updated: payment" + updated + ", val:" + val);
                        return;
                    }
                    // Toast.makeText(SplashScreen.this, "Fetch failed", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Config params updated: " + "Fetch failed");
                });

    }


    private void setUpRemoteConfigTrial(int planVal) {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.data_updated_default_value);
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        boolean updated = task.getResult();

                        int val = (int) mFirebaseRemoteConfig.getLong("trial_period");
                        PrefConfig.writeIntInPref(TabbedHomePage.this, val, getResources().getString(R.string.trial_period));

                        CallFirebaseForInfo.setSubscriptionId(firebaseFirestore, mAuth, PrefConfig.readIdInPref(TabbedHomePage.this, getResources().getString(R.string.subscription_id)), UtilityFunctions.getPlanIds(planVal),
                                PrefConfig.readIdInPref(TabbedHomePage.this, getResources().getString(R.string.customer_id))
                                , planVal, val, this, () -> {
                                    createAt = PrefConfig.readIdInPref(this, getResources().getString(R.string.created_at));
                                    customProgressDialogue.dismiss();
                                });

                        Log.d(TAG, "Config params updated: trial " + updated + ", val:" + val);
                        return;
                    }
                    // Toast.makeText(SplashScreen.this, "Fetch failed", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Config params updated: " + "Fetch failed");
                    customProgressDialogue.dismiss();
                });

    }

    private void setUiElements() {

        binding.tool.toolBar.kidsName.setText("Hi ," + PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_name)));
        binding.tool.toolBar.kidsAge.setText(UtilityFunctions.calculateAge(PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_dob))) + " years old");

        Log.i("ImageUrl", PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_profile_url)));

        binding.tool.logoutLayout.setVisibility(View.VISIBLE);

        int trialPeriod = PrefConfig.readIntInPref(TabbedHomePage.this, getResources().getString(R.string.trial_period), 0);
        int noOfDays = PrefConfig.readIntInPref(TabbedHomePage.this, getResources().getString(R.string.noOfdays), 0);
        if (PrefConfig.readIdInPref(TabbedHomePage.this, getResources().getString(R.string.payment_status)).equals("active"))
            binding.tool.toolBar.subscriptionStatus.setText("Your plan is activated");
        else if ((trialPeriod - UtilityFunctions.diffDate(createAt, new Date().toString())) == trialPeriod - 1)
            binding.tool.toolBar.subscriptionStatus.setText("Your trial period ends today ");
        else if (UtilityFunctions.diffDate(createAt, new Date().toString()) < trialPeriod)
            binding.tool.toolBar.subscriptionStatus.setText("Your trial period ends in " + (trialPeriod - UtilityFunctions.diffDate(createAt, new Date().toString())) + " days");
        else if (!PrefConfig.readIdInPref(TabbedHomePage.this, getResources().getString(R.string.payment_status)).equals("active"))
            binding.tool.toolBar.subscriptionStatus.setText("You don't have any valid plans");


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
        binding.appLogo.setOnClickListener(new View.OnClickListener() {
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

        binding.tool.manageSubscription.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ManageSubscription.class));
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


    private void goToProfile(String type) {
        Toast.makeText(this, "XXX", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), KidsInfoActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);

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


    private void checkAudioPermission() {
        // M = 23
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissionPadController();
        }
    }


    @SuppressLint("MissingSuperCall")
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (requestCode == REQUEST_RECORD_AUDIO) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(getApplicationContext(), TabbedHomePage.class));
                finish();
            } else {
                checkAudioPermission();
            }
        }

    }

    private void completeClose() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setUiElements();
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