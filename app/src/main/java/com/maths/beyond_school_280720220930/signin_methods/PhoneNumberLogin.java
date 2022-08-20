package com.maths.beyond_school_280720220930.signin_methods;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.maths.beyond_school_280720220930.KidsInfoActivity;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.Select_Sub_Activity;
import com.maths.beyond_school_280720220930.databinding.ActivityPhoneNumberLoginBinding;
import com.maths.beyond_school_280720220930.extras.CustomProgressDialogue;
import com.maths.beyond_school_280720220930.model.KidsData;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PhoneNumberLogin extends AppCompatActivity {

    private ActivityPhoneNumberLoginBinding binding;

    private String[] array;
    // variable for FirebaseAuth class
    private FirebaseAuth mAuth;
    // string for storing our verification ID
    private String verificationId;
    private ArrayAdapter adapter;
    private CustomProgressDialogue customProgressDialogue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPhoneNumberLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        customProgressDialogue=new CustomProgressDialogue(PhoneNumberLogin.this);
        mAuth = FirebaseAuth.getInstance();

        setUpTextLayoutGrade();

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
                    String phone = "+91" + binding.idEdtPhoneNumber.getText().toString();
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
                }
            }
        });


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

    private void checkUserAlreadyAvailable(FirebaseUser user) {


        if (user != null) {
            FirebaseFirestore kidsDb = FirebaseFirestore.getInstance();
            kidsDb.collection("users").document(user.getUid()).collection("kids").get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {


                        if (queryDocumentSnapshots.isEmpty()) {

                            Log.i("No_data", "No_data");
                            var intent = new Intent(getApplicationContext(), KidsInfoActivity.class);
                            intent.putExtra("type", "next");
                            startActivity(intent);
                            finish();
                        } else {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                KidsData kidsData = queryDocumentSnapshot.toObject(KidsData.class);
                                kidsData.setKids_id(queryDocumentSnapshot.getId());
                                try{
                                    if (!kidsData.getStatus().toLowerCase(Locale.ROOT).equals("deleted")){
                                        UtilityFunctions.saveDataLocally(getApplicationContext(),kidsData.getGrade(),kidsData.getName(),kidsData.getAge(),kidsData.getProfile_url(),kidsData.getKids_id());
                                        Log.i("KidsData", kidsData.getName() + "");
                                        var i = new Intent(getApplicationContext(), Select_Sub_Activity.class);
                                        startActivity(i);
                                        finish();
                                        break;
                                    }
                                }catch (Exception e){

                                    UtilityFunctions.saveDataLocally(getApplicationContext(),kidsData.getGrade(),kidsData.getName(),kidsData.getAge(),kidsData.getProfile_url(),kidsData.getKids_id());
                                    Log.i("KidsData", kidsData.getName() + "");
                                    var i = new Intent(getApplicationContext(), Select_Sub_Activity.class);
                                    startActivity(i);
                                    finish();
                                    break;
                                }


                            }

                        }

                    });
        }


    }
}