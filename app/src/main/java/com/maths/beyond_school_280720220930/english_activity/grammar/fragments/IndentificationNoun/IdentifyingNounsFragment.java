package com.maths.beyond_school_280720220930.english_activity.grammar.fragments.IndentificationNoun;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_280720220930.database.english.grammer.model.GrammarModel;
import com.maths.beyond_school_280720220930.database.english.grammer.model.GrammarType;
import com.maths.beyond_school_280720220930.databinding.FragmentIdentifyingNounsBinding;
import com.maths.beyond_school_280720220930.dialogs.HintDialog;
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


@RequiresApi(api = Build.VERSION_CODES.N)
public class IdentifyingNounsFragment extends Fragment {

    public IdentifyingNounsFragment() {
        super(R.layout.fragment_identifying_nouns);
    }

    private static final String TAG = "IdentifyingNounsFragment";
    private FragmentIdentifyingNounsBinding binding;
    private EnglishGradeDatabase englishGradeDatabase;
    private List<GrammarModel> grammarModelList;
    private List<Fragment> fragmentList;
    private TextToSpeckConverter tts = null;
    private TextToSpeckConverter ttsHelper = null;
    private MediaPlayer mediaPlayer = null;

    private final int REQUEST_DES_COM = 44 * 2;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentIdentifyingNounsBinding.bind(view);
        englishGradeDatabase = EnglishGradeDatabase.getDbInstance(requireContext());
        setViews();
    }

    private void setViews() {
        var list = englishGradeDatabase.grammarDao().getAllGrammar();
        grammarModelList = getFilterGrammar(list);
        if (grammarModelList == null)
            return;
        fragmentList = mapToFragment(grammarModelList);
        setPager();
    }

    private void setPager() {
        var adapter = new EnglishViewPager(fragmentList, getChildFragmentManager(), getLifecycle());
        binding.viewPagerIdentifyingNouns.setAdapter(adapter);
        buttonClick();
    }

    private void buttonClick() {
        init();
        binding.playPause.setOnClickListener(v -> {
            if (binding.playPause.isChecked()) {
                initTTS();
                startSpeaking();
            } else
                destroyEngine();
        });
    }

    private void startSpeaking() {
        initMediaPlayer();
        var currentModel = grammarModelList.get(binding.viewPagerIdentifyingNouns.getCurrentItem());
        playPauseAnimation(true);
        tts.initialize(currentModel.getDescription() + "..." + "'"
                        + currentModel.getWord() + "'!" + "is noun here."
                        + "Now It's your turn to Click on the noun to identify it"

                , requireActivity());
    }

    private void initMediaPlayer() {
        mediaPlayer = UtilityFunctions.playClapSound(requireActivity());
    }


    private void init() {
        initTTS();
        initMediaPlayer();
        setToggleButtonChecked(true);
        playPauseAnimation(true);
        try {
            helperTTS("Hi, Kids. Let us learn about nouns. Nouns are words for people" +
                    ",places, or things. example :- 'girl','bag', 'shirt'" +
                    "'doctor', 'office','town' . " +
                    "Let’s learn how to identify nouns in a " +
                    "sentence,go through each word." +
                    "See if it's a person, place," +
                    "thing, or emotion or idea.", false, REQUEST_DES_COM);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<Fragment> mapToFragment(List<GrammarModel> grammarModels) {
        return CollectionUtils.mapWithIndex(grammarModels.stream(), (index, item) ->
                new RowItemFragment(item, index + 1)).collect(Collectors.toList());
    }

    private List<GrammarModel> getFilterGrammar(List<GrammarType> list) {
        for (var grammarType : list) {
            if (grammarType.getType().equals(getResources().getString(R.string.grammar_1))) {
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
                    playPauseAnimation(false);
                    var currentFragment = (RowItemFragment) fragmentList.get(binding.viewPagerIdentifyingNouns.getCurrentItem());
                    UtilityFunctions.runOnUiThread(() -> {
                        currentFragment.setSentenceButtonClick(pos -> {
                            if (pos == 2) {
                                try {
                                    playPauseAnimation(true);
                                    mediaPlayer.start();
                                    helperTTS(UtilityFunctions.getCompliment(true), true, 0);
                                } catch (ExecutionException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                playPauseAnimation(true);
                                tts.initialize(UtilityFunctions.getCompliment(false), requireActivity());
                            }
                        });
                    });
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


    private void helperTTS(String message, boolean canNavigate, int request) throws
            ExecutionException, InterruptedException {
        ttsHelper = new TTSHelperAsyncTask().execute(new ConversionCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCompletion() {
                if (!canNavigate && request == REQUEST_DES_COM) {
                    startSpeaking();
                    return;
                }
                if (canNavigate) {
                    UtilityFunctions.runOnUiThread(() -> {
                        if (binding.viewPagerIdentifyingNouns.getCurrentItem() == grammarModelList.size() - 1) {
                            UtilityFunctions.runOnUiThread(() -> {
                                playPauseAnimation(false);
                                setToggleButtonChecked(false);
                                displayCompleteDialog();
                            });
                            return;
                        }
                        mediaPlayer.pause();
                        binding.viewPagerIdentifyingNouns.setCurrentItem(binding.viewPagerIdentifyingNouns.getCurrentItem() + 1);
                        startSpeaking();
                    });
                }
            }

            @Override
            public void onErrorOccurred(String errorMessage) {

            }
        }).get().initialize(message, requireActivity());
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

    public void restartEngine() {
        destroyEngine();
        startSpeaking();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyEngine();
    }

    @Override
    public void onPause() {
        super.onPause();
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
