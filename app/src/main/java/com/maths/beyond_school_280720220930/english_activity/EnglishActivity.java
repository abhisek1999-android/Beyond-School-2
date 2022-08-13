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
    private SpeechToTextConverterEnglish stt = null;
    private List<VocabularyDetails> vocabularyList;
    private Boolean isSpeaking = false;
    private Boolean isSayWordFinish = true;
    private int currentTryCount = 0;
    private VisibleListener listener;

    public interface VisibleListener {
        void setVisible(Boolean isVisible);
    }

    public void setVisibleListener(VisibleListener listener) {
        this.listener = listener;
    }

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
        binding.textViewCategory.setText(getResources().getString(R.string.category, data.getCategory()));
        var fragments = getFragments(data);

        var pagerAdapter = new EnglishViewPager(
                fragments,
                getSupportFragmentManager(),
                getLifecycle()
        );

        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setUserInputEnabled(false);
    }

    private void buttonClick() {
        binding.playPause.setOnClickListener(view -> {
            isSpeaking = binding.playPause.isChecked();
            if (binding.playPause.isChecked()) {
                binding.tapInfoTextView.setVisibility(View.INVISIBLE);
                try {
                    initTTS();
                    intSTT();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                startSpeaking();
            } else {
                binding.tapInfoTextView.setVisibility(View.INVISIBLE);
                isSayWordFinish = true;
                tts.destroy();
                stt.destroy();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    private List<Fragment> getFragments(VocabularyModel data) {
        vocabularyList = data.getVocabularyDetails();
        return vocabularyList.stream().map(VocabularyFragment::new).collect(Collectors.toList());
    }

    private void initTTS() throws ExecutionException, InterruptedException {
        var task = new TTSAsyncTask();
        tts = task.execute(new ConversionCallback() {
            @Override
            public void onCompletion() {
                UtilityFunctions.runOnUiThread(() -> {
                    if (isSayWordFinish) {
                        isSayWordFinish = false;
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

    private void startSpeaking() {
        tts.initialize(vocabularyList.get(binding.viewPager.getCurrentItem()).getWord()
                + ". "
                + vocabularyList.get(binding.viewPager.getCurrentItem()).getDefinition(), this);

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
                            helperTTS("No Problem. Let's go to next word ", true);
                            currentTryCount = 0;
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        return;
                    }

//                    Check  the result
                    if (result.toLowerCase(Locale.ROOT).equals(vocabularyList.get(binding.viewPager.getCurrentItem()).getWord().toLowerCase(Locale.ROOT))) {
                        helperTTS("Correct", true);
                    } else {
                        helperTTS("Wrong", false);
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCompletion() {
                listener.setVisible(false);
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
            listener.setVisible(true);
            stt.initialize("Start Listening", this);
        });

    }


    private void helperTTS(String message, boolean canNavigate) throws ExecutionException, InterruptedException {
        new TTSHelperAsyncTask().execute(new ConversionCallback() {
            @Override
            public void onCompletion() {
                if (canNavigate) {
                    UtilityFunctions.runOnUiThread(() -> {
                        binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
                        isSayWordFinish = true;
                        if (isSpeaking)
                            startSpeaking();
                        else {
                            binding.playPause.setChecked(false);
                            binding.tapInfoTextView.setVisibility(View.VISIBLE);
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