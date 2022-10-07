package com.maths.beyond_school_new_071020221400;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.maths.beyond_school_new_071020221400.model.KidsData;

public class ProfileActivity extends AppCompatActivity {


    FirebaseFirestore kidsDb=FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    TextView kidsName,kidsAge;
    TextView titleText;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth=FirebaseAuth.getInstance();
        mCurrentUser=mAuth.getCurrentUser();

        kidsName=findViewById(R.id.kidsName);
        kidsAge=findViewById(R.id.kidsAge);

        back = findViewById(R.id.imageViewBack);

        titleText=findViewById(R.id.titleText);
        titleText.setText("Profile");




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        kidsDb.collection("users").document("123456").collection("kids").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {


                    for (QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){

                        KidsData kidsData=queryDocumentSnapshot.toObject(KidsData.class);
                        kidsData.setKids_id(queryDocumentSnapshot.getId());
                       // Toast.makeText(this, kidsData.getKids_id()+"", Toast.LENGTH_SHORT).show();

                        kidsName.setText("Hi, "+kidsData.getName().toString());
                        kidsAge.setText("You are "+kidsData.getAge()+" years old");
                        Log.i("KidsData",kidsData.getName()+"");
                    }
        });


    }
}