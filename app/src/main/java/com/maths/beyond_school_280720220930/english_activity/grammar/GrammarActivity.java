package com.maths.beyond_school_280720220930.english_activity.grammar;

import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_GRAMMAR_CATEGORY;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.maths.beyond_school_280720220930.LearningActivity;
import com.maths.beyond_school_280720220930.LogActivity;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.databinding.ActivityGrammarBinding;
import com.maths.beyond_school_280720220930.databinding.AnimSingleLayoutBinding;
import com.maths.beyond_school_280720220930.model.AnimData;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.AnimationUtil;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class GrammarActivity extends AppCompatActivity {

    private static final String TAG = "GrammarActivity";
    private final Context context = GrammarActivity.this;
    private ActivityGrammarBinding binding;
    private Fragment currentFragment;
    private String category;
    private TextToSpeckConverter ttsHelperAnim;

    private int num=0;
    private List<AnimData> animEng;

    private LinearLayout addAnimLayout,finalView;
    private TextView descTextView, finalText;
    private Animation slideLeftAnim, slideRightAnim, fadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGrammarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setToolbar();
        getIntent().getStringExtra(EXTRA_GRAMMAR_CATEGORY);
        getDataFromIntent();

        binding.hintButton.setOnClickListener(v->{
            try {
                displayTutorialAnimation();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }


    private void initTTSHelperAnim() throws ExecutionException, InterruptedException {
        ttsHelperAnim = new TTSHelperAsyncTaskAnim().execute(new ConversionCallback[]{new ConversionCallback() {
            @Override
            public void onCompletion() {

                Log.i("Index_Size",num+"");
                if (num<animEng.size()) {

                    UtilityFunctions.runOnUiThread(() -> {

                        ttsHelperAnim.initialize(animEng.get(num).getDescription(), GrammarActivity.this);
                        animHandel(animEng.get(num).getAnswer(),animEng.get(num).getDescription(),animEng.get(num).getOperation());
                        num++;
                    }, 500);
                }
                else
                {
                    num=0;
                }
            }

            @Override
            public void onErrorOccurred(String errorMessage) {

                ttsHelperAnim.destroy();
            }
        }}).get();
    }



    private void displayTutorialAnimation() throws ExecutionException, InterruptedException {


        animEng= AnimationUtil.animGrammar(category);

        final AlertDialog.Builder alert = new AlertDialog.Builder(GrammarActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.tutorial_anim, null);

        num=0;
        ImageView closeButton = mView.findViewById(R.id.closeButton);
        addAnimLayout=mView.findViewById(R.id.insert_point);
        finalText=mView.findViewById(R.id.finalAns);
        finalView=mView.findViewById(R.id.finalView);
        descTextView=mView.findViewById(R.id.descTextView);

        initTTSHelperAnim();

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        String initText= "Hi kids, Let us learn about "+category;

        descTextView.setText(initText);
        ttsHelperAnim.initialize(initText,GrammarActivity.this);
        try {
            alertDialog.show();
        } catch (Exception e) {

        }

        closeButton.setOnClickListener(v -> {alertDialog.dismiss();
            ttsHelperAnim.stop();
        });

    }



    private void animHandel(String answer, String description, String operation) {

        slideLeftAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_left);
        slideRightAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.anim_single_layout, null);
        AnimSingleLayoutBinding binding=AnimSingleLayoutBinding.bind(view);

        binding.description.setText(description);
        binding.slNumView.setText((num+1)+".");

        binding.mathLayout.setVisibility(View.GONE);
        view.startAnimation(slideRightAnim);
        ViewGroup main = (ViewGroup) addAnimLayout;
        main.addView(view, num);


//        if (num==animEng.size()-1){
//
//            finalView.setVisibility(View.VISIBLE);
//            finalView.startAnimation(slideRightAnim);
//            finalText.setText(answer.split("_")[2]);
//        }


    }


    private void textButtonClick() {
        binding.giveTestButton.setOnClickListener(v -> {
            UtilityFunctions.simpleToast(context, "TODO : Implement Test");
        });
    }

    private void getDataFromIntent() {
        if (!getIntent().hasExtra(EXTRA_GRAMMAR_CATEGORY)) {
            UtilityFunctions.simpleToast(context, "No data found");
            return;
        }
        category = getIntent().getStringExtra(EXTRA_GRAMMAR_CATEGORY);
        binding.textViewCategory.setText(category.replace("Grammar", ""));
       // setViewPager();
    }

    private void setToolbar() {
        binding.toolBar.imageViewBack.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.toolBar.titleText.setText(getResources().getString(R.string.grammar));
        binding.textViewCategory.setText(getIntent().
                getStringExtra(EXTRA_GRAMMAR_CATEGORY)
                .replace("Grammar", ""));

        binding.toolBar.getRoot().inflateMenu(R.menu.log_menu);
        binding.toolBar.getRoot().setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_log) {
                startActivity(new Intent(getApplicationContext(), LogActivity.class));

                return true;
            }
            return super.onOptionsItemSelected(item);

        });
    }

    static class TTSHelperAsyncTaskAnim extends AsyncTask<ConversionCallback, Void, TextToSpeckConverter> {
        @Override
        protected TextToSpeckConverter doInBackground(ConversionCallback... conversionCallbacks) {
            return TextToSpeechBuilder.builder(conversionCallbacks[0]);
        }
    }

}