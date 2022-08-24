package com.maths.beyond_school_280720220930.english_activity;

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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.database.english.EnglishDao;
import com.maths.beyond_school_280720220930.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_280720220930.database.english.model.VocabularyDetails;
import com.maths.beyond_school_280720220930.database.english.model.VocabularyModel;
import com.maths.beyond_school_280720220930.database.log.LogDatabase;
import com.maths.beyond_school_280720220930.databinding.ActivityEnglishBinding;
import com.maths.beyond_school_280720220930.english_activity.practice.EnglishVocabularyPracticeActivity;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.SpeechToTextBuilder;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.SpeechToTextConverterEnglish;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.CollectionUtils;
import com.maths.beyond_school_280720220930.utils.Constants;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class EnglishActivity extends AppCompatActivity implements VocabularyFragment.OnRootClick {

    private ActivityEnglishBinding binding;
    private static final String TAG = EnglishActivity.class.getSimpleName();
    private static final int MAX_TRY = 2 /* Giver u three chance */;
    private static final int MAX_TRY_FOR_SPEECH = 4 /* Giver u three chance */;
    private static final int DELAY_TIME = 800;
    private final int REQUEST_FOR_DES = 345 * 34;
    private final int REQUEST_FOR_QUESTION = 345 * 35;

    private EnglishDao dao;
    private TextToSpeckConverter tts = null;
    private TextToSpeckConverter ttsHelper = null;
    private SpeechToTextConverterEnglish stt = null;
    private List<VocabularyDetails> vocabularyList;
    private List<Fragment> fragmentList;
    private Boolean isSpeaking = false;
    private Boolean isSayWordFinish = true;
    private int currentTryCount = 0, currentTryCountForSpeech = 0;
    private LogDatabase logDatabase;
    private String logs = "";
    private long startTime = 0, endTime = 0;
    private FirebaseAnalytics analytics;
    private FirebaseAuth auth;
    private UtilityFunctions.VocabularyCategories category;
    private MediaPlayer mediaPlayer = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEnglishBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        var database = EnglishGradeDatabase.getDbInstance(this);
        dao = database.englishDao();
        logDatabase = LogDatabase.getDbInstance(this);
        analytics = FirebaseAnalytics.getInstance(getApplicationContext());
        auth = FirebaseAuth.getInstance();
        setToolbar();
        setViewPager();
        buttonClick();
        practiceButton();
    }

    private void practiceButton() {
        binding.giveTestButton.setOnClickListener(v -> {
            if (tts != null && stt != null) {
                destroyedEngines();
            }
            var intent = new Intent(this, EnglishVocabularyPracticeActivity.class);
            intent.putExtra(Constants.EXTRA_VOCABULARY_CATEGORY, category.name());
            startActivity(intent);
        });
    }


    private void setToolbar() {
        binding.toolBar.titleText.setText(getResources().getString(R.string.vocabulary));
        binding.toolBar.imageViewBack.setOnClickListener(view -> finish());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setViewPager() {
        if (getIntent().hasExtra(Constants.EXTRA_VOCABULARY_DETAIL_CATEGORY)) {
            category = UtilityFunctions.getVocabularyFromString(getIntent().getStringExtra(Constants.EXTRA_VOCABULARY_DETAIL_CATEGORY));
        } else {
            category = UtilityFunctions.VocabularyCategories.bathroom;
        }
        var data = UtilityFunctions.
                getVocabularyDetailsFromType(dao.getEnglishModel(
                        UtilityFunctions.getGrade(
                                PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_grade))
                        )
                ).getVocabulary(), category);
        if (data == null) {
            UtilityFunctions.simpleToast(this, "No data found");
            return;
        }
        binding.textViewCategory.setText(getResources().getString(R.string.category,
                UtilityFunctions.vocabularyCategoriesToString(category)));
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
            initTTS();
            intSTT();
            initMediaPlayer();
            helperTTS(UtilityFunctions.getQuestionTitle(category), false, REQUEST_FOR_QUESTION);
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

    private void buttonClick() {
        binding.playPause.setOnClickListener(view -> {
            isSpeaking = binding.playPause.isChecked();
            startEngine();
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
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    private List<Fragment> getFragments(VocabularyModel data) {
        vocabularyList = data.getVocabularyDetails();
        fragmentList = CollectionUtils.mapWithIndex(vocabularyList.stream(), (index, item) -> new VocabularyFragment(item, index + 1, this)).collect(Collectors.toList());
        return fragmentList;
    }

    private void initMediaPlayer() {
        mediaPlayer = UtilityFunctions.playClapSound(this);
    }

    private void initTTS() throws ExecutionException, InterruptedException {
        var task = new TTSAsyncTask();
        tts = task.execute(new ConversionCallback() {
            @Override
            public void onCompletion() {
                UtilityFunctions.runOnUiThread(() -> {
                    if (isSayWordFinish) {
                        isSayWordFinish = false;
                        tts.setTextViewAndSentence(null);
//                        tts.initialize("Now Say the word " + vocabularyList.get(binding.viewPager.getCurrentItem()).getWord(), EnglishActivity.this);
                        tts.initialize(UtilityFunctions.getQuestionsFromVocabularyCategories(category), EnglishActivity.this);
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
                var textView = (TextView) this.findViewById(R.id.text_view_description);
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

    private void startSpeaking() throws ExecutionException, InterruptedException {
        helperTTS(vocabularyList.get(binding.viewPager.getCurrentItem()).getWord(), false, REQUEST_FOR_DES);
//        Question
        logs += "Question : " + vocabularyList.get(binding.viewPager.getCurrentItem()).getWord() + " : " + vocabularyList.get(binding.viewPager.getCurrentItem()).getDefinition() + ". \n";

//        Stop when reach ot last item
        if (binding.viewPager.getCurrentItem() == (vocabularyList.size() - 1))
            isSpeaking = false;
    }


    private void intSTT() throws ExecutionException, InterruptedException {
        var task = new STTAsyncTask();
        stt = task.execute(new ConversionCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "onSuccess:  result " + result + " word " + vocabularyList.get(binding.viewPager.getCurrentItem()).getWord());
                try {
//                    Check the maximum try count
                    if (currentTryCount > MAX_TRY) {
                        try {
                            helperTTS("No Problem. Let's go to next word ", true, 0);
                            currentTryCount = 0;
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        return;
                    }

//                    Check  the result
                    endTime = new Date().getTime();
                    long diff = endTime - startTime;
                    if (UtilityFunctions.checkString(result.toLowerCase(Locale.ROOT), vocabularyList.get(binding.viewPager.getCurrentItem()).getWord().toLowerCase(Locale.ROOT))) {
                        logs += "Time Take :" + UtilityFunctions.formatTime(diff) + ", Correct .\n";
                        helperTTS(UtilityFunctions.getCompliment(true), true, 0);
                        mediaPlayer.start();
                        UtilityFunctions.sendDataToAnalytics(analytics, auth.getUid().toString(), "kidsid", "Ayaan", "english Vocabulary", 22,
                                vocabularyList.get(binding.viewPager.getCurrentItem()).getWord(), result, true, (int) diff,
                                vocabularyList.get(binding.viewPager.getCurrentItem()).getWord() + " : " + vocabularyList.get(binding.viewPager.getCurrentItem()).getDefinition(), "english");
                    } else {
                        logs += "Time Take :" + UtilityFunctions.formatTime(diff) + ", Wrong .\n";
                        helperTTS(UtilityFunctions.getCompliment(false), false, 0);
                        UtilityFunctions.sendDataToAnalytics(analytics, auth.getUid().toString(), "kidsid", "Ayaan",
                                "english Vocabulary", 22,
                                vocabularyList.get(binding.viewPager.getCurrentItem()).getWord(), result, false, (int) diff,
                                vocabularyList.get(binding.viewPager.getCurrentItem()).getWord() + " : " + vocabularyList.get(binding.viewPager.getCurrentItem()).getDefinition(), "english"
                        );
                    }


                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCompletion() {
                var current = (VocabularyFragment) fragmentList.get(binding.viewPager.getCurrentItem());
                current.getAnimationView().setVisibility(View.GONE);
            }

            @Override
            public void onErrorOccurred(String errorMessage) {
                Log.d(TAG, "onErrorOccurred: " + errorMessage);
                if (errorMessage.equals("No match")) {
                    currentTryCountForSpeech++;
                    if (currentTryCountForSpeech < MAX_TRY_FOR_SPEECH) {
                        Log.d(TAG, "onErrorOccurred: " + currentTryCountForSpeech);
                        UtilityFunctions.runOnUiThread(() -> {
                            startListening();
                        }, 400);

                        return;
                    }
                }
                binding.playPause.setChecked(false);
                currentTryCountForSpeech = 0;
                var current = (VocabularyFragment) fragmentList.get(binding.viewPager.getCurrentItem());
                current.getAnimationView().setVisibility(View.GONE);
                isSayWordFinish = true;
                currentTryCount = 0;
            }

            @Override
            public void getLogResult(String title) {
                ConversionCallback.super.getLogResult(title);
                logs += title + "\n";
            }
        }).get();

    }

    //    Helper method to start listening
    private void startListening() {
        UtilityFunctions.runOnUiThread(() -> {
            startTime = new Date().getTime();
            var current = (VocabularyFragment) fragmentList.get(binding.viewPager.getCurrentItem());
            current.getAnimationView().setVisibility(View.VISIBLE);
            stt.initialize("Start Listening", this);
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        checkLogIsEnable();
    }

    private void checkLogIsEnable() {
        if (PrefConfig.readIdInPref(getApplicationContext(), "IS_LOG_ENABLE").equals("true"))
            saveLog();
    }

    private void saveLog() {
        Log.d(TAG, "saveLog: Called " + logs);
        UtilityFunctions.saveLog(logDatabase, logs);
    }


    private void helperTTS(String message, boolean canNavigate, int request) throws
            ExecutionException, InterruptedException {
        ttsHelper = new TTSHelperAsyncTask().execute(new ConversionCallback() {
            @Override
            public void onCompletion() {
                if (request == REQUEST_FOR_DES && !canNavigate) {
                    tts.setTextViewAndSentence(vocabularyList.get(binding.viewPager.getCurrentItem()).getDefinition());
                    tts.initialize(vocabularyList.get(binding.viewPager.getCurrentItem()).getDefinition(), EnglishActivity.this);
                    return;
                }
                if (request == REQUEST_FOR_QUESTION && !canNavigate) {
                    UtilityFunctions.runOnUiThread(() -> {
                        try {
                            startSpeaking();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }, DELAY_TIME);
                    return;
                }

                if (canNavigate) {
                    UtilityFunctions.runOnUiThread(() -> {
                        mediaPlayer.pause();
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
                        }
                    }, DELAY_TIME);
                } else {
                    isSayWordFinish = false;
                    currentTryCount++;
                    tts.initialize("Say the word ones again " + vocabularyList.get(binding.viewPager.getCurrentItem()).getWord(), EnglishActivity.this);
                }
            }

            @Override
            public void onErrorOccurred(String errorMessage) {

            }
        }).get().initialize(message, this);
    }

    @Override
    public void onRootClick() {
        binding.playPause.setChecked(!binding.playPause.isChecked());
        startEngine();
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

    @Override
    protected void onStop() {
        super.onStop();
        destroyedEngines();
    }

    private void destroyedEngines() {
        binding.playPause.setChecked(false);
        if (tts != null)
            tts.destroy();
        if (stt != null)
            stt.destroy();
        if (ttsHelper != null)
            ttsHelper.destroy();
        if (mediaPlayer != null)
            mediaPlayer.release();
        var current = (VocabularyFragment) fragmentList.get(binding.viewPager.getCurrentItem());
        current.getAnimationView().setVisibility(View.GONE);
    }
}