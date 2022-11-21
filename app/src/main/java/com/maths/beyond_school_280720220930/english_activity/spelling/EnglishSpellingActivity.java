package com.maths.beyond_school_280720220930.english_activity.spelling;

import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_CATEGORY_ID;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_DATA;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_FLAG_HAVE_DATA;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_IS_OPEN_FROM_LEARN;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_ONLINE_FLAG;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_OPEN_TYPE;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_SPELLING_DETAIL;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_TITLE;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.maths.beyond_school_280720220930.LogActivity;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_280720220930.database.english.spelling_common_words.SpellingCommonWordDao;
import com.maths.beyond_school_280720220930.database.english.spelling_common_words.model.SpellingCategoryModel;
import com.maths.beyond_school_280720220930.database.english.spelling_common_words.model.SpellingDetail;
import com.maths.beyond_school_280720220930.database.log.LogDatabase;
import com.maths.beyond_school_280720220930.database.process.ProgressDataBase;
import com.maths.beyond_school_280720220930.database.process.ProgressM;
import com.maths.beyond_school_280720220930.databinding.ActivitySpellingCommonWordBinding;
import com.maths.beyond_school_280720220930.dialogs.HintDialog;
import com.maths.beyond_school_280720220930.english_activity.spelling.spelling_test.SpellingTest;
import com.maths.beyond_school_280720220930.english_activity.vocabulary.EnglishViewPager;
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

public class EnglishSpellingActivity extends AppCompatActivity {

    private static final String TAG = EnglishSpellingActivity.class.getSimpleName();
    private static final int MAX_TRY = 5 /* Giver u three chance */;
    private static final int DELAY_TIME = 800;
    private final int REQUEST_FOR_DES = 345 * 34;
    private final int REQUEST_FOR_QUESTION = 345 * 35;
    private static final int DELAY_BETWEEN_RESET_VIEW = 1000;              // delay between reset view when user input is wrong


    private Boolean isSpeaking = false;
    private Boolean isSayWordFinish = true;
    private int currentTryCount = 0;

    private ActivitySpellingCommonWordBinding binding;
    private String category;
    private TextToSpeckConverter tts = null;
    private TextToSpeckConverter ttsHelper = null;
    private MediaPlayer mediaPlayer = null;
    private ButtonClick buttonClickListener;
    private LogDatabase logDatabase;
    private String logs = "";
    private String inputWord = "";
    private long startTime = 0, endTime = 0;
    private StringBuilder currentWordBuilder = new StringBuilder();
    private SpellingCommonWordDao dao;
    private List<SpellingDetail> spellingDetails;
    private List<Fragment> fragments;
    private int currentWordLetterPosition = 0;
    private long timeSpend = 0;
    public static final int TIMER_VALUE = 15;
    private List<ProgressM> progressData;
    private ProgressDataBase progressDataBase;

    private boolean isOnline = false;

    private String kidsGrade;
    private String kidsId;
    private int kidAge;
    private String kidName;
    private FirebaseAnalytics analytics;
    private FirebaseAuth auth;
    private String parentsContactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpellingCommonWordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        logDatabase = LogDatabase.getDbInstance(this);
        progressDataBase = ProgressDataBase.getDbInstance(this);
        progressData = new ArrayList<>();

        kidAge = UtilityFunctions.calculateAge(PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_dob)));
        kidsId = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_id));
        kidName = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_name));
        parentsContactId = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.parent_contact_details));
        auth = FirebaseAuth.getInstance();
        analytics = FirebaseAnalytics.getInstance(this);
        setToolbar();
        setData();
    }


    private void setData() {
        if (getIntent().hasExtra(EXTRA_SPELLING_DETAIL)) {
            category = getIntent().getStringExtra(EXTRA_SPELLING_DETAIL);
            isOnline = getIntent().getBooleanExtra(EXTRA_ONLINE_FLAG, false);
            dao = EnglishGradeDatabase.getDbInstance(this).spellingCommonWordDao();
            if (isOnline) {
                getSubjectData();
            } else {
                setViews();
            }
        } else {
            UtilityFunctions.simpleToast(this, "No data found");
        }
        buttonClick();
        binding.giveTestButton.setOnClickListener((view) -> {
            navigateToTest();
        });
    }

    private void getSubjectData() {
        if (getIntent().hasExtra(Constants.EXTRA_FLAG_HAVE_DATA)) {
            var d = (ContentModelNew) getIntent().getSerializableExtra(Constants.EXTRA_DATA);
            mapData(d.getLearning());
        } else {
            Retrofit retrofit = ApiClient.getClient();

            var api = retrofit.create(ApiInterface.class);
            api.getVocabularySubject(PrefConfig.readIdInPref(this, getResources().getString(R.string.kids_grade)).toLowerCase().replace(" ", ""),
                    "english", getIntent().getStringExtra(EXTRA_TITLE).toLowerCase(), category).enqueue(new retrofit2.Callback<>() {
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


    private void sendDataToAnalytics(String currentWord, String result, long diff, boolean b) {
        UtilityFunctions.sendDataToAnalytics(analytics, auth, Objects.requireNonNull(auth.getCurrentUser()).getUid(), kidsId, kidName, "English-Practice-" + (!isOnline ? "grammar" : getIntent().getStringExtra(EXTRA_TITLE).toLowerCase()), kidAge, currentWord, result, b, (int) (diff), UtilityFunctions.getQuestionForGrammarTest(this, category), "English", parentsContactId);
    }

    private void navigateToTest() {
        var intent = new Intent(this, SpellingTest.class);
        intent.putExtra(EXTRA_SPELLING_DETAIL, category);
        intent.putExtra(Constants.EXTRA_ONLINE_FLAG, true);
        if (getIntent().hasExtra(Constants.EXTRA_FLAG_HAVE_DATA)) {
            var d = (ContentModelNew) getIntent().getSerializableExtra(Constants.EXTRA_DATA);
            intent.putExtra(EXTRA_DATA, d);
            intent.putExtra(EXTRA_OPEN_TYPE, Constants.OpenType.EXERCISE.name());
            intent.putExtra(EXTRA_FLAG_HAVE_DATA, true);
            intent.putExtra(EXTRA_IS_OPEN_FROM_LEARN, true);
        }

        intent.putExtra(EXTRA_OPEN_TYPE, Constants.OpenType.EXERCISE.name());
        intent.putExtra(EXTRA_CATEGORY_ID, getIntent().getStringExtra(EXTRA_CATEGORY_ID));
        intent.putExtra(EXTRA_TITLE, getIntent().getStringExtra(EXTRA_TITLE));
        startActivity(intent);
    }

    private void buttonClick() {
        binding.playPause.setOnClickListener(view -> {
            isSpeaking = binding.playPause.isChecked();
            startEngine();
            timer();
        });

    }

    private void playPauseAnimation(Boolean play) {
        UtilityFunctions.runOnUiThread(() -> {
            if (play)
                binding.imageViewTeacher.playAnimation();
            else
                binding.imageViewTeacher.pauseAnimation();
        });
    }

    private void startEngine() {
        if (binding.playPause.isChecked()) {
            try {
                initTTS();
                initMediaPlayer();
                startSpeaking();

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            destroyedEngines();
            isSayWordFinish = true;
        }
    }

    private void setViews() {
        binding.subSub.setText(category.replace("Spelling_CommonWords", ""));
        setPager();
    }

    private void setPager() {

        if (!isOnline) {
            var data = getSpellingFromType(dao.getSpellingModel(1).getSpelling(),
                    category);

            if (data == null) {
                UtilityFunctions.simpleToast(this, "No data found List is empty");
                return;
            }
            spellingDetails = data.getDetails();
        }
        List<Fragment> fragments = getFragments(spellingDetails);

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
        handleDeleteWord();
        try {
            binding.playPause.setChecked(true);
            isSpeaking = binding.playPause.isChecked();
            initTTS();
            initMediaPlayer();
            playPauseAnimation(true);
            helperTTS("Hi kids !! Let us, learn spelling of some"
                            + category.replace("Spelling_CommonWords", "")
                    ,
                    false
                    , REQUEST_FOR_QUESTION);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    private String getQuestion() {
        return "The " + UtilityFunctions.
                convertCardinalNumberToOrdinalNumber(binding.viewPager.getCurrentItem() + 1)
                + " word is " +
                (spellingDetails.get(binding.viewPager.getCurrentItem()).getWord().equals("The")
                        ? "'di'" :
                        "'" + spellingDetails.get(binding.viewPager.getCurrentItem()).getWord() + "'"
                ) + ".\n\n"
                + "We spell it as "
                ;
    }

    private String getDescription() {
        return spellingDetails.get(binding.viewPager.getCurrentItem()).getDescription();
    }

    private String getSpellLetter() {
        return "Now, it's your turn to type the spelling of the word '" +
                (spellingDetails.get(binding.viewPager.getCurrentItem()).getWord().equals("The")
                        ? "'di'" :
                        "'" + spellingDetails.get(binding.viewPager.getCurrentItem()).getWord()) + "' .";
    }

    private List<Fragment> getFragments(List<SpellingDetail> data) {
        fragments = CollectionUtils.
                mapWithIndex(
                        spellingDetails.stream(), (index, item) ->
                                new SpellingFragment(item, index + 1)).collect(Collectors.toList()
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


    private void initMediaPlayer() {
        mediaPlayer = UtilityFunctions.playClapSound(this);
    }

    private void initTTS() throws ExecutionException, InterruptedException {
        var task = new TTSAsyncTask();
        tts = task.execute(new ConversionCallback() {
            @Override
            public void onCompletion() {
                UtilityFunctions.runOnUiThread(() -> {
                    if (!tts.getTextRanceListener()) {
                        setButtonText();
                        setLayoutVisible(true);
                        setInputForCurrentWord();
                        return;
                    }
                    removeHighlight();
                    tts.setTextViewAndSentence(null);
                    tts.initialize(getSpellLetter(),
                            EnglishSpellingActivity.this);

                });
            }

            @Override
            public void onErrorOccurred(String errorMessage) {
                Log.e(TAG, "onErrorOccurred: " + errorMessage);
            }
        }).get();
        tts.setTextRangeListener((utteranceId, sentence, start, end, frame) -> {
            UtilityFunctions.runOnUiThread(() -> {
                var textView = (TextView) this.findViewById(R.id.text_view_des);
                if (textView != null) {
                    var sen = sentence.replace("<b>", "").replace("</b>", "").replace("<br>", "");
                    Log.d("AAA", "initTTS: " + sen);
                    Spannable textWithHighlights = new SpannableString(sen);
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

    private void setInputForCurrentWord() {
        startTime = new Date().getTime();
        playPauseAnimation(false);
        var currentPosition = binding.viewPager.getCurrentItem();                                               // get current position of view pager
        var currentWord = spellingDetails.get(currentPosition).getWord();                                     // get current word
        var currentFragment = (SpellingFragment) fragments.get(currentPosition);                                   // get current fragment
        var textView = currentFragment.getTextView();
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

    private void checkAnswer() {
        endTime = new Date().getTime();
        var currentPosition = binding.viewPager.getCurrentItem();                                               // get current position of view pager
        var currentWord = spellingDetails.get(currentPosition).getWord();                                     // get current word
        var currentFragment = (SpellingFragment) fragments.get(currentPosition);                                   // get current fragment
        var textView = currentFragment.getTextView();                                                     // get text view of current fragment
        currentWordLetterPosition = 0;
        playPauseAnimation(false);
        var diff = endTime - startTime;
        var currentDes = spellingDetails.get(currentPosition).getDescription();

        if (inputWord.toLowerCase(Locale.ROOT).equals(currentWord.toLowerCase(Locale.ROOT))) {
            logs += "Time Take :" + UtilityFunctions.formatTime(diff) + ", Correct .\n";
            textView.setTextColor(ContextCompat.getColor(this, R.color.green));
            playPauseAnimation(true);
            helperTTS(UtilityFunctions.getCompliment(true), true, 0);
            mediaPlayer.start();
            inputWord = "";
            textView.setText(currentWord);
            sendDataToAnalytics(currentDes, currentWord, diff, true);
        } else {
            textView.setTextColor(ContextCompat.getColor(this, R.color.sweet_red));
            logs += "Time Take :" + UtilityFunctions.formatTime(diff) + ", Wrong .\n";
            sendDataToAnalytics(currentDes, currentWord, diff, false);
            UtilityFunctions.runOnUiThread(() -> {
                playPauseAnimation(true);
                helperTTS(UtilityFunctions.getCompliment(false), false, 0);
                resetTextViews(currentWord, currentFragment);
                setTextColor(R.color.text_color);
                setButtonText();
                inputWord = "";
            }, DELAY_BETWEEN_RESET_VIEW);
        }
    }

    private void resetTextViews(String currentWord, SpellingFragment currentFragment) {
        var textView = currentFragment.getTextView();                                                     // get text view of current fragment
        textView.setText(currentWord
                .replaceAll("[a-zA-Z]", "_"));
    }

    private void setTextColor(@ColorRes int res) {
        var currentPosition = binding.viewPager.getCurrentItem();                                              // get current position of view pager
        var currentFragment = (SpellingFragment) fragments.get(currentPosition);                                   // get current fragment
        var textView = currentFragment.getTextView();                                                     // get text view of current fragment
        textView.setTextColor(ContextCompat.getColor(this, res));
    }

    private void removeHighlight() {
        UtilityFunctions.runOnUiThread(() -> {
            var textView = (TextView) findViewById(R.id.text_view_des);
            Spannable textWithHighlights = new SpannableString(textView.getText().toString());
            textWithHighlights.setSpan(new ForegroundColorSpan(
                            ContextCompat.
                                    getColor(
                                            EnglishSpellingActivity.this,
                                            android.R.color.primary_text_light
                                    )),
                    0,
                    textView.getText().toString().length(),
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            textView.setText(textWithHighlights);
        });
    }

    private void startSpeaking() throws ExecutionException, InterruptedException {
        setLayoutVisible(false);
        isSpeaking = true;
        UtilityFunctions.runOnUiThread(() -> playPauseAnimation(true));
        helperTTS(getQuestion(), false, 3 * 45);
//        Question
        logs += "Question : " + spellingDetails.get(binding.viewPager.getCurrentItem()).getWord() + " : " + spellingDetails.get(binding.viewPager.getCurrentItem()).getDescription() + ". \n";

//        Stop when reach ot last item


    }

    private void setLayoutVisible(Boolean visible) {
        UtilityFunctions.runOnUiThread(() -> {
            binding.linearLayout.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        });
    }

    private void helperTTS(String message, boolean canNavigate, int request) {
        try {
            ttsHelper = new TTSHelperAsyncTask().execute(new ConversionCallback() {
                        @Override
                        public void onCompletion() {
                            if (request == REQUEST_FOR_QUESTION && !canNavigate) {
                                try {
                                    startSpeaking();
                                } catch (ExecutionException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                                return;
                            }
                            if (request == 3 * 45) {
                                var currentWord = spellingDetails.get(binding.viewPager.getCurrentItem()).getWord();
                                helperTTS(UtilityFunctions.addComma(currentWord), false, 3 * 45 + 1);
                                return;
                            }
                            if (request == 3 * 45 + 1) {
                                helperTTS("For example, ", false, REQUEST_FOR_DES);
                                return;
                            }
                            if (request == REQUEST_FOR_DES && !canNavigate) {
                                tts.setTextViewAndSentence(spellingDetails.get(binding.viewPager.getCurrentItem())
                                        .getDescription().replace("<b>", "").replace("</b>", "").replace("<br>", ""));
                                tts.initialize(getDescription().replace("<b>", "").replace("</b>", "").replace("<br>", "")
                                        , EnglishSpellingActivity.this);
                                return;
                            }
                            if (canNavigate) {                                                                  // navigate to next word
                                if (binding.viewPager.getCurrentItem() == spellingDetails.size() - 1) {
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
                                try {
                                    startSpeaking();
                                } catch (ExecutionException | InterruptedException e) {
                                    e.printStackTrace();
                                }

                                return;
                            }
                        }

                        @Override
                        public void onErrorOccurred(String errorMessage) {

                        }
                    }).
                    get().
                    initialize(message, this);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
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

    private void handleDeleteWord() {
        binding.key5.setOnClickListener(v -> {
            if (inputWord.length() > 0) {
                var currentFragment = (SpellingFragment) fragments.get(binding.viewPager.getCurrentItem());
                var textView = currentFragment.getTextView();
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

    private void handleButtonClick() {
        var currentPosition = binding.viewPager.getCurrentItem();                                        // get current position of view pager
        var currentWord = spellingDetails.get(currentPosition).getWord();
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

    private void destroyedEngines() {
        binding.playPause.setChecked(false);
        playPauseAnimation(false);
        setLayoutVisible(false);
        if (tts != null)
            tts.destroy();
        if (ttsHelper != null)
            ttsHelper.destroy();
        if (mediaPlayer != null)
            mediaPlayer.release();
        try {
            var current = (SpellingFragment) fragments.get(binding.viewPager.getCurrentItem());
            current.getAnimationView().setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        checkLogIsEnable();
        destroyedEngines();
        checkProgressData();
        UtilityFunctions.checkProgressAvailable(progressDataBase, getIntent().getStringExtra(EXTRA_CATEGORY_ID), category, new Date(),
                timeSpend + Integer.parseInt(binding.progress.timeText.getText().toString()), false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        destroyedEngines();
    }

    private void checkLogIsEnable() {
        if (PrefConfig.readIdInPref(getApplicationContext(), "IS_LOG_ENABLE").equals("true"))
            saveLog();
    }

    private void saveLog() {
        Log.d(TAG, "saveLog: Called " + logs);
        UtilityFunctions.saveLog(logDatabase, logs);
    }


    private void setToolbar() {
        binding.toolBar.titleText.setText(getResources().getString(R.string.spelling_common_words).replace("Spelling_", ""));
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


    public void setButtonText() {
        var currentPosition = binding.viewPager.getCurrentItem();
        var currentWord = spellingDetails.get(currentPosition).getWord();
        var currentLetter = currentWord.charAt(currentWordLetterPosition);

        var array = new ArrayList<Character>();
        array.add(currentLetter);
        array.add(UtilityFunctions.getRandomAlphabet(currentLetter));
        array.add(UtilityFunctions.getRandomAlphabet(currentLetter));
        array.add(UtilityFunctions.getRandomAlphabet(currentLetter));
        var shuffleArray = UtilityFunctions.shuffleArray(array);
        binding.key1.setText(shuffleArray.get(0).toString());
        binding.key2.setText(shuffleArray.get(1).toString());
        binding.key3.setText(shuffleArray.get(2).toString());
        binding.key4.setText(shuffleArray.get(3).toString());
    }

    private void displayCompleteDialog() {

        HintDialog hintDialog = new HintDialog(EnglishSpellingActivity.this);
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
            initTTS();
            initMediaPlayer();
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
                .doOnNext(x -> {
                    // update your view here

                    binding.progress.timerProgress.setMax(15);
                    binding.progress.timerProgress.setProgress(Integer.parseInt((x + 1) + ""));
                    binding.progress.timeText.setText((x + 1) + "");
                    Log.i("task", x + "");
                })
                .takeUntil(aLong -> aLong == TIMER_VALUE)
                .doOnComplete(() ->
                        // do whatever you need on complete
                        Log.i("TSK", "task")
                ).subscribe();
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

    interface ButtonClick {
        void onClick(String s);
    }
}