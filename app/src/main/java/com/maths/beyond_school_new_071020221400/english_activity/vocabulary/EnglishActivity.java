package com.maths.beyond_school_new_071020221400.english_activity.vocabulary;

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
import com.maths.beyond_school_new_071020221400.LogActivity;
import com.maths.beyond_school_new_071020221400.R;
import com.maths.beyond_school_new_071020221400.SP.PrefConfig;
import com.maths.beyond_school_new_071020221400.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_new_071020221400.database.english.vocabulary.VocabularyDao;
import com.maths.beyond_school_new_071020221400.database.english.vocabulary.model.VocabularyCategoryModel;
import com.maths.beyond_school_new_071020221400.database.english.vocabulary.model.VocabularyDetails;
import com.maths.beyond_school_new_071020221400.database.log.LogDatabase;
import com.maths.beyond_school_new_071020221400.database.process.ProgressDataBase;
import com.maths.beyond_school_new_071020221400.database.process.ProgressM;
import com.maths.beyond_school_new_071020221400.databinding.ActivityEnglishBinding;
import com.maths.beyond_school_new_071020221400.dialogs.HintDialog;
import com.maths.beyond_school_new_071020221400.english_activity.vocabulary.practice.EnglishVocabularyPracticeActivity;
import com.maths.beyond_school_new_071020221400.translation_engine.ConversionCallback;
import com.maths.beyond_school_new_071020221400.translation_engine.SpeechToTextBuilder;
import com.maths.beyond_school_new_071020221400.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_new_071020221400.translation_engine.translator.SpeechToTextConverterEnglish;
import com.maths.beyond_school_new_071020221400.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_new_071020221400.utils.CollectionUtils;
import com.maths.beyond_school_new_071020221400.utils.Constants;
import com.maths.beyond_school_new_071020221400.utils.UtilityFunctions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class EnglishActivity extends AppCompatActivity implements VocabularyFragment.OnRootClick {

    private ActivityEnglishBinding binding;
    private static final String TAG = EnglishActivity.class.getSimpleName();
    private static final int MAX_TRY = 2 /* Giver u three chance */;
    private static final int MAX_TRY_FOR_SPEECH = 4 /* Giver u three chance */;
    private static final int DELAY_TIME = 800;
    private final int REQUEST_FOR_DES = 345 * 34;
    private final int REQUEST_FOR_QUESTION = 345 * 35;

    private VocabularyDao dao;
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

    private long timeSpend = 0;
    public static final int TIMER_VALUE = 15;
    private List<ProgressM> progressData;
    private ProgressDataBase progressDataBase;
    private String parentsContactId="";

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
        progressDataBase = ProgressDataBase.getDbInstance(this);
        progressData = new ArrayList<>();
        setToolbar();
        setViewPager();
        buttonClick();
        practiceButton();

        parentsContactId=PrefConfig.readIdInPref(EnglishActivity.this,getResources().getString(R.string.parent_contact_details));
    }

    private void practiceButton() {
        binding.giveTestButton.setOnClickListener(v -> {
            if (tts != null && stt != null) {
                destroyedEngines();
            }
            navigateToTest();
        });
    }

    private void navigateToTest() {
        var intent = new Intent(this, EnglishVocabularyPracticeActivity.class);
        intent.putExtra(Constants.EXTRA_VOCABULARY_CATEGORY, category.name());
        startActivity(intent);
    }


    private void setToolbar() {
        binding.toolBar.titleText.setText(getResources().getString(R.string.vocabulary));
        binding.toolBar.imageViewBack.setOnClickListener(view -> finish());
        binding.toolBar.getRoot().inflateMenu(R.menu.log_menu);
        binding.toolBar.getRoot().setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.action_log) {
                startActivity(new Intent(getApplicationContext(), LogActivity.class));

                return true;
            }
            return super.onOptionsItemSelected(item);

        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setViewPager() {
        if (getIntent().hasExtra(Constants.EXTRA_VOCABULARY_DETAIL_CATEGORY)) {
            category = UtilityFunctions.getVocabularyFromString(getIntent().getStringExtra(Constants.EXTRA_VOCABULARY_DETAIL_CATEGORY));
        } else {
            category = UtilityFunctions.VocabularyCategories.bathroom_1;
        }
        var data = UtilityFunctions.
                getVocabularyDetailsFromType(dao.getEnglishModel(
                        1
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
            isSpeaking = binding.playPause.isChecked();
            initTTS();
            intSTT();
            initMediaPlayer();
            playPauseAnimation(true);
            helperTTS(UtilityFunctions.getQuestionTitleVocabulary(category), false, REQUEST_FOR_QUESTION);
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
            timer();
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
            playPauseAnimation(false);
            destroyedEngines();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    private List<Fragment> getFragments(VocabularyCategoryModel data) {
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
                                            getColor(this, R.color.primary)),
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
        if (binding.viewPager.getCurrentItem() == (vocabularyList.size() - 1)) {
            isSpeaking = false;
        }
    }


    private void intSTT() throws ExecutionException, InterruptedException {
        var task = new STTAsyncTask();
        stt = task.execute(new ConversionCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(String result) {
                playPauseAnimation(false);
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
                                vocabularyList.get(binding.viewPager.getCurrentItem()).getWord() + " : " + vocabularyList.get(binding.viewPager.getCurrentItem()).getDefinition(), "english", parentsContactId);
                        playPauseAnimation(true);
                    } else {
                        logs += "Time Take :" + UtilityFunctions.formatTime(diff) + ", Wrong .\n";
                        helperTTS(UtilityFunctions.getCompliment(false), false, 0);
                        UtilityFunctions.sendDataToAnalytics(analytics, auth.getUid().toString(), "kidsid", "Ayaan",
                                "english Vocabulary", 22,
                                vocabularyList.get(binding.viewPager.getCurrentItem()).getWord(), result, false, (int) diff,
                                vocabularyList.get(binding.viewPager.getCurrentItem()).getWord() + " : " + vocabularyList.get(binding.viewPager.getCurrentItem()).getDefinition(), "english",
                                parentsContactId);
                        playPauseAnimation(true);
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
        checkProgressData();
        UtilityFunctions.checkProgressAvailable(progressDataBase, "English" + "Vocabulary", UtilityFunctions.vocabularyCategoriesToString(category), new Date(),
                timeSpend + Integer.parseInt(binding.timeText.getText().toString()), false);
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
                        try {
                            mediaPlayer.pause();
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
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
                            playPauseAnimation(false);
                            if (binding.viewPager.getCurrentItem() == (vocabularyList.size()) - 1)
                                displayCompleteDialog();
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
        VocabularyFragment current = null;
        try {
            current = (VocabularyFragment) fragmentList.get(binding.viewPager.getCurrentItem());
            current.getAnimationView().setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playPauseAnimation(Boolean play) {
        if (play)
            binding.imageViewTeacher.playAnimation();
        else
            binding.imageViewTeacher.pauseAnimation();
    }

    private void displayCompleteDialog() {

        HintDialog hintDialog = new HintDialog(EnglishActivity.this);
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


    public void restartEngine() {
        destroyedEngines();
        try {
            startSpeaking();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        binding.playPause.setChecked(true);
    }

    private void timer() {
        boolean isTimerRunning = false;
        Observable.interval(60, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    public void accept(Long x) throws Exception {
                        // update your view here

                        binding.timerProgress.setMax(15);
                        binding.timerProgress.setProgress(Integer.parseInt((x + 1) + ""));
                        binding.timeText.setText((x + 1) + "");
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
        progressData = UtilityFunctions.checkProgressAvailable(progressDataBase, "English" + "Vocabulary", UtilityFunctions.vocabularyCategoriesToString(category),
                new Date(), 0, true);

        try {
            if (progressData != null) {
                timeSpend = progressData.get(0).time_spend;
            }
        } catch (Exception e) {
            timeSpend = 0;
        }

    }
}