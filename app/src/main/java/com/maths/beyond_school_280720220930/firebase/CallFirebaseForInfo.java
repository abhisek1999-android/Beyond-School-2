package com.maths.beyond_school_280720220930.firebase;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.maths.beyond_school_280720220930.model.KidsData;
import com.maths.beyond_school_280720220930.utils.Utils;

public class CallFirebaseForInfo {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser mCurrentUser=mAuth.getCurrentUser();
    QuerySnapshot qD = null;

    public QuerySnapshot retrieveKidsInfo(){

        firebaseFirestore.collection("users").document(mCurrentUser.getUid()).collection("kids").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        //     Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
                        Log.i("No_data", "No_data");
                    } else {
                        qD=queryDocumentSnapshots;
                    }
                });

        return qD;

    }



}
