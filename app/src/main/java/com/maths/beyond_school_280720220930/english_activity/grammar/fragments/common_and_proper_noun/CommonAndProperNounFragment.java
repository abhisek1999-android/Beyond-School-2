package com.maths.beyond_school_280720220930.english_activity.grammar.fragments.common_and_proper_noun;


import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_280720220930.database.english.grammer.model.GrammarModel;
import com.maths.beyond_school_280720220930.database.english.grammer.model.GrammarType;
import com.maths.beyond_school_280720220930.database.log.LogDatabase;
import com.maths.beyond_school_280720220930.databinding.FragmentIdentifyingNounsBinding;
import com.maths.beyond_school_280720220930.dialogs.HintDialog;
import com.maths.beyond_school_280720220930.english_activity.grammar.fragments.common_and_proper_noun.sub_fragment.RowItemCommonAndProperNoun;
import com.maths.beyond_school_280720220930.english_activity.vocabulary.EnglishViewPager;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.CollectionUtils;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


@RequiresApi(api = Build.VERSION_CODES.N)
public class CommonAndProperNounFragment extends Fragment {


    public CommonAndProperNounFragment() {
        super(R.layout.fragment_identifying_nouns);
    }

    private static final String TAG = "CommonAndProperNounFrag";
    private FragmentIdentifyingNounsBinding binding;
    private EnglishGradeDatabase englishGradeDatabase;
    private List<GrammarModel> grammarModelList;
    private List<Fragment> fragmentList;
    private TextToSpeckConverter tts = null;
    private TextToSpeckConverter ttsHelper = null;
    private MediaPlayer mediaPlayer = null;
    private final int REQUEST_INTRO = 44 * 234;

    private LogDatabase logDatabase;
    private String logs = "";
    private long startTime = 0;

    private final String[] typeArray = new String[]{"common noun", "proper noun"};
    private onButtonClick listener;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentIdentifyingNounsBinding.bind(view);
        englishGradeDatabase = EnglishGradeDatabase.getDbInstance(requireContext());
        logDatabase = LogDatabase.getDbInstance(requireContext());
        setView();
    }

    private void setView() {
        var list = englishGradeDatabase.grammarDao().getAllGrammar();
        grammarModelList = getFilterGrammar(list);
        if (grammarModelList == null) {
            return;
        }
        fragmentList = mapToFragment(grammarModelList);
        setPager();
        intIntro();
        setButtonClick();
        handleButtonClick();
    }

    private void intIntro() {
        initTTS();
        initMediaPlayer();
        var introSentence = "Common noun : It is the name of general objects." +
                " For example :- There are many cars,'car' is a common noun." +
                "Proper noun : A name for a specific object," +
                "and always have capitalize first letter." +
                " For example :- 'Monday', 'Tuesday'";
        playPauseAnimation(true);
        setToggleButtonChecked(true);
        helperTTS(introSentence, false, REQUEST_INTRO);

    }

    private void initMediaPlayer() {
        mediaPlayer = UtilityFunctions.playClapSound(requireActivity());
    }

    private void startSpeaking() {
        startTime = new Date().getTime();
        var currentWord = grammarModelList.get(binding.viewPagerIdentifyingNouns
                .getCurrentItem()).getWord();
        var type = grammarModelList.get(binding.viewPagerIdentifyingNouns
                .getCurrentItem()).getDescription();
        var sentence = "'" + currentWord + "' ." + type;
        tts.initialize(sentence, requireActivity());
        tts.setTextViewAndSentence(sentence);
    }

    private void setButtonClick() {
        binding.playPause.setOnClickListener(v -> {
            if (binding.playPause.isChecked()) {
                initTTS();
                startSpeaking();
            } else {
                destroyEngine();
            }
        });
    }

    private void setPager() {
        var adapter = new EnglishViewPager(fragmentList, requireActivity()
                .getSupportFragmentManager()
                , getLifecycle());
        binding.viewPagerIdentifyingNouns.setAdapter(adapter);
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


    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<Fragment> mapToFragment(List<GrammarModel> grammarModels) {
        return CollectionUtils.mapWithIndex(grammarModels.stream(), (index, item) ->
                new RowItemCommonAndProperNoun(item, index + 1)).collect(Collectors.toList());
    }

    private List<GrammarModel> getFilterGrammar(List<GrammarType> list) {
        for (var grammarType : list) {
            if (grammarType.getType().equals(getResources().getString(R.string.grammar_3))) {
                return grammarModelList = grammarType.getGrammarModelList();
            }
        }
        return null;
    }


    private void initTTS() {
        var task = new TTSAsyncTask();
        try {
            tts = task.execute(new ConversionCallback() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onCompletion() {
                    if (!tts.getTextRanceListener()) {
                        checkAnswer();
                        return;
                    }
                    var list = new String[]{"Tell me which noun is this?",
                            "Please answer which noun is this ?"};
                    tts.setTextViewAndSentence(null);
                    tts.initialize(UtilityFunctions.getRandomItem(list), requireActivity());
                }

                @Override
                public void onErrorOccurred(String errorMessage) {
                }

                @Override
                public void getLogResult(String title) {
                    ConversionCallback.super.getLogResult(title);
                    logs = title + "\n";
                }
            }).get();

            tts.setTextRangeListener((utteranceId, sentence, start, end, frame) -> {

            });

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkAnswer() {
        var endTime = new Date().getTime();
        var diff = endTime - startTime;
        setLayoutVisibility(true);
        listener = type -> {
            var currentType = grammarModelList.get(binding.viewPagerIdentifyingNouns
                    .getCurrentItem()).getDescription();
            Log.d("XXX", "checkAnswer: " + type + " " + currentType);
            if (currentType.contains(type)) {
                setLayoutVisibility(false);
                mediaPlayer.start();
                logs += "Time Take :" + UtilityFunctions.formatTime(diff) + ", Correct .\n";
                helperTTS(
                        UtilityFunctions.getCompliment(true),
                        true,
                        0
                );
            } else {
                logs += "Time Take :" + UtilityFunctions.formatTime(diff) + ", Wrong .\n";
                tts.initialize(
                        UtilityFunctions.getCompliment(false),
                        requireActivity()
                );
            }
        };
    }

    private void setLayoutVisibility(boolean visibility) {
        UtilityFunctions.runOnUiThread(() -> binding.linearLayout
                .setVisibility(visibility ? View.VISIBLE : View.GONE));
    }

    private void handleButtonClick() {
        UtilityFunctions.runOnUiThread(() -> {
            binding.key1.setText(typeArray[0]);
            binding.key2.setText(typeArray[1]);
            binding.key1.setOnClickListener(v -> listener.setOnButtonClick(typeArray[0]));
            binding.key2.setOnClickListener(v -> listener.setOnButtonClick(typeArray[1]));
        });
    }

    private void helperTTS(String message, boolean canNavigate, int request) {
        try {
            ttsHelper = new TTSHelperAsyncTask().execute(new ConversionCallback() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onCompletion() {
                    if (!canNavigate && request == REQUEST_INTRO) {
                        startSpeaking();
                        return;
                    }
                    if (canNavigate) {
                        mediaPlayer.pause();
                        if (binding.viewPagerIdentifyingNouns.getCurrentItem() == grammarModelList.size() - 1) {
                            UtilityFunctions.runOnUiThread(() -> {
                                playPauseAnimation(false);
                                setToggleButtonChecked(false);
                                displayCompleteDialog();
                            });
                            return;
                        }
                        binding.viewPagerIdentifyingNouns.
                                setCurrentItem(binding.viewPagerIdentifyingNouns
                                        .getCurrentItem() + 1);
                        startSpeaking();
                    }
                }

                @Override
                public void onErrorOccurred(String errorMessage) {

                }
            }).get().initialize(message, requireActivity());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void displayCompleteDialog() {

        HintDialog hintDialog = new HintDialog(requireContext());
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
                    UtilityFunctions.simpleToast(requireContext(), "TODO : Implement Test");
                    break;
            }
        });


        hintDialog.show();

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
        checkLogIsEnable();
        destroyEngine();
    }

    public void destroyEngine() {
        playPauseAnimation(false);
        if (tts != null) {
            tts.destroy();
        }
        if (ttsHelper != null) {
            ttsHelper.destroy();
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        setLayoutVisibility(false);
    }

    interface onButtonClick {
        void setOnButtonClick(String type);
    }

}
