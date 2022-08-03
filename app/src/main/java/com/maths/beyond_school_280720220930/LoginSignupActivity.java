package com.maths.beyond_school_280720220930;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.maths.beyond_school_280720220930.model.KidsData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginSignupActivity extends AppCompatActivity {


    private FirebaseAuth mFirebaseAuth;

    // declaring a const int value which we
    // will be using in Firebase auth.
    public static final int RC_SIGN_IN = 1;

    // creating an auth listener for our Firebase auth
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    FirebaseUser user;
        /* below is the line for adding
    // email and password authentication.
    new AuthUI.IdpConfig.EmailBuilder().build(),*/

    List<AuthUI.IdpConfig> providers = Arrays.asList(

            // below line is used for adding google
            // authentication builder in our app.
            new AuthUI.IdpConfig.GoogleBuilder().build(),

            // below line is used for adding phone
            // authentication builder in our app.
            new AuthUI.IdpConfig.PhoneBuilder().build());



    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        mFirebaseAuth = FirebaseAuth.getInstance();

        // below line is used for calling auth listener
        // for oue Firebase authentication.
        mAuthStateListner = new FirebaseAuth.AuthStateListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                 user = firebaseAuth.getCurrentUser();

                // checking if the user
                // is null or not.
                if (user != null) {
                    checkUserAlreadyAvailable();
                } else {

                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(providers)
                                    .setTheme(R.style.LoginTheme)
                                    .build(),
                            RC_SIGN_IN
                    );
                }
            }
        };

    }

    private void checkUserAlreadyAvailable() {

//        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
//        DocumentReference docIdRef = rootRef.collection("users").document(user.getUid());
//      //  Toast.makeText(this, user.getUid()+"", Toast.LENGTH_SHORT).show();
//        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//
//
//                    Log.i("taskResult",document.getId()+"");
//
//                    if (document.getId().replace(" ","").equals(user.getUid()+"")) {
//                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                        finish();
//
//                    } else {
//                        startActivity(new Intent(getApplicationContext(),KidsInfoActivity.class));
//                        finish();
//                    }
//                } else {
//                    Log.d("TAG", "Failed with: ", task.getException());
//                }
//            }
//        });

        FirebaseFirestore kidsDb=FirebaseFirestore.getInstance();
        kidsDb.collection("users").document(user.getUid()).collection("kids").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {


                    if (queryDocumentSnapshots.isEmpty()){

                        Log.i("No_data","No_data");
                        startActivity(new Intent(getApplicationContext(),KidsInfoActivity.class));
                        finish();
                    }else{
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }

                });
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...

          //  Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // we are calling our auth
        // listener method on app resume.
        mFirebaseAuth.addAuthStateListener(mAuthStateListner);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // here we are calling remove auth
        // listener method on stop.
        mFirebaseAuth.removeAuthStateListener(mAuthStateListner);
    }

}