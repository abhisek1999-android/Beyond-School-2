package com.maths.beyond_school270620220930;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;

public class LoginSignupActivity extends AppCompatActivity {
Button login,signup,log,sign;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String regex = "^(.+)@(.+)$";
    CardView logcard,signcard;
    TextInputEditText email,password,name,confirmpassword,emaillog,passwordlog;
    Intent loginIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        email=findViewById(R.id.useremail);
        password=findViewById(R.id.password);
        signup=findViewById(R.id.signup);
        name=findViewById(R.id.username);
        emaillog=findViewById(R.id.useremaillog);
        passwordlog=findViewById(R.id.passwordlog);
        login=findViewById(R.id.login);
        log=findViewById(R.id.button3);
        sign=findViewById(R.id.button5);
        logcard=findViewById(R.id.logincard);
        signcard=findViewById(R.id.signupcard);
        mAuth = FirebaseAuth.getInstance();
        loginIntent=new Intent(getApplicationContext(),TestActivity.class);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signcard.setVisibility(View.GONE);
                logcard.setVisibility(View.VISIBLE);
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signcard.setVisibility(View.VISIBLE);
                logcard.setVisibility(View.GONE);
            }
        });
        login.setOnClickListener(view -> {
            mAuth.signInWithEmailAndPassword(emaillog.getText().toString(), passwordlog.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                getuser();
                                startActivity(loginIntent);
                            } else {
                                // If sign in fails, display a message to the user.
                            //    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                             //   updateUI(null);
                            }
                        }
                    });
        });
        signup.setOnClickListener(view -> {
            String mail,username,pass,confirmpass;
            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                               // Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(getApplicationContext(), "signup done", Toast.LENGTH_SHORT).show();
                                adduser();
                             //   updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                              //  Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(LoginSignupActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                               // updateUI(null);
                            }
                        }
                    });
        });
    }
    public  void getuser(){
        DocumentReference docRef = db.collection("users").document(mAuth.getCurrentUser().getUid().toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        loginIntent.putExtra("Nameofchild",  document.get("name").toString());
                    } else {
                      //  Log.d(TAG, "No such document");
                    }
                } else {
                   // Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
    public void adduser(){
        Map<String, Object> user = new HashMap<>();
       user.put("email",email.getText().toString());
       user.put("name",name.getText().toString());
// Add a new document with a generated ID
        db.collection("users").document(mAuth.getCurrentUser().getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       // Log.d(TAG, "DocumentSnapshot successfully written!");
                        Toast.makeText(getApplicationContext(),"added",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                      //  Log.w(TAG, "Error writing document", e);
                    }
                });
    }
}