package com.maths.beyond_school_280720220930.payments;

import android.content.Context;
import android.os.AsyncTask;

import com.maths.beyond_school_280720220930.R;
import com.razorpay.Customer;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateCustomer extends AsyncTask<Void, Void, Customer> {

    Customer customer;
    String phoneNumber;
    Context mContext;
    CompleteListener completeListener;

    public CreateCustomer(Context context, String phoneNumber, CompleteListener completeListener) {
        this.mContext = context;

        this.phoneNumber=phoneNumber;
        this.completeListener=completeListener;
    }

    @Override
    protected Customer doInBackground(Void... voids) {


        RazorpayClient razorpay = null;

        try {
            razorpay = new RazorpayClient(mContext.getResources().getString(R.string.razorpay_api_key),
                    mContext.getResources().getString(R.string.razorpay_api_secret));
        } catch (RazorpayException e) {
            e.printStackTrace();
        }

        JSONObject customerRequest = new JSONObject();
        try {
            customerRequest.put("name","");
            customerRequest.put("contact",phoneNumber);
            customerRequest.put("email",phoneNumber+"@beyondschool.live");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Customer customer = null;
        try {
            customer = razorpay.customers.create(customerRequest);
        } catch (RazorpayException e) {
            e.printStackTrace();
        }

        return customer;
    }

    @Override
    protected void onPostExecute(Customer customer) {
        super.onPostExecute(customer);
        completeListener.onCompleteCustomerCreation(customer);
    }

    public interface CompleteListener{

        public void onCompleteCustomerCreation(Customer customer);
    }
}
