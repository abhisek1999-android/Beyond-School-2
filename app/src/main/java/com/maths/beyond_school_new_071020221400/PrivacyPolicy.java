package com.maths.beyond_school_new_071020221400;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.maths.beyond_school_new_071020221400.databinding.ActivityAdditionBinding;
import com.maths.beyond_school_new_071020221400.databinding.ActivityPrivacyPolicyBinding;

public class PrivacyPolicy extends AppCompatActivity {


    private ActivityPrivacyPolicyBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        binding =ActivityPrivacyPolicyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolBar.titleText.setText("Privacy Policy");

        binding.toolBar.imageViewBack.setOnClickListener(v->{onBackPressed();});



        binding.privacyPolicyView.getSettings().setBuiltInZoomControls(true);
        binding.privacyPolicyView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        binding.privacyPolicyView.setBackgroundColor(Color.TRANSPARENT);
        binding.privacyPolicyView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_INSET);
        binding.privacyPolicyView.getSettings().setJavaScriptEnabled(true);
        binding.privacyPolicyView.getSettings().setDomStorageEnabled(true);
        binding.privacyPolicyView.getSettings().setPluginState(WebSettings.PluginState.ON);
        binding.privacyPolicyView.getSettings().setAllowFileAccess(true);
        binding.privacyPolicyView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });

        binding.privacyPolicyView.loadUrl("https://shashwatgupta.notion.site/BeyondSchool-Privacy-Policy-48e11ee1de54429d9fbcb6ed9330910f");
    }
}