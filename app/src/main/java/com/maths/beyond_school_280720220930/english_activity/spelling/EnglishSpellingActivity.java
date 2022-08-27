package com.maths.beyond_school_280720220930.english_activity.spelling;

import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_SPELLING_DETAIL;

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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_280720220930.database.english.spelling.SpellingDao;
import com.maths.beyond_school_280720220930.database.english.spelling.model.SpellingCategoryModel;
import com.maths.beyond_school_280720220930.database.english.spelling.model.SpellingDetail;
import com.maths.beyond_school_280720220930.database.log.LogDatabase;
import com.maths.beyond_school_280720220930.databinding.ActivityEnglishSpellingBinding;
import com.maths.beyond_school_280720220930.english_activity.vocabulary.EnglishViewPager;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.SpeechToTextBuilder;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.SpeechToTextConverterSpelling;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.CollectionUtils;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RequiresApi(api = Build.VERSION_CODES.N)
public class EnglishSpellingActivity extends AppCompatActivity {

    private static final String TAG = EnglishSpellingActivity.class.getSimpleName();
    private static final int MAX_TRY = 2 /* Giver u three chance */;
    private static final int MAX_TRY_FOR_SPEECH = 4 /* Giver u three chance */;
    private static final int DELAY_TIME = 800;
    private final int REQUEST_FOR_DES = 345 * 34;
    private final int REQUEST_FOR_QUESTION = 345 * 35;


    private Boolean isSpeaking = false;
    private Boolean isSayWordFinish = true;
    private int currentTryCount = 0, currentTryCountForSpeech = 0;

    private ActivityEnglishSpellingBinding binding;
    private UtilityFunctions.Spellings spellings = null;
    private TextToSpeckConverter tts = null;
    private TextToSpeckConverter ttsHelper = null;
    private SpeechToTextConverterSpelling stt = null;
    private MediaPlayer mediaPlayer = null;
    private LogDatabase logDatabase;
    private String logs = "";
    private long startTime = 0, endTime = 0;

    private SpellingDao dao;
    private List<SpellingDetail> spellingDetails;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEnglishSpellingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        logDatabase = LogDatabase.getDbInstance(this);
        setToolbar();
        setData();
    }


    private void setData() {
        if (getIntent().hasExtra(EXTRA_SPELLING_DETAIL)) {
            spellings = UtilityFunctions.getSpellingsFromString(getIntent().getStringExtra(EXTRA_SPELLING_DETAIL));
            dao = EnglishGradeDatabase.getDbInstance(this).spellingDao();
            setViews();
            buttonClick();
        } else {
            UtilityFunctions.simpleToast(this, "No data found");
        }
    }

    private void buttonClick() {
        binding.playPause.setOnClickListener(view -> {
            isSpeaking = binding.playPause.isChecked();
            startEngine();
        });

    }

    private void playPauseAnimation(Boolean play) {
        if (play)
            binding.imageViewTeacher.playAnimation();
        else
            binding.imageViewTeacher.pauseAnimation();
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

    private void setViews() {
        binding.subSub.setText(UtilityFunctions.getDBNameSpelling(spellings, this).replace("Spelling", "").trim());
        setPager();
    }

    private void setPager() {
        Log.d(TAG, "setPager: " + UtilityFunctions.getDBNameSpelling(spellings, this).replace("Spelling", ""));
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

        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setPageTransformer((page, position) -> {
            updatePagerHeightForChild(page, binding.viewPager);
        });
        try {
            binding.playPause.setChecked(true);
            isSpeaking = binding.playPause.isChecked();
            initTTS();
            intSTT();
            initMediaPlayer();
            playPauseAnimation(true);
            helperTTS("Let us learn about " + UtilityFunctions.getDBNameSpelling(spellings, this).replace("Spelling", ""), false, REQUEST_FOR_QUESTION);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<Fragment> getFragments(SpellingCategoryModel data) {
        spellingDetails = data.getDetails();
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
                    if (isSayWordFinish) {
                        isSayWordFinish = false;
                        tts.setTextViewAndSentence(null);

                        tts.initialize("Spell the word  ," + UtilityFunctions.addSpace(spellingDetails.get(binding.viewPager.getCurrentItem()).getWord()),
                                EnglishSpellingActivity.this);
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
                var textView = (TextView) this.findViewById(R.id.text_view_des);
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
        UtilityFunctions.runOnUiThread(() -> playPauseAnimation(true));
        helperTTS(spellingDetails.get(binding.viewPager.getCurrentItem()).getWord(), false, REQUEST_FOR_DES);
//        Question
        logs += "Question : " + spellingDetails.get(binding.viewPager.getCurrentItem()).getWord() + " : " + spellingDetails.get(binding.viewPager.getCurrentItem()).getDescription() + ". \n";

//        Stop when reach ot last item
        if (binding.viewPager.getCurrentItem() == (spellingDetails.size() - 1))
            isSpeaking = false;
    }


    private void intSTT() throws ExecutionException, InterruptedException {
        var task = new STTAsyncTask();
        stt = task.execute(new ConversionCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(String result) {

                if (currentTryCount > MAX_TRY) {
                    try {
                        helperTTS("No Problem. Let's go to next word ", true, 0);
                        currentTryCount = 0;
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                endTime = new Date().getTime();
                long diff = endTime - startTime;
                var split = result.split(" ");
                if (split.length != 0) {
                    var currentWord = spellingDetails.get(binding.viewPager.getCurrentItem()).getWord().toLowerCase(Locale.ROOT);
                    var answer = String.join("", split);
                    Log.d(TAG, "onSuccess: " + answer);
                    try {
                        if (UtilityFunctions.checkString(answer, currentWord)) {
                            mediaPlayer.start();
                            playPauseAnimation(true);
                            var currentFragment = (SpellingFragment) fragments.get(binding.viewPager.getCurrentItem());
                            currentFragment.getTextView().setText(currentWord);

                            helperTTS(UtilityFunctions.getCompliment(true), true, 0);
                            logs += "Time Take :" + UtilityFunctions.formatTime(diff) + ", Correct .\n";
                        } else {
                            playPauseAnimation(true);
                            helperTTS(UtilityFunctions.getCompliment(false), false, 0);
                            logs += "Time Take :" + UtilityFunctions.formatTime(diff) + ", Wrong .\n";
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    UtilityFunctions.runOnUiThread(() -> {
                        startListening();
                    }, 400);
                }
            }

            @Override
            public void onCompletion() {
                var current = (SpellingFragment) fragments.get(binding.viewPager.getCurrentItem());
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
                var current = (SpellingFragment) fragments.get(binding.viewPager.getCurrentItem());
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
            var current = (SpellingFragment) fragments.get(binding.viewPager.getCurrentItem());
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
                        if (request == REQUEST_FOR_QUESTION && !canNavigate) {
                            try {
                                startSpeaking();
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                        if (request == REQUEST_FOR_DES && !canNavigate) {
                            tts.setTextViewAndSentence(spellingDetails.get(binding.viewPager.getCurrentItem()).getDescription());
                            tts.initialize(spellingDetails.get(binding.viewPager.getCurrentItem()).getDescription(), EnglishSpellingActivity.this);
                            return;
                        }
                        if (canNavigate) {
                            UtilityFunctions.runOnUiThread(() -> {
                                if (mediaPlayer != null)
                                    mediaPlayer.pause();
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
                                }
                            }, DELAY_TIME);
                        } else {
                            isSayWordFinish = false;
                            currentTryCount++;
                            tts.initialize("Spell the Word " + UtilityFunctions.addSpace(spellingDetails.get(binding.viewPager.getCurrentItem()).getWord()), EnglishSpellingActivity.this);
                        }
                    }

                    @Override
                    public void onErrorOccurred(String errorMessage) {

                    }
                }).
                get().
                initialize(message, this);
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
        var current = (SpellingFragment) fragments.get(binding.viewPager.getCurrentItem());
        current.getAnimationView().setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        checkLogIsEnable();
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
        binding.toolBar.titleText.setText(getResources().getString(R.string.spelling));
        binding.toolBar.imageViewBack.setOnClickListener(v -> onBackPressed());
    }
}