package com.maths.beyond_school_280720220930.payments;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.maths.beyond_school_280720220930.R;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Subscription;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class CreateSubscription extends AsyncTask<Void, Void, Subscription> {

    Subscription subscription;
    String plan,phoneNumber,customerId;
    Context mContext;
    CompleteListener completeListener;

    public CreateSubscription(Context context, String plan,String phoneNumber,String customerId,CompleteListener completeListener) {
        this.mContext = context;
        this.plan = plan;
        this.phoneNumber=phoneNumber;
        this.completeListener=completeListener;
        this.customerId=customerId;
    }

    @Override
    protected Subscription doInBackground(Void... voids) {


        RazorpayClient razorpay = null;

        try {
            razorpay = new RazorpayClient(mContext.getResources().getString(R.string.razorpay_api_key),
                    mContext.getResources().getString(R.string.razorpay_api_secret));
        } catch (RazorpayException e) {
            e.printStackTrace();
        }

        JSONObject subscriptionRequest = new JSONObject();
        try {

            subscriptionRequest.put("plan_id", plan);
            subscriptionRequest.put("total_count", 12);
            subscriptionRequest.put("quantity", 1);
            subscriptionRequest.put("customer_id", customerId);
            subscriptionRequest.put("customer_notify", 1);

            subscriptionRequest.put("start_at", null);
            subscriptionRequest.put("expire_by", null);
            JSONObject notifyInfo = new JSONObject();
            notifyInfo.put("notify_phone", phoneNumber);
            notifyInfo.put("notify_email", "abcd@gmail.com");
            subscriptionRequest.put("notify_info", notifyInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            subscription = razorpay.subscriptions.create(subscriptionRequest);
            Log.d("RazorPaySubs", "doInBackground: success");

        } catch (RazorpayException e) {
            Log.d("RazorPayExp", "doInBackground: " + e.getMessage());
            e.printStackTrace();
        }


        return subscription;
    }

    @Override
    protected void onPostExecute(Subscription subscription) {
        super.onPostExecute(subscription);
        completeListener.onCompleteSubscription(subscription,customerId);
    }

    public interface CompleteListener{

        public void onCompleteSubscription(Subscription subscription,String customerId);
    }
}
