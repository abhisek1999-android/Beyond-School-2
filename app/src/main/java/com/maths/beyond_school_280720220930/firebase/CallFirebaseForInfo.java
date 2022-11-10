package com.maths.beyond_school_280720220930.firebase;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.model.KidsActivity;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;
import com.razorpay.PaymentData;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CallFirebaseForInfo  {

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
    
    public  static void  checkActivityData(FirebaseFirestore firebaseFirestore, JSONArray kidsJsonArray, String status, FirebaseAuth auth, String kidsId, String chapters, String subSub, int correctAnswer, int wrongAnswer, int noQuestion, String subject) throws JSONException {


        DocumentReference kidsActivityRef= firebaseFirestore.collection("users").document(auth.getCurrentUser().getUid())
                .collection("kids").document(kidsId).collection("grades").document("grade1").collection("test").document(subject+"_"+subSub+"_"+chapters);
        JSONObject kidsActivityJsonObj = new JSONObject();
        kidsActivityJsonObj.put("result", kidsJsonArray);

        Map<String, Object> activityData = new HashMap<>();
        activityData.put("time_stamp", new Date().getTime());
        activityData.put("result", kidsActivityJsonObj.toString());
        activityData.put("status", status);
        activityData.put("correct",correctAnswer);
        activityData.put("wrong",wrongAnswer);
        activityData.put("total_count",noQuestion);


        kidsActivityRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()){

                KidsActivity kidsActivity=documentSnapshot.toObject(KidsActivity.class);
                if (!kidsActivity.getStatus().equals("pass")){

                    storeActivityData(kidsActivityRef,activityData);
                }
                else{

                    updateKidsData(kidsActivityRef,kidsActivity.getCorrect()+correctAnswer,kidsActivity.getWrong()+wrongAnswer,kidsActivity.getTotal_count()+noQuestion);
                }
            }
            else{
                storeActivityData(kidsActivityRef,activityData);
            }

        });


        
        
    }
    public  static void  checkActivityData(FirebaseFirestore firebaseFirestore, JSONArray kidsJsonArray, String status, FirebaseAuth auth, String kidsId,String grade, String chapters, String subSub, int correctAnswer, int wrongAnswer, int noQuestion, String subject) throws JSONException {


        DocumentReference kidsActivityRef= firebaseFirestore.collection("users").document(auth.getCurrentUser().getUid())
                .collection("kids").document(kidsId).collection("grades").document(grade).collection("test").document(subject+"_"+subSub+"_"+chapters);
        JSONObject kidsActivityJsonObj = new JSONObject();
        kidsActivityJsonObj.put("result", kidsJsonArray);

        Map<String, Object> activityData = new HashMap<>();
        activityData.put("time_stamp", new Date().getTime());
        activityData.put("result", kidsActivityJsonObj.toString());
        activityData.put("status", status);
        activityData.put("correct",correctAnswer);
        activityData.put("wrong",wrongAnswer);
        activityData.put("total_count",noQuestion);


        kidsActivityRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()){

                KidsActivity kidsActivity=documentSnapshot.toObject(KidsActivity.class);
                if (!kidsActivity.getStatus().equals("pass")){

                    storeActivityData(kidsActivityRef,activityData);
                }
                else{

                    updateKidsData(kidsActivityRef,kidsActivity.getCorrect()+correctAnswer,kidsActivity.getWrong()+wrongAnswer,kidsActivity.getTotal_count()+noQuestion);
                }
            }
            else{
                storeActivityData(kidsActivityRef,activityData);
            }

        });




    }

    private static void updateKidsData(DocumentReference kidsActivityRef, long correct, long wrong, long total) {


        kidsActivityRef.update("correct",correct,"wrong",wrong,"total_count",total).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });


    }

    public  static void  storeActivityData(DocumentReference kidsActivityRef, Map<String, Object> activityData) {

        kidsActivityRef
                .set(activityData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Log.d(TAG, "DocumentSnapshot successfully written!");
                        //    Toast.makeText(getApplicationContext(),"added",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //  Log.w(TAG, "Error writing document", e);
                    }
                });


    }


    public static boolean checkPaymentStatus(){
        return false;
    }

    public static void addPaymentInfo(FirebaseFirestore firebaseFirestore, FirebaseAuth mAuth, Boolean isPaymentDone, PaymentData paymentData,Context mContext) {


        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd MMM,yyyy");
        Map paymentMap=new HashMap();
        paymentMap.put("isPaymentDone",isPaymentDone);
        paymentMap.put("date",date.toString());
        paymentMap.put("payment_id",paymentData.getPaymentId());
        paymentMap.put("payment_info",paymentData.getData().toString());

        firebaseFirestore.collection("users").document(mAuth.getCurrentUser().getUid()).collection("payments")
                .document(simpleDateFormat.format(date).toString()).set(paymentMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("FirebasePaymentData", "onSuccess: ");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext, "Data not Saved", Toast.LENGTH_SHORT).show();
                        Log.d("FirebasePaymentData", "onFailure: ");
                    }
                });


    }

    public static void upDateActivities(FirebaseFirestore kidsDb, FirebaseAuth mAuth, String kids_id, String grade, Context context, GradeDatabase database) {
        CollectionReference kidsActivityRef= kidsDb.collection("users").document(mAuth.getCurrentUser().getUid())
                .collection("kids").document(kids_id).collection("grades").document(grade).collection("test");

        kidsActivityRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "onComplete:Firebase Update ");
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        KidsActivity kidsActivity=document.toObject(KidsActivity.class);


                        if (kidsActivity.getStatus().equals("pass")){

                            try{
                                if (!document.getId().split("_")[1].equals("multiplication")){
                                    Log.i("doc_id",document.getId().replace(" ","").split("_")[1]);
                                    UtilityFunctions.updateDbUnlock(database,document.getId().replace(" ","").split("_")[1],document.getId().split("_")[2]);
                                }
                                else{
                                    Log.i("doc_id",document.getId().replace(" ","").split("_")[2].split("\\(")[1].split("")[0]);
                                    int maxVal=Integer.parseInt(document.getId().replace(" ","").split("_")[2].split("\\(")[1].split("")[0]);
                                    if (PrefConfig.readIntInPref(context,context.getResources().getString(R.string.multiplication_upto))<maxVal){
                                        PrefConfig.writeIntInPref(context,maxVal,context.getResources().getString(R.string.multiplication_upto));
                                    }

                                }
                            }catch (Exception e){}


                        }
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });

    }



    public static void upDateActivities(FirebaseFirestore kidsDb, FirebaseAuth mAuth, String kids_id, String grade, Context context, GradeDatabase database,Callback callback) {
        CollectionReference kidsActivityRef= kidsDb.collection("users").document(mAuth.getCurrentUser().getUid())
                .collection("kids").document(kids_id).collection("grades").document(grade).collection("test");

        kidsActivityRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "onComplete:Firebase Update ");
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        KidsActivity kidsActivity=document.toObject(KidsActivity.class);


                        if (kidsActivity.getStatus().equals("pass")){

                            try{
                                if (!document.getId().split("_")[1].equals("multiplication")){
                                    Log.i("doc_id",document.getId().replace(" ","").split("_")[1]);
                                    UtilityFunctions.updateDbUnlock(database,document.getId().replace(" ","").split("_")[1],document.getId().split("_")[2]);
                                }
                                else{
                                    Log.i("doc_id",document.getId().replace(" ","").split("_")[2].split("\\(")[1].split("")[0]);
                                    int maxVal=Integer.parseInt(document.getId().replace(" ","").split("_")[2].split("\\(")[1].split("")[0]);
                                    if (PrefConfig.readIntInPref(context,context.getResources().getString(R.string.multiplication_upto))<maxVal){
                                        PrefConfig.writeIntInPref(context,maxVal,context.getResources().getString(R.string.multiplication_upto));
                                    }

                                }
                            }catch (Exception e){}


                        }
                    }

                    callback.dataUpdated();
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });

    }

    public interface  Callback{
        public void dataUpdated();
    }

}
