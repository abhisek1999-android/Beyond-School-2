package com.maths.beyond_school_280720220930.payments;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.maths.beyond_school_280720220930.R;
import com.razorpay.Plan;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

public class FetchPlanDetails extends AsyncTask<Void, Void, Plan> {


    RazorpayClient razorpay;
    Context context;
    String planId;
    Plan plan;
    public FetchPlanDetails(Context context, String planId){
        this.context=context;
        this.planId=planId;
    }

    @Override
    protected Plan doInBackground(Void... voids) {

        try {
            razorpay = new RazorpayClient(context.getResources().getString(R.string.razorpay_api_key),
                    context.getResources().getString(R.string.razorpay_api_secret));
        } catch (RazorpayException e) {
            e.printStackTrace();
        }
        try {
            plan = razorpay.plans.fetch(planId);
            Log.d("TAG", "doInBackground: "+plan);
        } catch (RazorpayException e) {
            e.printStackTrace();
            Log.d("TAG", "doInBackground: Exception"+e.getMessage());
        }

        return plan;
    }

    @Override
    protected void onPostExecute(Plan plans) {
        super.onPostExecute(plans);

    }




}
