package com.maths.beyond_school_280720220930;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.databinding.ActivityManageSubscriptionBinding;
import com.maths.beyond_school_280720220930.extras.CustomProgressDialogue;
import com.maths.beyond_school_280720220930.payments.CancelSubscription;
import com.maths.beyond_school_280720220930.payments.CompleteListener;
import com.maths.beyond_school_280720220930.payments.FetchPlanDetails;
import com.maths.beyond_school_280720220930.payments.FetchSubscriptionStatus;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;
import com.razorpay.Plan;
import com.razorpay.Subscription;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;

public class ManageSubscription extends AppCompatActivity implements CancelSubscription.CompleteListener, CompleteListener {

    private Subscription subscription;
    private ActivityManageSubscriptionBinding binding;
    private String subscriptionId = "";
    private String customerId = "";
    private String planId = "";
    private Plan plan;
    private final String TAG="ManageSubscription";
    private CustomProgressDialogue customProgressDialogue;
    private int paymentAmount;
    private FirebaseAuth mAuth;
    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageSubscriptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        customProgressDialogue=new CustomProgressDialogue(ManageSubscription.this);

        firebaseAnalytics=FirebaseAnalytics.getInstance(this);
        mAuth=FirebaseAuth.getInstance();

        subscriptionId = PrefConfig.readIdInPref(ManageSubscription.this, getResources().getString(R.string.subscription_id));
        customerId = PrefConfig.readIdInPref(ManageSubscription.this, getResources().getString(R.string.customer_id));
        planId=PrefConfig.readIdInPref(ManageSubscription.this,getResources().getString(R.string.plan_id));
        paymentAmount=PrefConfig.readIntDInPref(ManageSubscription.this,getResources().getString(R.string.plan_value));
        Log.d(TAG, "onCreate: "+subscriptionId);
        binding.toolBar.titleText.setText("Manage Subscription");
        binding.gotoPaymentPage.setText("Subscribe @ Rs "+paymentAmount+"/ Month");
        binding.toolBar.imageViewBack.setOnClickListener(v->{
            onBackPressed();
        });



        binding.infoCard.setVisibility(View.INVISIBLE);
        customProgressDialogue.show();

        UtilityFunctions.runOnUiThread(()->{


            try {
                plan = new FetchPlanDetails(ManageSubscription.this, planId,this).execute().get();

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                if (subscriptionId.equals(""))
                    checkSubscriptionValid();
                else {
                    Log.d(TAG, "onCreate: "+subscriptionId);
                    subscription = new FetchSubscriptionStatus(ManageSubscription.this, subscriptionId,this).execute().get();
                    checkSubscriptionValid();
                }

            } catch (ExecutionException e) {
                customProgressDialogue.dismiss();
                e.printStackTrace();
            } catch (InterruptedException e) {
                customProgressDialogue.dismiss();
                e.printStackTrace();
            } catch (JSONException e) {
                customProgressDialogue.dismiss();
                e.printStackTrace();
            }
        },100);


        binding.cancelSubscription.setOnClickListener(v -> {
            customProgressDialogue.show();
            new CancelSubscription(ManageSubscription.this, subscriptionId, this).execute();
        });

        binding.gotoPaymentPage.setOnClickListener(v->{
            startActivity(new Intent(ManageSubscription.this,PaymentActivity.class));
        });

        binding.completePayment.setOnClickListener(v->{
            startActivity(new Intent(ManageSubscription.this,PaymentActivity.class));
        });
    }

    public void checkSubscriptionValid() throws JSONException {

        try{
        if (subscriptionId.equals("")||subscription.get("status").equals("cancelled")) {
            Log.d(TAG, "checkSubscriptionValid: if");
            binding.subscriptionTitle.setText("You don't have any plan yet");
            binding.infoSubscription.setVisibility(View.GONE);
            binding.gotoPaymentPage.setVisibility(View.VISIBLE);
            binding.subscriptionStatus.setVisibility(View.GONE);
            binding.cancelSubscription.setVisibility(View.INVISIBLE);
            binding.completePayment.setVisibility(View.GONE);
        } else {
            setUiElements(subscription);
            Log.d(TAG, "checkSubscriptionValid: else");
        }}catch (Exception e){
            Log.d(TAG, "checkSubscriptionValid: "+e.getMessage());
            binding.subscriptionTitle.setText("You don't have any plan yet");
            binding.infoSubscription.setVisibility(View.GONE);
            binding.gotoPaymentPage.setVisibility(View.VISIBLE);
            binding.subscriptionStatus.setVisibility(View.GONE);
            binding.cancelSubscription.setVisibility(View.INVISIBLE);
            binding.completePayment.setVisibility(View.GONE);
        }

        customProgressDialogue.dismiss();

    }

    public void setUiElements(Subscription subscription) throws JSONException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM, yyyy");
        binding.subscriptionTitle.setText(plan.toJson().getJSONObject("item").get("name").toString());
        binding.subscriptionPrice.setText((plan.toJson().getJSONObject("item").getInt("amount") / 100) + " " + plan.toJson().getJSONObject("item").get("currency") + "/ Month");

        if (subscription.get("status").equals("active")){
            binding.subscriptionStatus.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.primary));
            binding.cancelSubscription.setVisibility(View.VISIBLE);
            binding.completePayment.setVisibility(View.GONE);
        }
        binding.subscriptionStatus.setText(subscription.get("status").toString().toUpperCase());
        binding.subscriptionId.setText("Subscription Id:" + subscription.get("id"));
        try {
            binding.subscriptionRenewDate.setText("Due on: " + simpleDateFormat.format(new java.util.Date((long) subscription.toJson().getLong("current_end") * 1000)));
        }catch (Exception e){}

        customProgressDialogue.dismiss();
    }

    @Override
    public void onCompleteSubscriptionCancellation(Subscription subscription) throws JSONException {
        subscriptionId = "";
        PrefConfig.writeIdInPref(ManageSubscription.this, "", getResources().getString(R.string.subscription_id));
        PrefConfig.writeIdInPref(this,  "cancelled",getResources().getString(R.string.payment_status));
        UtilityFunctions.attemptPayment(firebaseAnalytics,mAuth,PrefConfig.readIdInPref(ManageSubscription.this,getResources().getString(R.string.parent_contact_details)),"N/A",subscriptionId,paymentAmount,"cancelled");
        checkSubscriptionValid();
        customProgressDialogue.dismiss();
    }

    @Override
    public void onCompleteSubscriptionCancellation() throws JSONException {
        customProgressDialogue.dismiss();
        binding.infoCard.setVisibility(View.VISIBLE);
    }
}