package com.maths.beyond_school_280720220930.english_activity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

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
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class EnglishActivity extends AppCompatActivity {

    private ActivityEnglishBinding binding;
    private static final String TAG = EnglishActivity.class.getSimpleName();

    private EnglishDao dao;
    private TextToSpeckConverter tts = null;
    private List<VocabularyDetails> vocabularyList;
    private Boolean isSpeaking = false;

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
                try {
                    initTTS();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                startSpeaking();
            } else {
                tts.destroy();
            }
        });
    }


    private void startSpeaking() {
        tts.initialize(vocabularyList.get(binding.viewPager.getCurrentItem()).getWord()
                + ". "
                + vocabularyList.get(binding.viewPager.getCurrentItem()).getDefinition(), this);

//        Stop when reach ot last item
        if (binding.viewPager.getCurrentItem() == (vocabularyList.size() - 1))
            isSpeaking = false;
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
                            binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
                            if (isSpeaking) {
                                startSpeaking();
                            }
                        });
                    }

                    @Override
                    public void onErrorOccurred(String errorMessage) {
                        Log.e(TAG, "onErrorOccurred: " + errorMessage);
                    }
                }).

                get();
    }

    static class TTSAsyncTask extends AsyncTask<ConversionCallback, Void, TextToSpeckConverter> {
        @Override
        protected TextToSpeckConverter doInBackground(ConversionCallback... conversionCallbacks) {
            return TextToSpeechBuilder.builder(conversionCallbacks[0]);
        }
    }
}