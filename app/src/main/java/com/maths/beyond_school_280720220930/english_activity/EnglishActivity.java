package com.maths.beyond_school_280720220930.english_activity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.english.EnglishDao;
import com.maths.beyond_school_280720220930.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_280720220930.database.english.model.VocabularyDetails;
import com.maths.beyond_school_280720220930.database.english.model.VocabularyModel;
import com.maths.beyond_school_280720220930.databinding.ActivityEnglishBinding;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.SpeechToTextBuilder;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.SpeechToTextConverterEnglish;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class EnglishActivity extends AppCompatActivity {

    private ActivityEnglishBinding binding;
    private static final String TAG = EnglishActivity.class.getSimpleName();
    private static final int MAX_TRY = 2 /* Giver u three chance */;

    private EnglishDao dao;
    private TextToSpeckConverter tts = null;
    private TextToSpeckConverter ttsHelper = null;
    private SpeechToTextConverterEnglish stt = null;
    private List<VocabularyDetails> vocabularyList;
    private List<Fragment> fragmentList;
    private Boolean isSpeaking = false;
    private Boolean isSayWordFinish = true;
    private int currentTryCount = 0;
    private final int REQUEST_FOR_DES = 345 * 34;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEnglishBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        var database = EnglishGradeDatabase.getDbInstance(this);
        dao = database.englishDao();
        setToolbar();
        setViewPager();
        buttonClick();
    }


    private void setToolbar() {
        binding.toolBar.titleText.setText(getResources().getString(R.string.vocabulary));
        binding.toolBar.imageViewBack.setOnClickListener(view -> finish());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setViewPager() {
        var data = dao.getEnglishModel(1).getVocabulary().get(0);
        binding.textViewCategory.setText(getResources().getString(R.string.category, data.getCategory().toUpperCase(Locale.ROOT)));
        List<Fragment> fragments = getFragments(data);
        var pagerAdapter = new EnglishViewPager(
                fragments,
                getSupportFragmentManager(),
                getLifecycle()
        );

        binding.viewPager.setAdapter(pagerAdapter);

    }

    private void buttonClick() {
        binding.playPause.setOnClickListener(view -> {
            isSpeaking = binding.playPause.isChecked();
            if (binding.playPause.isChecked()) {
                try {
                    initTTS();
                    intSTT();

                    startSpeaking();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                isSayWordFinish = true;
                tts.destroy();
                stt.destroy();
                ttsHelper.destroy();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    private List<Fragment> getFragments(VocabularyModel data) {
        vocabularyList = data.getVocabularyDetails();
        fragmentList = vocabularyList.stream().map(VocabularyFragment::new).collect(Collectors.toList());
        return fragmentList;
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
                        tts.initialize("Now Say the word " + vocabularyList.get(binding.viewPager.getCurrentItem()).getWord(), EnglishActivity.this);

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
    }

    private void startSpeaking() throws ExecutionException, InterruptedException {
        helperTTS(vocabularyList.get(binding.viewPager.getCurrentItem()).getWord(), false, REQUEST_FOR_DES);

//        Stop when reach ot last item
        if (binding.viewPager.getCurrentItem() == (vocabularyList.size() - 1))
            isSpeaking = false;
    }


    private void intSTT() throws ExecutionException, InterruptedException {
        var task = new STTAsyncTask();
        stt = task.execute(new ConversionCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "onSuccess:  result " + result + " word " + vocabularyList.get(binding.viewPager.getCurrentItem()).getWord());
                try {
//                    Check the maximum try count
                    if (currentTryCount >= MAX_TRY) {
                        try {
                            helperTTS("No Problem. Let's go to next word ", true, 0);
                            currentTryCount = 0;
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        return;
                    }

//                    Check  the result
                    if (UtilityFunctions.checkString(result.toLowerCase(Locale.ROOT), vocabularyList.get(binding.viewPager.getCurrentItem()).getWord().toLowerCase(Locale.ROOT))) {
                        helperTTS("Correct", true, 0);

                    } else {
                        helperTTS("Wrong", false, 0);

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
                binding.playPause.setChecked(false);
                isSayWordFinish = true;
            }
        }).get();

    }

    //    Helper method to start listening
    private void startListening() {
        UtilityFunctions.runOnUiThread(() -> {
            var current = (VocabularyFragment) fragmentList.get(binding.viewPager.getCurrentItem());
            current.getAnimationView().setVisibility(View.VISIBLE);
            stt.initialize("Start Listening", this);
        });

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
                if (canNavigate) {
                    UtilityFunctions.runOnUiThread(() -> {
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
                    });
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