package com.maths.beyond_school_new_071020221400;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import com.maths.beyond_school_new_071020221400.adapters.ChaptersRecyclerAdapter;
import com.maths.beyond_school_new_071020221400.database.grade_tables.GradeData;
import com.maths.beyond_school_new_071020221400.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_new_071020221400.databinding.ActivityTestBinding;
import com.maths.beyond_school_new_071020221400.firebase.CallFirebaseForInfo;
import com.maths.beyond_school_new_071020221400.translation_engine.ConversionCallback;
import com.maths.beyond_school_new_071020221400.translation_engine.SpeechToTextBuilder;
import com.maths.beyond_school_new_071020221400.translation_engine.translator.SpeechToTextConverter;
import com.maths.beyond_school_new_071020221400.translation_engine.translator.SpeechToTextConverterVosk;
import com.maths.beyond_school_new_071020221400.utils.UtilityFunctions;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.razorpay.Plan;
import com.razorpay.Subscription;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TestActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private String TAG = "TestActivity";

    private GradeDatabase gradeDatabase;
    private ActivityTestBinding binding;
    private ChaptersRecyclerAdapter chaptersRecyclerAdapter;
    private List<GradeData> chapterList;
    private List<Plan> plansList;
    private Subscription subscription;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private SpeechToTextConverterVosk stt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        gradeDatabase = GradeDatabase.getDbInstance(this);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

//        Checkout.preload(getApplicationContext());
//        try {
//            subscription = new CreateSubscription(TestActivity.this).execute().get();
////            Log.d(TAG, "onCreate: subscription"+subscription.get("id"));
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        try {
//            plansList= new FetchSubscriptionPlans(TestActivity.this).execute().get();
//            Log.d(TAG, "onCreate: List"+plansList.get(0).get("id"));
//            binding.paymentInfo.setText(plansList.get(0).get("id")+"\n Amount:"+Integer.parseInt(plansList.get(0).get("amount"))/100);
//        } catch (ExecutionException e) {
//            Log.d(TAG, "onCreate:Err "+e.getMessage());
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


//        stt= SpeechToTextBuilder.builderVosk(new ConversionCallback() {
//
//            @Override
//            public void onSuccess(String result) throws JSONException {
//                ConversionCallback.super.onSuccess(result);
//
//                if (!result.equals("-10001")){
//                try {
//
//                    Log.d(TAG, "onSuccess: "+result);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.d(TAG, "onSuccess: "+e.getMessage());
//                }}
//
//            }
//
//            @Override
//            public void onCompletion() {
//                Log.d(TAG, "onCompletion: ");
//            }
//
//            @Override
//            public void onErrorOccurred(String errorMessage) {
//
//                Log.d(TAG, "onErrorOccurred: "+errorMessage);
//
//            }
//        });


        initSTT();
        stt.initialize("",TestActivity.this);

    }


    private void initSTT() {
        stt = SpeechToTextBuilder.builderVosk(new ConversionCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(String result) {

                Log.i(TAG, "On Succcess" + result);

                if (!result.equals("-10001")){
                    try {

                        Log.d(TAG, "onSuccess: "+result);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "onSuccess: "+e.getMessage());
                    }}
            }


            @Override
            public void onCompletion() {
                Log.d(TAG, "onCompletion: Done");

            }

            @Override
            public void getLogResult(String title) {
                Log.i("INLOG", title);
                ConversionCallback.super.getLogResult(title);

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void getStringResult(String title) throws JSONException {
                ConversionCallback.super.getStringResult(title);
//                Log.i("SoundXCalled", title + ",title: " + Soundex.getCode(title) + ", ans:" + Soundex.getCode(UtilityFunctions.numberToWords(currentAnswer)));
//                if (Soundex.getCode(title).equals(Soundex.getCode(UtilityFunctions.numberToWords(currentAnswer)))) {
//                    successResultCalling(currentAnswer + "");
//                } else {
//                    // Changes
//                    try {
//                        UtilityFunctions.runOnUiThread(() -> {
//                            isCallSTT = true;
//                            tts.initialize("", LearningActivityNew.this);
//                        }, 250);
//                    } catch (Exception e) {
//                        Log.i(TAG, "Inside String res");
//                        pause();
//                    }
//                }


            }

            @Override
            public void onErrorOccurred(String errorMessage) {

//                Log.i(TAG, "Inside err" + errorMessage);
//                if (errorMessage.equals("No match")) {
//                    try {
//                        UtilityFunctions.runOnUiThread(() -> {
//                            isCallSTT = true;
//                            tts.initialize("", LearningActivityNew.this);
//                        }, 250);
//
//                    } catch (Exception e) {
//                        Log.i(TAG, "Inside err");
//                        pause();
//                    }
//                } else {
//
//                    Log.i(TAG, "InsideErrElse" + errorMessage + isCallSTT + isCallTTS + isAnsByTyping);
//                    // binding.playPause.setChecked(false);
//                    //pause();
//                }


            }
        });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stt.destroy();
    }

    public void buttonClick(View view) {
        stt.pause(binding.toggleButton.isChecked());

    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        CallFirebaseForInfo.addPaymentInfo(firebaseFirestore, mAuth, true, paymentData, this);
        Log.d(TAG, "onPaymentSuccess: "+paymentData.getPaymentId());
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        CallFirebaseForInfo.addPaymentInfo(firebaseFirestore, mAuth, false, paymentData, this);
        Log.d(TAG, "onPaymentErr: "+paymentData.toString());
    }


}