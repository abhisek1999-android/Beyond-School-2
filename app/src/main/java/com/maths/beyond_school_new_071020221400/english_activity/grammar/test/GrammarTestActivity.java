package com.maths.beyond_school_new_071020221400.english_activity.grammar.test;

import static com.maths.beyond_school_new_071020221400.utils.Constants.EXTRA_CATEGORY_ID;
import static com.maths.beyond_school_new_071020221400.utils.Constants.EXTRA_GRAMMAR_CATEGORY;
import static com.maths.beyond_school_new_071020221400.utils.Constants.EXTRA_ONLINE_FLAG;
import static com.maths.beyond_school_new_071020221400.utils.Constants.EXTRA_TITLE;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maths.beyond_school_new_071020221400.LogActivity;
import com.maths.beyond_school_new_071020221400.R;
import com.maths.beyond_school_new_071020221400.SP.PrefConfig;
import com.maths.beyond_school_new_071020221400.ScoreActivity;
import com.maths.beyond_school_new_071020221400.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_new_071020221400.database.english.grammer.model.GrammarModel;
import com.maths.beyond_school_new_071020221400.database.english.grammer.model.GrammarType;
import com.maths.beyond_school_new_071020221400.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_new_071020221400.database.log.LogDatabase;
import com.maths.beyond_school_new_071020221400.database.process.ProgressDataBase;
import com.maths.beyond_school_new_071020221400.database.process.ProgressM;
import com.maths.beyond_school_new_071020221400.databinding.ActivityGrammarTestBinding;
import com.maths.beyond_school_new_071020221400.english_activity.grammar.GrammarTypeConverter;
import com.maths.beyond_school_new_071020221400.english_activity.vocabulary.EnglishViewPager;
import com.maths.beyond_school_new_071020221400.firebase.CallFirebaseForInfo;
import com.maths.beyond_school_new_071020221400.retrofit.ApiClient;
import com.maths.beyond_school_new_071020221400.retrofit.ApiInterface;
import com.maths.beyond_school_new_071020221400.retrofit.model.content.ContentModel;
import com.maths.beyond_school_new_071020221400.translation_engine.ConversionCallback;
import com.maths.beyond_school_new_071020221400.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_new_071020221400.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_new_071020221400.utils.CollectionUtils;
import com.maths.beyond_school_new_071020221400.utils.UtilityFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GrammarTestActivity extends AppCompatActivity {

    private static final String TAG = "GrammarTestActivity";
    private final Context context = GrammarTestActivity.this;
    private ActivityGrammarTestBinding binding;
    private static String category;
    private EnglishGradeDatabase englishGradeDatabase;
    private List<GrammarModel> grammarModelList;
    private TextToSpeckConverter tts = null;
    private TextToSpeckConverter ttsHelper = null;
    private MediaPlayer mediaPlayer = null;
    private ButtonClick listener = null;
    private int correctAnswerCount = 0, wrongAnswerCount = 0;
    private long statTime = 0;
    private static final int MAX_TRY_AGAIN_COUNT = 2;
    private int tryAgainCount = 1;


    private GradeDatabase databaseGrade;
    private String kidsGrade;
    private FirebaseFirestore kidsDb;
    private String kidsId;

    private LogDatabase logDatabase;
    private String logs = "";
    private long startTime = 0;
    private FirebaseAnalytics analytics;
    private FirebaseAuth auth;
    private int kidAge;
    private String kidName;
    private final JSONArray kidsActivityJsonArray = new JSONArray();
    private ProgressDataBase progressDataBase;
    public static final int TIMER_VALUE = 15;
    private String parentsContactId = "";

    private Boolean isOnline = false;
    private long timeSpend = 0;
    private ContentModel.Meta meta;
    private List<ProgressM> progressData;
    private boolean isTimerRunning=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGrammarTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        englishGradeDatabase = EnglishGradeDatabase.getDbInstance(context);

        logDatabase = LogDatabase.getDbInstance(context);
        databaseGrade = GradeDatabase.getDbInstance(context);
        progressDataBase = ProgressDataBase.getDbInstance(context);

        kidsGrade = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_grade));
        kidsId = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_id));
        kidsDb = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        analytics = FirebaseAnalytics.getInstance(this);
        var id = "";
        if (auth.getCurrentUser() != null) {
            id = auth.getCurrentUser().getUid();
        }
        analytics.setUserId(id);
        kidAge = UtilityFunctions.calculateAge(PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_dob)));
        kidsId = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_id));
        kidName = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_name));
        parentsContactId = PrefConfig.readIdInPref(context, getResources().getString(R.string.parent_contact_details));
        setToolbar();
        getDataFromIntent();
        learnButtonClick();
    }

    private void learnButtonClick() {
        binding.giveTestButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void getDataFromIntent() {
        if (!getIntent().hasExtra(EXTRA_GRAMMAR_CATEGORY)) {
            UtilityFunctions.simpleToast(context, "No data found");
            return;
        }
        category = getIntent().getStringExtra(EXTRA_GRAMMAR_CATEGORY);
        isOnline = getIntent().getBooleanExtra(EXTRA_ONLINE_FLAG, false);
        if (isOnline) {
            getSubjectData();
        } else setViewPager();
    }


    private void setViewPager() {
        if (!isOnline) if (offlineData()) return;
        List<Fragment> fragmentList = mapToFragment(grammarModelList);
        var pagerAdapter = new EnglishViewPager(fragmentList, getSupportFragmentManager(), getLifecycle());
        binding.viewPagerIdentifyingNouns.setAdapter(pagerAdapter);
        binding.viewPagerIdentifyingNouns.setUserInputEnabled(false);
        setButton();
        setOptionButtonClick();
    }

    private void getSubjectData() {
        Retrofit retrofit = ApiClient.getClient();
        var api = retrofit.create(ApiInterface.class);
        api.getVocabularySubject(
                PrefConfig.readIdInPref(context, getResources().getString(R.string.kids_grade)).toLowerCase().replace(" ", ""),
                "english",
                getIntent().getStringExtra(EXTRA_TITLE).toLowerCase(),
                category).enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(Call<ContentModel> call, Response<ContentModel> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.body() != null) {
                    Log.d(TAG, "onResponse: " + response.body().getContent().toString());
                    setData(response.body().getContent());
                    meta = response.body().getMeta();
                }
            }

            @Override
            public void onFailure(Call<ContentModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void setData(List<ContentModel.Content> content) {
        var typeConverter = new GrammarTypeConverter();
        grammarModelList = typeConverter.mapToList(content);
        setViewPager();
    }

    private boolean offlineData() {
        var list = englishGradeDatabase.grammarDao().getAllGrammar();
        grammarModelList = getFilterGrammar(list);
        if (grammarModelList == null) {
            UtilityFunctions.simpleToast(context, "No data found");
            return true;
        }
        return false;
    }

    private void initMediaPlayer() {
        mediaPlayer = UtilityFunctions.playClapSound(this);
    }


    private void setButton() {
        binding.playPause.setOnClickListener(v -> {
            if (binding.playPause.isChecked()) {
                initTTS();
                initMediaPlayer();
                startSpeaking();
                timer();
            } else destroyEngine();
        });
    }

    private void startSpeaking() {
        startTime = new Date().getTime();
        playPauseAnimation(true);
        var questionContent = !isOnline ? UtilityFunctions.getQuestionForGrammarTest(context, category) : meta.getQuestion();
        var question = (binding.viewPagerIdentifyingNouns.getCurrentItem() < 2) ? questionContent : "";
        tts.initialize(question, this);
    }

    private void initTTS() {
        var task = new TTSAsyncTask();
        try {
            tts = task.execute(new ConversionCallback() {
                @Override
                public void onCompletion() {
                    setOptionButton();
                    setVisibilityOfLinearLayout(true);
                    playPauseAnimation(false);
                    checkAnswer();
                }

                @Override
                public void onErrorOccurred(String errorMessage) {

                }

                @Override
                public void getLogResult(String title) {
                    ConversionCallback.super.getLogResult(title);
                    logs += title + "\n";
                }
            }).get();

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void checkAnswer() {
        var endTime = new Date().getTime();
        var diff = endTime - startTime;
        var currentModel = grammarModelList.get(binding.viewPagerIdentifyingNouns.getCurrentItem());
        var currentAnswer = currentModel.getWord().toLowerCase(Locale.ROOT).trim().replace(".", "");
        var currentDes = currentModel.getDescription().toLowerCase(Locale.ROOT).trim();
        if (category.equals(getResources().getString(R.string.grammar_3)))
            logs += "Question : " + UtilityFunctions.getQuestionForGrammarTest(context, category) + "Answer" + currentDes + "time taken" + diff + "\n";
        else
            logs += "Question : " + UtilityFunctions.getQuestionForGrammarTest(context, category) + "Answer" + currentAnswer + "time taken" + diff + "\n";
        listener = text -> {
            Log.d("XXX", "checkAnswer: " + text + " " + currentAnswer);
            if (category.equals(getResources().getString(R.string.grammar_3))) {
                if (currentDes.contains(text)) {
                    tryAgainCount = 1;
                    playPauseAnimation(true);
                    try {
                        mediaPlayer.start();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                    putJsonData("Question : " + UtilityFunctions.getQuestionForGrammarTest(context, category), currentDes, diff, true);
                    sendDataToAnalytics(currentDes, text, diff, true);

                    helperTTS(UtilityFunctions.getCompliment(true), true, 0);
                    correctAnswerCount++;
                } else {
                    playPauseAnimation(true);
                    if (tryAgainCount > MAX_TRY_AGAIN_COUNT) {
                        wrongAnswerCount++;
                        putJsonData("Question : " + UtilityFunctions.getQuestionForGrammarTest(context, category), currentDes, diff, false);
                        sendDataToAnalytics(currentDes, text, diff, false);
                        helperTTS(UtilityFunctions.getCompliment(false), true, 0);
                        tryAgainCount = 1;
                        return;
                    }
                    tryAgainCount++;
                    playPauseAnimation(true);
                    tts.initialize(UtilityFunctions.getCompliment(false), this);

                }
                return;
            }

            if (text.trim().replace(".", "").equals(currentAnswer)) {
                tryAgainCount = 1;
                playPauseAnimation(true);
                correctAnswerCount++;
                mediaPlayer.start();
                putJsonData("Question : " + UtilityFunctions.getQuestionForGrammarTest(context, category), text, diff, true);
                sendDataToAnalytics(currentAnswer, text, diff, true);
                helperTTS(UtilityFunctions.getCompliment(true), true, 0);
            } else {
                if (tryAgainCount > MAX_TRY_AGAIN_COUNT) {
                    playPauseAnimation(true);
                    wrongAnswerCount++;
                    sendDataToAnalytics(currentAnswer, text, diff, false);
                    putJsonData("Question : " + UtilityFunctions.getQuestionForGrammarTest(context, category), text, diff, false);
                    helperTTS(UtilityFunctions.getCompliment(false), true, 0);
                    tryAgainCount = 1;
                    return;
                }
                tryAgainCount++;
                playPauseAnimation(true);
                tts.initialize(UtilityFunctions.getCompliment(false), this);
            }

        };
    }

    private void sendDataToAnalytics(String currentWord, String result, long diff, boolean b) {
        UtilityFunctions.sendDataToAnalytics(analytics, Objects.requireNonNull(auth.getCurrentUser()).getUid(), kidsId, kidName, "English-Test-" + (!isOnline ? "grammar" : getIntent().getStringExtra(EXTRA_TITLE).toLowerCase()), kidAge, currentWord, result, b, (int) (diff), UtilityFunctions.getQuestionForGrammarTest(context, category), "English", parentsContactId);
    }

    private void helperTTS(String message, boolean canNavigate, int request) {
        try {
            ttsHelper = new TTSHelperAsyncTask().execute(new ConversionCallback() {
                @Override
                public void onCompletion() {
                    if (canNavigate) {
                        setVisibilityOfLinearLayout(false);
                        try {
                            mediaPlayer.pause();
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                        if (binding.viewPagerIdentifyingNouns.getCurrentItem() == grammarModelList.size() - 1) {
                            playPauseAnimation(false);
                            setToggleButtonChecked(false);
                            uploadData();
                            return;
                        }

                        UtilityFunctions.runOnUiThread(() -> binding.viewPagerIdentifyingNouns.setCurrentItem(binding.viewPagerIdentifyingNouns.getCurrentItem() + 1));
                        updateMarksViews();
                        startSpeaking();
                    }
                }

                @Override
                public void onErrorOccurred(String errorMessage) {
                    logs += errorMessage + "\n";
                }
            }).get().initialize(message, this);
        } catch (ExecutionException | InterruptedException e) {
            logs += e.getMessage() + "\n";
        }
    }

    private void updateMarksViews() {
        UtilityFunctions.runOnUiThread(() -> {
            binding.correctText.setText(String.valueOf(correctAnswerCount));
            binding.wrongText.setText(String.valueOf(wrongAnswerCount));
        });
    }

    private void setOptionButton() {
        UtilityFunctions.runOnUiThread(() -> {
            var currentModel = grammarModelList.get(binding.viewPagerIdentifyingNouns.getCurrentItem());
            var extra = currentModel.getExtra();
            var split = extra.split(",");
            if (split.length <= 1) {
                UtilityFunctions.simpleToast(context, "No data found");
                return;
            }
            if (split.length == 2) {
                binding.key1.setText(split[0].trim());
                binding.key2.setText(split[1].trim());
                binding.key3.setVisibility(View.GONE);
                binding.key4.setVisibility(View.GONE);
                binding.linearLayout.setWeightSum(2);
            }
            if (split.length == 3) {
                binding.key1.setText(split[0].trim());
                binding.key2.setText(split[1].trim());
                binding.key3.setText(split[2].trim());
                binding.linearLayout.setWeightSum(3);
                binding.key3.setVisibility(View.VISIBLE);
                binding.key4.setVisibility(View.GONE);
            }
            if (split.length == 4) {
                binding.key1.setText(split[0].trim());
                binding.key2.setText(split[1].trim());
                binding.key3.setText(split[2].trim());
                binding.key4.setText(split[3].trim());
                binding.linearLayout.setWeightSum(4);
                binding.key3.setVisibility(View.VISIBLE);
                binding.key4.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setOptionButtonClick() {
        binding.key1.setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(binding.key1.getText().toString().toLowerCase(Locale.ROOT).trim());
        });
        binding.key2.setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(binding.key2.getText().toString().toLowerCase(Locale.ROOT).trim());
        });
        binding.key3.setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(binding.key3.getText().toString().toLowerCase(Locale.ROOT).trim());
        });
        binding.key4.setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(binding.key4.getText().toString().toLowerCase(Locale.ROOT).trim());
        });
    }


    private void setToggleButtonChecked(boolean isChecked) {
        UtilityFunctions.runOnUiThread(() -> binding.playPause.setChecked(isChecked));
    }

    private void playPauseAnimation(Boolean play) {
        UtilityFunctions.runOnUiThread(() -> {
            if (play) binding.imageViewTeacher.playAnimation();
            else binding.imageViewTeacher.pauseAnimation();
        });
    }

    private void setVisibilityOfLinearLayout(boolean isVisible) {
        UtilityFunctions.runOnUiThread(() -> {
            binding.linearLayout.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
        });
    }

    @SuppressWarnings("deprecation")
    static class TTSAsyncTask extends AsyncTask<ConversionCallback, Void, TextToSpeckConverter> {
        @Override
        protected TextToSpeckConverter doInBackground(ConversionCallback... conversionCallbacks) {
            return TextToSpeechBuilder.builder(conversionCallbacks[0]);
        }
    }

    @SuppressWarnings("deprecation")
    static class TTSHelperAsyncTask extends AsyncTask<ConversionCallback, Void, TextToSpeckConverter> {
        @Override
        protected TextToSpeckConverter doInBackground(ConversionCallback... conversionCallbacks) {
            return TextToSpeechBuilder.builder(conversionCallbacks[0]);
        }

    }


    private List<Fragment> mapToFragment(List<GrammarModel> grammarModels) {
        return CollectionUtils.mapWithIndex(grammarModels.stream(), (index, item) -> new RowItemTestFragment(item, index + 1, category)).collect(Collectors.toList());
    }

    private List<GrammarModel> getFilterGrammar(List<GrammarType> list) {
        var c = "";
        if (category.equals(getResources().getString(R.string.grammar_4)))
            c = getResources().getString(R.string.grammar_4_1);
        else if (category.equals(getResources().getString(R.string.grammar_5)))
            c = getResources().getString(R.string.grammar_5_1);
        else c = category;
        for (var grammarType : list) {
            if (grammarType.getType().equals(c)) {
                // list to array list
                return UtilityFunctions.shuffleArray(new ArrayList(grammarType.getGrammarModelList()));
            }
        }
        return null;
    }

    private void setToolbar() {
        binding.toolBar.imageViewBack.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.toolBar.titleText.setText((getIntent().hasExtra(EXTRA_TITLE)) ? getIntent().getStringExtra(EXTRA_TITLE) : getResources().getString(R.string.grammar));
        //changed here.......
        binding.textViewCategory.setText(getIntent().getStringExtra(EXTRA_GRAMMAR_CATEGORY).replace("Grammar", ""));

        binding.toolBar.getRoot().inflateMenu(R.menu.log_menu);
        binding.toolBar.getRoot().setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_log) {
                startActivity(new Intent(getApplicationContext(), LogActivity.class));

                return true;
            }
            return super.onOptionsItemSelected(item);

        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        checkLogIsEnable();
        destroyEngine();
        checkProgressData();
        UtilityFunctions.checkProgressAvailable(progressDataBase, (isOnline ? getIntent().getStringExtra(EXTRA_CATEGORY_ID) : "Grammar"), category, new Date(), timeSpend + Integer.parseInt(binding.layoutExtTimer.timeText.getText().toString()), false);
    }



    private void checkProgressData() {
        progressData = UtilityFunctions.checkProgressAvailable(progressDataBase, (isOnline ? getIntent().getStringExtra(EXTRA_CATEGORY_ID) : "Grammar"), category, new Date(), 0, true);

        try {
            if (progressData != null) {
                timeSpend = progressData.get(0).time_spend;
            }
        } catch (Exception e) {
            timeSpend = 0;
        }

    }

    public void destroyEngine() {
        playPauseAnimation(false);
        setToggleButtonChecked(false);
        setVisibilityOfLinearLayout(false);
        if (tts != null) {
            tts.destroy();
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        if (ttsHelper != null) {
            ttsHelper.destroy();
        }
    }


    private void uploadData() {
        try {

            var chapter = !isOnline ? "grammar" : getIntent().getStringExtra(EXTRA_TITLE);
            if (correctAnswerCount >= UtilityFunctions.getNinetyPercentage(grammarModelList.size())) {
                UtilityFunctions.updateDbUnlock(databaseGrade, chapter, category);
                CallFirebaseForInfo.checkActivityData(kidsDb, kidsActivityJsonArray, "pass", auth, kidsId, kidsGrade.toLowerCase().replace(" ", ""), category, chapter, correctAnswerCount, wrongAnswerCount, grammarModelList.size(), "english");

                progressDataBase.progressDao().updateScore(correctAnswerCount, wrongAnswerCount, category);

            } else {
                CallFirebaseForInfo.checkActivityData(kidsDb, kidsActivityJsonArray, "fail", auth, kidsId, kidsGrade.toLowerCase().replace(" ", ""), category, chapter, correctAnswerCount, wrongAnswerCount, grammarModelList.size(), "english");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            gotoScoreCard();
        }
    }

    private void putJsonData(String question, String ans, long timeTaken, boolean isCorrect) {
        JSONObject activityData = new JSONObject();
        try {
            activityData.put("question", question);
            activityData.put("ans", ans);
            activityData.put("time_taken", timeTaken);
            activityData.put("is_correct", isCorrect);

        } catch (JSONException e) {
            logs += e.getMessage() + "\n";
        }
        kidsActivityJsonArray.put(activityData);
    }


    private void gotoScoreCard() {
        Intent intent = new Intent(getApplicationContext(), ScoreActivity.class);
        intent.putExtra("wrongAns", wrongAnswerCount);
        intent.putExtra("correctAns", correctAnswerCount);
        intent.putExtra("chapter", category.replace("Grammar", ""));
        startActivity(intent);
        finish();
    }

    private void checkLogIsEnable() {
        if (PrefConfig.readIdInPref(getApplicationContext(), "IS_LOG_ENABLE").equals("true"))
            saveLog();
    }

    private void saveLog() {
        Log.d(TAG, "saveLog: Called " + logs);
        if (!logs.isEmpty()) UtilityFunctions.saveLog(logDatabase, logs);
    }

    private void timer() {
        if (isTimerRunning) {
            return;
        }

        Observable.interval(60, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).doOnNext(new Consumer<Long>() {
            public void accept(Long x) throws Exception {
                // update your view here

                binding.layoutExtTimer.timerProgress.setMax(15);
                binding.layoutExtTimer.timerProgress.setProgress(Integer.parseInt((x + 1) + ""));
                binding.layoutExtTimer.timeText.setText((x + 1) + "");
                isTimerRunning = true;
                Log.i("task", x + "");
            }
        }).takeUntil(aLong -> aLong == TIMER_VALUE).doOnComplete(() ->
                // do whatever you need on complete
                Log.i("TSK", "task")).subscribe();
    }

    interface ButtonClick {
        void onClick(String text);
    }
}