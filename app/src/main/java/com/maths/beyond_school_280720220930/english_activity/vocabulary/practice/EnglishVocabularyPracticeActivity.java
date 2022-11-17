package com.maths.beyond_school_280720220930.english_activity.vocabulary.practice;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.ScoreActivity;
import com.maths.beyond_school_280720220930.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_280720220930.database.english.vocabulary.VocabularyDao;
import com.maths.beyond_school_280720220930.database.english.vocabulary.model.VocabularyCategoryModel;
import com.maths.beyond_school_280720220930.database.english.vocabulary.model.VocabularyDetails;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.database.log.LogDatabase;
import com.maths.beyond_school_280720220930.database.process.ProgressDataBase;
import com.maths.beyond_school_280720220930.database.process.ProgressM;
import com.maths.beyond_school_280720220930.databinding.ActivityEnglishVocabularyPracticeBinding;
import com.maths.beyond_school_280720220930.english_activity.vocabulary.EnglishViewPager;
import com.maths.beyond_school_280720220930.firebase.CallFirebaseForInfo;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.SpeechToTextBuilder;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.SpeechToTextConverterEnglish;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.CollectionUtils;
import com.maths.beyond_school_280720220930.utils.Constants;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class EnglishVocabularyPracticeActivity extends AppCompatActivity {

    private final String TAG = EnglishVocabularyPracticeActivity.class.getSimpleName();
    private static final int DELAY_TIME = 800;
    private static final int MAX_TRY_FOR_SPEECH = 4 /* Giver u three chance */;
    private ActivityEnglishVocabularyPracticeBinding binding;
    private VocabularyDao dao;
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
    private MediaPlayer mediaPlayer = null;
    private GradeDatabase databaseGrade;
    private String kidsGrade;
    private FirebaseFirestore kidsDb;
    private String kidsId;
    private LogDatabase logDatabase;
    private String logs = "";
    private long startTime = 0, endTime = 0;
    private JSONArray kidsActivityJsonArray = new JSONArray();

    private ProgressDataBase progressDataBase;
    private FirebaseAnalytics analytics;
    private FirebaseAuth auth;
    private int kidAge;
    private String kidName;
    private List<ProgressM> progressData;
    private long timeSpend = 0;
    public static final int TIMER_VALUE = 15;
    private boolean isTimerRunning = true;
    private String parentsContactId = "";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEnglishVocabularyPracticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dao = EnglishGradeDatabase.getDbInstance(this).englishDao();
        progressDataBase = ProgressDataBase.getDbInstance(EnglishVocabularyPracticeActivity.this);
        databaseGrade = GradeDatabase.getDbInstance(this);
        kidsGrade = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_grade));
        logDatabase = LogDatabase.getDbInstance(this);
        kidsId = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_id));
        kidsDb = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        analytics = FirebaseAnalytics.getInstance(this);
        analytics.setUserId(auth.getCurrentUser().getUid());
        kidAge = UtilityFunctions.calculateAge(PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_dob)));
        kidsId = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_id));
        kidName = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_name));
        parentsContactId = PrefConfig.readIdInPref(EnglishVocabularyPracticeActivity.this, getResources().getString(R.string.parent_contact_details));

        progressData = new ArrayList<>();
        setToolbar();
        setPracticeClick();
        if (getIntent().hasExtra(Constants.EXTRA_VOCABULARY_CATEGORY)) {
            category = getIntent().getStringExtra(Constants.EXTRA_VOCABULARY_CATEGORY);
            setPager(category);
            buttonClick();
        } else {
            throw new IllegalArgumentException("No category provided");
        }
        checkProgressData();
    }

    private void timer() {
        isTimerRunning = false;
        Observable.interval(60, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    public void accept(Long x) throws Exception {
                        // update your view here

                        binding.timerProgress.setMax(15);
                        binding.timerProgress.setProgress(Integer.parseInt((x + 1) + ""));
                        binding.timeText.setText((timeSpend + x + 1) + "");
                        Log.i("task", x + "");
                    }
                })
                .takeUntil(aLong -> aLong == TIMER_VALUE)
                .doOnComplete(() ->
                        // do whatever you need on complete
                        Log.i("TSK", "task")
                ).subscribe();


    }

    private void checkProgressData() {
        progressData = UtilityFunctions.checkProgressAvailable(progressDataBase, "English" + "Vocabulary", category,
                new Date(), 0, true);

        try {
            if (progressData != null) {
                timeSpend = progressData.get(0).time_spend;
            }
        } catch (Exception e) {
            timeSpend = 0;
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
                        dao.getEnglishModel(1
                        ).getVocabulary(),
                        UtilityFunctions.VocabularyCategories.valueOf(category));
        try {
            if (data == null) {
                UtilityFunctions.simpleToast(this, "No data found");
                return;
            }
        } catch (Exception e) {
        }

        List<Fragment> fragments = getFragments(data);
        var pagerAdapter = new EnglishViewPager(
                fragments,
                getSupportFragmentManager(),
                getLifecycle()
        );

        binding.viewPagerTest.setAdapter(pagerAdapter);

        binding.viewPagerTest.setUserInputEnabled(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    private List<Fragment> getFragments(VocabularyCategoryModel data) {
        vocabularyList = data.getVocabularyDetails();
        Collections.shuffle(vocabularyList);
        fragmentList = CollectionUtils.
                mapWithIndex(vocabularyList.stream(),
                        (index, item) -> new VocabularyTestFragment(item, index + 1))
                .collect(Collectors.toList());
        return fragmentList;
    }


    private void buttonClick() {
        if (isTimerRunning)
            timer();

        binding.playPause.setOnClickListener(v -> {
            isSpeaking = binding.playPause.isChecked();
            if (binding.playPause.isChecked()) {
                try {
                    initTTS();
                    intSTT();
                    startSpeaking();
                    initMediaPlayer();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                destroyedEngines();
                playPauseAnimation(false);
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
        playPauseAnimation(true);
        tts.initialize(binding.textViewGuessQuestion.getText().toString(), this);
        logs += "Question : " + vocabularyList.get(binding.viewPagerTest.getCurrentItem()).getWord() + " : " + vocabularyList.get(binding.viewPagerTest.getCurrentItem()).getDefinition() + ". \n";
        if (binding.viewPagerTest.getCurrentItem() == (vocabularyList.size() - 1)) {
            isSpeaking = false;
        }
    }

    private void checkResult() {
        var auth = FirebaseAuth.getInstance();
        Log.d("XXXX", "checkResult: " + UtilityFunctions.getDbName(UtilityFunctions.getVocabularyFromString(category), this));
        if (correctAnswers >= UtilityFunctions.getNinetyPercentage(vocabularyList.size())) {
            binding.textViewGuessQuestion.setText("You have completed all the questions");
            UtilityFunctions.updateDbUnlock(
                    databaseGrade,
                    kidsGrade,
                    "Vocabulary",
                    UtilityFunctions.getDbName(UtilityFunctions.getVocabularyFromString(category), this)
            );
            try {
                CallFirebaseForInfo.checkActivityData(kidsDb,
                        kidsActivityJsonArray, "pass", auth, kidsId, UtilityFunctions.
                                getDbName(UtilityFunctions.getVocabularyFromString(category), this),
                        "vocabulary", correctAnswers, wrongAnswers, vocabularyList.size(), "english");
                progressDataBase.progressDao().updateScore(correctAnswers, wrongAnswers, category);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                CallFirebaseForInfo.checkActivityData(kidsDb,
                        kidsActivityJsonArray, "fail", auth, kidsId, UtilityFunctions.
                                getDbName(UtilityFunctions.getVocabularyFromString(category), this),
                        "vocabulary", correctAnswers, wrongAnswers, vocabularyList.size(), "english");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        gotoScoreCard();
    }

    private void initMediaPlayer() {
        mediaPlayer = UtilityFunctions.playClapSound(this);
    }

    private void intSTT() throws ExecutionException, InterruptedException {
        var task = new STTAsyncTask();
        stt = task.execute(new ConversionCallback() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(String result) throws JSONException {
                ConversionCallback.super.onSuccess(result);
                try {
                    playPauseAnimation(false);

                    endTime = new Date().getTime();
                    long diff = endTime - startTime;

                    if (UtilityFunctions.checkString(result.toLowerCase(Locale.ROOT),
                            vocabularyList.get(binding.viewPagerTest.getCurrentItem()).getWord().toLowerCase(Locale.ROOT))
                    ) {
                        mediaPlayer.start();
                        playPauseAnimation(true);
                        helperTTS(UtilityFunctions.getCompliment(true), true, false);
                        ((VocabularyTestFragment) fragmentList.get(binding.viewPagerTest.getCurrentItem())).getTextView()
                                .setText(vocabularyList.get(binding.viewPagerTest.getCurrentItem()).getWord());
                        correctAnswers++;
                        putJsonData("Question : " +
                                        vocabularyList.get(binding.viewPagerTest.getCurrentItem()).getWord()
                                        + " : " + vocabularyList.get(binding.viewPagerTest.getCurrentItem()).getDefinition(),
                                result, diff, true);
                        logs += "Time Take :" + UtilityFunctions.formatTime(diff) + ", Correct .\n";
                        UtilityFunctions.sendDataToAnalytics(analytics, auth, auth.getCurrentUser().getUid().toString(), kidsId, kidName,
                                "English-Test-" + "vocabulary", kidAge, vocabularyList.get(binding.viewPagerTest.getCurrentItem()).getWord()
                                , result, true, (int) (diff), vocabularyList.get(binding.viewPagerTest.getCurrentItem()).getWord()
                                        + " : " + vocabularyList.get(binding.viewPagerTest.getCurrentItem()).getDefinition(), "english", parentsContactId);
                    } else {
                        if (tryAgainCount == 2) {
                            wrongAnswers++;
                            putJsonData("Question : " +
                                            vocabularyList.get(binding.viewPagerTest.getCurrentItem()).getWord()
                                            + " : " + vocabularyList.get(binding.viewPagerTest.getCurrentItem()).getDefinition(),
                                    result, diff, false);
                            updateViews();
                            logs += "Time Take :" + UtilityFunctions.formatTime(diff) + ", Wrong .\n";
                            playPauseAnimation(true);
                            helperTTS("No problem ,Answer is  " + vocabularyList.get(binding.viewPagerTest.getCurrentItem()).getWord(), false, true);
                            ((VocabularyTestFragment) fragmentList.get(binding.viewPagerTest.getCurrentItem())).getTextView()
                                    .setText(vocabularyList.get(binding.viewPagerTest.getCurrentItem()).getWord());
                            UtilityFunctions.sendDataToAnalytics(analytics, auth,auth.getCurrentUser().getUid().toString(), kidsId, kidName,
                                    "English-Test-" + "vocabulary", kidAge, vocabularyList.get(binding.viewPagerTest.getCurrentItem()).getWord()
                                    , result, false, (int) (diff), vocabularyList.get(binding.viewPagerTest.getCurrentItem()).getWord()
                                            + " : " + vocabularyList.get(binding.viewPagerTest.getCurrentItem()).getDefinition(), "english", parentsContactId);
                            return;
                        }
                        helperTTS(UtilityFunctions.getCompliment(false), false, false);
                        playPauseAnimation(true);
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

            @Override
            public void getLogResult(String title) {
                ConversionCallback.super.getLogResult(title);
                logs += title + "\n";
            }
        }).get();
    }

    private void updateViews() {
        binding.correctText.setText(String.valueOf(correctAnswers));
        binding.wrongText.setText(String.valueOf(wrongAnswers));
    }

    private void startListening() {
        UtilityFunctions.runOnUiThread(() -> {
            playPauseAnimation(false);
            startTime = new Date().getTime();
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
                    playPauseAnimation(true);
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
                        mediaPlayer.pause();
                        binding.viewPagerTest.setCurrentItem(binding.viewPagerTest.getCurrentItem() + 1);
                        binding.textViewGuessQuestion.
                                setText(UtilityFunctions.
                                        getQuestionsFromVocabularyCategories(
                                                UtilityFunctions.getVocabularyFromString(category)
                                        ));
                        try {
                            playPauseAnimation(true);
                            if (isSpeaking)
                                startSpeaking();
                            else {
                                checkResult();
                                binding.playPause.setChecked(false);
                            }
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
        checkLogIsEnable();
        UtilityFunctions.checkProgressAvailable(progressDataBase, "English" + "Vocabulary", category, new Date(),
                timeSpend + Integer.parseInt(binding.timeText.getText().toString()), false);
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
        var current = (VocabularyTestFragment) fragmentList.get(binding.viewPagerTest.getCurrentItem());
        current.getAnimationView().setVisibility(View.GONE);
    }


    private void checkLogIsEnable() {
        if (PrefConfig.readIdInPref(getApplicationContext(), "IS_LOG_ENABLE").equals("true"))
            saveLog();
    }

    private void saveLog() {
        Log.d(TAG, "saveLog: Called " + logs);
        UtilityFunctions.saveLog(logDatabase, logs);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyedEngines();
    }

    private void putJsonData(String question, String ans, long timeTaken, boolean isCorrect) throws JSONException {


        JSONObject activityData = new JSONObject();
        try {
            activityData.put("question", question);
            activityData.put("ans", ans);
            activityData.put("time_taken", timeTaken);
            activityData.put("is_correct", isCorrect);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        kidsActivityJsonArray.put(activityData);


    }

    private void playPauseAnimation(Boolean play) {
        if (play)
            binding.imageViewTeacher.playAnimation();
        else
            binding.imageViewTeacher.pauseAnimation();
    }


    private void gotoScoreCard() {

        Intent intent = new Intent(getApplicationContext(), ScoreActivity.class);
        intent.putExtra("wrongAns", wrongAnswers);
        intent.putExtra("correctAns", correctAnswers);
        intent.putExtra("chapter", category);

        startActivity(intent);
    }


}