package com.maths.beyond_school_280720220930.english_activity.grammar.fragments.Irregular_plural_noun;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_280720220930.database.english.grammer.model.GrammarModel;
import com.maths.beyond_school_280720220930.database.english.grammer.model.GrammarType;
import com.maths.beyond_school_280720220930.database.log.LogDatabase;
import com.maths.beyond_school_280720220930.databinding.FragmentIdentifyingNounsBinding;
import com.maths.beyond_school_280720220930.english_activity.grammar.fragments.Irregular_plural_noun.sub_fragment.RowItemIrregularPluralNoun;
import com.maths.beyond_school_280720220930.english_activity.vocabulary.EnglishViewPager;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.SpeechToTextBuilder;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.SpeechToTextConverterEnglish;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.CollectionUtils;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RequiresApi(api = Build.VERSION_CODES.N)
public class IrregularPluralNounFragment extends Fragment {

    public IrregularPluralNounFragment() {
        super(R.layout.fragment_identifying_nouns);
    }

    private static final String TAG = "IrregularPluralNounFrag";

    private FragmentIdentifyingNounsBinding binding;
    private EnglishGradeDatabase englishGradeDatabase;
    private List<GrammarModel> grammarModelList;
    private List<Fragment> fragmentList;
    private TextToSpeckConverter tts = null;
    private TextToSpeckConverter ttsHelper = null;
    private SpeechToTextConverterEnglish stt = null;
    private MediaPlayer mediaPlayer = null;
    private LogDatabase logDatabase;
    private String logs = "";
    private long startTime = 0;
    private final int REQUEST_INTRO = 44 * 2;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentIdentifyingNounsBinding.bind(view);
        englishGradeDatabase = EnglishGradeDatabase.getDbInstance(requireContext());
        logDatabase = LogDatabase.getDbInstance(requireContext());
        setViews();
    }

    private void setViews() {
        var list = englishGradeDatabase.grammarDao().getAllGrammar();
        grammarModelList = getFilterGrammar(list);
        if (grammarModelList == null)
            return;
        fragmentList = mapToFragment(grammarModelList);
        setPager();
        buttonClick();

    }

    private void buttonClick() {
        binding.playPause.setOnClickListener(v -> {
            if (binding.playPause.isChecked()) {
                initEngineAndStartSpeaking();
            } else {
                destroyEngine();
            }
        });
    }

    private void initEngineAndStartSpeaking() {
        setToggleButton(true);
        try {
            intSTT();
            initTTS();
            initMediaPlayer();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        startSpeaking();
    }

    private void initMediaPlayer() {
        mediaPlayer = UtilityFunctions.playClapSound(requireActivity());
    }

    private void setPager() {
        var adapter = new EnglishViewPager(fragmentList, getChildFragmentManager(), getLifecycle());
        binding.viewPagerIdentifyingNouns.setAdapter(adapter);
        intIntro();
        try {
            initTTS();
            intSTT();
            initMediaPlayer();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void intIntro() {
        try {
            setToggleButton(true);
            helperTTS("Some nouns have irregular plural forms." +
                            "They turn into different words." +
                            "Let us memorize them."
                    , false
                    , REQUEST_INTRO);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initTTS() throws ExecutionException, InterruptedException {
        var task = new TTSAsyncTask();
        tts = task.execute(new ConversionCallback() {
            @Override
            public void onCompletion() {
                if (!tts.getTextRanceListener()) {
                    startListening();
                    return;
                }
                tts.setTextViewAndSentence(null);

                var currentModel = grammarModelList
                        .get(binding.viewPagerIdentifyingNouns
                                .getCurrentItem());
                tts.initialize("Now, Please tell the plural of the "
                                + currentModel.getWord(),
                        requireActivity());
            }

            @Override
            public void onErrorOccurred(String errorMessage) {
                logs += "Error occurred while speaking: " + errorMessage + "\n";
            }

            @Override
            public void getLogResult(String title) {
                ConversionCallback.super.getLogResult(title);
                logs += title + "\n";
            }
        }).get();
        tts.setTextRangeListener((utteranceId, sentence, start, end, frame) -> {
            UtilityFunctions.runOnUiThread(() -> {
                var textView = (TextView) requireActivity().findViewById(R.id.text_view_word);
                if (textView != null) {
                    var acSen = sentence.replace(",", "→");
                    Spannable textWithHighlights = new SpannableString(acSen);
                    textWithHighlights.setSpan(new ForegroundColorSpan(
                                    ContextCompat.
                                            getColor(requireContext(), R.color.primary)),
                            start,
                            end,
                            Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    textView.setText(textWithHighlights);
                }
            });
        });
    }

    private void startListening() {
        UtilityFunctions.runOnUiThread(() -> {
            startTime = new Date().getTime();
            stt.initialize("Speak", requireActivity());
            var current = (RowItemIrregularPluralNoun) fragmentList
                    .get(binding.viewPagerIdentifyingNouns.getCurrentItem());
            current.getLottieAnimationView().setVisibility(View.VISIBLE);
        });
    }

    private void intSTT() throws ExecutionException, InterruptedException {
        var task = new STTAsyncTask();
        stt = task.execute(new ConversionCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(String result) {
                var endTime = new Date().getTime();
                long diff = endTime - startTime;
                playPauseAnimation(false);
                var currentWordModel = grammarModelList
                        .get(binding.viewPagerIdentifyingNouns.getCurrentItem());
                Log.d("XXX", "onSuccess: " + result + " " + currentWordModel.getPlural());
                var correctAnswer = currentWordModel.getPlural()
                        .toLowerCase(Locale.ROOT).trim();
                logs += "Correct Answer: " + correctAnswer + "\nDetected: " + result + "\n";
                if (result.equals(correctAnswer)) {
                    logs += "Time Take :" + UtilityFunctions.formatTime(diff) + ", Correct .\n";
                    try {
                        mediaPlayer.start();
                        helperTTS(UtilityFunctions.getCompliment(true), true, 0);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                tts.initialize(UtilityFunctions.getCompliment(false), requireActivity());
                logs += "Time Take :" + UtilityFunctions.formatTime(diff) + ", Wrong.\n";
            }

            @Override
            public void onCompletion() {
                var current = (RowItemIrregularPluralNoun) fragmentList
                        .get(binding.viewPagerIdentifyingNouns.getCurrentItem());
                current.getLottieAnimationView().setVisibility(View.GONE);
            }

            @Override
            public void onErrorOccurred(String errorMessage) {
                startListening();
            }

            @Override
            public void getLogResult(String title) {
                ConversionCallback.super.getLogResult(title);
                logs += title + "\n";
            }
        }).get();

    }

    private void helperTTS(String message, boolean canNavigate, int request) throws
            ExecutionException, InterruptedException {
        ttsHelper = new TTSHelperAsyncTask().execute(new ConversionCallback() {
            @Override
            public void onCompletion() {
                if (!canNavigate && request == REQUEST_INTRO) {
                    startSpeaking();
                    return;
                }
                if (canNavigate) {
                    UtilityFunctions.runOnUiThread(() -> {
                        mediaPlayer.pause();
                    });
                    binding.viewPagerIdentifyingNouns.
                            setCurrentItem(
                                    binding.viewPagerIdentifyingNouns
                                            .getCurrentItem() + 1);
                    playPauseAnimation(true);
                    startSpeaking();
                }
            }

            @Override
            public void onErrorOccurred(String errorMessage) {
                logs += errorMessage + "\n";
            }
        }).get().initialize(message, requireActivity());
    }

    private void startSpeaking() {

        logs += "Question : What is the plural " +
                grammarModelList.get(binding.viewPagerIdentifyingNouns.getCurrentItem()).getWord()
                + ". \n";
        var currentModel = grammarModelList
                .get(binding.viewPagerIdentifyingNouns
                        .getCurrentItem());
        var sentence = currentModel.getDescription()
                .replace("→", ",");

        tts.initialize(sentence
                , getActivity());
        tts.setTextViewAndSentence(sentence);
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

    @SuppressWarnings("deprecation")
    static class STTAsyncTask extends AsyncTask<ConversionCallback, Void, SpeechToTextConverterEnglish> {
        @Override
        protected SpeechToTextConverterEnglish doInBackground(ConversionCallback... conversionCallbacks) {
            return SpeechToTextBuilder.englishBuilder(conversionCallbacks[0]);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyEngine();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<Fragment> mapToFragment(List<GrammarModel> grammarModels) {
        return CollectionUtils.mapWithIndex(grammarModels.stream(), (index, item) ->
                new RowItemIrregularPluralNoun(item, index + 1)).collect(Collectors.toList());
    }

    private List<GrammarModel> getFilterGrammar(List<GrammarType> list) {
        for (var grammarType : list) {
            if (grammarType.getType().equals(getResources().getString(R.string.grammar_2))) {
                return grammarModelList = grammarType.getGrammarModelList();
            }
        }
        return null;
    }

    public void restartEngine() {
        destroyEngine();
        initEngineAndStartSpeaking();
    }

    public void destroyEngine() {
        playPauseAnimation(false);
        if (tts != null) {
            tts.destroy();
        }
        if (stt != null)
            stt.destroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        if (ttsHelper != null) {
            ttsHelper.destroy();
        }
        try {
            var current = (RowItemIrregularPluralNoun) fragmentList
                    .get(binding.viewPagerIdentifyingNouns.getCurrentItem());
            if (current.getLottieAnimationView() != null)
                current.getLottieAnimationView().setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkLogIsEnable() {
        if (PrefConfig.readIdInPref(requireContext(), "IS_LOG_ENABLE").equals("true"))
            saveLog();
    }

    private void saveLog() {
        if (!logs.isEmpty()) {
            Log.d(TAG, "saveLog: Called " + logs);
            UtilityFunctions.saveLog(logDatabase, logs);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        destroyEngine();
        checkLogIsEnable();
    }

    private void setToggleButton(Boolean isChecked) {
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
}
