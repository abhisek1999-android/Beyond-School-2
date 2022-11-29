package com.maths.beyond_school_new_071020221400;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maths.beyond_school_new_071020221400.SP.PrefConfig;
import com.maths.beyond_school_new_071020221400.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_new_071020221400.database.log.LogDatabase;
import com.maths.beyond_school_new_071020221400.database.process.ProgressDataBase;
import com.maths.beyond_school_new_071020221400.database.process.ProgressM;
import com.maths.beyond_school_new_071020221400.databinding.ActivityAdditionBinding;
import com.maths.beyond_school_new_071020221400.extras.CustomProgressDialogue;
import com.maths.beyond_school_new_071020221400.firebase.CallFirebaseForInfo;
import com.maths.beyond_school_new_071020221400.subjects.MathsHelper;
import com.maths.beyond_school_new_071020221400.translation_engine.ConversionCallback;
import com.maths.beyond_school_new_071020221400.translation_engine.SpeechToTextBuilder;
import com.maths.beyond_school_new_071020221400.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_new_071020221400.translation_engine.translator.SpeechToTextConverter;
import com.maths.beyond_school_new_071020221400.translation_engine.translator.SpeechToTextConverterVosk;
import com.maths.beyond_school_new_071020221400.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_new_071020221400.utils.Soundex;
import com.maths.beyond_school_new_071020221400.utils.UtilityFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AdditionActivityNew extends AppCompatActivity {

    private static final String TAG = AdditionActivityNew.class.getSimpleName();
    private ActivityAdditionBinding binding;
    private int currentAnswer;
    private int currentQuestion = 1;
    private int correctAnswer = 0;
    private int wrongAnswer = 0;
    private final int MAX_QUESTION = 10;
    private int DELAY_ON_STARTING_STT = 500;
    private int DELAY_ON_SETTING_QUESTION = 3000;
    private TextToSpeckConverter tts;
    private SpeechToTextConverterVosk stt;
    private Boolean isCallTTS = true;
    private Boolean isNewQuestionGenerated = true;
    private Boolean isAnswered = false;
    private Toolbar toolbar;

    private CustomProgressDialogue customProgressDialogue;
    long timeLimit = 10000;
    private long maxStQuesTime = 0;
    private long maxEtQuesTime = 0;

    private TextView digit1Text;
    private String subject = "";
    private String digit = "", videoUrl = "", selectedSub = "";

    private LogDatabase logDatabase;
    private FirebaseAnalytics analytics;
    private FirebaseAuth auth;
    private long startTime = 0, endTime = 0, diff = 0;
    private String logs = "";
    private int currentNum1 = 0;
    private int currentNum2 = 0;
    private FirebaseFirestore kidsDb;
    private List ansList;
    private List timeList;
    private String kidsGrade;
    private GradeDatabase databaseGrade;

    private String kidsId;
    private List<Integer> numberList;
    private JSONArray kidsActivityJsonArray = new JSONArray();
    private ProgressDataBase progressDataBase;
    private MediaPlayer mediaPlayer = null;
    private List<ProgressM> progressData;
    private long timeSpend = 0;
    public static final int TIMER_VALUE = 15;
    private boolean isTimerRunning = true;
    private String kidName;
    private int kidAge;
    private String parentsContactId = "";
    private boolean isAnsByTyping = false;
    private String id="";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdditionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setToolbar();

        ansList = new ArrayList();
        timeList = new ArrayList();
        numberList = new ArrayList<>();
        progressData = new ArrayList<>();

        progressDataBase = ProgressDataBase.getDbInstance(AdditionActivityNew.this);

        databaseGrade = GradeDatabase.getDbInstance(this);
        binding.toolBar.titleText.setText("Addition");
        subject = getIntent().getStringExtra("subject");
        digit = getIntent().getStringExtra("max_digit");
        videoUrl = getIntent().getStringExtra("video_url");
        selectedSub = getIntent().getStringExtra("selected_sub");
        id=getIntent().getStringExtra("id");

        customProgressDialogue = new CustomProgressDialogue(this);

        kidsId = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_id));
        kidsGrade = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_grade));
        kidName = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_name));
        kidAge = UtilityFunctions.calculateAge(PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_dob)));
        parentsContactId = PrefConfig.readIdInPref(AdditionActivityNew.this, getResources().getString(R.string.parent_contact_details));
        logDatabase = LogDatabase.getDbInstance(this);
        analytics = FirebaseAnalytics.getInstance(getApplicationContext());

        auth = FirebaseAuth.getInstance();

        analytics.setUserId(auth.getCurrentUser().getUid());

        kidsDb = FirebaseFirestore.getInstance();

        customProgressDialogue.show();

        initTTS();
        initSTT();
        stt.initialize("", AdditionActivityNew.this);
        setButtonClick();
        setBasicUiElement();
        checkProgressData();
        initMediaPlayer();

        binding.toolBar.imageViewBack.setOnClickListener(v -> {
            onBackPressed();
        });


        // UtilityFunctions.updateDbUnlock(databaseGrade, kidsGrade, selectedSub, subject);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stt.destroy();
    }

    private void checkProgressData() {
        progressData = UtilityFunctions.checkProgressAvailable(progressDataBase, "Mathematics" + subject, selectedSub, new Date(), 0, true);

        try {
            if (progressData != null) {
                timeSpend = progressData.get(0).time_spend;
                binding.timeText.setText(timeSpend + "");
            }
        } catch (Exception e) {
            timeSpend = 0;
        }

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

    private TextView.OnEditorActionListener editorActionListener = (textView, i, keyEvent) -> {

        switch (i) {
            case EditorInfo.IME_ACTION_DONE:
                isCallTTS = true;
                //  initSTT();
                isNewQuestionGenerated = true;

                if (!binding.playPause.isChecked())
                    binding.playPause.setChecked(true);

                try {
                    successResultCalling(binding.ansTextView.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                binding.ansTextView.clearFocus();

                break;
        }
        return false;

    };


    private void initProcess() {

        tts.initialize("Tap the play button to start the test , you can speak and write answer in the answer box", AdditionActivityNew.this);

        //   binding.animWoman.playAnimation();
        // play();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setBasicUiElement() {

        // binding.toolBar.titleText.setText("Learn "+ subject.substring(0, 1).toUpperCase() + subject.substring(1));
        binding.toolBar.titleText.setText(selectedSub);
        // binding.subSub.setText(digit+" Digit "+subject.substring(0, 1).toUpperCase() + subject.substring(1));


        binding.ansTextView.setOnEditorActionListener(editorActionListener);
        binding.ansTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    pause();
                    UtilityFunctions.runOnUiThread(() -> {
                        binding.ansTextView.setText("");
                    }, 100);


                    isAnsByTyping = true;
                    binding.ansTextView.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                            Log.i("ExecutedMethod", editable + "," + currentAnswer);

                            if (isAnsByTyping) {

                                try {
                                    if (Integer.parseInt(editable + "") == currentAnswer) {
                                        isAnsByTyping = false;
                                        UtilityFunctions.runOnUiThread(() -> {

                                            Log.i("Executed", editable + "," + currentAnswer);
                                            isCallTTS = true;
                                            // initSTT();
                                            isNewQuestionGenerated = true;
                                            try {
                                                successResultCalling(binding.ansTextView.getText().toString());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            binding.ansTextView.clearFocus();
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                        }, 100);

                                    }
                                } catch (Exception e) {
                                }

                            }
                        }
                    });
                } else {
                    //lost focus
                }
            }
        });
        if (subject.equals("Addition")) {
            binding.operator.setText("+");
            binding.digitOneH.setVisibility(View.VISIBLE);
            binding.digitOne.setVisibility(View.INVISIBLE);

            digit1Text = binding.digitOneH;

        } else if (subject.equals("Subtraction")) {
            binding.operator.setText("-");
            binding.digitOneH.setVisibility(View.VISIBLE);
            binding.digitOne.setVisibility(View.INVISIBLE);
            digit1Text = binding.digitOneH;
        } else if (subject.equals("Multiplication Tables")) {
            binding.operator.setText("×");
            //   binding.subSub.setText("Multiplication upto "+digit +"'s Table");
            digit1Text = binding.digitOne;

        } else if (subject.equals("Division")) {
            binding.operator.setText("÷");
            digit1Text = binding.digitOne;
        }


//        binding.learnOrTest.setOnClickListener(v->{
//            Intent intent =new Intent(getApplicationContext(), AdditionActivity.class);
//            intent.putExtra("subject",subject);
//            intent.putExtra("max_digit", digit);
//            startActivity(intent);
//        });


        binding.learnOrTest.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LearningActivity.class);
            intent.putExtra("subject", subject);
            intent.putExtra("max_digit", digit);
            intent.putExtra("video_url", videoUrl);
            intent.putExtra("selected_sub", selectedSub);
            startActivity(intent);
            finish();
        });

    }


    private void initMediaPlayer() {
        mediaPlayer = UtilityFunctions.playClapSound(this);
    }

    /**
     * Initialize TTS engine
     * Answer will be check here
     */
    private void initTTS() {
        tts = TextToSpeechBuilder.builder(new ConversionCallback() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onCompletion() {

                if (isCallTTS) {
                    Log.i("inSideTTS", "InitSST");
                    UtilityFunctions.runOnUiThread(() -> {
                        startTime = new Date().getTime();
                        maxStQuesTime = new Date().getTime();
                        binding.animWoman.cancelAnimation();
                        UtilityFunctions.muteAudioStream(AdditionActivityNew.this);
                        if (binding.playPause.isChecked())
                            binding.animationVoice.setVisibility(View.VISIBLE);
                    }, DELAY_ON_STARTING_STT);
                }
                // binding.animWoman.pauseAnimation();
            }

            @Override
            public void onErrorOccurred(String errorMessage) {
                UtilityFunctions.simpleToast(AdditionActivityNew.this, errorMessage);
            }
        });

    }

    /**
     * Initialize STT engine
     * Answer will be check here
     */
    private void initSTT() {
        stt = SpeechToTextBuilder.builderVosk(new ConversionCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(String result) throws JSONException {

                Log.i(TAG, "On Succcess" + result);

                if (!result.equals("-10001")) {
                    binding.animationVoice.setVisibility(View.GONE);
                    try {

                        Log.d(TAG, "onSuccess: " + result);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "onSuccess: " + e.getMessage());
                    }
                    successResultCalling(result);
                    //Added
                    //   stt.pause(false);


                }
            }


            @Override
            public void onCompletion() {
                Log.d(TAG, "onCompletion: Done");
                binding.animationVoice.setVisibility(View.GONE);
            }

            @Override
            public void getLogResult(String title) {
                Log.i("INLOG", title);
                ConversionCallback.super.getLogResult(title);
                logs += title + "\n";
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void getStringResult(String title) throws JSONException {
                ConversionCallback.super.getStringResult(title);
//                Log.i("SoundXCalled", title + ",title: " + Soundex.getCode(title) + ", ans:" + Soundex.getCode(UtilityFunctions.numberToWords(currentAnswer)));
//                if (Soundex.getCode(title).equals(Soundex.getCode(UtilityFunctions.numberToWords(currentAnswer)))) {
//                    successResultCalling(currentAnswer + "");
//                } else {
//                    // Changes
//                    try {
//                        UtilityFunctions.runOnUiThread(() -> {
//                            isCallSTT = true;
//                            tts.initialize("", LearningActivityNew.this);
//                        }, 250);
//                    } catch (Exception e) {
//                        Log.i(TAG, "Inside String res");
//                        pause();
//                    }
//                }


            }

            @Override
            public void onErrorOccurred(String errorMessage) {

//                Log.i(TAG, "Inside err" + errorMessage);
//                if (errorMessage.equals("No match")) {
//                    try {
//                        UtilityFunctions.runOnUiThread(() -> {
//                            isCallSTT = true;
//                            tts.initialize("", LearningActivityNew.this);
//                        }, 250);
//
//                    } catch (Exception e) {
//                        Log.i(TAG, "Inside err");
//                        pause();
//                    }
//                } else {
//
//                    Log.i(TAG, "InsideErrElse" + errorMessage + isCallSTT + isCallTTS + isAnsByTyping);
//                    // binding.playPause.setChecked(false);
//                    //pause();
//                }


            }

            @Override
            public void successInit() {
                ConversionCallback.super.successInit();
                customProgressDialogue.dismiss();
                binding.animationVoice.setVisibility(View.GONE);
                stt.pause(false);
                initProcess();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void successResultCalling(String result) throws JSONException {

        endTime = new Date().getTime();
        isAnswered = true;

        try {
            UtilityFunctions.unMuteAudioStream(AdditionActivityNew.this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ansList.add(result);
        timeList.add((endTime - startTime));

        logs += "Detected Result" + result + "\n";
        //stt.stop();
        binding.ansTextView.setText(result);
        Boolean lcsResult = new UtilityFunctions().matchingSeq(result.trim(), currentAnswer + "");


        binding.animWoman.playAnimation();


        if (lcsResult) {

            diff = endTime - startTime;
            binding.ansTextView.setBackgroundTintList(ContextCompat.getColorStateList(AdditionActivityNew.this, R.color.green));
            tts.initialize(UtilityFunctions.getCompliment(true), AdditionActivityNew.this);
            logs += "Tag: Correct\n" + "Time Taken: " + UtilityFunctions.formatTime(diff) + "\n";

            try {
                mediaPlayer.start();
            } catch (Exception e) {
            }

            UtilityFunctions.sendDataToAnalytics(analytics, auth.getCurrentUser().getUid().toString(), kidsId, kidName,
                    "Mathematics-Test-" + subject, kidAge, currentAnswer + "", result, true, (int) (diff),
                    currentNum1 + "" + binding.operator.getText() + "" + currentNum2 + "=?", "maths", parentsContactId);
            putJsonData(currentNum1 + "" + binding.operator.getText() + "" + currentNum2 + "=?", result, diff, true);

            DELAY_ON_STARTING_STT = 500;
            DELAY_ON_SETTING_QUESTION = 2000;
            correctAnswer++;
        } else {
            binding.ansTextView.setBackgroundTintList(ContextCompat.getColorStateList(AdditionActivityNew.this, R.color.red));
            logs += "Tag: Wrong\n" + "Time Taken: " + UtilityFunctions.formatTime(diff) + "\n";
            tts.initialize(UtilityFunctions.getCompliment(false), AdditionActivityNew.this);
            putJsonData(currentNum1 + "" + binding.operator.getText() + "" + currentNum2 + "=?", result, diff, false);
            UtilityFunctions.sendDataToAnalytics(analytics, auth.getCurrentUser().getUid().toString(), kidsId, kidName,
                    "Mathematics-Test-" + subject, kidAge, currentAnswer + "", result, false, (int) (diff),
                    currentNum1 + "" + binding.operator.getText() + "" + currentNum2 + "=?", "maths", parentsContactId);
            DELAY_ON_STARTING_STT = 500;
            DELAY_ON_SETTING_QUESTION = 2000;
            wrongAnswer++;
        }
        setWrongCorrectView();
        currentQuestion++;
        if (currentQuestion <= MAX_QUESTION) {

            UtilityFunctions.runOnUiThread(() -> {
                try {
                    setQuestion();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, DELAY_ON_SETTING_QUESTION);
        } else {

            //null check req
            if (correctAnswer >= 9) {
                CallFirebaseForInfo.checkActivityDataMaths(kidsDb, kidsActivityJsonArray, "pass", auth, kidsId,kidsGrade.toLowerCase().replace(" ", ""),
                        selectedSub, subject, correctAnswer, wrongAnswer, currentQuestion - 1, "mathematics");

                progressDataBase.progressDao().updateScore(correctAnswer, wrongAnswer, id);
                if (!subject.equals("multiplication tables"))
                    UtilityFunctions.updateDbUnlock(databaseGrade, kidsGrade, selectedSub, subject);

            } else
                CallFirebaseForInfo.checkActivityDataMaths(kidsDb, kidsActivityJsonArray, "fail", auth, kidsId,kidsGrade.toLowerCase().replace(" ", ""),
                        selectedSub, subject, correctAnswer, wrongAnswer, currentQuestion - 1, "mathematics");

            resetViews();
        }

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

    private void setWrongCorrectView() {
        binding.wrongText.setText(String.valueOf(wrongAnswer));
        binding.correctText.setText(String.valueOf(correctAnswer));
    }

    private void resetViews() {
        binding.playPause.setChecked(false);
        isCallTTS = false;
        gotoScoreCard();
        currentQuestion = 1;
        correctAnswer = 0;
        wrongAnswer = 0;

        try {
            mediaPlayer.pause();
        } catch (Exception e) {
        }
        numberList.clear();
        binding.animWoman.cancelAnimation();
        //  binding.textView26.setVisibility(View.VISIBLE);
        //  binding.textViewQuestion.setVisibility(View.GONE);
        binding.tapInfoTextView.setVisibility(View.INVISIBLE);
        binding.progress.setText("1/10");


    }

    private void gotoScoreCard() {

        Intent intent = new Intent(getApplicationContext(), ScoreActivity.class);
        intent.putExtra("wrongAns", wrongAnswer);
        intent.putExtra("correctAns", correctAnswer);
        intent.putExtra("chapter", selectedSub);

        startActivity(intent);
    }


    private void setToolbar() {


        binding.toolBar.getRoot().inflateMenu(R.menu.log_menu);
        binding.toolBar.getRoot().setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {
                case R.id.action_log:
                    startActivity(new Intent(getApplicationContext(), LogActivity.class));

                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }

        });

        binding.toolBar.titleText.setText(getIntent().getStringExtra("status"));

    }

    private void setButtonClick() {
        binding.playPause.setOnClickListener(v -> {

            stt.pause(!binding.playPause.isChecked());

            if (binding.playPause.isChecked()) {

                if (isTimerRunning)
                    timer();

                binding.correctText.setText("0");
                binding.wrongText.setText("0");
                binding.tapInfoTextView.setVisibility(View.INVISIBLE);

                isCallTTS = true;
                try {
                    setQuestion();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {

                pause();

            }
        });
    }

    public void pause() {
        binding.animationVoice.setVisibility(View.GONE);
        binding.questionProgress.setProgress(0);
        binding.tapInfoTextView.setVisibility(View.INVISIBLE);
        isCallTTS = false;

        try {
            mediaPlayer.pause();
        } catch (Exception e) {
        }

        if (!isAnswered)
            isNewQuestionGenerated = false;
    }

    private void setQuestion() throws InterruptedException {

        try {
            mediaPlayer.pause();
        } catch (Exception e) {
        }

        UtilityFunctions.unMuteAudioStream(AdditionActivityNew.this);
        isAnswered = false;
        binding.ansTextView.setBackgroundTintList(ContextCompat.getColorStateList(AdditionActivityNew.this, R.color.green));
        if (isCallTTS) {
            int maxVal = 20;
            int minVal = 2;
            if (digit.equals("2")) {
                maxVal = 99;
                minVal = 10;
            }

            if (digit.equals("3")) {
                maxVal = 999;
                minVal = 100;

            }
            if (digit.equals("4")) {
                maxVal = 9999;
                minVal = 1000;

            }
            if (digit.equals("5")) {
                maxVal = 99999;
                minVal = 10000;

            }
            if (isNewQuestionGenerated) {
                if (digit.equals("1")) {
                    currentNum1 = UtilityFunctions.getRandomNumber(Integer.parseInt(digit.trim()));
                    currentNum2 = UtilityFunctions.getRandomNumber(Integer.parseInt(digit.trim()));
                } else {
                    currentNum1 = UtilityFunctions.getRandomIntegerUpto(maxVal, minVal);
                    currentNum2 = UtilityFunctions.getRandomIntegerUpto(maxVal, minVal);
                }

                if (subject.equals("Subtraction")) {
                    if (currentNum1 < currentNum2) {
                        int temp = currentNum1;
                        currentNum1 = currentNum2;
                        currentNum2 = temp;
                    }
                }
                if (subject.equals("Division")) {
                    currentNum1 = UtilityFunctions.getRandomIntegerUpto(maxVal, minVal);
                    currentNum2 = UtilityFunctions.getRandomIntegerUpto(9, 2);
                    while (!UtilityFunctions.isDivisible(currentNum1, currentNum2)) {
                        currentNum1 = UtilityFunctions.getRandomIntegerUpto(maxVal, minVal);
                    }
                }

                if (subject.equals("Multiplication Tables")) {
                    currentNum1 = Integer.parseInt(digit);

                    currentNum2 = UtilityFunctions.getRandomNumber(1);
                    while (true) {
                        if (numberList.contains(currentNum2)) {
                            currentNum2 = UtilityFunctions.getRandomIntegerUpto(11, 1);
                            Log.i("CurrentNumber", currentNum2 + "");
                        } else {
                            numberList.add(currentNum2);
                            break;
                        }

                    }
                }

            }


            logs += "Question :" + currentNum1 + binding.operator.getText() + "" + currentNum2 + "=?\n";
            digit1Text.setText(currentNum1 + "");
            binding.digitTwo.setText(currentNum2 + "");
            binding.progress.setText(currentQuestion + "/ " + MAX_QUESTION);
            binding.ansTextView.setText("?");
            currentAnswer = MathsHelper.getMathResult(subject, currentNum1, currentNum2);
            tts.initialize(MathsHelper.getMathQuestion(subject, currentNum1, currentNum2), this);
            binding.animWoman.playAnimation();
            isNewQuestionGenerated = true;
        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        isCallTTS = false;
        binding.animWoman.cancelAnimation();
        try {
            UtilityFunctions.unMuteAudioStream(AdditionActivityNew.this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tts.stop();
        stt.destroy();

        checkLogIsEnable();
        UtilityFunctions.checkProgressAvailable(progressDataBase, id, selectedSub, new Date(),
                Integer.parseInt(binding.timeText.getText().toString()), false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        isCallTTS = true;
        isNewQuestionGenerated = true;
        isAnswered = false;
        initTTS();

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
        tts.destroy();
        stt.destroy();
        try {
            UtilityFunctions.unMuteAudioStream(AdditionActivityNew.this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}