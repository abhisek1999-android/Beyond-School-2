package com.maths.beyond_school_new_071020221400.english_activity.vocabulary2;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.maths.beyond_school_new_071020221400.R;
import com.maths.beyond_school_new_071020221400.database.english.vocabulary.VocabularyDao;
import com.maths.beyond_school_new_071020221400.database.english.vocabulary.model.VocabularyDetails;
import com.maths.beyond_school_new_071020221400.database.log.LogDatabase;
import com.maths.beyond_school_new_071020221400.database.process.ProgressDataBase;
import com.maths.beyond_school_new_071020221400.database.process.ProgressM;
import com.maths.beyond_school_new_071020221400.databinding.ActivityEnglishBinding;
import com.maths.beyond_school_new_071020221400.dialogs.HintDialog;
import com.maths.beyond_school_new_071020221400.english_activity.vocabulary.EnglishViewPager;
import com.maths.beyond_school_new_071020221400.english_activity.vocabulary.VocabularyFragment;
import com.maths.beyond_school_new_071020221400.retrofit.ApiClient;
import com.maths.beyond_school_new_071020221400.retrofit.ApiInterface;
import com.maths.beyond_school_new_071020221400.retrofit.model.content.ContentModel;
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
import java.util.stream.Collectors;

import retrofit2.Retrofit;

public class VocabularyActivity extends AppCompatActivity implements VocabularyFragment.OnRootClick {

    private static final String TAG = "VocabularyActivity";

    private ActivityEnglishBinding binding;
    private static String chapterName;

    private List<VocabularyDetails> vocabularyList;
    private List<Fragment> fragmentList;

    private ContentModel.Meta meta;
    private static final int MAX_TRY = 2 /* Giver u three chance */;
    private static final int MAX_TRY_FOR_SPEECH = 4 /* Giver u three chance */;
    private static final int DELAY_TIME = 800;
    private final int REQUEST_FOR_DES = 345 * 34;
    private final int REQUEST_FOR_QUESTION = 345 * 35;

    private VocabularyDao dao;
    private TextToSpeckConverter tts = null;
    private TextToSpeckConverter ttsHelper = null;
    private SpeechToTextConverterEnglish stt = null;

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
    private String parentsContactId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEnglishBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chapterName = getIntent().getStringExtra(Constants.EXTRA_VOCABULARY_CATEGORY);
        logDatabase = LogDatabase.getDbInstance(this);
        fragmentList=new ArrayList<>();
        analytics = FirebaseAnalytics.getInstance(getApplicationContext());
        auth = FirebaseAuth.getInstance();
        progressDataBase = ProgressDataBase.getDbInstance(this);
        progressData = new ArrayList<>();
        getSubjectData();
    }

    private void getSubjectData() {
        Retrofit retrofit = ApiClient.getClient();
        var api = retrofit.create(ApiInterface.class);
//        api.getVocabularySubject(chapterName).enqueue(new retrofit2.Callback<>() {
//            @Override
//            public void onResponse(Call<ContentModel> call, Response<ContentModel> response) {
//                Log.d(TAG, "onResponse: " + response.code());
//                Log.d(TAG, "onResponse: " + response.body().getContent().toString());
//                var contentModel = response.body();
//                meta = contentModel.getMeta();
//                var list = contentModel.getContent();
//                var converter = new GrammarTypeConverter();
////                vocabularyList = converter.mapToList(list);
//                setViewPager();
//            }
//
//            @Override
//            public void onFailure(Call<ContentModel> call, Throwable t) {
//                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
//            }
//        });
    }

    private void setViewPager() {

        binding.textViewCategory.setText(chapterName);
        List<Fragment> fragments = getFragments();
        var pagerAdapter = new EnglishViewPager(fragments, getSupportFragmentManager(), getLifecycle());

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
            helperTTS(meta.getQuestion(), false, REQUEST_FOR_QUESTION);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

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
                        tts.initialize("Now Say the word " +
                                vocabularyList.get(binding.viewPager.getCurrentItem())
                                        .getWord(), VocabularyActivity.this);

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
                    textWithHighlights.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.primary)), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
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
//                        UtilityFunctions.sendDataToAnalytics(analytics, auth.getUid().toString(), "kidsid", "Ayaan", "english Vocabulary", 22, vocabularyList.get(binding.viewPager.getCurrentItem()).getWord(), result, true, (int) diff, vocabularyList.get(binding.viewPager.getCurrentItem()).getWord() + " : " + vocabularyList.get(binding.viewPager.getCurrentItem()).getDefinition(), "english", parentsContactId);
                        playPauseAnimation(true);
                    } else {
                        logs += "Time Take :" + UtilityFunctions.formatTime(diff) + ", Wrong .\n";
                        helperTTS(UtilityFunctions.getCompliment(false), false, 0);
//                        UtilityFunctions.sendDataToAnalytics(analytics, auth.getUid().toString(), "kidsid", "Ayaan", "english Vocabulary", 22, vocabularyList.get(binding.viewPager.getCurrentItem()).getWord(), result, false, (int) diff, vocabularyList.get(binding.viewPager.getCurrentItem()).getWord() + " : " + vocabularyList.get(binding.viewPager.getCurrentItem()).getDefinition(), "english", parentsContactId);
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


    @NonNull
    private List<Fragment> getFragments() {
        fragmentList = CollectionUtils.mapWithIndex(vocabularyList.stream(), (index, item) -> new VocabularyFragment(item, index + 1, this)).collect(Collectors.toList());
        return fragmentList;
    }

    @Override
    public void onRootClick() {

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


    private void helperTTS(String message, boolean canNavigate, int request) throws ExecutionException, InterruptedException {
        ttsHelper = new TTSHelperAsyncTask().execute(new ConversionCallback() {
            @Override
            public void onCompletion() {
                if (request == REQUEST_FOR_DES && !canNavigate) {
                    tts.setTextViewAndSentence(vocabularyList.get(binding.viewPager.getCurrentItem()).getDefinition());
                    tts.initialize(vocabularyList.get(binding.viewPager.getCurrentItem()).getDefinition(), VocabularyActivity.this);
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
                    tts.initialize("Say the word ones again " + vocabularyList.get(binding.viewPager.getCurrentItem()).getWord(), VocabularyActivity.this);
                }
            }

            @Override
            public void onErrorOccurred(String errorMessage) {

            }
        }).get().initialize(message, this);
    }

    private void playPauseAnimation(Boolean play) {
        if (play) binding.imageViewTeacher.playAnimation();
        else binding.imageViewTeacher.pauseAnimation();
    }

    private void displayCompleteDialog() {

        HintDialog hintDialog = new HintDialog(this);
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
//                    navigateToTest();
                    break;
            }
        });


        hintDialog.show();

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
}
