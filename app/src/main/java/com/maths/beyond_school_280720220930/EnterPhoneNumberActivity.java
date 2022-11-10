package com.maths.beyond_school_280720220930;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.maths.beyond_school_280720220930.databinding.ActivityEnterPhoneNumberBinding;
import com.maths.beyond_school_280720220930.signin_methods.PhoneNumberLogin;

import java.util.Objects;

public class EnterPhoneNumberActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private ActivityEnterPhoneNumberBinding  binding;
    private final static int RESOLVE_HINT = 1011;
    private ArrayAdapter adapter;
    private String[] array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityEnterPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpTextLayoutGrade();
        phoneSelection();
        binding.idBtnGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // below line is for checking weather the user
                // has entered his mobile number or not.
                if (TextUtils.isEmpty(binding.idEdtPhoneNumber.getText().toString())) {
                    // when mobile number text field is empty
                    // displaying a toast message.
                    Toast.makeText(EnterPhoneNumberActivity.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                } else {
                    // if the text field is not empty we are calling our
                    // send OTP method for getting OTP from Firebase.
                    String phone = binding.countryCode.getText().toString() + binding.idEdtPhoneNumber.getText().toString();
                    Intent intent=new Intent(getApplicationContext(),PhoneNumberLogin.class);
                    intent.putExtra("phoneNumber",phone);
                    startActivity(intent);

                }
            }
        });
    }

    private void phoneSelection() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.CREDENTIALS_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();
        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient, hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), RESOLVE_HINT, null, 0, 0, 0, null);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                if (credential != null) {
                    String number = credential.getId().replace(binding.countryCode.getText().toString(), "");
                    binding.idEdtPhoneNumber.setText(number);
                } else {
                    Toast.makeText(getApplicationContext(), "No phone numbers found", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    private void setUpTextLayoutGrade() {
        array = getResources().getStringArray(R.array.country_code);
        adapter = new ArrayAdapter(this, R.layout.list_item, array);
        AutoCompleteTextView editText = Objects.requireNonNull((AutoCompleteTextView) binding.textInputLayoutCountryCode.getEditText());
        editText.setAdapter(adapter);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}