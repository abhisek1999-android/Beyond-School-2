package com.maths.beyond_school_280720220930.english_activity.expression;

import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_EXPRESSION_DETAIL;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_SPELLING_DETAIL;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.maths.beyond_school_280720220930.LogActivity;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_280720220930.database.english.expression.ExpressionDao;
import com.maths.beyond_school_280720220930.database.english.expression.model.ExpressionCategoryModel;
import com.maths.beyond_school_280720220930.database.english.expression.model.ExpressionDetails;
import com.maths.beyond_school_280720220930.database.english.spelling.model.SpellingCategoryModel;
import com.maths.beyond_school_280720220930.database.english.spelling.model.SpellingDetail;
import com.maths.beyond_school_280720220930.database.log.LogDatabase;
import com.maths.beyond_school_280720220930.database.process.ProgressDataBase;
import com.maths.beyond_school_280720220930.database.process.ProgressM;
import com.maths.beyond_school_280720220930.databinding.ActivityExpressionBinding;
import com.maths.beyond_school_280720220930.dialogs.HintDialog;
import com.maths.beyond_school_280720220930.english_activity.spelling.EnglishSpellingActivity;
import com.maths.beyond_school_280720220930.english_activity.spelling.spelling_test.SpellingTest;
import com.maths.beyond_school_280720220930.english_activity.vocabulary.EnglishViewPager;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.SpeechToTextBuilder;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.SpeechToTextConverterEnglish;
import com.maths.beyond_school_280720220930.translation_engine.translator.SpeechToTextConverterSpelling;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.CollectionUtils;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
@RequiresApi(api = Build.VERSION_CODES.N)
public class ExpressionActivity extends AppCompatActivity {

    private ActivityExpressionBinding binding;
    private static final String TAG = ExpressionActivity.class.getSimpleName();
    private static final int MAX_TRY = 5 /* Giver u three chance */;
    private static final int DELAY_TIME = 800;
    private final int REQUEST_FOR_DES = 345 * 34;
    private final int REQUEST_FOR_QUESTION = 345 * 35;

    private List<ProgressM> progressData;
    private ProgressDataBase progressDataBase;
    private LogDatabase logDatabase;
    private ExpressionDao expressionDao;
    private UtilityFunctions.Expression expression=null;
    private List<ExpressionDetails> expressionDetails;
    private List<Fragment> fragments;
    private MediaPlayer mediaPlayer = null;
    private TextToSpeckConverter tts = null;
    private TextToSpeckConverter ttsHelper = null;
    private SpeechToTextConverterEnglish stt = null;
    private int replaceChar=0;
    private Boolean isSpeaking = false;
    private Boolean isSayWordFinish = true;
    private int currentTryCount = 0;
    private String logs = "";
    private long startTime = 0, endTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityExpressionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        logDatabase = LogDatabase.getDbInstance(this);
        progressDataBase = ProgressDataBase.getDbInstance(this);
        progressData = new ArrayList<>();
        setToolbar();
        setData();
    }

    private void setToolbar() {

        binding.toolBar.titleText.setText(getResources().getString(R.string.expression));
        binding.toolBar.imageViewBack.setOnClickListener(v -> onBackPressed());
        binding.toolBar.getRoot().inflateMenu(R.menu.log_menu);
        binding.toolBar.getRoot().setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.action_log) {
                startActivity(new Intent(getApplicationContext(), LogActivity.class));

                return true;
            }
            return super.onOptionsItemSelected(item);

        });
    }




    private void setData(){
        if (getIntent().hasExtra(EXTRA_EXPRESSION_DETAIL)) {
            expression = UtilityFunctions.getExpressionFromString(getIntent().getStringExtra(EXTRA_EXPRESSION_DETAIL).trim());
            expressionDao = EnglishGradeDatabase.getDbInstance(this).expressionDao();
            setViews();
            buttonClick();
            binding.giveTestButton.setOnClickListener((view) -> {
              //  navigateToTest();
            });
        } else {
            UtilityFunctions.simpleToast(this, "No data found");

        }
    }

    private void buttonClick() {
        binding.playPause.setOnClickListener(view -> {
            isSpeaking = binding.playPause.isChecked();
            startEngine();
            //timer();
        });

    }


    private void startEngine() {
        if (binding.playPause.isChecked()) {
            try {
                initTTS();
                intSTT();
                initMediaPlayer();
                startSpeaking();

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            destroyedEngines();
            isSayWordFinish = true;
        }
    }


    private void initMediaPlayer() {
        mediaPlayer = UtilityFunctions.playClapSound(this);
    }


    private void startSpeaking() throws ExecutionException, InterruptedException {
        isSpeaking = true;
        UtilityFunctions.runOnUiThread(() -> playPauseAnimation(true));
        helperTTS(getQuestion(), false, 3 * 46);
//        ttsHelper.setTextViewAndSentence(UtilityFunctions.addComma(spellingDetails.get(binding.viewPager.getCurrentItem()).getWord()));
//        Question
        logs += "Question : " + expressionDetails.get(binding.viewPager.getCurrentItem()).getQuestion() + " : " + expressionDetails.get(binding.viewPager.getCurrentItem()).getDefinition() + ". \n";

//        Stop when reach ot last item
        if (binding.viewPager.getCurrentItem() == (expressionDetails.size() - 1)) {
            isSpeaking = false;
            //       displayCompleteDialog();
        }
    }



    private void helperTTS(String message, boolean canNavigate, int request) throws
            ExecutionException, InterruptedException {
        ttsHelper = new ExpressionActivity.TTSHelperAsyncTask().execute(new ConversionCallback() {
                    @Override
                    public void onCompletion() {
                        if (request == 3 * 46) {
                            try {
                                helperTTS("Example : ", false, REQUEST_FOR_DES);
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            return;
                        }

                        if (request == 3 * 45) {
//                            try {
//                                helperTTS(UtilityFunctions.addComma(expressionDetails.get(binding.viewPager.getCurrentItem()).getWord())
//                                        , false, 3 * 46);
//
//                            } catch (ExecutionException | InterruptedException e) {
//                                e.printStackTrace();
//                            }
                            return;
                        }

                        if (request == 2 * 45) {
                            UtilityFunctions.runOnUiThread(() -> {
                                var current = (ExpressionFragment) fragments.get(binding.viewPager.getCurrentItem());
                                current.getAnimationView().setVisibility(View.VISIBLE);
                                stt.initialize("", ExpressionActivity.this);
                            }, 800);
                            return;
                        }
                        if (request == REQUEST_FOR_QUESTION && !canNavigate) {
                            try {
                                startSpeaking();
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                        if (request == REQUEST_FOR_DES && !canNavigate) {
                            tts.setTextViewAndSentence(expressionDetails.get(binding.viewPager.getCurrentItem()).getDefinition());
                            tts.initialize(getDescription(), ExpressionActivity.this);
                            return;
                        }
                        if (canNavigate) {
                            UtilityFunctions.runOnUiThread(() -> {
                                try {
                                    if (mediaPlayer != null)
                                        mediaPlayer.pause();
                                } catch (IllegalStateException e) {
                                    e.printStackTrace();
                                }

                                var currentFrag=(ExpressionFragment)fragments.get(binding.viewPager.getCurrentItem());
                                currentFrag.getTextView().setText(expressionDetails.get(binding.viewPager.getCurrentItem()).getQuestion().replaceAll("[A-Za-z]", " _ "));
                                binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
                                isSayWordFinish = true;
                                if (isSpeaking) {
                                    try {
                                        startSpeaking();
                                    } catch (ExecutionException | InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    binding.playPause.setChecked(false);
                                    if (binding.viewPager.getCurrentItem() == (expressionDetails.size() - 1)) {
                                        displayCompleteDialog();
                                    }
                                    playPauseAnimation(false);
                                }
                            }, DELAY_TIME);
                        } else {
                            isSayWordFinish = false;
                            currentTryCount++;
                            tts.initialize(expressionDetails.get(binding.viewPager.getCurrentItem()).getQuestion(), ExpressionActivity.this);
                        }
                    }

                    @Override
                    public void onErrorOccurred(String errorMessage) {

                    }
                }).
                get().
                initialize(message, this);
    }
    private void displayCompleteDialog() {

        HintDialog hintDialog = new HintDialog(ExpressionActivity.this);
        hintDialog.setCancelable(false);
        hintDialog.setAlertTitle("Woohoo!!");
        hintDialog.setAlertDesciption("Hey, you completed practice successfully !!\nNow you can proceed to take test.");

        hintDialog.actionButton("START TEST");
        hintDialog.actionButtonBackgroundColor(R.color.primary);
        hintDialog.setOnActionListener(viewId -> {

            switch (viewId.getId()) {

                case R.id.closeButton:
                    hintDialog.dismiss();
                    break;
                case R.id.buttonAction:
                    navigateToTest();
                    break;
            }
        });


        hintDialog.show();

    }
    private void initTTS() throws ExecutionException, InterruptedException {
        var task = new ExpressionActivity.TTSAsyncTask();
        tts = task.execute(new ConversionCallback() {
            @Override
            public void onCompletion() {
                UtilityFunctions.runOnUiThread(() -> {
                    if (isSayWordFinish) {
                        isSayWordFinish = false;
                        removeHighlight();
                        tts.setTextViewAndSentence(null);
                        tts.initialize(getSpellLetter(),
                                ExpressionActivity.this);
                    } else {
                        startListening();
                    }
                });
            }

            @Override
            public void onErrorOccurred(String errorMessage) {
                Log.e(TAG, "onErrorOccurred: " + errorMessage);
            }
        }).get();
        tts.setTextRangeListener((utteranceId, sentence, start, end, frame) -> {
            UtilityFunctions.runOnUiThread(() -> {
                var textView = (TextView) this.findViewById(R.id.text_view_word);
                if (textView != null) {
                    Spannable textWithHighlights = new SpannableString(sentence);
                    textWithHighlights.setSpan(new ForegroundColorSpan(
                                    ContextCompat.
                                            getColor(
                                                    this,
                                                    R.color.primary
                                            )),
                            start,
                            end,
                            Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    textView.setText(textWithHighlights);
                }
            });
        });
    }

    //    Helper method to start listening
    private void startListening() {
        UtilityFunctions.runOnUiThread(() -> {
            startTime = new Date().getTime();
            var current = (ExpressionFragment) fragments.get(binding.viewPager.getCurrentItem());
            current.getAnimationView().setVisibility(View.VISIBLE);
            playPauseAnimation(false);
            stt.initialize("Start Listening", this);
        });

    }

    private void removeHighlight() {
        var textView = (TextView) findViewById(R.id.text_view_word);
        Spannable textWithHighlights = new SpannableString(textView.getText().toString());
        textWithHighlights.setSpan(new ForegroundColorSpan(
                        ContextCompat.
                                getColor(
                                        ExpressionActivity.this,
                                        android.R.color.primary_text_light
                                )),
                0,
                textView.getText().toString().length(),
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        textView.setText(textWithHighlights);
    }


    private void navigateToTest() {
        var intent = new Intent(this, SpellingTest.class);
        intent.putExtra(EXTRA_SPELLING_DETAIL, getIntent().getStringExtra(EXTRA_SPELLING_DETAIL).trim());
        startActivity(intent);
    }


    private void playPauseAnimation(Boolean play) {
        if (play)
            binding.imageViewTeacher.playAnimation();
        else
            binding.imageViewTeacher.pauseAnimation();
    }


    //    TODO : STT is here
    private void intSTT() throws ExecutionException, InterruptedException {
        var task = new ExpressionActivity.STTAsyncTask();

        stt = task.execute(new ConversionCallback() {

            @Override
            public void onPartialResult(String result) {
                ConversionCallback.super.onPartialResult(result);
//                checkResult(result);
//                UtilityFunctions.simpleToast(EnglishSpellingActivity.this, result);
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "onSuccess: " + result);
                try {
                    checkResult(result);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void getArrayResult(List<String[]> list) {
                ConversionCallback.super.getArrayResult(list);


                // make fun for verifying
                // verifyResult(list);

            }

            @Override
            public void onCompletion() {
                var current = (ExpressionFragment) fragments.get(binding.viewPager.getCurrentItem());
                current.getAnimationView().setVisibility(View.GONE);
            }

            @Override
            public void onErrorOccurred(String errorMessage) {
                Log.d(TAG, "onErrorOccurred: " + errorMessage);
                if (errorMessage.equals("No match")) {
                    Log.d(TAG, "onErrorOccurred: " + errorMessage);
                    UtilityFunctions.runOnUiThread(() -> {
                        startListening();
                    }, 400);
                }
            }

            @Override
            public void getLogResult(String title) {
                ConversionCallback.super.getLogResult(title);
                logs += title + "\n";
            }
        }).get();

    }

    private void checkResult(String result) throws ExecutionException, InterruptedException {

        Toast.makeText(this, result+","+expressionDetails.get(binding.viewPager.getCurrentItem()).getAnswers().get(0), Toast.LENGTH_SHORT).show();
        if (expressionDetails.get(binding.viewPager.getCurrentItem()).getAnswers().get(0).toLowerCase(Locale.ROOT).contains(result.toLowerCase(Locale.ROOT))){
            helperTTS(UtilityFunctions.getCompliment(true), true, 0);
        }
        else {
            helperTTS(UtilityFunctions.getCompliment(false), false, 0);
        }


    }


    private void setViews() {
        binding.subSub.setText(UtilityFunctions.getDBNameExpression(expression, this).replace("Expression", "").trim());
        setPager();
    }


    private void setPager() {
        Log.d("XXX", "setPager: " + expression + " db name " + UtilityFunctions.getDBNameExpression(expression, this));
        var data = getExpressionFromType(expressionDao.getEnglishModel(1).getExpression(),
                UtilityFunctions.getDBNameExpression(expression, this).replace("Expression", "").trim());

        if (data == null) {
            UtilityFunctions.simpleToast(this, "No data found");
            return;
        }
        List<Fragment> fragments = getFragments(data);

        var pagerAdapter = new EnglishViewPager(
                fragments,
                getSupportFragmentManager(),
                getLifecycle()
        );

        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setPageTransformer((page, position) -> {
            updatePagerHeightForChild(page, binding.viewPager);
        });
        binding.viewPager.setUserInputEnabled(false);
        try {
            binding.playPause.setChecked(true);
            isSpeaking = binding.playPause.isChecked();
            initTTS();
            intSTT();
            initMediaPlayer();
            playPauseAnimation(true);
            helperTTS("Hi kids !! Let us, learn some Expression"
                            + UtilityFunctions.getDBNameExpression(expression, this).replace("Expression", "")
                    ,
                    false
                    , REQUEST_FOR_QUESTION);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }




    private void updatePagerHeightForChild(View view, ViewPager2 pager) {
        view.post(() -> {
            {
                var wMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY);
                var hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                view.measure(wMeasureSpec, hMeasureSpec);
                var lp = pager.getLayoutParams();
                lp.height = view.getMeasuredHeight();
                pager.setLayoutParams(lp);
                pager.invalidate();
            }
        });
    }

    public static ExpressionCategoryModel getExpressionFromType(List<ExpressionCategoryModel> models, String type) {
        var filterList = models.stream().filter(model -> model.getCategory().equals(type)).collect(Collectors.toList());
        if (filterList.size() > 0) {
            return filterList.get(0);
        } else {
            return null;
        }
    }

    private String getQuestion() {
        return "The " + UtilityFunctions.
                convertCardinalNumberToOrdinalNumber(binding.viewPager.getCurrentItem() + 1)
                + " expression is " + (expressionDetails.get(binding.viewPager.getCurrentItem()).getQuestion());
    }

    private String getDescription() {
        return expressionDetails.get(binding.viewPager.getCurrentItem()).getDefinition();
    }

//    private String getSpellLetter() {
//        return "IT's your turn to spell the word " + (expressionDetails.get(binding.viewPager.getCurrentItem()).getWord().equals("The")
//                ? "'di'" :
//                "'" + expressionDetails.get(binding.viewPager.getCurrentItem()).getWord()) + "' .";
//    }
   private String getSpellLetter() {
        return "IT's your turn to express " + (expressionDetails.get(binding.viewPager.getCurrentItem()).getQuestion());
    }

    public void restartEngine() {
        destroyedEngines();
        try {
            startSpeaking();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        isSpeaking=true;
        binding.playPause.setChecked(true);
    }
    private List<Fragment> getFragments(ExpressionCategoryModel data) {
        expressionDetails = data.getExpressionDetails();
        fragments = CollectionUtils.mapWithIndex(
                        expressionDetails.stream(), (index, item) ->
                                new ExpressionFragment(item, index + 1)).collect(Collectors.toList()
                );
        return fragments;
    }
    static class TTSAsyncTask extends AsyncTask<ConversionCallback, Void, TextToSpeckConverter> {
        @Override
        protected TextToSpeckConverter doInBackground(ConversionCallback... conversionCallbacks) {
            return TextToSpeechBuilder.builder(conversionCallbacks[0]);
        }
    }

    static class TTSHelperAsyncTask extends AsyncTask<ConversionCallback, Void, TextToSpeckConverter> {
        @Override
        protected TextToSpeckConverter doInBackground(ConversionCallback... conversionCallbacks) {
            return TextToSpeechBuilder.builder(conversionCallbacks[0]);
        }
    }

    static class STTAsyncTask extends AsyncTask<ConversionCallback, Void, SpeechToTextConverterEnglish> {
        @Override
        protected SpeechToTextConverterEnglish doInBackground(ConversionCallback... conversionCallbacks) {
            return SpeechToTextBuilder.englishBuilder(conversionCallbacks[0]);
        }
    }


    private void destroyedEngines() {
        binding.playPause.setChecked(false);
        playPauseAnimation(false);
        if (tts != null)
            tts.destroy();
        if (stt != null)
            stt.destroy();
        if (ttsHelper != null)
            ttsHelper.destroy();
        if (mediaPlayer != null)
            mediaPlayer.release();
        try {
            var current = (ExpressionFragment) fragments.get(binding.viewPager.getCurrentItem());
            current.getAnimationView().setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}