package com.maths.beyond_school_280720220930.payments;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.maths.beyond_school_280720220930.R;
import com.razorpay.Plan;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Subscription;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class FetchSubscriptionPlans  extends AsyncTask<Void, Void, List<Plan>> {


    List<Plan> planList;
    RazorpayClient razorpay;
    Context context;

    public FetchSubscriptionPlans(Context context){
        this.context=context;
    }

    @Override
    protected List<Plan> doInBackground(Void... voids) {


        try {
            razorpay = new RazorpayClient(context.getResources().getString(R.string.razorpay_api_key),
                    context.getResources().getString(R.string.razorpay_api_secret));
        } catch (RazorpayException e) {
            e.printStackTrace();
        }

        JSONObject params = new JSONObject();
        try {
            params.put("count","1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            planList = razorpay.plans.fetchAll(params);
            Log.d("TAG", "onCreate: ListOfSubs"+planList);

        } catch (RazorpayException e) {
            e.printStackTrace();
        }

        return planList;
    }

    @Override
    protected void onPostExecute(List<Plan> plans) {
        super.onPostExecute(plans);


    }


}
