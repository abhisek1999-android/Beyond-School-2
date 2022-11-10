package com.maths.beyond_school_280720220930;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import com.maths.beyond_school_280720220930.adapters.ChaptersRecyclerAdapter;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeData;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.databinding.ActivityTestBinding;
import com.maths.beyond_school_280720220930.firebase.CallFirebaseForInfo;
import com.maths.beyond_school_280720220930.payments.CreateSubscription;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.razorpay.Plan;
import com.razorpay.Subscription;

import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        gradeDatabase = GradeDatabase.getDbInstance(this);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Checkout.preload(getApplicationContext());
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


    }

    public void buttonClick(View view) {

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_UG0wPL54QTV1fA");

        checkout.setImage(R.drawable.logo);
        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", "Beyond School");
            options.put("description", "Reference No. #123456");
            options.put("image", R.drawable.app_logo_v2);
            //  options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            //  options.put("theme.color", "");
            // options.put("subscription_id",subscription.get("id"));
            // options.put("subscription_id","sub_KdTfH6zI6M7mv4");
            options.put("currency", "INR");
            options.put("amount", "19900");//pass amount in currency subunits
            options.put("prefill.email", "");
            options.put("prefill.contact", "7908777407");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch (Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }

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