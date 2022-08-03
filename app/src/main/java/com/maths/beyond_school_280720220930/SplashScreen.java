package com.maths.beyond_school_280720220930;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maths.beyond_school_280720220930.extras.ReadText;

public class SplashScreen extends AppCompatActivity {


    private TextView returnedText,textGot;
    private ImageButton button;
    private ProgressBar progressBar;
    private SpeechRecognizer speech ;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";
    private int REQUEST_RECORD_AUDIO=1;
    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    FirebaseFirestore kidsDb=FirebaseFirestore.getInstance();


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        hideSystemUI();
        button=findViewById(R.id.mainButton);
        startService(new Intent(getBaseContext(), ClearService.class));

        mAuth=FirebaseAuth.getInstance();
        mCurrentUser=mAuth.getCurrentUser();
      //  Toast.makeText(this, mAuth.getCurrentUser().getUid()+"", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                /* Create an Intent that will start the Menu-Activity. */
                if (mCurrentUser==null){
                    startActivity( new Intent(getApplicationContext(),LoginSignupActivity.class));
                    finish();
                }

                else{
                    checkUserAlreadyAvailable();
                }
            }
        }, 1000);



        button.setOnClickListener(view -> {

            Toast.makeText(this, "xxx", Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void checkUserAlreadyAvailable() {

        FirebaseFirestore kidsDb=FirebaseFirestore.getInstance();
        kidsDb.collection("users").document(mCurrentUser.getUid()).collection("kids").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {


//                    if (queryDocumentSnapshots.isEmpty()){
//                        Log.i("No_data","No_data");
                        startActivity(new Intent(getApplicationContext(),KidsInfoActivity.class));
//                    }else{
//                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                    }
                    finish();

                });
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();


    }
    public void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


}