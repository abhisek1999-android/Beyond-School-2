package com.maths.beyond_school_280720220930.payments;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.maths.beyond_school_280720220930.R;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Subscription;

import org.json.JSONException;

public class FetchSubscriptionStatus extends AsyncTask<Void, Void, Subscription> {


    RazorpayClient razorpay;
    Context context;
    String subscriptionId;
    Subscription subscription;
    CompleteListener completeListener;
    public FetchSubscriptionStatus(Context context,String subscriptionId,CompleteListener completeListener){
        this.context=context;
        this.subscriptionId=subscriptionId;
        this.completeListener=completeListener;
    }

    @Override
    protected Subscription doInBackground(Void... voids) {

        try {
            razorpay = new RazorpayClient(context.getResources().getString(R.string.razorpay_api_key),
                    context.getResources().getString(R.string.razorpay_api_secret));
        } catch (RazorpayException e) {
            e.printStackTrace();
        }
        try {
            subscription = razorpay.subscriptions.fetch(subscriptionId);
            Log.d("TAG", "doInBackground: "+subscription);
        } catch (RazorpayException e) {
            e.printStackTrace();
            Log.d("TAG", "doInBackground: Exception"+e.getMessage());
        }

        return subscription;
    }

    @Override
    protected void onPostExecute(Subscription plans) {
        super.onPostExecute(plans);

        try {
            completeListener.onCompleteSubscriptionCancellation();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
