package com.maths.beyond_school_280720220930;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maths.beyond_school_280720220930.databinding.ActivityAdditionBinding;
import com.maths.beyond_school_280720220930.databinding.ActivityLoginSignupBinding;
import com.maths.beyond_school_280720220930.signin_methods.GoogleSignInActivity;
import com.maths.beyond_school_280720220930.signin_methods.PhoneNumberLogin;

import java.util.Arrays;
import java.util.List;

public class LoginSignupActivity extends AppCompatActivity {


    private ActivityLoginSignupBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginSignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.googleSignIn.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(), GoogleSignInActivity.class));
        });

        binding.phoneNumberSignIn.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(), PhoneNumberLogin.class));
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

}