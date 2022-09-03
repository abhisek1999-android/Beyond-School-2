package com.maths.beyond_school_280720220930.english_activity.spelling.spelling_test;

import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_SPELLING_DETAIL;
import static com.maths.beyond_school_280720220930.utils.UtilityFunctions.capitalize;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maths.beyond_school_280720220930.AdditionActivity;
import com.maths.beyond_school_280720220930.LogActivity;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.ScoreActivity;
import com.maths.beyond_school_280720220930.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_280720220930.database.english.spelling.SpellingDao;
import com.maths.beyond_school_280720220930.database.english.spelling.model.SpellingCategoryModel;
import com.maths.beyond_school_280720220930.database.english.spelling.model.SpellingDetail;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.database.log.LogDatabase;
import com.maths.beyond_school_280720220930.database.process.ProgressDataBase;
import com.maths.beyond_school_280720220930.database.process.ProgressM;
import com.maths.beyond_school_280720220930.databinding.ActivitySpellingTestBinding;
import com.maths.beyond_school_280720220930.english_activity.vocabulary.EnglishViewPager;
import com.maths.beyond_school_280720220930.firebase.CallFirebaseForInfo;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.SpeechToTextBuilder;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.SpeechToTextConverterSpelling;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.CollectionUtils;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

@RequiresApi(api = Build.VERSION_CODES.N)
public class SpellingTest extends AppCompatActivity {


    private static final String TAG = "SpellingTest";
    private ActivitySpellingTestBinding binding;
    private static final int MAX_TRY = 2 /* Giver u three chance */;
    private static final int DELAY_TIME = 800;
    private final int REQUEST_FOR_QUESTION = 345 * 35;


    private Boolean isSpeaking = false;
    private int currentTryCount = 0;

    private UtilityFunctions.Spellings spellings = null;
    private TextToSpeckConverter tts = null;
    private TextToSpeckConverter ttsHelper = null;
    private SpeechToTextConverterSpelling stt = null;
    private MediaPlayer mediaPlayer = null;
    private LogDatabase logDatabase;

    private String logs = "";
    private long startTime = 0, endTime = 0;
    private int correctCount = 0, wrongCount = 0;
    private StringBuilder currentWordBuilder = new StringBuilder();
    private SpellingDao dao;
    private List<SpellingDetail> spellingDetails;
    private List<Fragment> fragments;
    private int currentWordPosition = 0;
    private GradeDatabase gradeDatabase;
    private FirebaseFirestore kidsDb;
    private String kidsGrade;
    private String kidsId;
    private GradeDatabase databaseGrade;
    private JSONArray kidsActivityJsonArray = new JSONArray();

    private FirebaseAnalytics analytics;
    private FirebaseAuth auth;
    private int kidAge;
    private String kidName;
    private String gResult = "";
    private long timeSpend = 0;
    public static final int TIMER_VALUE = 15;
    private boolean isTimerRunning = true;
    private ProgressDataBase progressDataBase;
    private List<ProgressM> progressData;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpellingTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        logDatabase = LogDatabase.getDbInstance(this);
        databaseGrade = GradeDatabase.getDbInstance(this);
        kidsGrade = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_grade));
        logDatabase = LogDatabase.getDbInstance(this);
        kidsId = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_id));
        kidsDb = FirebaseFirestore.getInstance();
        analytics = FirebaseAnalytics.getInstance(this);
        auth = FirebaseAuth.getInstance();
        kidAge = UtilityFunctions.calculateAge(PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_dob)));
        kidsId = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_id));
        kidName = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_name));
        progressDataBase = ProgressDataBase.getDbInstance(SpellingTest.this);
        progressData = new ArrayList<>();
        setToolbar();
        setData();
        binding.learnOrTest.setOnClickListener((c) -> {
            onBackPressed();
        });
    }

    private void setData() {
        if (getIntent().hasExtra(EXTRA_SPELLING_DETAIL)) {
            spellings = UtilityFunctions.getSpellingsFromString(getIntent().getStringExtra(EXTRA_SPELLING_DETAIL).trim());
            dao = EnglishGradeDatabase.getDbInstance(this).spellingDao();
            setViews();
            buttonClick();
        } else {
            UtilityFunctions.simpleToast(this, "No data found");
        }
    }


    private void setViews() {
        binding.subSub.setText(UtilityFunctions.getDBNameSpelling(spellings, this).replace("Spelling", "").trim());
        setPager();
    }


    private void buttonClick() {
        binding.playPause.setOnClickListener(view -> {
            isSpeaking = binding.playPause.isChecked();
            logs += "Question : " + spellingDetails.get(binding.viewPagerTest.getCurrentItem()).getWord() + " : " + spellingDetails.get(binding.viewPagerTest.getCurrentItem()).getDescription() + ". \n";
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

    private void initMediaPlayer() {
        mediaPlayer = UtilityFunctions.playClapSound(this);
    }


    private void setPager() {
        var data = getSpellingFromType(dao.getSpellingModel(1).getSpelling(),
                UtilityFunctions.getDBNameSpelling(spellings, this).replace("Spelling", "").trim());

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
        binding.viewPagerTest.setUserInputEnabled(false);
        binding.viewPagerTest.setAdapter(pagerAdapter);
        isSpeaking = binding.playPause.isChecked();
    }


    private List<Fragment> getFragments(SpellingCategoryModel data) {
        spellingDetails = data.getDetails();
        fragments = CollectionUtils.
                mapWithIndex(
                        spellingDetails.stream(), (index, item) ->
                                new SpellingTestFragment(item, index + 1)).collect(Collectors.toList()
                );
        return fragments;
    }

    public static SpellingCategoryModel getSpellingFromType(List<SpellingCategoryModel> models, String type) {
        var filterList = models.stream().filter(model -> model.getCategory().equals(type)).collect(Collectors.toList());
        if (filterList.size() > 0) {
            return filterList.get(0);
        } else {
            return null;
        }
    }


    private void initTTS() throws ExecutionException, InterruptedException {
        var task = new TTSAsyncTask();
        tts = task.execute(new ConversionCallback() {
            @Override
            public void onCompletion() {
                UtilityFunctions.runOnUiThread(() -> {
                    tts.setTextViewAndSentence(null);
                    playPauseAnimation(false);
                    var textView = (TextView) findViewById(R.id.text_view_part_1);
                    var spellingDetail = spellingDetails.get(binding.viewPagerTest.getCurrentItem());
                    var split = spellingDetail.getDescription().toLowerCase(Locale.ROOT).split(spellingDetail.getWord().toLowerCase(Locale.ROOT), 2);

                    textView.setText(Html.fromHtml(capitalize(split[0]) + " <font color='#64c1c7'>" +
                            spellingDetail.getWord().replaceAll("[A-Za-z]", "_") + "</font> "
                            + split[1]));
                    try {
                        helperTTS("Now Spell the word " + (spellingDetails.get(binding.viewPagerTest.getCurrentItem()).getWord().equals("The")
                                ? "'di'" :
                                "'" + spellingDetails.get(binding.viewPagerTest.getCurrentItem()).getWord()) + "' .", false, REQUEST_FOR_QUESTION);
                        playPauseAnimation(true);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
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
                var textView = (TextView) this.findViewById(R.id.text_view_part_1);
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
        tts.initialize(
                spellingDetails.get(binding.viewPagerTest.getCurrentItem()).getDescription()
                , SpellingTest.this);
        var spellingDetail = spellingDetails.get(binding.viewPagerTest.getCurrentItem());
        var split = spellingDetail.getDescription().toLowerCase(Locale.ROOT).split(spellingDetail.getWord().toLowerCase(Locale.ROOT), 2);

        var sentence = capitalize(split[0]) +
                spellingDetail.getWord().replaceAll("[A-Z a-z]", "_") +
                split[1];
        tts.setTextViewAndSentence(sentence);
        playPauseAnimation(true);
        if (binding.viewPagerTest.getCurrentItem() == (spellingDetails.size() - 1))
            isSpeaking = false;
    }


    //
    private void intSTT() throws ExecutionException, InterruptedException {
        var task = new STTAsyncTask();
        stt = task.execute(new ConversionCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "onSuccess: " + result);
                gResult = result;
                var currentWord = spellingDetails.get(binding.viewPagerTest.getCurrentItem()).getWord().toLowerCase(Locale.ROOT);
                var currentWordArray = currentWord.toCharArray();
                var split = result.replace(" ", "").toCharArray();

                Log.d(TAG, "onSuccess: currentTryCount " + currentTryCount);
                if (currentTryCount > MAX_TRY) {
                    try {
                        currentTryCount = 0;
                        currentWordPosition = 0;
                        helperTTS("No problem ,We spell it as " + UtilityFunctions.addComma(
                                spellingDetails.get(binding.viewPagerTest.getCurrentItem()).getWord()), true, 2 * 44);
                        playPauseAnimation(true);
                        currentWordBuilder = new StringBuilder();
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                endTime = new Date().getTime();
                long diff = endTime - startTime;
                if (split.length != 0) {
                    if (split.length == 1) {
                        logs += "Single Word :" + UtilityFunctions.formatTime(diff) + ", Correct" + split[0] + "\n";
                        Log.d(TAG, "onSuccess: currentWord " + currentWordPosition);
                        if (currentWordArray[currentWordPosition] == (split[0])) {
                            logs += "Single Word :" + UtilityFunctions.formatTime(diff) + ", Correct" + split[0] + "\n";
                            currentWordBuilder.append(split[0]);
                            currentWordPosition++;
                            var textView = ((SpellingTestFragment) fragments.get(binding.viewPagerTest.getCurrentItem())).getTextView();
                            textView.setText(Html.fromHtml(
                                    textView.getText().toString()
                                            .replaceFirst("_", "<font color='#64c1c7'>" + split[0] + "</font>")
                            ));
                            try {
                                if (currentWordPosition < currentWord.length())
                                    helperTTS("", false, 2 * 45);
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                currentTryCount++;
                                Log.d(TAG, "onSuccess: count Called" + currentTryCount);
                                helperTTS("", false, 2 * 45);
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            for (int i = 0; i < split.length; i++) {
                                if (currentWordArray[0] == (split[0])) {
                                    if (i < currentWordArray.length) //
                                        if (currentWordArray[i] == split[i]) {
                                            currentWordBuilder.append(split[i]);
                                            var textView = ((SpellingTestFragment) fragments.get(binding.viewPagerTest.getCurrentItem())).getTextView();
                                            textView.setText(Html.fromHtml(
                                                    textView.getText().toString()
                                                            .replaceFirst("_", "<font color='#64c1c7'>" + split[i] + "</font>")
                                            ));
                                            currentWordPosition++;
                                        }
                                } else if (currentWordArray[currentWordPosition] == (split[i])) {
                                    if (i < currentWordArray.length) //
                                        if (currentWordArray[currentWordPosition] == split[i]) {
                                            currentWordBuilder.append(split[i]);
                                            var textView = ((SpellingTestFragment) fragments.get(binding.viewPagerTest.getCurrentItem())).getTextView();
                                            textView.setText(Html.fromHtml(
                                                    textView.getText().toString()
                                                            .replaceFirst("_", "<font color='#64c1c7'>" + split[i] + "</font>")
                                            ));
                                            currentWordPosition++;
                                        }
                                }
                                logs += "Multi Word :" + UtilityFunctions.formatTime(diff) + ", Correct" + currentWordBuilder.toString() + "\n";
                            }
                            if (currentWordPosition < currentWord.length())
                                helperTTS("", false, 2 * 45);
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (currentWord.length() == currentWordBuilder.length()) {
                        try {
                            if (currentWordBuilder.toString().equals(currentWord)) {
                                mediaPlayer.start();
                                playPauseAnimation(true);
                                var textView = ((SpellingTestFragment) fragments.get(binding.viewPagerTest.getCurrentItem())).getTextView();
                                textView.setText(Html.fromHtml(
                                        textView.getText().toString()
                                                .replaceFirst(currentWordBuilder.toString(), "<font color='#64c1c7'>" + currentWordBuilder + "</font>")
                                ));
                                helperTTS(UtilityFunctions.getCompliment(true), true, 0);
                                playPauseAnimation(true);
                                logs += "Time Take :" + UtilityFunctions.formatTime(diff) + ", Correct .\n";
                                currentWordBuilder = new StringBuilder();
                                currentWordPosition = 0;
                                correctCount++;
                                putJsonData(
                                        spellingDetails.get(binding.viewPagerTest.getCurrentItem())
                                                .getDescription(),
                                        spellingDetails.get(binding.viewPagerTest.getCurrentItem())
                                                .getWord(),
                                        diff,
                                        true);

                                UtilityFunctions.sendDataToAnalytics(analytics, auth.getCurrentUser().getUid().toString(), kidsId, kidName,
                                        "English-Test-" + "Spelling", kidAge, spellingDetails.get(binding.viewPagerTest.getCurrentItem()).getWord()
                                        , result, false, (int) (diff),
                                        spellingDetails.get(binding.viewPagerTest.getCurrentItem()).getDescription(),
                                        "english");
                            } else {
                                currentTryCount++;
                                currentWordPosition = 0;
                                Log.d(TAG, "onSuccess: count Called" + currentTryCount);
                                logs += "Time Take :" + UtilityFunctions.formatTime(diff) + ", Wrong .\n";
                                helperTTS("", false, 2 * 45);
                            }
                        } catch (ExecutionException | InterruptedException | JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (split.length > 1 && currentWord.length() < currentWordBuilder.length()) {
                        try {
                            currentWordBuilder = new StringBuilder();
                            currentTryCount = 0;
                            currentWordPosition = 0;
                            playPauseAnimation(true);
                            helperTTS("No problem ,We spell it as " + UtilityFunctions.addComma(currentWord), true, 2 * 44);
                            logs += "Time Take :" + UtilityFunctions.formatTime(diff) + ", Wrong .\n";
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    UtilityFunctions.runOnUiThread(() -> {
                        startListening();
                    }, 400);
                }
                updateViews();
            }

            @Override
            public void onCompletion() {
                var current = (SpellingTestFragment) fragments.get(binding.viewPagerTest.getCurrentItem());
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

    private void updateViews() {
        binding.correctText.setText(String.valueOf(correctCount));
        binding.wrongText.setText(String.valueOf(wrongCount));
    }

    private void startListening() {
        UtilityFunctions.runOnUiThread(() -> {
            startTime = new Date().getTime();
            var current = (SpellingTestFragment) fragments.get(binding.viewPagerTest.getCurrentItem());
            current.getAnimationView().setVisibility(View.VISIBLE);
            playPauseAnimation(false);
            stt.initialize("Start Listening", this);
        });

    }

    private void helperTTS(String message, boolean canNavigate, int request) throws
            ExecutionException, InterruptedException {
        ttsHelper = new TTSHelperAsyncTask().execute(new ConversionCallback() {
                    @Override
                    public void onCompletion() {
                        UtilityFunctions.runOnUiThread(() -> playPauseAnimation(false));
                        if (canNavigate && request == 2 * 44) {
                            UtilityFunctions.runOnUiThread(() -> {
                                binding.viewPagerTest.setCurrentItem(binding.viewPagerTest.getCurrentItem() + 1);
                                wrongCount++;
                                try {
                                    putJsonData(
                                            spellingDetails.get(binding.viewPagerTest.getCurrentItem())
                                                    .getDescription(),
                                            spellingDetails.get(binding.viewPagerTest.getCurrentItem())
                                                    .getWord(),
                                            endTime - startTime,
                                            true);
                                    UtilityFunctions.sendDataToAnalytics(analytics, auth.getCurrentUser().getUid().toString(), kidsId, kidName,
                                            "English-Test-" + "Spelling", kidAge, spellingDetails.get(binding.viewPagerTest.getCurrentItem()).getWord()
                                            , gResult, false, (int) (endTime - startTime),
                                            spellingDetails.get(binding.viewPagerTest.getCurrentItem()).getDescription(),
                                            "english");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                updateViews();
                                try {
                                    startSpeaking();
                                } catch (ExecutionException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Log.d(TAG, "onCompletion: Called");
                            }, 400);
                            return;
                        }

                        if (!canNavigate && request == 2 * 45) {
                            startListening();
                            return;
                        }

                        if (!canNavigate && request == REQUEST_FOR_QUESTION) {
                            startListening();
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
                                binding.viewPagerTest.setCurrentItem(binding.viewPagerTest.getCurrentItem() + 1);
                                if (isSpeaking) {
                                    try {
                                        startSpeaking();
                                    } catch (ExecutionException | InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    binding.playPause.setChecked(false);
                                    checkResult();
                                }
                            }, DELAY_TIME);
                        } else {
                            playPauseAnimation(true);
                            tts.initialize("Spell the Word " + (spellingDetails.get(binding.viewPagerTest.getCurrentItem()).getWord().equals("The")
                                    ? "'di'" :
                                    "'" + spellingDetails.get(binding.viewPagerTest.getCurrentItem()).getWord()) + "' .", SpellingTest.this);
                        }
                    }

                    @Override
                    public void onErrorOccurred(String errorMessage) {

                    }
                }).
                get().
                initialize(message, this);
    }

    private void checkResult() {
        var auth = FirebaseAuth.getInstance();
        if (correctCount >= spellingDetails.size() - 2) {
            UtilityFunctions.updateDbUnlock(
                    databaseGrade,
                    kidsGrade,
                    "spelling",
                    UtilityFunctions.getDBNameSpelling(spellings, this)
            );
            try {
                CallFirebaseForInfo.checkActivityData(kidsDb,
                        kidsActivityJsonArray, "pass", auth, kidsId, UtilityFunctions.
                                getDBNameSpelling(spellings, this),
                        "spelling", correctCount, wrongCount, spellingDetails.size(), "english");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                CallFirebaseForInfo.checkActivityData(kidsDb,
                        kidsActivityJsonArray, "fail", auth, kidsId, UtilityFunctions.
                                getDBNameSpelling(spellings, this),
                        "spelling", correctCount, wrongCount, spellingDetails.size(), "english");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        gotoScoreCard();
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

    static class STTAsyncTask extends AsyncTask<ConversionCallback, Void, SpeechToTextConverterSpelling> {
        @Override
        protected SpeechToTextConverterSpelling doInBackground(ConversionCallback... conversionCallbacks) {
            return SpeechToTextBuilder.spellingBuilder(conversionCallbacks[0]);
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
        var current = (SpellingTestFragment) fragments.get(binding.viewPagerTest.getCurrentItem());
        current.getAnimationView().setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        checkLogIsEnable();
        destroyedEngines();
        checkProgressData();
        UtilityFunctions.checkProgressAvailable(progressDataBase, "English" + "Spelling", UtilityFunctions.getDBNameSpelling(spellings, this), new Date(),
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        destroyedEngines();
    }

    private void playPauseAnimation(Boolean play) {
        if (play)
            binding.imageViewTeacher.playAnimation();
        else
            binding.imageViewTeacher.pauseAnimation();
    }

    private void setToolbar() {
        binding.toolBar.titleText.setText(getResources().getString(R.string.spelling));
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

    private void gotoScoreCard() {

        Intent intent = new Intent(getApplicationContext(), ScoreActivity.class);
        intent.putExtra("wrongAns", correctCount);
        intent.putExtra("correctAns", wrongCount);
        intent.putExtra("chapter", UtilityFunctions.getDBNameSpelling(spellings, this).replace("Spelling", ""));

        startActivity(intent);
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
        progressData = UtilityFunctions.checkProgressAvailable(progressDataBase, "English" + "Spelling", UtilityFunctions.getDBNameSpelling(spellings, this), new Date(), 0, true);

        try {
            if (progressData != null) {
                timeSpend = progressData.get(0).time_spend;
            }
        } catch (Exception e) {
            timeSpend = 0;
        }

    }
}