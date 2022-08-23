package com.maths.beyond_school_280720220930.english_activity.practice;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.database.english.EnglishDao;
import com.maths.beyond_school_280720220930.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_280720220930.database.english.model.VocabularyDetails;
import com.maths.beyond_school_280720220930.database.english.model.VocabularyModel;
import com.maths.beyond_school_280720220930.databinding.ActivityEnglishVocabularyPracticeBinding;
import com.maths.beyond_school_280720220930.english_activity.EnglishViewPager;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.SpeechToTextBuilder;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.SpeechToTextConverterEnglish;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.CollectionUtils;
import com.maths.beyond_school_280720220930.utils.Constants;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class EnglishVocabularyPracticeActivity extends AppCompatActivity {

    private final String TAG = EnglishVocabularyPracticeActivity.class.getSimpleName();
    private static final int DELAY_TIME = 500;
    private static final int MAX_TRY_FOR_SPEECH = 4 /* Giver u three chance */;
    private ActivityEnglishVocabularyPracticeBinding binding;
    private EnglishDao dao;
    private List<VocabularyDetails> vocabularyList;
    private List<Fragment> fragmentList;
    private TextToSpeckConverter tts = null;
    private TextToSpeckConverter ttsHelper = null;
    private SpeechToTextConverterEnglish stt = null;
    private int correctAnswers = 0, currentTryCountForSpeech = 0;
    private int wrongAnswers = 0;
    private Boolean isSpeaking = false;
    private int tryAgainCount = 0;
    private String category;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEnglishVocabularyPracticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dao = EnglishGradeDatabase.getDbInstance(this).englishDao();
        setToolbar();
        setPracticeClick();
        if (getIntent().hasExtra(Constants.EXTRA_VOCABULARY_CATEGORY)) {
            category = getIntent().getStringExtra(Constants.EXTRA_VOCABULARY_CATEGORY);
            setPager(category);
            buttonClick();
        } else {
            throw new IllegalArgumentException("No category provided");
        }
    }

    private void setPracticeClick() {
        binding.learnOrTest.setOnClickListener(v -> {
            destroyedEngines();
            finish();
        });
    }


    private void setToolbar() {
        binding.toolBar.titleText.setText(getResources().getString(R.string.vocabulary));
        binding.toolBar.imageViewBack.setOnClickListener(view -> finish());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setPager(String category) {
        binding.textViewGuessQuestion.
                setText(UtilityFunctions.
                        getQuestionsFromVocabularyCategories(
                                UtilityFunctions.getVocabularyFromString(category)
                        ));
        var data = UtilityFunctions.
                getVocabularyDetailsFromType(
                        dao.getEnglishModel(UtilityFunctions.getGrade(PrefConfig.readIdInPref(
                                this,
                                getResources().getString(R.string.kids_grade)))
                        ).getVocabulary(),
                        UtilityFunctions.VocabularyCategories.valueOf(category));
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

        binding.viewPagerTest.setAdapter(pagerAdapter);

//        binding.viewPagerTest.setUserInputEnabled(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    private List<Fragment> getFragments(VocabularyModel data) {
        vocabularyList = data.getVocabularyDetails();
        fragmentList = CollectionUtils.
                mapWithIndex(vocabularyList.stream(),
                        (index, item) -> new VocabularyTestFragment(item, index + 1))
                .collect(Collectors.toList());
        return fragmentList;
    }


    private void buttonClick() {
        binding.playPause.setOnClickListener(v -> {
            isSpeaking = binding.playPause.isChecked();
            if (binding.playPause.isChecked()) {
                try {
                    initTTS();
                    intSTT();
                    startSpeaking();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                destroyedEngines();
            }
        });
    }

    private void initTTS() throws ExecutionException, InterruptedException {
        var task = new TTSAsyncTask();
        tts = task.execute(new ConversionCallback() {
            @Override
            public void onCompletion() {
                UtilityFunctions.runOnUiThread(() -> startListening());
            }

            @Override
            public void onErrorOccurred(String errorMessage) {

            }
        }).get();
    }

    private void startSpeaking() throws ExecutionException, InterruptedException {
        tts.initialize(binding.textViewGuessQuestion.getText().toString(), this);
        if (binding.viewPagerTest.getCurrentItem() == (vocabularyList.size() - 1))
            isSpeaking = false;
    }


    private void intSTT() throws ExecutionException, InterruptedException {
        var task = new STTAsyncTask();
        stt = task.execute(new ConversionCallback() {

            @Override
            public void onSuccess(String result) {
                ConversionCallback.super.onSuccess(result);
                try {
                    if (UtilityFunctions.checkString(result.toLowerCase(Locale.ROOT),
                            vocabularyList.get(binding.viewPagerTest.getCurrentItem()).getWord().toLowerCase(Locale.ROOT))
                    ) {
                        helperTTS(UtilityFunctions.getCompliment(true), true, false);
                        ((VocabularyTestFragment) fragmentList.get(binding.viewPagerTest.getCurrentItem())).getTextView()
                                .setText(vocabularyList.get(binding.viewPagerTest.getCurrentItem()).getWord());
                        correctAnswers++;
                    } else {
                        if (tryAgainCount == 2) {
                            wrongAnswers++;
                            updateViews();
                            helperTTS("No problem ,Answer is  " + vocabularyList.get(binding.viewPagerTest.getCurrentItem()).getWord(), false, true);
                            ((VocabularyTestFragment) fragmentList.get(binding.viewPagerTest.getCurrentItem())).getTextView()
                                    .setText(vocabularyList.get(binding.viewPagerTest.getCurrentItem()).getWord());
                            return;
                        }
                        helperTTS(UtilityFunctions.getCompliment(false), false, false);
                    }
                    updateViews();

                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCompletion() {
                var current = (VocabularyTestFragment) fragmentList.get(binding.viewPagerTest.getCurrentItem());
                current.getAnimationView().setVisibility(View.GONE);
            }


            @Override
            public void onErrorOccurred(String errorMessage) {
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
                var current = (VocabularyTestFragment) fragmentList.get(binding.viewPagerTest.getCurrentItem());
                current.getAnimationView().setVisibility(View.GONE);
                binding.playPause.setChecked(false);
            }
        }).get();
    }

    private void updateViews() {
        binding.correctText.setText(String.valueOf(correctAnswers));
        binding.wrongText.setText(String.valueOf(wrongAnswers));
    }

    private void startListening() {
        UtilityFunctions.runOnUiThread(() -> {
            var current = (VocabularyTestFragment) fragmentList.get(binding.viewPagerTest.getCurrentItem());
            current.getAnimationView().setVisibility(View.VISIBLE);
            stt.initialize("Start Listening", this);
        });

    }


    private void helperTTS(String message, Boolean canNavigate, Boolean requestTryAgain) throws
            ExecutionException, InterruptedException {
        ttsHelper = new TTSHelperAsyncTask().execute(new ConversionCallback() {
            @Override
            public void onCompletion() {

                if (requestTryAgain) {
                    UtilityFunctions.runOnUiThread(() -> {
                        binding.viewPagerTest.setCurrentItem(binding.viewPagerTest.getCurrentItem() + 1);
                        tryAgainCount = 0;
                        try {
                            startSpeaking();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }, DELAY_TIME);
                    return;
                }

                if (!canNavigate && tryAgainCount != 2) {
                    tryAgainCount++;
                    tts.initialize("", EnglishVocabularyPracticeActivity.this);
                } else
                    UtilityFunctions.runOnUiThread(() -> {
                        binding.viewPagerTest.setCurrentItem(binding.viewPagerTest.getCurrentItem() + 1);
                        binding.textViewGuessQuestion.
                                setText(UtilityFunctions.
                                        getQuestionsFromVocabularyCategories(
                                                UtilityFunctions.getVocabularyFromString(category)
                                        ));
                        try {
                            if (isSpeaking)
                                startSpeaking();
                            else
                                binding.playPause.setChecked(false);
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }, DELAY_TIME);

            }

            @Override
            public void onErrorOccurred(String errorMessage) {

            }
        }).get().initialize(message, this);
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
    protected void onPause() {
        super.onPause();
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
        var current = (VocabularyTestFragment) fragmentList.get(binding.viewPagerTest.getCurrentItem());
        current.getAnimationView().setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyedEngines();
    }

}