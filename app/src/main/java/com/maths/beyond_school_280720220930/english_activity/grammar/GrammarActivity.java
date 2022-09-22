package com.maths.beyond_school_280720220930.english_activity.grammar;

import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_GRAMMAR_CATEGORY;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.maths.beyond_school_280720220930.LogActivity;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_280720220930.database.english.grammer.model.GrammarModel;
import com.maths.beyond_school_280720220930.database.english.grammer.model.GrammarType;
import com.maths.beyond_school_280720220930.database.process.ProgressDataBase;
import com.maths.beyond_school_280720220930.database.process.ProgressM;
import com.maths.beyond_school_280720220930.databinding.ActivityGrammarBinding;
import com.maths.beyond_school_280720220930.databinding.AnimSingleLayoutBinding;
import com.maths.beyond_school_280720220930.dialogs.HintDialog;
import com.maths.beyond_school_280720220930.english_activity.grammar.test.GrammarTestActivity;
import com.maths.beyond_school_280720220930.english_activity.vocabulary.EnglishViewPager;
import com.maths.beyond_school_280720220930.model.AnimData;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.AnimationUtil;
import com.maths.beyond_school_280720220930.utils.CollectionUtils;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

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
    private ButtonClick listener = null;

    private final int REQUEST_INTRO = 44 * 2;
    public static final int TIMER_VALUE = 15;
    private AlertDialog.Builder alert = null;
    private AlertDialog alertDialog = null;


    private TextToSpeckConverter ttsHelperAnim;
    private int num = 0;
    private List<AnimData> animEng;

    private LinearLayout addAnimLayout, finalView;
    private TextView descTextView, finalText;
    private Animation slideLeftAnim, slideRightAnim, fadeIn;

    private long timeSpend = 0;
    private List<ProgressM> progressData;
    private ProgressDataBase progressDataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGrammarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        englishGradeDatabase = EnglishGradeDatabase.getDbInstance(context);
        progressDataBase = ProgressDataBase.getDbInstance(this);
        progressData = new ArrayList<>();

        setToolbar();
        getDataFromIntent();
    }

    private void firstOpen() {
        var isOpen = !PrefConfig.readBooleanInPref(context, category);
        if (isOpen)
            displayTutorialDialog();
        else
            setIntro();
    }

    private void displayDialog() {
        binding.hintButton.setOnClickListener(v -> {
            displayTutorialDialog();
        });
    }

    private void displayTutorialDialog() {
        try {
            destroyEngine();
            displayTutorialAnimation();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleTestButtonClick() {
        binding.giveTestButton.setOnClickListener(v -> {
            navigateToTest();
        });
    }

    private void initMediaPlayer() {
        mediaPlayer = UtilityFunctions.playClapSound(this);
    }

    private void getDataFromIntent() {
        if (!getIntent().hasExtra(EXTRA_GRAMMAR_CATEGORY)) {
            UtilityFunctions.simpleToast(context, "No data found");
            return;
        }
        category = getIntent().getStringExtra(EXTRA_GRAMMAR_CATEGORY);
        binding.textViewCategory.setText(category.replace("Grammar", ""));
        setViewPager();
        firstOpen();
        displayDialog();
        handleTestButtonClick();
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
        binding.viewPagerIdentifyingNouns.setUserInputEnabled(false);

        setButton();
        setOptionButtonClick();
    }

    private void setButton() {
        binding.playPause.setOnClickListener(v -> {
            if (binding.playPause.isChecked()) {
                initTTS();
                timer();
                initMediaPlayer();
                startSpeaking();
            } else
                destroyEngine();
        });
    }


    private void setIntro() {
        initTTS();
        initMediaPlayer();
        playPauseAnimation(true);
        setToggleButtonChecked(true);
        helperTTS(UtilityFunctions.getIntroForGrammar(context, category)
                , false
                , REQUEST_INTRO
        );
    }

    private void startSpeaking() {
        var currentModel = grammarModelList.
                get(binding.viewPagerIdentifyingNouns.getCurrentItem());
        var question = UtilityFunctions.getQuestionForGrammar(context
                , currentModel,
                category)[0];
        if (binding.viewPagerIdentifyingNouns.getCurrentItem() < 2)
            tts.setTextViewAndSentence(question);
        tts.initialize(
                question
                , this
        );
    }


    private void initTTS() {
        var task = new TTSAsyncTask();
        try {
            tts = task.execute(new ConversionCallback() {
                @Override
                public void onCompletion() {
                    if (!tts.getTextRanceListener()) {
                        UtilityFunctions.runOnUiThread(() -> {
                            var currentModel = grammarModelList.
                                    get(binding.viewPagerIdentifyingNouns.getCurrentItem());
                            var currentFragment = (RowItemFragment) fragmentList.
                                    get(binding.viewPagerIdentifyingNouns.getCurrentItem());
                            currentFragment.getTextView()
                                    .setText(currentModel.getDescription());

                        });
                        setOptionButton();
                        setVisibilityOfLinearLayout(true);
                        playPauseAnimation(false);
                        checkAnswer();
                        Log.d("XXX", "onCompletion: Called ");
                        return;
                    }
                    var currentModel = grammarModelList.
                            get(binding.viewPagerIdentifyingNouns.getCurrentItem());
                    var question = UtilityFunctions.getQuestionForGrammar(context
                            , currentModel,
                            category)[1];
                    tts.setTextViewAndSentence(null);
                    tts.initialize(
                            question
                            , GrammarActivity.this
                    );
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

    private void checkAnswer() {
        var currentModel = grammarModelList.
                get(binding.viewPagerIdentifyingNouns.getCurrentItem());
        var currentAnswer = currentModel.getWord()
                .toLowerCase(Locale.ROOT).trim();
        var currentDes = currentModel.getDescription()
                .toLowerCase(Locale.ROOT).trim();
        listener = text -> {
            Log.d("XXX", "checkAnswer: " + text + " " + currentAnswer);
            if (category.equals(getResources().getString(R.string.grammar_3))) {
                if (currentDes.contains(text)) {
                    playPauseAnimation(true);
                    mediaPlayer.start();
                    helperTTS(
                            UtilityFunctions.getCompliment(true)
                            , true
                            , 0
                    );
                } else {
                    playPauseAnimation(true);
                    tts.initialize(UtilityFunctions.getCompliment(false)
                            , this);
                }
                return;
            }
            if (text.equals(currentAnswer)) {
                playPauseAnimation(true);
                mediaPlayer.start();
                helperTTS(
                        UtilityFunctions.getCompliment(true)
                        , true
                        , 0
                );
            } else {
                playPauseAnimation(true);
                tts.initialize(UtilityFunctions.getCompliment(false)
                        , this);
            }
        };
    }

    private void helperTTS(String message, boolean canNavigate, int request) {
        try {
            ttsHelper = new TTSHelperAsyncTask().execute(new ConversionCallback() {
                        @Override
                        public void onCompletion() {
                            if (!canNavigate && request == REQUEST_INTRO) {
                                startSpeaking();
                                return;
                            }
                            if (canNavigate) {
                                mediaPlayer.pause();
                                if (binding.viewPagerIdentifyingNouns.getCurrentItem()
                                        == grammarModelList.size() - 1) {
                                    UtilityFunctions.runOnUiThread(() -> {
                                        playPauseAnimation(false);
                                        binding.playPause.setChecked(false);
                                        displayCompleteDialog();
                                    });
                                    return;
                                }
                                binding.viewPagerIdentifyingNouns
                                        .setCurrentItem(binding
                                                .viewPagerIdentifyingNouns.getCurrentItem() + 1);
                                setVisibilityOfLinearLayout(false);
                                startSpeaking();
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

    private void setOptionButton() {
        UtilityFunctions.runOnUiThread(() -> {
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
        });
    }

    private void setOptionButtonClick() {
        binding.key1.setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(binding.key1.getText().toString()
                        .toLowerCase(Locale.ROOT).trim());
        });
        binding.key2.setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(binding.key2.getText().toString()
                        .toLowerCase(Locale.ROOT).trim());
        });
        binding.key3.setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(binding.key3.getText().toString()
                        .toLowerCase(Locale.ROOT).trim());
        });
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
            var grammarModel = grammarModelList.
                    get(binding.viewPagerIdentifyingNouns.getCurrentItem());
            var currentFragment = (RowItemFragment) fragmentList.
                    get(binding.viewPagerIdentifyingNouns.getCurrentItem());

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

    @SuppressWarnings("deprecation")
    static class TTSHelperAsyncTaskAnim extends AsyncTask<ConversionCallback, Void, TextToSpeckConverter> {
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
    protected void onPause() {
        super.onPause();
        destroyEngine();
        checkProgressData();
        UtilityFunctions.checkProgressAvailable(progressDataBase, "English" + "Grammar", category, new Date(),
                timeSpend + Integer.parseInt(binding.layoutExtTimer.timeText.getText().toString()), false);
    }

    private void checkProgressData() {
        progressData = UtilityFunctions.checkProgressAvailable(progressDataBase, "English" + "Spelling", category,
                new Date(), 0, true);

        try {
            if (progressData != null) {
                timeSpend = progressData.get(0).time_spend;
            }
        } catch (Exception e) {
            timeSpend = 0;
        }

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

    public void restartEngine() {
        destroyEngine();
        initTTS();
        startSpeaking();
        setToggleButtonChecked(true);
        playPauseAnimation(true);
    }

    public void destroyEngine() {
        playPauseAnimation(false);
        setToggleButtonChecked(false);
        setVisibilityOfLinearLayout(false);
        if (tts != null) {
            tts.destroy();
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        if (ttsHelper != null) {
            ttsHelper.destroy();
        }
        if (ttsHelperAnim != null) {
            ttsHelperAnim.destroy();
        }
    }

    private void initTTSHelperAnim() throws ExecutionException, InterruptedException {
        ttsHelperAnim = new TTSHelperAsyncTaskAnim().execute(new ConversionCallback[]{new ConversionCallback() {
            @Override
            public void onCompletion() {

                Log.i("Index_Size", num + "");
                if (num < animEng.size()) {

                    UtilityFunctions.runOnUiThread(() -> {
                        ttsHelperAnim.initialize(animEng.get(num).getDescription(), GrammarActivity.this);
                        animHandel(animEng.get(num).getAnswer(), animEng.get(num).getDescription(), animEng.get(num).getOperation());
                        num++;
                    }, 500);
                } else {
                    num = 0;
                    PrefConfig.writeBooleanInPref(context, true, category);
                    if (alertDialog != null && alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                }
            }

            @Override
            public void onErrorOccurred(String errorMessage) {
                ttsHelperAnim.destroy();
            }
        }}).get();
    }


    private void displayTutorialAnimation() throws ExecutionException, InterruptedException {

        animEng = AnimationUtil.animGrammar(category, context);

        alert = new AlertDialog.Builder(GrammarActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.tutorial_anim, null);

        num = 0;
        ImageView closeButton = mView.findViewById(R.id.closeButton);
        addAnimLayout = mView.findViewById(R.id.insert_point);
        finalText = mView.findViewById(R.id.finalAns);
        finalView = mView.findViewById(R.id.finalView);
        descTextView = mView.findViewById(R.id.descTextView);

        initTTSHelperAnim();

        alert.setView(mView);
        alertDialog = alert.create();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        var getTitle = (category.equals(getResources().getString(R.string.grammar_1))) ?
                "Noun" : category;
        String initText = "Hi kids, Let us learn about " + getTitle;

        descTextView.setText(initText);
        ttsHelperAnim.initialize(initText, GrammarActivity.this);
        try {
            alertDialog.show();
        } catch (Exception ignored) {

        }

        closeButton.setOnClickListener(v -> {
            alertDialog.dismiss();
            ttsHelperAnim.stop();
        });

        alertDialog.setOnDismissListener(dialog -> {
            setIntro();
            ttsHelperAnim.stop();
            ttsHelperAnim.destroy();
        });

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
                    navigateToTest();
                    break;
            }
        });


        hintDialog.show();

    }


    private void animHandel(String answer, String description, String operation) {

        slideLeftAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_left);
        slideRightAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.anim_single_layout, null);
        AnimSingleLayoutBinding binding = AnimSingleLayoutBinding.bind(view);

        binding.description.setText(description);
        binding.slNumView.setText((num + 1) + ".");

        binding.mathLayout.setVisibility(View.GONE);
        view.startAnimation(slideRightAnim);
        ViewGroup main = (ViewGroup) addAnimLayout;
        main.addView(view, num);

    }

    private void navigateToTest() {
        Intent intent = new Intent(this, GrammarTestActivity.class);
        intent.putExtra(EXTRA_GRAMMAR_CATEGORY, category);
        startActivity(intent);
    }

    private void timer() {
        boolean isTimerRunning = false;
        Observable.interval(60, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    public void accept(Long x) throws Exception {
                        // update your view here

                        binding.layoutExtTimer.timerProgress.setMax(15);
                        binding.layoutExtTimer.timerProgress.setProgress(Integer.parseInt((x + 1) + ""));
                        binding.layoutExtTimer.timeText.setText((x + 1) + "");
                        Log.i("task", x + "");
                    }
                })
                .takeUntil(aLong -> aLong == TIMER_VALUE)
                .doOnComplete(() ->
                        // do whatever you need on complete
                        Log.i("TSK", "task")
                ).subscribe();
    }


    interface ButtonClick {
        void onClick(String text);
    }
}