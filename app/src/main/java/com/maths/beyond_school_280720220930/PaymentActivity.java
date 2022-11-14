package com.maths.beyond_school_280720220930;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.databinding.ActivityPaymentBinding;
import com.maths.beyond_school_280720220930.extras.CustomProgressDialogue;
import com.maths.beyond_school_280720220930.firebase.CallFirebaseForInfo;
import com.maths.beyond_school_280720220930.payments.CreateCustomer;
import com.maths.beyond_school_280720220930.payments.CreateSubscription;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;
import com.razorpay.Checkout;
import com.razorpay.Customer;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.razorpay.Subscription;

import org.json.JSONObject;

import java.util.Date;

public class PaymentActivity extends AppCompatActivity implements CreateSubscription.CompleteListener, PaymentResultWithDataListener, CreateCustomer.CompleteListener {


    // id abhisek---> py6zLsbEIHcai6JkCyLs2R1Xfj33
    private String parentsPhoneNumber = "";
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private static final String TAG = "PaymentActivity";
    private String planId = "";
    private String customerId = "";
    private String subscriptionId = "";
    private ActivityPaymentBinding binding;
    private CustomProgressDialogue progressDialogue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        customerId = PrefConfig.readIdInPref(PaymentActivity.this, getResources().getString(R.string.customer_id));
        subscriptionId = PrefConfig.readIdInPref(PaymentActivity.this, getResources().getString(R.string.subscription_id));
        planId=PrefConfig.readIdInPref(PaymentActivity.this,getResources().getString(R.string.plan_id));
        progressDialogue = new CustomProgressDialogue(PaymentActivity.this);

        progressDialogue.show();

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        parentsPhoneNumber = PrefConfig.readIdInPref(this, getResources().getString(R.string.parent_contact_details));


        if (!customerId.equals("") && !subscriptionId.equals(""))
            startPayment(subscriptionId, customerId);
        else {
            if (customerId.equals(""))
                new CreateCustomer(PaymentActivity.this, parentsPhoneNumber, this).execute();
            else if (!customerId.equals(""))
                new CreateSubscription(PaymentActivity.this, planId, parentsPhoneNumber, customerId, this).execute();
            }

        binding.gotoHomeScreen.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), TabbedHomePage.class));
            finish();
        });


//       startPayment("sub_Kfn4KkFy0ExhqZ","cust_Kfn4JIQH98LK2x");
    }


    private void startPayment(String subscriptionId, String customerId) {

        Checkout checkout = new Checkout();
        checkout.setKeyID(getResources().getString(R.string.razorpay_api_key));

        checkout.setImage(R.drawable.logo);
        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", "Beyond School");
            options.put("description", "Reference No. #" + new Date().getTime());
            options.put("image", R.drawable.app_logo_v2);
            //options.put("customer_id",customerId);
            options.put("subscription_id", subscriptionId);
            options.put("recurring", true);
            options.put("currency", "INR");
            options.put("contact", parentsPhoneNumber);

            //options.put("amount", "19900");//pass amount in currency subunits

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
    public void onCompleteSubscription(Subscription subscription, String customerId) {
        Log.d(TAG, "onCompleteSubscription: ");
        progressDialogue.dismiss();
        CallFirebaseForInfo.setSubscriptionId(firebaseFirestore, mAuth, subscription.get("id"),subscription.get("plan_id"), PrefConfig.readIdInPref(PaymentActivity.this, getResources().getString(R.string.customer_id)));
        PrefConfig.writeIdInPref(PaymentActivity.this, subscription.get("id"), getResources().getString(R.string.subscription_id));
        startPayment(subscription.get("id"), customerId);
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        CallFirebaseForInfo.addPaymentInfo(firebaseFirestore, mAuth, true, paymentData, this);
        PrefConfig.writeIdInPref(PaymentActivity.this, "active", getResources().getString(R.string.payment_status));
        binding.statusText.setText("Payment Successful\nPayment Id: " + paymentData.getPaymentId());
        progressDialogue.dismiss();
        Log.d(TAG, "onPaymentSuccess: " + paymentData.getPaymentId());
        binding.gotoHomeScreen.setVisibility(View.VISIBLE);
        binding.statusLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        progressDialogue.dismiss();
        CallFirebaseForInfo.addPaymentInfo(firebaseFirestore, mAuth, false, paymentData, this);
        PrefConfig.writeIdInPref(PaymentActivity.this, "Inactive", getResources().getString(R.string.payment_status));
        binding.statusImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_wrong));
        binding.statusImage.setImageTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.sweet_red));
        binding.statusText.setText("Payment Failed");
        Log.d(TAG, "onPaymentErr: " + i + ", Payment data" + paymentData.getData() + "");
        binding.gotoHomeScreen.setVisibility(View.VISIBLE);
        binding.statusLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCompleteCustomerCreation(Customer customer) {

        new CreateSubscription(PaymentActivity.this, planId, parentsPhoneNumber, customer.get("id"), this).execute();
        PrefConfig.writeIdInPref(PaymentActivity.this, customer.get("id"), getResources().getString(R.string.customer_id));
    }
}