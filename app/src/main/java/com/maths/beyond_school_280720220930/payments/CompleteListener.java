package com.maths.beyond_school_280720220930.payments;

import com.razorpay.Subscription;

import org.json.JSONException;

public interface CompleteListener {
    public void onCompleteSubscriptionCancellation() throws JSONException;
}
