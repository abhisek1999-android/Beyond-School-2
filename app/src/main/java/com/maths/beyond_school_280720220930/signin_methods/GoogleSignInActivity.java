package com.maths.beyond_school_280720220930.signin_methods;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.maths.beyond_school_280720220930.GradeActivity;
import com.maths.beyond_school_280720220930.KidsInfoActivity;
import com.maths.beyond_school_280720220930.LoginSignupActivity;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.Select_Sub_Activity;
import com.maths.beyond_school_280720220930.model.KidsData;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

public class GoogleSignInActivity extends LoginSignupActivity {

    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN=101;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth=FirebaseAuth.getInstance();
        mCurrentUser=mAuth.getCurrentUser();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        Context context;
        progressDialog=new ProgressDialog(GoogleSignInActivity.this);
        progressDialog.setMessage("Google Sign in...");
        progressDialog.show();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account.getIdToken());
                progressDialog.dismiss();
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                progressDialog.dismiss();
                Toast.makeText(GoogleSignInActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                finish();

            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {

                            progressDialog.dismiss();
                            Toast.makeText(GoogleSignInActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }


    private void checkUserAlreadyAvailable(FirebaseUser user) {


        if (user!=null){
            FirebaseFirestore kidsDb = FirebaseFirestore.getInstance();
            kidsDb.collection("users").document(user.getUid()).collection("kids").get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {


                        if (queryDocumentSnapshots.isEmpty()) {

                            Log.i("No_data", "No_data");
                            var intent = new Intent(getApplicationContext(), KidsInfoActivity.class);
                            intent.putExtra("type","next");
                            startActivity(intent);
                            finish();
                        } else {


                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {

                                KidsData kidsData = queryDocumentSnapshot.toObject(KidsData.class);
                                kidsData.setKids_id(queryDocumentSnapshot.getId());
                                UtilityFunctions.saveDataLocally(getApplicationContext(),kidsData.getGrade(),kidsData.getName(),kidsData.getAge(),kidsData.getProfile_url(),kidsData.getKids_id());
                                Log.i("KidsData", kidsData.getName() + "");

                            }
                            var i = new Intent(getApplicationContext(), Select_Sub_Activity.class);
                            startActivity(i);
                            finish();

                        }

                    });
        }


    }

    private void updateUI(FirebaseUser user) {
        checkUserAlreadyAvailable(user);
    }
}