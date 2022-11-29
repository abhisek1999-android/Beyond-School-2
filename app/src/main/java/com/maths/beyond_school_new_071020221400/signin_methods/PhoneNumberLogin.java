package com.maths.beyond_school_new_071020221400.signin_methods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.maths.beyond_school_new_071020221400.GradeActivity;
import com.maths.beyond_school_new_071020221400.HomeScreen;
import com.maths.beyond_school_new_071020221400.MainActivity;
import com.maths.beyond_school_new_071020221400.R;
import com.maths.beyond_school_new_071020221400.SP.PrefConfig;
import com.maths.beyond_school_new_071020221400.TabbedHomePage;
import com.maths.beyond_school_new_071020221400.TablesActivity;
import com.maths.beyond_school_new_071020221400.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_new_071020221400.databinding.ActivityPhoneNumberLoginBinding;
import com.maths.beyond_school_new_071020221400.databinding.AlarmDialogBinding;
import com.maths.beyond_school_new_071020221400.extras.CustomProgressDialogue;
import com.maths.beyond_school_new_071020221400.firebase.CallFirebaseForInfo;
import com.maths.beyond_school_new_071020221400.model.KidsData;
import com.maths.beyond_school_new_071020221400.retrofit.ApiClient;
import com.maths.beyond_school_new_071020221400.retrofit.ApiInterface;
import com.maths.beyond_school_new_071020221400.retrofit.model.grade.GradeModel;
import com.maths.beyond_school_new_071020221400.utils.UtilityFunctions;
import com.maths.beyond_school_new_071020221400.utils.typeconverters.GradeConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;
import retrofit2.Retrofit;

public class PhoneNumberLogin extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "PhoneNumberLogin";
    private ActivityPhoneNumberLoginBinding binding;

   
    private String[] arrayGrades;
    private ArrayAdapter adapterGrades;
    // variable for FirebaseAuth class
    private FirebaseAuth mAuth;
    // string for storing our verification ID
    private String verificationId;
    private ArrayAdapter adapter;
    private String[] array;
    private GradeDatabase database;
    private CustomProgressDialogue customProgressDialogue;
    private FirebaseFirestore kidsDb = FirebaseFirestore.getInstance();
    private TelecomManager telecomManager;
    private int CREDENTIAL_PICKER_REQUEST = 1;
    private AlarmDialogBinding alarmDialogBinding;
    private final static int RESOLVE_HINT = 1011;
    private FirebaseAnalytics analytics;
    private GradeDatabase gradeDatabase;
    private String phoneNumber="";
    //Declare timer
    CountDownTimer cTimer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPhoneNumberLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        analytics = FirebaseAnalytics.getInstance(getApplicationContext());
        gradeDatabase = GradeDatabase.getDbInstance(PhoneNumberLogin.this);
        customProgressDialogue = new CustomProgressDialogue(PhoneNumberLogin.this);
        mAuth = FirebaseAuth.getInstance();
        database = GradeDatabase.getDbInstance(this);
        telecomManager = (TelecomManager) getApplicationContext().getSystemService(Context.TELECOM_SERVICE);
        
        phoneNumber=getIntent().getStringExtra("phoneNumber");

        sendVerificationCode(phoneNumber);
        customProgressDialogue.show();
        startTimer();
        
        binding.resendOtp.setOnClickListener(view -> {
            if (TextUtils.isEmpty(phoneNumber)) {
                Toast.makeText(PhoneNumberLogin.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
            } else {
                startTimer();
                UtilityFunctions.attemptPhoneNumberLogin(analytics, phoneNumber);
                sendVerificationCode(phoneNumber);
            }
        });

        binding.idBtnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validating if the OTP text field is empty or not.
                if (TextUtils.isEmpty(binding.idEdtOtp.getOTP().toString())) {
                    Toast.makeText(PhoneNumberLogin.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {

                    verifyCode(binding.idEdtOtp.getOTP().toString());
                    customProgressDialogue.show();
                    closeKeyboard();
                }
            }
        });


        binding.idEdtOtp.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }
            @Override
            public void onOTPComplete(String otp) {
                // fired when user has entered the OTP fully.
              //  Toast.makeText(PhoneNumberLogin.this, "The OTP is " + otp,  Toast.LENGTH_LONG).show();
             //   verifyCode(binding.idEdtOtp.getOTP().toString());
               // customProgressDialogue.show();
                closeKeyboard();
            }
        });


    }




    //start timer function
    void startTimer() {
        binding.timer.setVisibility(View.VISIBLE);
        binding.resendOtp.setVisibility(View.GONE);
        cTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {

                binding.timer.setText("Resend OTP in:"+ millisUntilFinished/1000);

            }
            public void onFinish() {
                binding.timer.setVisibility(View.GONE);
                binding.resendOtp.setVisibility(View.VISIBLE);
            }
        };
        cTimer.start();
    }


    //cancel timer
    void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }

    private void closeKeyboard() {

        View view = this.getCurrentFocus();

        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            checkUserAlreadyAvailable(mAuth.getCurrentUser());
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            customProgressDialogue.dismiss();
                        }
                    }
                });
    }


    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            customProgressDialogue.dismiss();
            verificationId = s;
            binding.otpLayout.setVisibility(View.VISIBLE);
        }


        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            final String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                customProgressDialogue.show();
                binding.idBtnVerify.setEnabled(false);
                binding.idEdtOtp.setOTP(code);
                verifyCode(code);
            }
        }


        @Override
        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            customProgressDialogue.dismiss();
            binding.idBtnVerify.setEnabled(true);
        }
    };



    private void verifyCode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void saveKidsData(String imageUrl) {
        if (mAuth != null) {

            String uuid = kidsDb.collection("users").document(mAuth.getCurrentUser().getUid()).collection("kids").document().getId();
            Map<String, Object> kidsData = new HashMap<>();
            kidsData.put("name", "Kids Name");
            kidsData.put("kids_id", uuid);
            kidsData.put("profile_url", imageUrl);
            kidsData.put("parent_id", mAuth.getCurrentUser().getUid());
            kidsData.put("age", "01/08/2017");
            kidsData.put("status", "active");
            kidsData.put("grade", "GRADE 1");
            // Add a new document with a generated ID
            kidsDb.collection("users").document(mAuth.getCurrentUser().getUid()).collection("kids").document(uuid)
                    .set(kidsData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Log.d(TAG, "DocumentSnapshot successfully written!");


                            UtilityFunctions.saveDataLocally(getApplicationContext(), "GRADE 1", "Kids Name",
                                    "01/08/2017", imageUrl, uuid);

                            PrefConfig.writeIdInPref(PhoneNumberLogin.this, phoneNumber, getResources().getString(R.string.parent_contact_details));
                            Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                            intent.putExtra("name", "Kids Name");
                            intent.putExtra("image", imageUrl);
                            intent.putExtra("age", "01/08/2017");
                            intent.putExtra("grade", "GRADE 1");
                            startActivity(intent);
                            finish();
                            customProgressDialogue.dismiss();
                            //    Toast.makeText(getApplicationContext(),"added",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //  Log.w(TAG, "Error writing document", e);
                            Toast.makeText(PhoneNumberLogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            customProgressDialogue.dismiss();
                        }
                    });
        }

    }

    private void checkUserAlreadyAvailable(FirebaseUser user) {

        if (user != null) {
            FirebaseFirestore kidsDb = FirebaseFirestore.getInstance();
            kidsDb.collection("users").document(user.getUid()).collection("kids").get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {


                        if (queryDocumentSnapshots.isEmpty()) {
                            customProgressDialogue.dismiss();
                            Log.i("No_data", "No_data");
                            var intent = new Intent(getApplicationContext(), GradeActivity.class);
                            startActivity(intent);
                            cancelTimer();
                            finish();
                            //saveKidsData("default");


                        } else {
                            PrefConfig.writeIdInPref(getApplicationContext(), "old_user", getResources().getString(R.string.user_type));
                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                KidsData kidsData = queryDocumentSnapshot.toObject(KidsData.class);
                                kidsData.setKids_id(queryDocumentSnapshot.getId());
                                try {
                                    if (!kidsData.getStatus().toLowerCase(Locale.ROOT).equals("deleted")) {
                                        PrefConfig.writeIdInPref(PhoneNumberLogin.this, phoneNumber, getResources().getString(R.string.parent_contact_details));

                                        getNewData(kidsData.getGrade().toLowerCase().replace(" ", ""), kidsData);
                                        UtilityFunctions.saveDataLocally(getApplicationContext(), kidsData.getGrade(), kidsData.getName(), kidsData.getAge(), kidsData.getProfile_url(), kidsData.getKids_id());
                                        // CallFirebaseForInfo.upDateActivities(kidsDb,mAuth,kidsData.getKids_id(),kidsData.getGrade().toLowerCase().replace(" ", ""),PhoneNumberLogin.this,database);
                                        break;
                                    }
                                } catch (Exception e) {
                                    PrefConfig.writeIdInPref(PhoneNumberLogin.this, phoneNumber, getResources().getString(R.string.parent_contact_details));
                                    getNewData(kidsData.getGrade().toLowerCase().replace(" ", ""), kidsData);
                                    //CallFirebaseForInfo.upDateActivities(kidsDb,mAuth,kidsData.getKids_id(),kidsData.getGrade(),PhoneNumberLogin.this,database);
                                    UtilityFunctions.saveDataLocally(getApplicationContext(), kidsData.getGrade(), kidsData.getName(), kidsData.getAge(), kidsData.getProfile_url(), kidsData.getKids_id());
                                    break;
                                }


                            }

                        }

                    });
        }

    }


    private void getNewData(String kidsGrade, KidsData kidsData) {
        Retrofit retrofit = ApiClient.getClient();
        var api = retrofit.create(ApiInterface.class);
        api.getGradeData(kidsGrade).enqueue(new retrofit2.Callback<>() {

            @Override
            public void onResponse(@NonNull retrofit2.Call<GradeModel> call, @NonNull retrofit2.Response<GradeModel> response) {
                if (response.body() != null) {
                    var list = response.body().getEnglish();
                    mapToGradeModel(list, kidsData);


                } else {
                    Toast.makeText(PhoneNumberLogin.this, "Something wrong occurs", Toast.LENGTH_SHORT).show();
                    customProgressDialogue.dismiss();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<com.maths.beyond_school_new_071020221400.retrofit.model.grade.GradeModel> call, Throwable t) {

                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
                customProgressDialogue.dismiss();
            }
        });
    }

    private void mapToGradeModel(List<GradeModel.EnglishModel> list, KidsData kidsData) {
        list.forEach(subject -> {
            var mapper = new GradeConverter(subject.getSubject());
            var chapterList = mapper.mapToList(subject.getChapters());
            gradeDatabase.gradesDaoUpdated().insertNotes(chapterList);
        });

        CallFirebaseForInfo.upDateActivitiesMaths(kidsDb, mAuth, kidsData.getKids_id(), kidsData.getGrade().toLowerCase().replace(" ", ""), PhoneNumberLogin.this, database, () -> {
            Log.i("KidsData", kidsData.getName() + "");
            customProgressDialogue.dismiss();
            var i = new Intent(getApplicationContext(), TablesActivity.class);
            startActivity(i);
            cancelTimer();
            finish();
        });


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}