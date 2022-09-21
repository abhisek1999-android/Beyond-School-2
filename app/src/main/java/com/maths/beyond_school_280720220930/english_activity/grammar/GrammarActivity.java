package com.maths.beyond_school_280720220930.english_activity.grammar;

import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_GRAMMAR_CATEGORY;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.maths.beyond_school_280720220930.LogActivity;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_280720220930.database.english.grammer.model.GrammarModel;
import com.maths.beyond_school_280720220930.database.english.grammer.model.GrammarType;
import com.maths.beyond_school_280720220930.databinding.ActivityGrammarBinding;
import com.maths.beyond_school_280720220930.english_activity.grammar.fragments.IndentificationNoun.sub_fragment.RowItemFragment;
import com.maths.beyond_school_280720220930.english_activity.vocabulary.EnglishViewPager;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.CollectionUtils;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class GrammarActivity extends AppCompatActivity {

    private static final String TAG = "GrammarActivity";
    private final Context context = GrammarActivity.this;
    private ActivityGrammarBinding binding;
    private static String category;
    private EnglishGradeDatabase englishGradeDatabase;
    private List<GrammarModel> grammarModelList;
    private List<Fragment> fragmentList;

    private TextToSpeckConverter tts = null;
    private TextToSpeckConverter ttsHelper = null;
    private MediaPlayer mediaPlayer = null;

    private final int REQUEST_INTRO = 44 * 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGrammarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        englishGradeDatabase = EnglishGradeDatabase.getDbInstance(context);

        setToolbar();
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        if (!getIntent().hasExtra(EXTRA_GRAMMAR_CATEGORY)) {
            UtilityFunctions.simpleToast(context, "No data found");
            return;
        }
        category = getIntent().getStringExtra(EXTRA_GRAMMAR_CATEGORY);
        binding.textViewCategory.setText(category.replace("Grammar", ""));
        setViewPager();
    }

    private void setViewPager() {
        var list = englishGradeDatabase.grammarDao().getAllGrammar();
        grammarModelList = getFilterGrammar(list);
        if (grammarModelList == null) {
            UtilityFunctions.simpleToast(context, "No data found");
            return;
        }
        fragmentList = mapToFragment(grammarModelList);
        var pagerAdapter = new EnglishViewPager(fragmentList, getSupportFragmentManager(), getLifecycle());
        binding.viewPagerIdentifyingNouns.setAdapter(pagerAdapter);
        setIntro();
        setButtons();
        setVisibilityOfLinearLayout(true);
    }


    private void setIntro() {
        playPauseAnimation(true);
        setToggleButtonChecked(true);
        helperTTS(UtilityFunctions.getIntroForGrammar(context, category)
                , false
                , REQUEST_INTRO
        );
    }


    private void initTTS() {
        var task = new TTSAsyncTask();
        try {
            tts = task.execute(new ConversionCallback() {
                @Override
                public void onCompletion() {


                }

                @Override
                public void onErrorOccurred(String errorMessage) {

                }

                @Override
                public void getLogResult(String title) {
                    ConversionCallback.super.getLogResult(title);
                }
            }).get();

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void helperTTS(String message, boolean canNavigate, int request) {
        try {
            ttsHelper = new TTSHelperAsyncTask().execute(new ConversionCallback() {
                        @Override
                        public void onCompletion() {


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


    private void setToolbar() {
        binding.toolBar.imageViewBack.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.toolBar.titleText.setText(getResources().getString(R.string.grammar));
        binding.textViewCategory.setText(getIntent().
                getStringExtra(EXTRA_GRAMMAR_CATEGORY)
                .replace("Grammar", ""));

        binding.toolBar.getRoot().inflateMenu(R.menu.log_menu);
        binding.toolBar.getRoot().setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_log) {
                startActivity(new Intent(getApplicationContext(), LogActivity.class));

                return true;
            }
            return super.onOptionsItemSelected(item);

        });
    }

    private void setButtons() {
        var currentModel = grammarModelList.
                get(binding.viewPagerIdentifyingNouns
                        .getCurrentItem());
        var extra = currentModel.getExtra();
        var split = extra.split(",");
        if (split.length <= 1) {
            UtilityFunctions.simpleToast(context, "No data found");
            return;
        }
        if (split.length == 2) {
            binding.key1.setText(split[0]);
            binding.key2.setText(split[1]);
            binding.key3.setVisibility(View.GONE);
            binding.linearLayout.setWeightSum(2);
        }
        if (split.length == 3) {
            binding.key1.setText(split[0]);
            binding.key2.setText(split[1]);
            binding.key3.setText(split[2]);
            binding.linearLayout.setWeightSum(3);
            binding.key3.setVisibility(View.VISIBLE);
        }
    }


    private void setToggleButtonChecked(boolean isChecked) {
        UtilityFunctions.runOnUiThread(() -> binding.playPause.setChecked(isChecked));
    }

    private void playPauseAnimation(Boolean play) {
        UtilityFunctions.runOnUiThread(() -> {
            if (play)
                binding.imageViewTeacher.playAnimation();
            else
                binding.imageViewTeacher.pauseAnimation();
        });
    }

    private void setVisibilityOfLinearLayout(boolean isVisible) {
        UtilityFunctions.runOnUiThread(() -> {
            binding.linearLayout.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
        });
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

    private List<Fragment> mapToFragment(List<GrammarModel> grammarModels) {
        return CollectionUtils.mapWithIndex(grammarModels.stream(), (index, item) ->
                new RowItemFragment(item, index + 1)).collect(Collectors.toList());
    }

    private List<GrammarModel> getFilterGrammar(List<GrammarType> list) {
        for (var grammarType : list) {
            if (grammarType.getType().equals(category)) {
                return grammarModelList = grammarType.getGrammarModelList();
            }
        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyEngine();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        destroyEngine();
    }

    public void destroyEngine() {
        playPauseAnimation(false);
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
}