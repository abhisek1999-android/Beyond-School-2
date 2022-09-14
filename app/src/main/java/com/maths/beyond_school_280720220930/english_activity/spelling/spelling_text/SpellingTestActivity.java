package com.maths.beyond_school_280720220930.english_activity.spelling.spelling_text;

import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_SPELLING_CATEGORY;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_SPELLING_LIST;

import android.content.Context;
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
import com.maths.beyond_school_280720220930.AdditionActivity;
import com.maths.beyond_school_280720220930.LogActivity;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.ScoreActivity;
import com.maths.beyond_school_280720220930.database.english.spelling.model.SpellingModel;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.database.log.LogDatabase;
import com.maths.beyond_school_280720220930.database.process.ProgressDataBase;
import com.maths.beyond_school_280720220930.databinding.ActivitySpellingTestBinding;
import com.maths.beyond_school_280720220930.english_activity.vocabulary.EnglishViewPager;
import com.maths.beyond_school_280720220930.firebase.CallFirebaseForInfo;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.CollectionUtils;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RequiresApi(api = Build.VERSION_CODES.N)
public class SpellingTestActivity extends AppCompatActivity {

    private static final String TAG = "SpellingTextActivity";
    private final Context context = SpellingTestActivity.this;
    private ActivitySpellingTestBinding binding;

    private String category;
    private ArrayList<SpellingModel> spellingList;
    private List<Fragment> spellingFragments;
    private ButtonClick buttonClickListener;                                                        // ButtonClick is an interface to handle button clicks


    private boolean isSpeaking = false;
    private TextToSpeckConverter ttsHelper;
    private MediaPlayer mediaPlayer = null;
    private int correctAnswer = 0;                                                                  // correct answer counter
    private int wrongAnswer = 0;                                                                    // wrong answer counter
    private String inputWord = "";                                                                  // input text from user
    private int currentWordLetterPosition = 0;                                                      // current word letter position

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

    private final static int REQUEST_QUESTION = 2 * 44;                                             // request code for asking question
    private final static int REQUEST_CORRECT_OR_WRONG_ANSWER = 2 * 45;                              // request code for correct answer

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpellingTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        logDatabase = LogDatabase.getDbInstance(context);
        databaseGrade = GradeDatabase.getDbInstance(context);
        progressDataBase = ProgressDataBase.getDbInstance(SpellingTestActivity.this);

        kidsGrade = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_grade));
        kidsId = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_id));
        kidsDb = FirebaseFirestore.getInstance();
        analytics = FirebaseAnalytics.getInstance(this);
        auth = FirebaseAuth.getInstance();
        kidAge = UtilityFunctions.calculateAge(PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_dob)));
        kidsId = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_id));
        kidName = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_name));


        if (getIntent().hasExtra(EXTRA_SPELLING_LIST)) {
            var tempList = (ArrayList<SpellingModel>) getIntent().
                    getSerializableExtra(EXTRA_SPELLING_LIST);
            spellingList = UtilityFunctions.shuffleArray(tempList);
            category = getIntent().getStringExtra(EXTRA_SPELLING_CATEGORY).
                    replace("Spelling", "")
                    .replaceAll("\\d", "");                                         // remove unwanted string from category
            setToolbar();
            setPager();
        } else {
            UtilityFunctions.simpleToast(context, "Something went wrong");
        }
    }


    private void setPager() {
        spellingFragments = getFragments(spellingList);                                             // get all fragments
        var adapter = new EnglishViewPager(
                spellingFragments
                , getSupportFragmentManager()
                , getLifecycle());
        binding.viewPagerTest.setAdapter(adapter);
        binding.viewPagerTest.setOffscreenPageLimit(1);
        binding.viewPagerTest.setUserInputEnabled(false);
        handlePlayPause();
        setButtonText();
    }

    public void setButtonText() {                                                                   // set shuffled alphabet to button with one
        var currentPosition = binding.viewPagerTest.getCurrentItem();                           // correct answer
        var currentWord = spellingList.get(currentPosition).getWord();
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

    private void handlePlayPause() {
        isSpeaking = binding.playPause.isChecked();
        binding.playPause.setOnClickListener(v -> {
            isSpeaking = binding.playPause.isChecked();
            if (isSpeaking) {
                startSpeaking();
                if (spellingList.size() < binding.viewPagerTest.getCurrentItem())
                    isSpeaking = false;
            } else {
                destroyEngine();
            }
        });
    }

    private void startSpeaking() {
        try {
            playPauseAnimation(true);
            helperTTS("Type the spelling of the word",                                      // ask question to user
                    false, REQUEST_QUESTION);
            initMediaPlayer();
        } catch (ExecutionException | InterruptedException e) {
            logs += e.getMessage() + "\n";
        }
    }


    @NonNull
    private List<Fragment> getFragments(List<SpellingModel> spellingModels) {
        return CollectionUtils.mapWithIndex(spellingModels.stream(), (index, item) ->
                new FragmentSpellingTest(item, index + 1)).collect(Collectors.toList());
    }

    private void initMediaPlayer() {
        mediaPlayer = UtilityFunctions.playClapSound(this);
    }

    private void helperTTS(String message, boolean canNavigate, int request) throws
            ExecutionException, InterruptedException {
        ttsHelper = new TTSHelperAsyncTask().execute(new ConversionCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCompletion() {
                if (request == REQUEST_QUESTION && !canNavigate) {                                  // true if question is asked
                    startTime = new Date().getTime();
                    isVisibleButtonKeys(true);
                    playPauseAnimation(false);
                    handleInputWord();
                    return;
                }
                if (request == REQUEST_CORRECT_OR_WRONG_ANSWER && canNavigate) {                    // true if correct or wrong answer is asked
                    isVisibleButtonKeys(false);
                    mediaPlayer.pause();
                    if (binding.viewPagerTest.getCurrentItem() == spellingFragments.size() - 1) {
                        playPauseAnimation(false);
                        isPlayPauseChecked();
                        uploadData();
                        return;
                    }
                    if (isSpeaking) {
                        startSpeaking();
                        updateScoreAndResetViews();
                        binding.viewPagerTest.setCurrentItem(binding.viewPagerTest.getCurrentItem() + 1);
                    }
                }
            }

            @Override
            public void onErrorOccurred(String errorMessage) {

            }
        }).get().initialize(message, this);
    }

    private void uploadData() {
        var dbName = getIntent().getStringExtra(EXTRA_SPELLING_CATEGORY);
        try {
            if (correctAnswer >= UtilityFunctions.getNinetyPercentage(spellingList.size())) {
                UtilityFunctions.updateDbUnlock(
                        databaseGrade,
                        kidsGrade,
                        "Spelling",
                        dbName
                );
                CallFirebaseForInfo.checkActivityData(kidsDb,
                        kidsActivityJsonArray, "pass", auth, kidsId, dbName,
                        "spelling", correctAnswer, wrongAnswer, spellingList.size(), "english");

                progressDataBase.progressDao().updateScore(correctAnswer,wrongAnswer,category);

            } else {
                CallFirebaseForInfo.checkActivityData(kidsDb,
                        kidsActivityJsonArray, "fail", auth, kidsId, dbName,
                        "spelling", correctAnswer, wrongAnswer, spellingList.size(), "english");
            }
            gotoScoreCard();
        } catch (JSONException e) {
            e.printStackTrace();
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


    private void updateScoreAndResetViews() {
        UtilityFunctions.runOnUiThread(() -> {
            binding.wrongText.setText(String.valueOf(wrongAnswer));
            binding.correctText.setText(String.valueOf(correctAnswer));
            setButtonText();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleInputWord() {                                                                // handle input word
        var currentPosition = binding.viewPagerTest.getCurrentItem();                          // get current position
        var currentWord = spellingList.get(currentPosition).getWord();                       // get current word
        var currentFragment = (FragmentSpellingTest) spellingFragments.get(currentPosition);        // get current fragment
        var textView = currentFragment.getTextView();                                      // get text view of current fragment
        buttonClickListener = s -> {
            inputWord += s;
            var textViewText = textView.getText().toString();                                // get text from text view
            textView.setText(textViewText.replaceFirst("_", s));
            currentWordLetterPosition++;
            if (currentWordLetterPosition < currentWord.length())
                setButtonText();
            else
                checkAnswer();
        };
        handleButtonClick();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkAnswer() {
        var currentPosition = binding.viewPagerTest.getCurrentItem();                          // get current position
        var currentWord = spellingList.get(currentPosition).getWord();                       // get current word
        var currentFragment = (FragmentSpellingTest) spellingFragments.get(currentPosition);        // get current fragment
        var textView = currentFragment.getTextView();                                      // get text view of current fragment
        long endTime = new Date().getTime();
        var diff = endTime - startTime;
        if (inputWord.equals(currentWord)) {
            correctAnswer++;
            textView.setTextColor(getResources().getColor(R.color.green));
            logs += "Time Take :" + UtilityFunctions.formatTime(diff) + ", Correct .\n";
            sendDataToAnalytics(currentWord, diff, true);
            inputWord = "";
            try {
                helperTTS(UtilityFunctions.getCompliment(true),                             // true if correct answer
                        true, REQUEST_CORRECT_OR_WRONG_ANSWER);
                mediaPlayer.start();

                putJsonData("Question : " +
                                "Type is the spelling of " + currentWord + " ?",
                        currentWord,
                        diff, true);
            } catch (ExecutionException | InterruptedException e) {
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

                putJsonData("Question : " +
                                "Type is the spelling of " + currentWord + " ?",
                        currentWord,
                        diff, false);
            } catch (ExecutionException | InterruptedException e) {
                logs += e.getMessage() + "\n";
            }
        }
        isVisibleButtonKeys(false);
        currentWordLetterPosition = 0;
    }

    private void sendDataToAnalytics(String currentWord, long diff, boolean b) {
        UtilityFunctions.sendDataToAnalytics(analytics,
                Objects.requireNonNull(auth.getCurrentUser()).getUid(), kidsId, kidName,
                "English-Test-" + "spelling", kidAge, currentWord
                , inputWord, b, (int) (diff),
                "Type the spelling of the word" + currentWord, "English");
    }

    private void handleButtonClick() {
        var currentPosition = binding.viewPagerTest.getCurrentItem();                           // get current position of view pager
        var currentWord = spellingList.get(currentPosition).getWord();
        binding.key1.setOnClickListener(v -> {
                    if (currentWordLetterPosition < currentWord.length())
                        buttonClickListener.onClick(binding.key1.getText().toString());
                }
        );
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

    private void destroyEngine() {
        if (ttsHelper != null) {
            ttsHelper.destroy();
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        playPauseAnimation(false);
        isVisibleButtonKeys(false);
        correctAnswer = 0;
        wrongAnswer = 0;
        isPlayPauseChecked();
    }

    private void isPlayPauseChecked() {
        UtilityFunctions.runOnUiThread(() -> binding.playPause.setChecked(false));

    }


    private void playPauseAnimation(Boolean play) {
        UtilityFunctions.runOnUiThread(() -> {
            if (play)
                binding.imageViewTeacher.playAnimation();
            else
                binding.imageViewTeacher.pauseAnimation();
        });
    }

    private void isVisibleButtonKeys(boolean isVisible) {                                           // set visibility of button keys
        UtilityFunctions.runOnUiThread(() -> {
            if (isVisible)
                binding.linearLayout.setVisibility(View.VISIBLE);
            else
                binding.linearLayout.setVisibility(View.INVISIBLE);

        });
    }

    @SuppressWarnings("deprecation")
    static class TTSHelperAsyncTask extends AsyncTask<ConversionCallback, Void, TextToSpeckConverter> {
        @Override
        protected TextToSpeckConverter doInBackground(ConversionCallback... conversionCallbacks) {
            return TextToSpeechBuilder.builder(conversionCallbacks[0]);
        }
    }


    private void setToolbar() {
        binding.toolBar.titleText.setText(getResources().getString(R.string.spelling_test));
        binding.toolBar.imageViewBack.setOnClickListener(v -> onBackPressed());
        binding.text.setText(category);
        binding.toolBar.getRoot().inflateMenu(R.menu.log_menu);
        binding.toolBar.getRoot().setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.action_log) {
                startActivity(new Intent(getApplicationContext(), LogActivity.class));

                return true;
            }
            return super.onOptionsItemSelected(item);

        });
    }

    private void checkLogIsEnable() {
        if (PrefConfig.readIdInPref(getApplicationContext(), "IS_LOG_ENABLE").equals("true"))
            saveLog();
    }

    private void saveLog() {
        Log.d(TAG, "saveLog: Called " + logs);
        if (!logs.isEmpty())
            UtilityFunctions.saveLog(logDatabase, logs);
    }

    @Override
    protected void onPause() {
        super.onPause();
        destroyEngine();
        checkLogIsEnable();
    }


    private void gotoScoreCard() {
        Intent intent = new Intent(getApplicationContext(), ScoreActivity.class);
        intent.putExtra("wrongAns", wrongAnswer);
        intent.putExtra("correctAns", correctAnswer);
        intent.putExtra("chapter", category);
        startActivity(intent);
        finish();
    }

    interface ButtonClick {                                                                         // interface for test option button click
        void onClick(String s);
    }
}