package com.maths.beyond_school_280720220930.english_activity.spelling_objects;

import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_SPELLING_CATEGORY;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_SPELLING_DETAIL;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_SPELLING_LIST;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.maths.beyond_school_280720220930.LogActivity;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_280720220930.database.english.spelling_objects.SpellingObjectsDao;
import com.maths.beyond_school_280720220930.database.english.spelling_objects.model.SpellingModel;
import com.maths.beyond_school_280720220930.database.english.spelling_objects.model.SpellingType;
import com.maths.beyond_school_280720220930.database.log.LogDatabase;
import com.maths.beyond_school_280720220930.database.process.ProgressDataBase;
import com.maths.beyond_school_280720220930.database.process.ProgressM;
import com.maths.beyond_school_280720220930.databinding.ActivitySpellingBinding;
import com.maths.beyond_school_280720220930.dialogs.HintDialog;
import com.maths.beyond_school_280720220930.english_activity.spelling_objects.spelling_text.SpellingTestActivity;
import com.maths.beyond_school_280720220930.english_activity.vocabulary.EnglishViewPager;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.CollectionUtils;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class SpellingActivity extends AppCompatActivity {

    private static final String TAG = "SpellingActivity";
    private final Activity context = SpellingActivity.this;

    private ActivitySpellingBinding binding;
    private List<SpellingModel> spellingModels;
    private List<Fragment> fragments;
    private TextToSpeckConverter tts = null;
    private TextToSpeckConverter ttsHelper = null;
    private MediaPlayer mediaPlayer = null;
    private boolean isSpeaking = false;
    private ButtonClick buttonClickListener;                               // Listener for all buttons for words input
    private String inputWord = "";                                         //  Word input by user
    private int currentWordLetterPosition = 0;                             // position of letter in current word
    private String category = "";


    private final int REQUEST_INTRO = 2 * 44;                              // request code for intro speech
    private final int REQUEST_WORD = 2 * 44 + 1;                           // request code for word speech
    private static final int REQUEST_QUESTION = 2 * 44 + 2;                // request code for question speech
    private static final int DELAY_BETWEEN_RESET_VIEW = 1000;              // delay between reset view when user input is wrong
    public static final int TIMER_VALUE = 15;

    private LogDatabase logDatabase;
    private String logs = "";
    private long startTime = 0, endTime = 0;

    private long timeSpend = 0;
    private List<ProgressM> progressData;
    private ProgressDataBase progressDataBase;
    private boolean isTimerRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpellingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setToolbar();
        setData();
        binding.giveTestButton.setOnClickListener(v -> {
            navigateToTest();
        });
        progressDataBase = ProgressDataBase.getDbInstance(this);
        progressData = new ArrayList<>();
        logDatabase = LogDatabase.getDbInstance(this);
    }

    private void navigateToTest() {
        Intent intent = new Intent(context, SpellingTestActivity.class);
        intent.putExtra(EXTRA_SPELLING_CATEGORY, category);
        intent.putExtra(EXTRA_SPELLING_LIST, (ArrayList<SpellingModel>) spellingModels);
        startActivity(intent);
    }


    private void setData() {
        if (getIntent().hasExtra(EXTRA_SPELLING_DETAIL)) {
            category = getIntent().getStringExtra(EXTRA_SPELLING_DETAIL);
            var englishDatabase = EnglishGradeDatabase.getDbInstance(this);
            SpellingObjectsDao spellingObjectsDao = englishDatabase.spellingDao();
            spellingModels = getSpellingData(category, spellingObjectsDao.getAllSpelling());
            setPager(spellingModels);
            buttonClick();
        } else {
            UtilityFunctions.simpleToast(this, "No data found");
        }
    }


    private void setPager(List<SpellingModel> spellingModels) {
        fragments = getFragments(spellingModels);
        var adapter = new EnglishViewPager(fragments, getSupportFragmentManager(), getLifecycle());
        binding.viewPager.setAdapter(adapter);
        setButtonText();
        binding.viewPager.setUserInputEnabled(false);
        speakIntro();
        handleDeleteWord();
    }

    private void handleDeleteWord() {
        binding.key5.setOnClickListener(v -> {
            if (inputWord.length() > 0) {
                var currentFragment = (SpellingFragment) fragments.get(binding.viewPager.getCurrentItem());
                var textView = currentFragment.getAnswerTextView();
                var text = textView.getText().toString();
                var newText = UtilityFunctions.replace(text, currentWordLetterPosition - 1, '_');
                textView.setText(newText);
                currentWordLetterPosition--;
                setButtonText();
                inputWord = inputWord.substring(0, inputWord.length() - 1);
                Log.d(TAG, "handleDeleteWord: " + inputWord + " Current word letter position: " + currentWordLetterPosition);
            }
        });
    }

    private void speakIntro() {
        isSpeaking = true;
        initTTS();
        timer();
        initMediaPlayer();
        playPauseAnimation(true);
        binding.playPause.setChecked(true);
        try {
            helperTTS("Hi, Kids. We will learn the spelling of some," +
                            category.replace("Spelling", "").replaceAll("\\d", "") + " !!",
                    false, REQUEST_INTRO);
        } catch (ExecutionException | InterruptedException e) {
            logs += e.getMessage() + "\n";
        }
    }

    public void setButtonText() {
        var currentPosition = binding.viewPager.getCurrentItem();
        var currentWord = spellingModels.get(currentPosition).getWord();
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

    private void buttonClick() {
        binding.playPause.setOnClickListener(v -> {
            isSpeaking = binding.playPause.isChecked();
            if (isSpeaking) {
                startSpeaking();
                timer();
            } else {
                destroyEngine();
            }
            if (binding.viewPager.getCurrentItem() > spellingModels.size() - 1) {
                isSpeaking = false;
            }
        });
    }

    private void startSpeaking() {
        initTTS();
        initMediaPlayer();
        speakWord();
    }

    private void initMediaPlayer() {
        mediaPlayer = UtilityFunctions.playClapSound(this);
    }

    private void speakWord() {

        var currentPosition = binding.viewPager.getCurrentItem();                                            // get current position of view pager
        var currentWord = spellingModels.get(currentPosition).getWord();                                  // get current word
        var contentForSpeaking = "The " + UtilityFunctions.convertCardinalNumberToOrdinalNumber(currentPosition + 1)
                + " word is "
                + currentWord + "!!, We spell it as ";                                                           // add comma in between word
        UtilityFunctions.runOnUiThread(() -> {
            playPauseAnimation(true);
            binding.linearLayout.setVisibility(View.GONE);
            binding.playPause.setChecked(true);
        });
        try {
            helperTTS(contentForSpeaking,
                    false,
                    REQUEST_WORD);
        } catch (ExecutionException | InterruptedException e) {
            logs += e + "\n";
        }
    }


    private void initTTS() {
        var task = new TTSAsyncTask();
        try {
            tts = task.execute(new ConversionCallback() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onCompletion() {
                    try {
                        UtilityFunctions.runOnUiThread(() -> {
                            var textView = (TextView) context.findViewById(R.id.text_view_word);
                            if (textView != null) {
                                textView.setTextColor(ContextCompat.getColor(context, R.color.primary));
                            }
                        });
                        helperTTS("Now, it's your turn to type the spelling of the word",
                                false,
                                REQUEST_QUESTION
                        );

                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
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

            tts.setTextRangeListener((utteranceId, sentence, start, end, frame) -> {
                UtilityFunctions.runOnUiThread(() -> {
                    var textView = (TextView) this.findViewById(R.id.text_view_word);
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
        } catch (ExecutionException | InterruptedException e) {
            logs += e + "\n";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setInputForCurrentWord() {
        startTime = new Date().getTime();
        UtilityFunctions.runOnUiThread(() -> {
            playPauseAnimation(false);
            binding.linearLayout.setVisibility(View.VISIBLE);
        });

        var currentPosition = binding.viewPager.getCurrentItem();                                               // get current position of view pager
        var currentWord = spellingModels.get(currentPosition).getWord();                                     // get current word
        var currentFragment = (SpellingFragment) fragments.get(currentPosition);                                   // get current fragment
        var textView = currentFragment.getAnswerTextView();                                                     // get text view of current fragment

        buttonClickListener = s -> {
            inputWord += s;
            var textViewText = textView.getText().toString();                                              // get text from text view
            textView.setText(textViewText.replaceFirst("_", s));
            currentWordLetterPosition++;
            if (currentWordLetterPosition < currentWord.length())
                setButtonText();
            else
                checkAnswer();
            Log.d(TAG, "setInputForCurrentWord: " + s + " Current Word Position" + currentWordLetterPosition + " Input Word " + inputWord);
        };
        handleButtonClick();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkAnswer() {
        endTime = new Date().getTime();
        var currentPosition = binding.viewPager.getCurrentItem();                                               // get current position of view pager
        var currentWord = spellingModels.get(currentPosition).getWord();                                     // get current word
        var currentFragment = (SpellingFragment) fragments.get(currentPosition);                                   // get current fragment
        var textView = currentFragment.getAnswerTextView();                                                     // get text view of current fragment
        currentWordLetterPosition = 0;
        playPauseAnimation(true);
        var diff = endTime - startTime;
        try {
            if (inputWord.equals(currentWord)) {
                logs += "Time Take :" + UtilityFunctions.formatTime(diff) + ", Correct .\n";
                textView.setTextColor(ContextCompat.getColor(context, R.color.green));
                helperTTS(UtilityFunctions.getCompliment(true), true, 0);
                mediaPlayer.start();
                inputWord = "";
                textView.setText(currentWord);
            } else {
                textView.setTextColor(ContextCompat.getColor(context, R.color.sweet_red));
                logs += "Time Take :" + UtilityFunctions.formatTime(diff) + ", Wrong .\n";
                UtilityFunctions.runOnUiThread(() -> {
                    try {
                        helperTTS(UtilityFunctions.getCompliment(false), false, 0);
                    } catch (ExecutionException | InterruptedException e) {
                        logs += e + "\n";
                    }
                    resetTextViews(currentWord, currentFragment);
                    setTextColor(R.color.text_color);
                    setButtonText();
                    inputWord = "";
                }, DELAY_BETWEEN_RESET_VIEW);
            }
        } catch (ExecutionException | InterruptedException e) {
            logs += e + "\n";
        }
    }

    private void handleButtonClick() {
        var currentPosition = binding.viewPager.getCurrentItem();                                        // get current position of view pager
        var currentWord = spellingModels.get(currentPosition).getWord();
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

    @NonNull
    private List<Fragment> getFragments(List<SpellingModel> spellingModels) {
        return CollectionUtils.mapWithIndex(spellingModels.stream(), (index, item) ->
                new SpellingFragment(item, index + 1)).collect(Collectors.toList());
    }

    private List<SpellingModel> getSpellingData(String spellingDetail, List<SpellingType> allSpelling) {
        for (SpellingType spellingType : allSpelling) {
            if (spellingType.getType().equals(spellingDetail)) {
                return spellingType.getSpellingModels();
            }
        }
        return null;
    }


    private void helperTTS(String message, boolean canNavigate, int request) throws
            ExecutionException, InterruptedException {
        ttsHelper = new TTSHelperAsyncTask().execute(new ConversionCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCompletion() {
                if (request == REQUEST_INTRO && !canNavigate) {
                    speakWord();
                    return;
                }
                if (request == REQUEST_WORD && !canNavigate) {
                    UtilityFunctions.runOnUiThread(() -> {
                        playPauseAnimation(true);
                        var currentWord = spellingModels.get(binding.viewPager.getCurrentItem()).getWord();
                        var currentWordWithComma = UtilityFunctions.addSpace(currentWord);
                        tts.setTextViewAndSentence(currentWordWithComma);
                        tts.setPitchAndSpeed(.8f, .6f);
                        tts.initialize(currentWordWithComma, context);
                    });
                    return;
                }
                if (request == REQUEST_QUESTION && !canNavigate) {
                    setInputForCurrentWord();
                    return;
                }
                if (canNavigate) {                                                                  // navigate to next word
                    if (isSpeaking) {
                        if (binding.viewPager.getCurrentItem() == spellingModels.size() - 1) {
                            UtilityFunctions.runOnUiThread(() -> {
                                playPauseAnimation(false);
                                binding.playPause.setChecked(false);
                                displayCompleteDialog();
                            });
                            return;
                        }
                        binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
                        UtilityFunctions.runOnUiThread(() -> {
                            mediaPlayer.pause();
                            setButtonText();
                        });                                                                         // Set button for net word
                        speakWord();
                    }
                    return;
                }
                UtilityFunctions.runOnUiThread(() ->
                        playPauseAnimation(false)
                );
            }

            @Override
            public void onErrorOccurred(String errorMessage) {

            }
        }).get().initialize(message, this);
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


    public void restartEngine() {
        destroyEngine();
        startSpeaking();
    }

    private void destroyEngine() {
        currentWordLetterPosition = 0;
        playPauseAnimation(false);
        binding.playPause.setChecked(false);
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

    private void resetTextViews(String currentWord, SpellingFragment currentFragment) {
        var textView = currentFragment.getAnswerTextView();                                                     // get text view of current fragment
        textView.setText(currentWord
                .replaceAll("[a-zA-Z]", "_"));
    }

    private void setTextColor(@ColorRes int res) {
        var currentPosition = binding.viewPager.getCurrentItem();                                              // get current position of view pager
        var currentFragment = (SpellingFragment) fragments.get(currentPosition);                                   // get current fragment
        var textView = currentFragment.getAnswerTextView();                                                     // get text view of current fragment
        textView.setTextColor(ContextCompat.getColor(context, res));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        destroyEngine();
    }

    private void setToolbar() {
        binding.toolBar.titleText.setText(getResources().getString(R.string.spelling_objects).replace("_", " "));
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

    private void playPauseAnimation(Boolean play) {
        if (play)
            binding.imageViewTeacher.playAnimation();
        else
            binding.imageViewTeacher.pauseAnimation();
    }

    @SuppressLint("NonConstantResourceId")
    private void displayCompleteDialog() {

        HintDialog hintDialog = new HintDialog(context);
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
        checkLogIsEnable();
        checkProgressData();
        destroyEngine();
        UtilityFunctions.checkProgressAvailable(progressDataBase, "English" + "Spelling", category, new Date(),
                timeSpend + Integer.parseInt(binding.timeText.getText().toString()), false);
    }

    private void checkProgressData() {
        progressData = UtilityFunctions.checkProgressAvailable(progressDataBase, "English" + "Spelling", category,
                new Date(), 0, true);

        try {
            if (progressData != null) {
                timeSpend = progressData.get(0).time_spend;
            }
        } catch (Exception e) {
            timeSpend = 0;
        }

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

    interface ButtonClick {
        void onClick(String s);
    }
}