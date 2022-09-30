package com.maths.beyond_school_280720220930.signin_methods;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.CredentialsClient;
import com.google.android.gms.auth.api.credentials.CredentialsOptions;
import com.google.android.gms.auth.api.credentials.HintRequest;
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
import com.maths.beyond_school_280720220930.HomeScreen;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.databinding.ActivityPhoneNumberLoginBinding;
import com.maths.beyond_school_280720220930.databinding.AlarmDialogBinding;
import com.maths.beyond_school_280720220930.extras.CustomProgressDialogue;
import com.maths.beyond_school_280720220930.firebase.CallFirebaseForInfo;
import com.maths.beyond_school_280720220930.model.KidsData;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PhoneNumberLogin extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private ActivityPhoneNumberLoginBinding binding;

    private String[] array;
    private String[] arrayGrades;
    private ArrayAdapter adapterGrades;
    // variable for FirebaseAuth class
    private FirebaseAuth mAuth;
    // string for storing our verification ID
    private String verificationId;
    private ArrayAdapter adapter;
    private GradeDatabase database;
    private CustomProgressDialogue customProgressDialogue;
    private FirebaseFirestore kidsDb = FirebaseFirestore.getInstance();
    private TelecomManager telecomManager;
    private int CREDENTIAL_PICKER_REQUEST = 1;
    private AlarmDialogBinding alarmDialogBinding;
    private final static int RESOLVE_HINT = 1011;
    private FirebaseAnalytics analytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPhoneNumberLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        analytics = FirebaseAnalytics.getInstance(getApplicationContext());

        customProgressDialogue=new CustomProgressDialogue(PhoneNumberLogin.this);
        mAuth = FirebaseAuth.getInstance();
        database = GradeDatabase.getDbInstance(this);
        setUpTextLayoutGrade();
        telecomManager = (TelecomManager) getApplicationContext().getSystemService(Context.TELECOM_SERVICE);
        binding.idBtnGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // below line is for checking weather the user
                // has entered his mobile number or not.
                if (TextUtils.isEmpty(binding.idEdtPhoneNumber.getText().toString())) {
                    // when mobile number text field is empty
                    // displaying a toast message.
                    Toast.makeText(PhoneNumberLogin.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                } else {
                    // if the text field is not empty we are calling our
                    // send OTP method for getting OTP from Firebase.
                    String phone = binding.countryCode.getText().toString() + binding.idEdtPhoneNumber.getText().toString();
                    sendVerificationCode(phone);
                    customProgressDialogue.show();
                }
            }
        });

        binding.resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(binding.idEdtPhoneNumber.getText().toString())) {
                    // when mobile number text field is empty
                    // displaying a toast message.
                    Toast.makeText(PhoneNumberLogin.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                } else {
                    // if the text field is not empty we are calling our
                    // send OTP method for getting OTP from Firebase.
                    String phone =  binding.countryCode.getText().toString() + binding.idEdtPhoneNumber.getText().toString();
                    UtilityFunctions.attemptPhoneNumberLogin(analytics,phone);
                    sendVerificationCode(phone);
                }
            }
        });

        // initializing on click listener
        // for verify otp button
        binding.idBtnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validating if the OTP text field is empty or not.
                if (TextUtils.isEmpty(binding.idEdtOtp.getText().toString())) {
                    // if the OTP text field is empty display
                    // a message to user to enter OTP
                    Toast.makeText(PhoneNumberLogin.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    // if OTP field is not empty calling
                    // method to verify the OTP.
                    verifyCode(binding.idEdtOtp.getText().toString());
                    closeKeyboard();
                }
            }
        });


        phoneSelection();
    }

    private void closeKeyboard()
    {
        // this will give us the view
        // which is currently focus
        // in this layout
        View view = this.getCurrentFocus();

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void phoneSelection() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.CREDENTIALS_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();
        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient, hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), RESOLVE_HINT, null, 0, 0, 0, null);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                if (credential != null) {
                    String number=credential.getId().replace(binding.countryCode.getText().toString(),"");
                    binding.idEdtPhoneNumber.setText(number);


                } else {
                    Toast.makeText(getApplicationContext(), "No phone numbers found", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    private void setUpTextLayoutGrade() {
        array = getResources().getStringArray(R.array.country_code);
        adapter = new ArrayAdapter(this, R.layout.list_item, array);
        AutoCompleteTextView editText = Objects.requireNonNull((AutoCompleteTextView) binding.textInputLayoutCountryCode.getEditText());
        editText.setAdapter(adapter);

    }


    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                            checkUserAlreadyAvailable(mAuth.getCurrentUser());
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
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


    // callback method is called on Phone auth provider.
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            customProgressDialogue.dismiss();
            verificationId = s;
            binding.numberLayout.setVisibility(View.GONE);
            binding.otpLayout.setVisibility(View.VISIBLE);
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                binding.idEdtOtp.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            binding.numberLayout.setVisibility(View.VISIBLE);
            customProgressDialogue.dismiss();
        }
    };


    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
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
            kidsData.put("status","active");
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

                            PrefConfig.writeIdInPref(PhoneNumberLogin.this,binding.countryCode.getText().toString() + binding.idEdtPhoneNumber.getText().toString(),getResources().getString(R.string.parent_contact_details));
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

                            Log.i("No_data", "No_data");
//                            var intent = new Intent(getApplicationContext(), KidsInfoActivity.class);
//                            intent.putExtra("type", "next");
//                            startActivity(intent);
                            saveKidsData("default");

                        } else {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                KidsData kidsData = queryDocumentSnapshot.toObject(KidsData.class);
                                kidsData.setKids_id(queryDocumentSnapshot.getId());
                                try{
                                    if (!kidsData.getStatus().toLowerCase(Locale.ROOT).equals("deleted")){
                                        PrefConfig.writeIdInPref(PhoneNumberLogin.this,binding.countryCode.getText().toString() + binding.idEdtPhoneNumber.getText().toString(),getResources().getString(R.string.parent_contact_details));
                                        UtilityFunctions.saveDataLocally(getApplicationContext(),kidsData.getGrade(),kidsData.getName(),kidsData.getAge(),kidsData.getProfile_url(),kidsData.getKids_id());
                                        CallFirebaseForInfo.upDateActivities(kidsDb,mAuth,kidsData.getKids_id(),kidsData.getGrade(),PhoneNumberLogin.this,database);
                                        Log.i("KidsData", kidsData.getName() + "");


                                        displaySetEventDialog();

                                        break;
                                    }
                                }catch (Exception e){
                                    PrefConfig.writeIdInPref(PhoneNumberLogin.this,binding.countryCode.getText().toString() + binding.idEdtPhoneNumber.getText().toString(),getResources().getString(R.string.parent_contact_details));
                                    CallFirebaseForInfo.upDateActivities(kidsDb,mAuth,kidsData.getKids_id(),kidsData.getGrade(),PhoneNumberLogin.this,database);
                                    UtilityFunctions.saveDataLocally(getApplicationContext(),kidsData.getGrade(),kidsData.getName(),kidsData.getAge(),kidsData.getProfile_url(),kidsData.getKids_id());
                                    Log.i("KidsData", kidsData.getName() + "");
                                    try {
                                        displaySetEventDialog();
                                    } catch (ParseException ex) {
                                        ex.printStackTrace();
                                    }
                                    break;
                                }


                            }

                        }

                    });
        }


    }

    private void displaySetEventDialog() throws ParseException {


            final AlertDialog.Builder alert = new AlertDialog.Builder(PhoneNumberLogin.this);
            View mView = getLayoutInflater().inflate(R.layout.alarm_dialog, null);
             alarmDialogBinding =AlarmDialogBinding.bind(mView);

            alert.setView(mView);
            final AlertDialog alertDialog = alert.create();
            alertDialog.setCancelable(true);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        arrayGrades = getResources().getStringArray(R.array.time_slots);
        adapterGrades = new ArrayAdapter(this, R.layout.list_item, arrayGrades);
        AutoCompleteTextView editText = Objects.requireNonNull((AutoCompleteTextView) alarmDialogBinding.extraInclude.textInputLayoutTimer.getEditText());
        editText.setAdapter(adapterGrades);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
           alarmDialogBinding.extraInclude.kidsGrade.setText(alarmDialogBinding.extraInclude.kidsGrade.getAdapter().getItem(9).toString(), false);
        }

        alarmDialogBinding.extraInclude.selecttimebtn.setOnClickListener(v->{
            try {
                UtilityFunctions.setEvent(PhoneNumberLogin.this,alarmDialogBinding.extraInclude.textInputLayoutTimer);
                var i = new Intent(getApplicationContext(), HomeScreen.class);
                startActivity(i);
                finish();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });



            try {
                alertDialog.show();
            } catch (Exception e) {

            }

            alarmDialogBinding.closeButton.setOnClickListener(v -> {alertDialog.dismiss();
                var i = new Intent(getApplicationContext(), HomeScreen.class);
                startActivity(i);
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