package com.maths.beyond_school_280720220930.english_activity.grammar;

import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_GRAMMAR_CATEGORY;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_ONLINE_FLAG;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_QUESTION_FOR_TEST;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_TITLE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
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
import com.maths.beyond_school_280720220930.retrofit.ApiClient;
import com.maths.beyond_school_280720220930.retrofit.ApiInterface;
import com.maths.beyond_school_280720220930.retrofit.model.content.ContentModel;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.AnimationUtil;
import com.maths.beyond_school_280720220930.utils.CollectionUtils;
import com.maths.beyond_school_280720220930.utils.Constants;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

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

    private boolean isOnline = false;

    private ContentModel.Meta meta = null;
    private String kidsGrade;
    private String kidsId;
    private int kidAge;
    private String kidName;
    private String logs = "";
    private long startTime = 0;
    private FirebaseAnalytics analytics;
    private FirebaseAuth auth;
    private String parentsContactId;

    private boolean isTimerRunning = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGrammarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        englishGradeDatabase = EnglishGradeDatabase.getDbInstance(context);
        progressDataBase = ProgressDataBase.getDbInstance(this);
        progressData = new ArrayList<>();


        kidAge = UtilityFunctions.calculateAge(PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_dob)));
        kidsId = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_id));
        kidName = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_name));
        parentsContactId = PrefConfig.readIdInPref(context, getResources().getString(R.string.parent_contact_details));
        auth = FirebaseAuth.getInstance();
        analytics = FirebaseAnalytics.getInstance(this);
        initMediaPlayer();
        setToolbar();
        getDataFromIntent();
    }

    private void firstOpen() {
        var isOpen = !PrefConfig.readBooleanInPref(context, category);
        var isVisible = !category.equals(getResources().getString(R.string.grammar_2));
        binding.hintButton.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        if (isOpen && isVisible) displayTutorialDialog();
        else setIntro();
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
        if (mediaPlayer == null) mediaPlayer = UtilityFunctions.playClapSound(this);
        mediaPlayer = UtilityFunctions.playClapSound(this);
    }

    private void getDataFromIntent() {
        if (!getIntent().hasExtra(EXTRA_GRAMMAR_CATEGORY)) {
            UtilityFunctions.simpleToast(context, "No data found");
        } else {
            category = getIntent().getStringExtra(EXTRA_GRAMMAR_CATEGORY);
            binding.textViewCategory.setText(category.replace("Grammar", ""));
            isOnline = getIntent().getBooleanExtra(EXTRA_ONLINE_FLAG, false);
            if (isOnline) {
                getSubjectData();
            } else {
                firstOpen();
                setViewPager();
                displayDialog();
            }
            handleTestButtonClick();
        }
    }

    private void getSubjectData() {
        Retrofit retrofit = ApiClient.getClient();
        var api = retrofit.create(ApiInterface.class);
        api.getVocabularySubject(PrefConfig.readIdInPref(context, getResources().getString(R.string.kids_grade)).toLowerCase().replace(" ", ""), "english", getIntent().getStringExtra(EXTRA_TITLE).toLowerCase(), category).enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(Call<ContentModel> call, Response<ContentModel> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.body() != null) {
                    Log.d(TAG, "onResponse: " + response.body().getContent().toString());
                    setData(response.body().getContent());
                    meta = response.body().getMeta();
                    firstOpen();
                    displayDialog();
                }
            }

            @Override
            public void onFailure(Call<ContentModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void setData(List<ContentModel.Content> content) {
        var typeConverter = new GrammarTypeConverter();
        grammarModelList = typeConverter.mapToList(content);
        setViewPager();
    }

    private void setViewPager() {
        if (!isOnline) if (getDataFromOfflineSource()) return;

        fragmentList = mapToFragment(grammarModelList);
        var pagerAdapter = new EnglishViewPager(fragmentList, getSupportFragmentManager(), getLifecycle());
        binding.viewPagerIdentifyingNouns.setAdapter(pagerAdapter);
        binding.viewPagerIdentifyingNouns.setUserInputEnabled(false);
        setButton();
        setOptionButtonClick();
    }

    private boolean getDataFromOfflineSource() {
        var list = englishGradeDatabase.grammarDao().getAllGrammar();
        grammarModelList = getFilterGrammar(list);
        if (grammarModelList == null) {
            UtilityFunctions.simpleToast(context, "No data found");
            return true;
        }
        return false;
    }

    private void setButton() {
        binding.playPause.setOnClickListener(v -> {
            if (binding.playPause.isChecked()) {
                initTTS();
                timer();

                startSpeaking();
            } else destroyEngine();
        });
    }


    private void setIntro() {
        initTTS();
        timer();
        playPauseAnimation(true);
        setToggleButtonChecked(true);
        helperTTS(!isOnline ? UtilityFunctions.getIntroForGrammar(context, category) : "", false, REQUEST_INTRO);
    }

    private void startSpeaking() {

        startTime = new Date().getTime();
        var currentModel = grammarModelList.get(binding.viewPagerIdentifyingNouns.getCurrentItem());
        var question = !isOnline ? UtilityFunctions.getQuestionForGrammar(context, currentModel, category)[0] : meta.getQuestion();
        var des = currentModel.getDescription().trim();
        if (binding.viewPagerIdentifyingNouns.getCurrentItem() < 0 && !isOnline)
            tts.setTextViewAndSentence(question);

        if (isOnline) {
            if (binding.viewPagerIdentifyingNouns.getCurrentItem() <= 0)
                helperTTS(question, false, 44 * 4);
            else helperTTS("", false, 44 * 4);

        } else {
            if (binding.viewPagerIdentifyingNouns.getCurrentItem() <= 0)
                tts.initialize(question, this);
            else tts.initialize("", this);
        }


    }


    private void initTTS() {
        var task = new TTSAsyncTask();
        try {
            tts = task.execute(new ConversionCallback() {
                @Override
                public void onCompletion() {
                    Log.d(TAG, "tts: called");
                    if (!tts.getTextRanceListener()) {
                        UtilityFunctions.runOnUiThread(() -> {
                            var currentModel = grammarModelList.get(binding.viewPagerIdentifyingNouns.getCurrentItem());
                            var currentFragment = (RowItemFragment) fragmentList.get(binding.viewPagerIdentifyingNouns.getCurrentItem());
                            if (category.equals(getResources().getString(R.string.grammar_3)))
                                currentFragment.getTextView().setText(Html.fromHtml("<font color='#64c1c7'>" + currentModel.getWord() + "</font>\n", Html.FROM_HTML_MODE_COMPACT));
                            else
                                currentFragment.getTextView().setText(Html.fromHtml(currentModel.getDescription(), Html.FROM_HTML_MODE_COMPACT));

                        });
                        setOptionButton();
                        setVisibilityOfLinearLayout(true);
                        playPauseAnimation(false);
                        checkAnswer();
                        Log.d(TAG, "onCompletion: Called ");
                        return;
                    }
                    Log.d(TAG, "else: called");
                    var currentModel = grammarModelList.get(binding.viewPagerIdentifyingNouns.getCurrentItem());
                    var question = !isOnline ? UtilityFunctions.getQuestionForGrammar(context, currentModel, category)[1] : "Select the correct answer";
                    tts.setTextViewAndSentence(null);
                    tts.initialize(question, GrammarActivity.this);
                }

                @Override
                public void onErrorOccurred(String errorMessage) {
                    Log.e(TAG, "onErrorOccurred: " + errorMessage);
                }

                @Override
                public void getLogResult(String title) {
                    ConversionCallback.super.getLogResult(title);
                    Log.d(TAG, "getLogResult: " + title);
                }
            }).get();

            tts.setTextRangeListener((utteranceId, sentence, start, end, frame) -> {
                UtilityFunctions.runOnUiThread(() -> {
                    var textView = (TextView) this.findViewById(R.id.text_view_des_grammar);
                    if (textView != null) {
                        var s = grammarModelList.get(binding.viewPagerIdentifyingNouns.getCurrentItem()).getDescription()
                                .replace("<b>", "").replace("</b>", "")
                                .replace("blank", "_____").replace("<br>", "\n");
                        Spannable textWithHighlights = new SpannableString(s);
                        textWithHighlights.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.primary)), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        textView.setText(textWithHighlights);
                    }
                });
            });

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void checkAnswer() {
        var endTime = new Date().getTime();
        var diff = endTime - startTime;
        var currentModel = grammarModelList.get(binding.viewPagerIdentifyingNouns.getCurrentItem());
        var currentAnswer = currentModel.getWord().toLowerCase(Locale.ROOT).trim();
        var currentDes = currentModel.getDescription().toLowerCase(Locale.ROOT).trim();
        listener = text -> {
            Log.d("XXX", "checkAnswer: " + text + " " + currentAnswer);
            if (category.equals(getResources().getString(R.string.grammar_3))) {

                if (currentDes.contains(text)) {
                    playPauseAnimation(true);
                    mediaPlayer.start();
                    sendDataToAnalytics(currentDes, text, diff, true);
                    helperTTS(UtilityFunctions.getCompliment(true), true, 0);
                } else {
                    sendDataToAnalytics(currentDes, text, diff, false);
                    playPauseAnimation(true);
                    tts.initialize(UtilityFunctions.getCompliment(false), this);
                }
                return;
            }
            if (text.equals(currentAnswer)) {
                playPauseAnimation(true);
                try {
                    if (mediaPlayer != null) mediaPlayer.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                sendDataToAnalytics(currentDes, text, diff, true);
                helperTTS(UtilityFunctions.getCompliment(true), true, 0);
            } else {
                sendDataToAnalytics(currentDes, text, diff, false);
                playPauseAnimation(true);
                tts.initialize(UtilityFunctions.getCompliment(false), this);
            }
        };
    }

    private void helperTTS(String message, boolean canNavigate, int request) {
        try {
            ttsHelper = new TTSHelperAsyncTask().execute(new ConversionCallback() {
                @Override
                public void onCompletion() {
                    if (!canNavigate && request == 44 * 4) {
                        var currentModel = grammarModelList.get(binding.viewPagerIdentifyingNouns.getCurrentItem());
                        var des = currentModel.getDescription().trim().replace("<br>", "").replace("<b>", "").replace("</b>", "").replace("_____", "blank");
                        tts.setTextViewAndSentence(des);
                        tts.initialize(des, GrammarActivity.this);
                        return;
                    }
                    if (!canNavigate && request == REQUEST_INTRO) {
                        startSpeaking();
                        return;
                    }
                    if (canNavigate) {
                        mediaPlayer.pause();
                        if (binding.viewPagerIdentifyingNouns.getCurrentItem() == grammarModelList.size() - 1) {
                            UtilityFunctions.runOnUiThread(() -> {
                                playPauseAnimation(false);
                                binding.playPause.setChecked(false);
                                displayCompleteDialog();
                            });
                            return;
                        }
                        UtilityFunctions.runOnUiThread(() -> {
                            binding.viewPagerIdentifyingNouns.setCurrentItem(binding.viewPagerIdentifyingNouns.getCurrentItem() + 1);
                        });
                        setVisibilityOfLinearLayout(false);
                        startSpeaking();
                    }
                }

                @Override
                public void onErrorOccurred(String errorMessage) {

                }
            }).get().initialize(message, this);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void setToolbar() {
        binding.toolBar.imageViewBack.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.toolBar.titleText.setText((getIntent().hasExtra(EXTRA_TITLE)) ? getIntent().getStringExtra(EXTRA_TITLE) : getResources().getString(R.string.grammar));
        binding.textViewCategory.setText(getIntent().getStringExtra(EXTRA_GRAMMAR_CATEGORY).replace("Grammar", ""));

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
            var currentModel = grammarModelList.get(binding.viewPagerIdentifyingNouns.getCurrentItem());
            var extra = currentModel.getExtra();
            var split = extra.split(",");
            for (String s : split) {
                Log.d("XXX", "setOptionButton: " + s);
            }
            if (split.length <= 1) {
                UtilityFunctions.simpleToast(context, "No data found");
                return;
            }
            if (split.length == 2) {
                binding.key1.setText(split[0].trim());
                binding.key2.setText(split[1].trim());
                binding.key3.setVisibility(View.GONE);
                binding.key4.setVisibility(View.GONE);
                binding.linearLayout.setWeightSum(2);
            }
            if (split.length == 3) {
                binding.key1.setText(split[0].trim());
                binding.key2.setText(split[1].trim());
                binding.key3.setText(split[2].trim());
                binding.linearLayout.setWeightSum(3);
                binding.key3.setVisibility(View.VISIBLE);
                binding.key4.setVisibility(View.GONE);
            }
            if (split.length == 4) {
                binding.key1.setText(split[0].trim());
                binding.key2.setText(split[1].trim());
                binding.key3.setText(split[2].trim());
                binding.key4.setText(split[3].trim());
                binding.linearLayout.setWeightSum(4);
                binding.key3.setVisibility(View.VISIBLE);
                binding.key4.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setOptionButtonClick() {
        binding.key1.setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(binding.key1.getText().toString().toLowerCase(Locale.ROOT).trim());
        });
        binding.key2.setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(binding.key2.getText().toString().toLowerCase(Locale.ROOT).trim());
        });
        binding.key3.setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(binding.key3.getText().toString().toLowerCase(Locale.ROOT).trim());
        });
        binding.key4.setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(binding.key4.getText().toString().toLowerCase(Locale.ROOT).trim());
        });
    }


    private void setToggleButtonChecked(boolean isChecked) {
        UtilityFunctions.runOnUiThread(() -> binding.playPause.setChecked(isChecked));
    }

    private void playPauseAnimation(Boolean play) {
        UtilityFunctions.runOnUiThread(() -> {
            if (play) binding.imageViewTeacher.playAnimation();
            else binding.imageViewTeacher.pauseAnimation();
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

    @SuppressWarnings("deprecation")
    static class TTSHelperAsyncTaskAnim extends AsyncTask<ConversionCallback, Void, TextToSpeckConverter> {
        @Override
        protected TextToSpeckConverter doInBackground(ConversionCallback... conversionCallbacks) {
            return TextToSpeechBuilder.builder(conversionCallbacks[0]);
        }
    }


    private List<Fragment> mapToFragment(List<GrammarModel> grammarModels) {
        return CollectionUtils.mapWithIndex(grammarModels.stream(), (index, item) -> new RowItemFragment(item, index + 1, category, isOnline)).collect(Collectors.toList());
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
        destroyMediaPlayer();
        UtilityFunctions.checkProgressAvailable(progressDataBase, "English" + (isOnline ? getIntent().getStringExtra(EXTRA_TITLE) : "Grammar"), category, new Date(), timeSpend + Integer.parseInt(binding.layoutExtTimer.timeText.getText().toString()), false);
    }

    private void checkProgressData() {
        progressData = UtilityFunctions.checkProgressAvailable(progressDataBase, "English" + (isOnline ? getIntent().getStringExtra(EXTRA_TITLE) : "Grammar"), category, new Date(), 0, true);

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
        destroyMediaPlayer();
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
        if (ttsHelper != null) {
            ttsHelper.destroy();
        }
        if (ttsHelperAnim != null) {
            ttsHelperAnim.destroy();
        }
    }

    private void destroyMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void initTTSHelperAnim() throws ExecutionException, InterruptedException {
        ttsHelperAnim = new TTSHelperAsyncTaskAnim().execute(new ConversionCallback[]{new ConversionCallback() {
            @Override
            public void onCompletion() {

                Log.i("Index_Size", num + "");
                if (num < animEng.size()) {

                    UtilityFunctions.runOnUiThread(() -> {
                        ttsHelperAnim.initialize(animEng.get(num).getDescription().replace("<b>", "").replace("</b>", ""), GrammarActivity.this);
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

        if (isOnline && meta.getHint() == null) {
            binding.hintButton.setVisibility(View.GONE);
            setIntro();
            return;
        }
        animEng = !isOnline ? AnimationUtil.animGrammar(category, context) : AnimationUtil.animGrammar(meta);

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
        var getTitle = (category.equals(getResources().getString(R.string.grammar_1))) ? "Noun" : category.replace("Grammar", "");
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

        binding.description.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT));
        binding.slNumView.setText((num + 1) + ".");

        binding.mathLayout.setVisibility(View.GONE);
        view.startAnimation(slideRightAnim);
        ViewGroup main = (ViewGroup) addAnimLayout;
        main.addView(view, num);

    }

    private void navigateToTest() {
        Intent intent = new Intent(this, GrammarTestActivity.class);
        intent.putExtra(EXTRA_GRAMMAR_CATEGORY, category);
        if (isOnline) {
            intent.putExtra(Constants.EXTRA_ONLINE_FLAG, true);
            intent.putExtra(EXTRA_TITLE, getIntent().getStringExtra(EXTRA_TITLE));
            intent.putExtra(EXTRA_QUESTION_FOR_TEST, meta.getQuestion());
        }
        startActivity(intent);
    }


    private void sendDataToAnalytics(String currentWord, String result, long diff, boolean b) {
        UtilityFunctions.sendDataToAnalytics(analytics, Objects.requireNonNull(auth.getCurrentUser()).getUid(), kidsId, kidName, "English-Practice-" + (!isOnline ? "grammar" : getIntent().getStringExtra(EXTRA_TITLE).toLowerCase()), kidAge, currentWord, result, b, (int) (diff), UtilityFunctions.getQuestionForGrammarTest(context, category), "English", parentsContactId);
    }

    private void timer() {
        if (isTimerRunning) {
            return;
        }

        Observable.interval(60, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).doOnNext(new Consumer<Long>() {
            public void accept(Long x) throws Exception {
                // update your view here

                binding.layoutExtTimer.timerProgress.setMax(15);
                binding.layoutExtTimer.timerProgress.setProgress(Integer.parseInt((x + 1) + ""));
                binding.layoutExtTimer.timeText.setText((x + 1) + "");
                isTimerRunning = true;
                Log.i("task", x + "");
            }
        }).takeUntil(aLong -> aLong == TIMER_VALUE).doOnComplete(() ->
                // do whatever you need on complete
                Log.i("TSK", "task")).subscribe();
    }


    interface ButtonClick {
        void onClick(String text);
    }
}