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
    Context mContext;
    public CreateSubscription(Context context){
        this.mContext=context;
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

        subscriptionRequest.put("plan_id", "plan_KdRXbVQ5Db5BT4");
        subscriptionRequest.put("total_count", 12);
        subscriptionRequest.put("quantity", 1);
        subscriptionRequest.put("customer_notify", 1);
        subscriptionRequest.put("start_at", null);
        subscriptionRequest.put("expire_by", null);
        JSONObject notifyInfo = new JSONObject();
        notifyInfo.put("notify_phone","8346913181");
        notifyInfo.put("notify_phone","");
        subscriptionRequest.put("notify_info",notifyInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            subscription = razorpay.subscriptions.create(subscriptionRequest);
            Log.d("RazorPaySubs", "doInBackground: success");

        } catch (RazorpayException e) {
            Log.d("RazorPayExp", "doInBackground: "+e.getMessage());
            e.printStackTrace();
        }


        return subscription;
    }
}
