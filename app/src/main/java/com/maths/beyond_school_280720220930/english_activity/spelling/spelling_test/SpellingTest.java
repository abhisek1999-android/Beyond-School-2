package com.maths.beyond_school_280720220930.english_activity.spelling.spelling_test;

import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_CATEGORY_ID;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_IS_OPEN_FROM_LEARN;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_OPEN_TYPE;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_SPELLING_DETAIL;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_TITLE;
import static com.maths.beyond_school_280720220930.utils.UtilityFunctions.capitalize;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maths.beyond_school_280720220930.LogActivity;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.ScoreActivity;
import com.maths.beyond_school_280720220930.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_280720220930.database.english.spelling_common_words.SpellingCommonWordDao;
import com.maths.beyond_school_280720220930.database.english.spelling_common_words.model.SpellingCategoryModel;
import com.maths.beyond_school_280720220930.database.english.spelling_common_words.model.SpellingDetail;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.database.log.LogDatabase;
import com.maths.beyond_school_280720220930.database.process.ProgressDataBase;
import com.maths.beyond_school_280720220930.database.process.ProgressM;
import com.maths.beyond_school_280720220930.databinding.ActivitySpellingCwTestBinding;
import com.maths.beyond_school_280720220930.english_activity.spelling.SpellingTypeConverter;
import com.maths.beyond_school_280720220930.english_activity.vocabulary.EnglishViewPager;
import com.maths.beyond_school_280720220930.firebase.CallFirebaseForInfo;
import com.maths.beyond_school_280720220930.retrofit.ApiClient;
import com.maths.beyond_school_280720220930.retrofit.ApiInterface;
import com.maths.beyond_school_280720220930.retrofit.model.content.ContentModel;
import com.maths.beyond_school_280720220930.retrofit.model.content_new.ContentModelNew;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.CollectionUtils;
import com.maths.beyond_school_280720220930.utils.Constants;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

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
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;


public class SpellingTest extends AppCompatActivity {


    private static final String TAG = "SpellingTest";
    private ActivitySpellingCwTestBinding binding;
    private final int REQUEST_FOR_QUESTION = 345 * 35;


    private Boolean isSpeaking = false;

    private String category;
    private TextToSpeckConverter tts = null;
    private TextToSpeckConverter ttsHelper = null;
    private MediaPlayer mediaPlayer = null;
    private LogDatabase logDatabase;
    private int currentWordLetterPosition = 0;
    private ButtonClick buttonClickListener;

    private String logs = "";

    private SpellingCommonWordDao dao;
    private List<SpellingDetail> spellingDetails;
    private List<Fragment> fragments;
    private GradeDatabase gradeDatabase;
    private FirebaseFirestore kidsDb;
    private String kidsGrade;
    private String kidsId;
    private GradeDatabase databaseGrade;
    private JSONArray kidsActivityJsonArray = new JSONArray();
    private String inputWord = "";
    private long startTime;

    private FirebaseAnalytics analytics;
    private FirebaseAuth auth;
    private int kidAge;
    private String kidName;
    private String gResult = "";
    private String parentsContactId;
    private long timeSpend = 0;
    public static final int TIMER_VALUE = 15;
    private boolean isTimerRunning = true;
    private ProgressDataBase progressDataBase;
    private List<ProgressM> progressData;

    private int correctAnswer = 0;                                                                  // correct answer counter
    private int wrongAnswer = 0;
    private final static int REQUEST_QUESTION = 2 * 44;                                             // request code for asking question
    private final static int REQUEST_CORRECT_OR_WRONG_ANSWER = 2 * 45;

    private boolean isOnline = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpellingCwTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        logDatabase = LogDatabase.getDbInstance(this);
        databaseGrade = GradeDatabase.getDbInstance(this);
        kidsGrade = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_grade));
        logDatabase = LogDatabase.getDbInstance(this);
        kidsId = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_id));
        kidsDb = FirebaseFirestore.getInstance();
        analytics = FirebaseAnalytics.getInstance(this);
        auth = FirebaseAuth.getInstance();
        analytics.setUserId(auth.getCurrentUser().getUid());
        kidAge = UtilityFunctions.calculateAge(PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_dob)));
        kidsId = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_id));
        kidName = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_name));
        parentsContactId = PrefConfig.readIdInPref(this, getResources().getString(R.string.parent_contact_details));
        progressDataBase = ProgressDataBase.getDbInstance(SpellingTest.this);
        progressData = new ArrayList<>();
        setToolbar();
        setData();
        binding.learnOrTest.setOnClickListener((c) -> {
            onBackPressed();
        });
        setButtonVisibility();
    }

    private void setButtonVisibility() {
        var isVisible = getIntent().getBooleanExtra(EXTRA_IS_OPEN_FROM_LEARN, false);
        var param = binding.learnOrTest.getLayoutParams();
        if (!isVisible) {
            param.height = 1;
            binding.learnOrTest.setLayoutParams(param);
            binding.learnOrTest.setVisibility(View.INVISIBLE);
        }
    }


    private void setData() {
        if (getIntent().hasExtra(EXTRA_SPELLING_DETAIL)) {
            category = getIntent().getStringExtra(EXTRA_SPELLING_DETAIL).trim();
            isOnline = getIntent().getBooleanExtra(Constants.EXTRA_ONLINE_FLAG, false);
            dao = EnglishGradeDatabase.getDbInstance(this).spellingCommonWordDao();
            if (isOnline)
                getSubjectData();
            else
                setViews();

            buttonClick();
        } else {
            UtilityFunctions.simpleToast(this, "No data found");
        }

        Log.d(TAG, "setData: "+getIntent().getStringExtra(EXTRA_OPEN_TYPE));

//        if (getIntent().getStringExtra(EXTRA_OPEN_TYPE).equals(Constants.OpenType.LEARNING.name())){
//            GradeDatabase.getDbInstance(this).gradesDaoUpdated().updateIsComplete(true,category);
//            Log.d(TAG, "uploadData: "+"Leaning"+"Spelling");
//
//        }
//        else if(getIntent().getStringExtra(EXTRA_OPEN_TYPE).equals(Constants.OpenType.EXERCISE.name())) {
//            UtilityFunctions.updateDbUnlock(databaseGrade, "Spelling", category);
//            Log.d(TAG, "uploadData: "+"Exe");
//        }
    }

    private void getSubjectData() {
        if (getIntent().hasExtra(Constants.EXTRA_FLAG_HAVE_DATA)) {
            var d = (ContentModelNew) getIntent().getSerializableExtra(Constants.EXTRA_DATA);
            if (getIntent().getBooleanExtra(EXTRA_IS_OPEN_FROM_LEARN, false))
                mapData(d.getLearning());
            else mapData(d.getExercise());

        } else {
            Retrofit retrofit = ApiClient.getClient();
            var api = retrofit.create(ApiInterface.class);
            api.getVocabularySubject(PrefConfig.readIdInPref(this, getResources().getString(R.string.kids_grade)).toLowerCase().replace(" ", ""), "english", getIntent().getStringExtra(EXTRA_TITLE).toLowerCase(), category).enqueue(new retrofit2.Callback<>() {
                @Override
                public void onResponse(Call<ContentModel> call, Response<ContentModel> response) {
                    Log.d(TAG, "onResponse: " + response.code());
                    if (response.body() != null) {
                        Log.d(TAG, "onResponse: " + response.body().getContent().toString());
                        mapData(response.body().getContent());
                    }
                }

                @Override
                public void onFailure(Call<ContentModel> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
                }
            });
        }
    }

    private void mapData(List<ContentModel.Content> content) {
        var typeConverter = new SpellingTypeConverter();
        spellingDetails = typeConverter.mapToList(content);
        setViews();
    }


    private void setViews() {
        binding.text.setText(category.replace("Spelling_CommonWords", "").trim());
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
                initMediaPlayer();
                startSpeaking();
                setButtonText();
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
        if (!isOnline) {
            var data = getSpellingFromType(dao.getSpellingModel(1).getSpelling(), category);

            if (data == null) {
                UtilityFunctions.simpleToast(this, "No data found for this spelling");
                return;
            }
            spellingDetails = data.getDetails();
        }
        List<Fragment> fragments = getFragments(spellingDetails);

        var pagerAdapter = new EnglishViewPager(fragments, getSupportFragmentManager(), getLifecycle());
        binding.viewPagerTest.setUserInputEnabled(false);
        binding.viewPagerTest.setAdapter(pagerAdapter);
        isSpeaking = binding.playPause.isChecked();
        handleDeleteWord();
    }

    private void handleDeleteWord() {
        binding.key5.setOnClickListener(v -> {
            if (inputWord.length() > 0) {
                var currentFragment = (SpellingTestFragment) fragments.get(binding.viewPagerTest.getCurrentItem());
                var textView = currentFragment.getTextView();
                var text = textView.getText().toString();
                var newText = UtilityFunctions.replace(text, findFirstOccurrence(text, "_") - 1, '_');
                textView.setText(newText);
                currentWordLetterPosition--;
                setButtonText();
                inputWord = inputWord.substring(0, inputWord.length() - 1);
                Log.d(TAG, "handleDeleteWord: " + inputWord + " Current word letter position: " + currentWordLetterPosition);
            }
        });
    }

    public static int findFirstOccurrence(String str, String find) {
        int index = str.indexOf(find);
        return index;
    }


    private List<Fragment> getFragments(List<SpellingDetail> data) {
        fragments = CollectionUtils.mapWithIndex(spellingDetails.stream(), (index, item) -> new SpellingTestFragment(item, index + 1)).collect(Collectors.toList());
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

                    textView.setText(Html.fromHtml(capitalize(split[0]) + " <font color='#64c1c7'>" + spellingDetail.getWord().replaceAll("[A-Za-z]", "_") + "</font> " + split[1]));
                    try {
                        helperTTS("Now type the word " + (spellingDetails.get(binding.viewPagerTest.getCurrentItem()).getWord().equals("The") ? "'di'" : "'" + spellingDetails.get(binding.viewPagerTest.getCurrentItem()).getWord()) + "' .", false, REQUEST_FOR_QUESTION);
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
                    var sen = sentence.replace("<b>", "").replace("</b>", "");
                    Spannable textWithHighlights = new SpannableString(sen);
                    textWithHighlights.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.primary)), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    textView.setText(textWithHighlights);
                }
            });
        });
    }

    private void startSpeaking() throws ExecutionException, InterruptedException {
        timer();
        tts.initialize(spellingDetails.get(binding.viewPagerTest.getCurrentItem()).getDescription()
                .replace("<b>", "").replace("</b>", ""), SpellingTest.this);
        var spellingDetail = spellingDetails.get(binding.viewPagerTest.getCurrentItem());
        var split = spellingDetail.getDescription().toLowerCase(Locale.ROOT).split(spellingDetail.getWord().toLowerCase(Locale.ROOT), 2);

        var sentence = capitalize(split[0]) + spellingDetail.getWord().replaceAll("[A-Z a-z]", "_") + split[1];
        tts.setTextViewAndSentence(sentence.replace("<b>", "").replace("</b>", ""));
        playPauseAnimation(true);
        if (binding.viewPagerTest.getCurrentItem() == (spellingDetails.size() - 1))
            isSpeaking = false;
    }


    private void updateViews() {
        setButtonText();
        binding.correctText.setText(String.valueOf(correctAnswer));
        binding.wrongText.setText(String.valueOf(wrongAnswer));
    }

    public void setButtonText() {                                                                   // set shuffled alphabet to button with one
        var currentPosition = binding.viewPagerTest.getCurrentItem();                           // correct answer
        var currentWord = spellingDetails.get(currentPosition).getWord();
        var currentLetter = currentWord.charAt(currentWordLetterPosition);

        var array = new ArrayList<Character>();
        array.add(currentLetter);
        array.add(UtilityFunctions.getRandomAlphabet());
        array.add(UtilityFunctions.getRandomAlphabet());
        array.add(UtilityFunctions.getRandomAlphabet());
        var shuffleArray = UtilityFunctions.shuffleArray(array);
        binding.key1.setText(shuffleArray.get(0).toString());
        binding.key2.setText(shuffleArray.get(1).toString());
        binding.key3.setText(shuffleArray.get(2).toString());
        binding.key4.setText(shuffleArray.get(3).toString());
    }

    private void updateScoreAndResetViews() {
        UtilityFunctions.runOnUiThread(() -> {
            binding.wrongText.setText(String.valueOf(wrongAnswer));
            binding.correctText.setText(String.valueOf(correctAnswer));
            setButtonText();
        });
    }


    private void helperTTS(String message, boolean canNavigate, int request) throws ExecutionException, InterruptedException {
        ttsHelper = new TTSHelperAsyncTask().execute(new ConversionCallback() {
            @Override
            public void onCompletion() {
                if (!canNavigate && request == REQUEST_FOR_QUESTION) {
                    startTime = new Date().getTime();
                    isVisibleButtonKeys(true);
                    playPauseAnimation(false);
                    handleInputWord();
                }
                if (request == REQUEST_CORRECT_OR_WRONG_ANSWER && canNavigate) {                    // true if correct or wrong answer is asked
                    isVisibleButtonKeys(false);
                    mediaPlayer.pause();
                    if (binding.viewPagerTest.getCurrentItem() == spellingDetails.size() - 1) {
                        playPauseAnimation(false);
                        playPauseAnimation(false);
                        uploadData();
                        return;
                    }
                    if (isSpeaking) {
                        binding.viewPagerTest.setCurrentItem(binding.viewPagerTest.getCurrentItem() + 1);
                        try {
                            startSpeaking();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        updateScoreAndResetViews();
                    }
                }
            }

            @Override
            public void onErrorOccurred(String errorMessage) {

            }
        }).get().initialize(message, this);
    }

    private void handleInputWord() {                                                                // handle input word
        UtilityFunctions.runOnUiThread(() -> {

            var currentPosition = binding.viewPagerTest.getCurrentItem();                          // get current position
            var currentWord = spellingDetails.get(currentPosition).getWord();                       // get current word
            var currentFragment = (SpellingTestFragment) fragments.get(currentPosition);        // get current fragment
            var textView = currentFragment.getTextView();                                      // get text view of current fragment
            buttonClickListener = s -> {
                Log.d("XXX", "handleInputWord: " + s);
                inputWord += s;
                var textViewText = textView.getText().toString();                                // get text from text view
                textView.setText(textViewText.replaceFirst("_", s));
                currentWordLetterPosition++;
                if (currentWordLetterPosition < currentWord.length()) setButtonText();
                else checkAnswer();
            };
            handleButtonClick();
        });
    }

    private void checkAnswer() {
        var currentPosition = binding.viewPagerTest.getCurrentItem();                          // get current position
        var currentWord = spellingDetails.get(currentPosition).getWord();                       // get current word
        var currentFragment = (SpellingTestFragment) fragments.get(currentPosition);        // get current fragment
        var textView = currentFragment.getTextView();                                      // get text view of current fragment
        long endTime = new Date().getTime();
        var diff = endTime - startTime;
        if (inputWord.toLowerCase(Locale.ROOT).equals(currentWord.toLowerCase(Locale.ROOT))) {
            correctAnswer++;
            textView.setTextColor(getResources().getColor(R.color.green));
            logs += "Time Take :" + UtilityFunctions.formatTime(diff) + ", Correct .\n";
            sendDataToAnalytics(currentWord, diff, true);
            inputWord = "";
            try {
                helperTTS(UtilityFunctions.getCompliment(true),                             // true if correct answer
                        true, REQUEST_CORRECT_OR_WRONG_ANSWER);
                mediaPlayer.start();

                putJsonData("Question : " + "Type is the spelling of " + currentWord + " ?", currentWord, diff, true);
            } catch (ExecutionException | InterruptedException | JSONException e) {
                logs += e.getMessage() + "\n";
            }
        } else {
            wrongAnswer++;
            textView.setTextColor(getResources().getColor(R.color.sweet_red));
            logs += "Time Take :" + UtilityFunctions.formatTime(diff) + ", Wrong .\n";
            sendDataToAnalytics(currentWord, diff, false);
            inputWord = "";
            try {
                helperTTS(UtilityFunctions.getCompliment(false),                            // false if wrong answer
                        true, REQUEST_CORRECT_OR_WRONG_ANSWER);

                putJsonData("Question : " + "Type is the spelling of " + currentWord + " ?", currentWord, diff, false);
            } catch (ExecutionException | InterruptedException | JSONException e) {
                logs += e.getMessage() + "\n";
            }
        }
        isVisibleButtonKeys(false);
        currentWordLetterPosition = 0;
    }


    private void sendDataToAnalytics(String currentWord, long diff, boolean b) {
        UtilityFunctions.sendDataToAnalytics(analytics, auth, Objects.requireNonNull(auth.getCurrentUser()).getUid(), kidsId, kidName, "English-Test-" + "spelling common words", kidAge, currentWord, inputWord, b, (int) (diff), "Type the spelling of the word" + currentWord, "English", parentsContactId);
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

    private void playPauseAnimation(Boolean play) {
        UtilityFunctions.runOnUiThread(() -> {
            if (play) binding.imageViewTeacher.playAnimation();
            else binding.imageViewTeacher.pauseAnimation();
        });
    }

    private void isVisibleButtonKeys(boolean isVisible) {                                           // set visibility of button keys
        UtilityFunctions.runOnUiThread(() -> {
            if (isVisible) binding.linearLayout.setVisibility(View.VISIBLE);
            else binding.linearLayout.setVisibility(View.INVISIBLE);

        });
    }


    private void destroyedEngines() {
        binding.playPause.setChecked(false);
        playPauseAnimation(false);
        if (tts != null) tts.destroy();
        if (ttsHelper != null) ttsHelper.destroy();
        if (mediaPlayer != null) mediaPlayer.release();
        setLayoutVisible(false);
        var current = (SpellingTestFragment) fragments.get(binding.viewPagerTest.getCurrentItem());
        current.getAnimationView().setVisibility(View.GONE);
    }

    private void setLayoutVisible(Boolean visible) {
        UtilityFunctions.runOnUiThread(() -> {
            binding.linearLayout.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        checkLogIsEnable();
        destroyedEngines();
        checkProgressData();
        UtilityFunctions.checkProgressAvailable(progressDataBase, getIntent().getStringExtra(EXTRA_CATEGORY_ID), category, new Date(), timeSpend + Integer.parseInt(binding.timeText.getText().toString()), false);
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


    @SuppressLint("SetTextI18n")
    private void setToolbar() {
        binding.toolBar.titleText.setText(getResources().getString(R.string.spelling_common_words).replace("Spelling_", " ") + " text");
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
        intent.putExtra("wrongAns", wrongAnswer);
        intent.putExtra("correctAns", correctAnswer);
        intent.putExtra("chapter", category.replace("Spelling_CommonWords", " "));

        startActivity(intent);
    }

    private void timer() {
        isTimerRunning = false;
        Observable.interval(60, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).doOnNext(x -> {
            // update your view here

            binding.timerProgress.setMax(15);
            binding.timerProgress.setProgress(Integer.parseInt((x + 1) + ""));
            binding.timeText.setText((x + 1) + "");
            Log.i("task", x + "");
        }).takeUntil(aLong -> aLong == TIMER_VALUE).doOnComplete(() ->
                // do whatever you need on complete
                Log.i("TSK", "task")).subscribe();


    }

    private void checkProgressData() {
        progressData = UtilityFunctions.checkProgressAvailable(progressDataBase, getIntent().getStringExtra(EXTRA_CATEGORY_ID), category, new Date(), 0, true);

        try {
            if (progressData != null) {
                timeSpend = progressData.get(0).time_spend;
            }
        } catch (Exception e) {
            timeSpend = 0;
        }

    }

    private void uploadData() {

        try {
            if (correctAnswer >= UtilityFunctions.getNinetyPercentage(spellingDetails.size())) {
//                UtilityFunctions.updateDbUnlock(databaseGrade, kidsGrade, "Spelling_CommonWords", category);
                CallFirebaseForInfo.checkActivityData(kidsDb, kidsActivityJsonArray, "pass", auth, kidsId, kidsGrade.toLowerCase().replace(" ", ""),category, "Spelling",getIntent().getStringExtra(EXTRA_CATEGORY_ID),correctAnswer, wrongAnswer, spellingDetails.size(), "english");
                if (getIntent().getStringExtra(EXTRA_OPEN_TYPE).equals(Constants.OpenType.LEARNING.name())){
                    GradeDatabase.getDbInstance(this).gradesDaoUpdated().updateIsComplete(true,category);
                    Log.d(TAG, "uploadData: "+"Leaning"+"Spelling");

                }
                else if(getIntent().getStringExtra(EXTRA_OPEN_TYPE).equals(Constants.OpenType.EXERCISE.name())) {
                    UtilityFunctions.updateDbUnlock(databaseGrade, "Spelling", category,false);
                    Log.d(TAG, "uploadData: "+"Exe");
                }
                progressDataBase.progressDao().updateScore(correctAnswer, wrongAnswer, category);


            } else {
                CallFirebaseForInfo.checkActivityData(kidsDb, kidsActivityJsonArray, "fail", auth, kidsId, category,kidsGrade.toLowerCase().replace(" ", "") ,"Spelling",getIntent().getStringExtra(EXTRA_CATEGORY_ID) ,correctAnswer, wrongAnswer, spellingDetails.size(), "english");
            }
            gotoScoreCard();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void handleButtonClick() {
        var currentPosition = binding.viewPagerTest.getCurrentItem();                           // get current position of view pager
        var currentWord = spellingDetails.get(currentPosition).getWord();
        binding.key1.setOnClickListener(v -> {
            if (currentWordLetterPosition < currentWord.length())
                buttonClickListener.onClick(binding.key1.getText().toString());
        });
        binding.key2.setOnClickListener(v -> {
            if (currentWordLetterPosition < currentWord.length())
                buttonClickListener.onClick(binding.key2.getText().toString());
        });
        binding.key3.setOnClickListener(v -> {
            if (currentWordLetterPosition < currentWord.length())
                buttonClickListener.onClick(binding.key3.getText().toString());
        });
        binding.key4.setOnClickListener(v -> {
            if (currentWordLetterPosition < currentWord.length())
                buttonClickListener.onClick(binding.key4.getText().toString());
        });
    }

    interface ButtonClick {                                                                         // interface for test option button click
        void onClick(String s);
    }
}