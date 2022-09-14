package com.maths.beyond_school_280720220930.english_activity.spelling;

import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_SPELLING_DETAIL;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.color.MaterialColors;
import com.maths.beyond_school_280720220930.LogActivity;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_280720220930.database.english.spelling.SpellingDao;
import com.maths.beyond_school_280720220930.database.english.spelling.model.SpellingCategoryModel;
import com.maths.beyond_school_280720220930.database.english.spelling.model.SpellingDetail;
import com.maths.beyond_school_280720220930.database.log.LogDatabase;
import com.maths.beyond_school_280720220930.database.process.ProgressDataBase;
import com.maths.beyond_school_280720220930.database.process.ProgressM;
import com.maths.beyond_school_280720220930.databinding.ActivityEnglishSpellingBinding;
import com.maths.beyond_school_280720220930.dialogs.HintDialog;
import com.maths.beyond_school_280720220930.english_activity.spelling.spelling_test.SpellingTest;
import com.maths.beyond_school_280720220930.english_activity.vocabulary.EnglishViewPager;
import com.maths.beyond_school_280720220930.translation_engine.ConversionCallback;
import com.maths.beyond_school_280720220930.translation_engine.SpeechToTextBuilder;
import com.maths.beyond_school_280720220930.translation_engine.TextToSpeechBuilder;
import com.maths.beyond_school_280720220930.translation_engine.translator.SpeechToTextConverterSpelling;
import com.maths.beyond_school_280720220930.translation_engine.translator.TextToSpeckConverter;
import com.maths.beyond_school_280720220930.utils.CollectionUtils;
import com.maths.beyond_school_280720220930.utils.Soundex;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

@RequiresApi(api = Build.VERSION_CODES.N)
public class EnglishSpellingActivity extends AppCompatActivity {

    private static final String TAG = EnglishSpellingActivity.class.getSimpleName();
    private static final int MAX_TRY = 5 /* Giver u three chance */;
    private static final int DELAY_TIME = 800;
    private final int REQUEST_FOR_DES = 345 * 34;
    private final int REQUEST_FOR_QUESTION = 345 * 35;


    private int replaceChar=0;
    private Boolean isSpeaking = false;
    private Boolean isSayWordFinish = true;
    private int currentTryCount = 0;

    private ActivityEnglishSpellingBinding binding;
    private UtilityFunctions.Spellings spellings = null;
    private TextToSpeckConverter tts = null;
    private TextToSpeckConverter ttsHelper = null;
    private SpeechToTextConverterSpelling stt = null;
    private MediaPlayer mediaPlayer = null;
    private LogDatabase logDatabase;
    private String logs = "";
    private long startTime = 0, endTime = 0;
    private StringBuilder currentWordBuilder = new StringBuilder();
    private SpellingDao dao;
    private List<SpellingDetail> spellingDetails;
    private List<Fragment> fragments;
    private int currentWordPosition = 0;
    private long timeSpend = 0;
    public static final int TIMER_VALUE = 15;
    private List<ProgressM> progressData;
    private ProgressDataBase progressDataBase;
    private Map<String,List<String>> words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEnglishSpellingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        logDatabase = LogDatabase.getDbInstance(this);
        progressDataBase = ProgressDataBase.getDbInstance(this);
        progressData = new ArrayList<>();
        words=UtilityFunctions.phonetics();
        setToolbar();
        setData();
    }


    private void setData() {
        if (getIntent().hasExtra(EXTRA_SPELLING_DETAIL)) {
            spellings = UtilityFunctions.getSpellingsFromString(getIntent().getStringExtra(EXTRA_SPELLING_DETAIL).trim());
            Log.i("Spellings",spellings+"");
            dao = EnglishGradeDatabase.getDbInstance(this).spellingDao();
            setViews();
            buttonClick();
            binding.giveTestButton.setOnClickListener((view) -> {
                navigateToTest();
            });
        } else {
            UtilityFunctions.simpleToast(this, "No data found");
        }
    }

    private void navigateToTest() {
        var intent = new Intent(this, SpellingTest.class);
        intent.putExtra(EXTRA_SPELLING_DETAIL, getIntent().getStringExtra(EXTRA_SPELLING_DETAIL).trim());
        startActivity(intent);
    }

    private void buttonClick() {
        binding.playPause.setOnClickListener(view -> {
            isSpeaking = binding.playPause.isChecked();
            startEngine();
            timer();
        });

    }

    private void playPauseAnimation(Boolean play) {
        if (play)
            binding.imageViewTeacher.playAnimation();
        else
            binding.imageViewTeacher.pauseAnimation();
    }

    private void startEngine() {
        if (binding.playPause.isChecked()) {
            try {
                initTTS();
                intSTT();
                initMediaPlayer();
                startSpeaking();

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            destroyedEngines();
            isSayWordFinish = true;
        }
    }

    private void setViews() {
        binding.subSub.setText(UtilityFunctions.getDBNameSpelling(spellings, this).replace("Spelling", "").trim());
        setPager();
    }

    private void setPager() {
        Log.d("XXX", "setPager: " + spellings + " db name " + UtilityFunctions.getDBNameSpelling(spellings, this));
        var data = getSpellingFromType(dao.getSpellingModel(1).getSpelling(),
                UtilityFunctions.getDBNameSpelling(spellings, this).replace("Spelling", "").trim());

        if (data == null) {
            UtilityFunctions.simpleToast(this, "No data found");
            return;
        }
        List<Fragment> fragments = getFragments(data);

        var pagerAdapter = new EnglishViewPager(
                fragments,
                getSupportFragmentManager(),
                getLifecycle()
        );

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
            helperTTS("Hi kids !! Let us, learn spelling of some"
                            + UtilityFunctions.getDBNameSpelling(spellings, this).replace("Spelling", "")
                    ,
                    false
                    , REQUEST_FOR_QUESTION);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    private String getQuestion() {
        return "The " + UtilityFunctions.
                convertCardinalNumberToOrdinalNumber(binding.viewPager.getCurrentItem() + 1)
                + " word is " +
                (spellingDetails.get(binding.viewPager.getCurrentItem()).getWord().equals("The")
                        ? "'di'" :
                        "'" + spellingDetails.get(binding.viewPager.getCurrentItem()).getWord() + "'"
                ) + ".\n\n"
                + "We spell it as "
                ;
    }

    private String getDescription() {
        return spellingDetails.get(binding.viewPager.getCurrentItem()).getDescription();
    }

    private String getSpellLetter() {
        return "IT's your turn to spell the word " + (spellingDetails.get(binding.viewPager.getCurrentItem()).getWord().equals("The")
                ? "'di'" :
                "'" + spellingDetails.get(binding.viewPager.getCurrentItem()).getWord()) + "' .";
    }

    private List<Fragment> getFragments(SpellingCategoryModel data) {
        spellingDetails = data.getDetails();
        fragments = CollectionUtils.
                mapWithIndex(
                        spellingDetails.stream(), (index, item) ->
                                new SpellingFragment(item, index + 1)).collect(Collectors.toList()
                );
        return fragments;
    }

    public static SpellingCategoryModel getSpellingFromType(List<SpellingCategoryModel> models, String type) {
        var filterList = models.stream().filter(model -> model.getCategory().equals(type)).collect(Collectors.toList());
        if (filterList.size() > 0) {
            return filterList.get(0);
        } else {
            return null;
        }
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
                        removeHighlight();
                        tts.setTextViewAndSentence(null);
                        tts.initialize(getSpellLetter(),
                                EnglishSpellingActivity.this);
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
                var textView = (TextView) this.findViewById(R.id.text_view_word);
                if (textView != null) {
                    Spannable textWithHighlights = new SpannableString(sentence);
                    textWithHighlights.setSpan(new ForegroundColorSpan(
                                    ContextCompat.
                                            getColor(
                                                    this,
                                                    R.color.primary
                                            )),
                            start,
                            end,
                            Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    textView.setText(textWithHighlights);
                }
            });
        });
    }

    private void removeHighlight() {
        var textView = (TextView) findViewById(R.id.text_view_word);
        Spannable textWithHighlights = new SpannableString(textView.getText().toString());
        textWithHighlights.setSpan(new ForegroundColorSpan(
                        ContextCompat.
                                getColor(
                                        EnglishSpellingActivity.this,
                                        android.R.color.primary_text_light
                                )),
                0,
                textView.getText().toString().length(),
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        textView.setText(textWithHighlights);
    }

    private void startSpeaking() throws ExecutionException, InterruptedException {
        isSpeaking = true;
        UtilityFunctions.runOnUiThread(() -> playPauseAnimation(true));
        helperTTS(getQuestion(), false, 3 * 45);
//        ttsHelper.setTextViewAndSentence(UtilityFunctions.addComma(spellingDetails.get(binding.viewPager.getCurrentItem()).getWord()));
//        Question
        logs += "Question : " + spellingDetails.get(binding.viewPager.getCurrentItem()).getWord() + " : " + spellingDetails.get(binding.viewPager.getCurrentItem()).getDescription() + ". \n";

//        Stop when reach ot last item
        if (binding.viewPager.getCurrentItem() == (spellingDetails.size() - 1)) {
            isSpeaking = false;
            //       displayCompleteDialog();
        }
    }


    //    TODO : STT is here
    private void intSTT() throws ExecutionException, InterruptedException {
        var task = new STTAsyncTask();

        stt = task.execute(new ConversionCallback() {

            @Override
            public void onPartialResult(String result) {
                ConversionCallback.super.onPartialResult(result);
//                checkResult(result);
//                UtilityFunctions.simpleToast(EnglishSpellingActivity.this, result);
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "onSuccess: " + result);
             //   checkResult(result);
            }

            @Override
            public void getArrayResult(List<String[]> list) {
                ConversionCallback.super.getArrayResult(list);

                try {
                    verifyResult(list);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCompletion() {
                var current = (SpellingFragment) fragments.get(binding.viewPager.getCurrentItem());
                current.getAnimationView().setVisibility(View.GONE);
            }

            @Override
            public void onErrorOccurred(String errorMessage) {
                Log.d(TAG, "onErrorOccurred: " + errorMessage);
                if (errorMessage.equals("No match")) {
                    Log.d(TAG, "onErrorOccurred: " + errorMessage);
                    UtilityFunctions.runOnUiThread(() -> {
                        startListening();
                    }, 400);
                }
            }

            @Override
            public void getLogResult(String title) {
                ConversionCallback.super.getLogResult(title);
                logs += title + "\n";
            }
        }).get();

    }




    public  int algoMatch(String[] item, List<String[]> st,int index){

        var currentWord = spellingDetails.get(binding.viewPager.getCurrentItem()).getWord().toLowerCase(Locale.ROOT);
        var currentWordArray = currentWord.toCharArray();
        var textView = ((SpellingFragment) fragments.get(binding.viewPager.getCurrentItem())).getTextView();
        var string = textView.getText().toString();
        Log.i("CurrentWordP",index+"");
        boolean flag=false;
        //item.size
        for (int k=0;k<st.get(0).length;k++){
            flag=false;
            //k
            try{
                List<String> s_list=words.get(item[index]);
                index++;
                for (String s : s_list){
                    for(int i=0;i<st.size();i++){
                        for(int j=0;j<st.get(i).length;j++) {
                            if (matchingSoundX(s, st.get(i)[j])) {
                                flag = true;
                                Log.i("Matching_Words",s+","+st.get(i)[j]);
                                //k
                                string = string.replaceFirst("_", String.valueOf(currentWordArray[item.length-replaceChar]));
                                replaceChar--;
                                textView.setText(string);
                                currentWordPosition++;
                                break;
                            }
                            else{
                                Log.i("Matching_WordsNon",s+","+st.get(i)[j]);
                            }
                        }
                        if (flag)
                            break;
                    }
                    if (flag)
                        break;
                }

            }catch (Exception e){}

        }

        Log.i("CurrentWordPos",currentWordPosition+"");
        return currentWordPosition;
    }

    private static boolean matchingSoundX(String st1,String st2){
        if(Soundex.getCode(st1).equals(Soundex.getCode(st2))){
            return true;
        }else
        {
            return false;
        }
    }

    private void verifyResult(List<String[]> list) throws ExecutionException, InterruptedException {

        if (currentTryCount > MAX_TRY) {
            try {
                helperTTS("No Problem. Let's go to next word ", true, 0);
                currentTryCount = 0;
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        endTime = new Date().getTime();
        long diff = endTime - startTime;
        var currentWord = spellingDetails.get(binding.viewPager.getCurrentItem()).getWord().toLowerCase(Locale.ROOT);
        String[] currentWordArray = currentWord.split("");

        var textView = ((SpellingFragment) fragments.get(binding.viewPager.getCurrentItem())).getTextView();
        var string = textView.getText().toString();

//        boolean flag=false;
//
//        for (int i=0;i<list.size();i++){
//
//            for (int j=0;j<currentWordArray.length;j++){
//
//                try{
//                if (!String.valueOf(currentWordArray[j]).equals(list.get(i)[j].toLowerCase(Locale.ROOT))){
//                    Log.i("Matching_N",(currentWordArray[j])+", "+(list.get(i)[j]));
//                    flag=false;
//                    break;
//                }else{
//                    flag=true;
//                    Log.i("Matching",(currentWordArray[j])+", "+(list.get(i)[j].toLowerCase(Locale.ROOT)));
//                    string = string.replaceFirst("_", String.valueOf(currentWordArray[j]));
//                    textView.setText(string);
//                }
//
//            if (flag==true)
//                break;
//        }catch (Exception e){
//                }
//            }
//            }

        if (replaceChar==0)
            replaceChar=currentWordArray.length;

        int result=algoMatch(currentWordArray,list,currentWordPosition);

        Log.i("ViewString",string);
        if (replaceChar==0){
        helperTTS(UtilityFunctions.getCompliment(true), true, 0);
//        textView.setText("");
        currentWordPosition=0;
        replaceChar=0;
        }
        else if (result < currentWordArray.length ){
            stt.initialize("",EnglishSpellingActivity.this);
            var current = (SpellingFragment) fragments.get(binding.viewPager.getCurrentItem());
            current.getAnimationView().setVisibility(View.VISIBLE);
        }
        else{
        Log.i("algoMatching_",currentWordPosition+","+result);
        helperTTS(UtilityFunctions.getCompliment(false), false, 0);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkResult(String result) {
        if (currentTryCount > MAX_TRY) {
            try {
                helperTTS("No Problem. Let's go to next word ", true, 0);
                currentTryCount = 0;
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        endTime = new Date().getTime();
        long diff = endTime - startTime;
        var currentWord = spellingDetails.get(binding.viewPager.getCurrentItem()).getWord().toLowerCase(Locale.ROOT);
        var currentWordArray = currentWord.toCharArray();


        var split = result.replace(" ", "").toCharArray();
        Log.d(TAG, "");
        if (split.length != 0) {
            if (split.length == 1) {
                if (currentWordArray[currentWordPosition] == (split[0])) {
                    logs += "Single Word :" + UtilityFunctions.formatTime(diff) + ", Correct" + split[0] + "\n";
                    currentWordBuilder.append(split[0]);
                    currentWordPosition++;

                    var textView = ((SpellingFragment) fragments.get(binding.viewPager.getCurrentItem())).getTextView();
                    var string = textView.getText().toString();
                    textView.setText(string.replaceFirst("_", String.valueOf(split[0])));
                    try {
                        if (currentWordPosition < currentWord.length())
                            helperTTS("", false, 2 * 45);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        helperTTS("", false, 2 * 45);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Log.d(TAG, "onSuccess: called");
                try {
                    for (int i = 0; i < split.length; i++) {
                        if (currentWordArray[0] == (split[0])) { // the or T H E  both true
                            if (i < currentWordArray.length) //
                                if (currentWordArray[i] == split[i]) {
                                    currentWordBuilder.append(split[i]);
                                    currentWordPosition++;
                                }
                        } else if (currentWordArray[currentWordPosition] == (split[i])) {
                            Log.d(TAG, "checkResult: " + "True");
                            if (i < currentWordArray.length) //
                                if (currentWordArray[currentWordPosition] == split[i]) {
                                    currentWordBuilder.append(split[i]);
                                    currentWordPosition++;
                                }
                            Log.d(TAG, "checkResult: " + currentWordBuilder);
                        }
                    }
                    var textView = ((SpellingFragment) fragments.get(binding.viewPager.getCurrentItem())).getTextView();
                    var string = textView.getText().toString();
                    for (int i = 0; i < currentWordBuilder.length(); i++) {
                        string = string.replaceFirst("_", String.valueOf(currentWordBuilder.charAt(i)));
                    }
                    textView.setText(string);
                    Log.d(TAG, "onSuccess: called " + currentWordBuilder.toString());
                    if (currentWordPosition < currentWord.length())
                        helperTTS("", false, 2 * 45);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (currentWord.length() == currentWordBuilder.length()) {
                try {
                    if (currentWordBuilder.toString().equals(currentWord)) {
                        try {
                            mediaPlayer.start();
                        } catch (Exception ignored) {
                        }
                        playPauseAnimation(true);
                        var textView = ((SpellingFragment) fragments.get(binding.viewPager.getCurrentItem())).getTextView();
                        var string = textView.getText().toString();
                        for (int i = 0; i < currentWordBuilder.length(); i++) {
                            string = string.replaceFirst("_", String.valueOf(currentWordBuilder.charAt(i)));
                        }
                        textView.setText(string);
                        helperTTS(UtilityFunctions.getCompliment(true), true, 0);
                        logs += "Time Take :" + UtilityFunctions.formatTime(diff) + ", Correct .\n";
                        currentWordBuilder = new StringBuilder();
                        currentWordPosition = 0;
                    } else {
                        playPauseAnimation(true);
                        helperTTS(UtilityFunctions.getCompliment(false), false, 0);
                        currentTryCount++;
                        currentWordBuilder = new StringBuilder();
                        var currentFragment = (SpellingFragment) fragments.get(binding.viewPager.getCurrentItem());
                        currentFragment.getTextView().setText("");
                        logs += "Time Take :" + UtilityFunctions.formatTime(diff) + ", Wrong .\n";
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (split.length > 1 && currentWord.length() < currentWordBuilder.length()) {
                try {
                    helperTTS(UtilityFunctions.getCompliment(false), false, 0);
                    currentTryCount++;
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } else {
            UtilityFunctions.runOnUiThread(() -> {
                startListening();
            }, 400);
        }
    }


    //    Helper method to start listening
    private void startListening() {
        UtilityFunctions.runOnUiThread(() -> {
            startTime = new Date().getTime();
            var current = (SpellingFragment) fragments.get(binding.viewPager.getCurrentItem());
            current.getAnimationView().setVisibility(View.VISIBLE);
            playPauseAnimation(false);
            stt.initialize("Start Listening", this);
        });

    }


    private void helperTTS(String message, boolean canNavigate, int request) throws
            ExecutionException, InterruptedException {
        ttsHelper = new TTSHelperAsyncTask().execute(new ConversionCallback() {
                    @Override
                    public void onCompletion() {
                        if (request == 3 * 46) {
                            try {
                                helperTTS("Example : ", false, REQUEST_FOR_DES);
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            return;
                        }

                        if (request == 3 * 45) {
                            try {
                                helperTTS(UtilityFunctions.addComma(spellingDetails.get(binding.viewPager.getCurrentItem()).getWord())
                                        , false, 3 * 46);

                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            return;
                        }

                        if (request == 2 * 45) {
                            UtilityFunctions.runOnUiThread(() -> {
                                var current = (SpellingFragment) fragments.get(binding.viewPager.getCurrentItem());
                                current.getAnimationView().setVisibility(View.VISIBLE);
                                stt.initialize("", EnglishSpellingActivity.this);
                            }, 800);
                            return;
                        }
                        if (request == REQUEST_FOR_QUESTION && !canNavigate) {
                            try {
                                startSpeaking();
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                        if (request == REQUEST_FOR_DES && !canNavigate) {
                            tts.setTextViewAndSentence(spellingDetails.get(binding.viewPager.getCurrentItem()).getDescription());
                            tts.initialize(getDescription(), EnglishSpellingActivity.this);
                            return;
                        }
                        if (canNavigate) {
                            UtilityFunctions.runOnUiThread(() -> {
                                try {
                                    if (mediaPlayer != null)
                                        mediaPlayer.pause();
                                } catch (IllegalStateException e) {
                                    e.printStackTrace();
                                }

                                var currentFrag=(SpellingFragment)fragments.get(binding.viewPager.getCurrentItem());
                                currentFrag.getTextView().setText(spellingDetails.get(binding.viewPager.getCurrentItem()).getWord().replaceAll("[A-Za-z]", " _ "));
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
                                    if (binding.viewPager.getCurrentItem() == (spellingDetails.size() - 1)) {
                                        displayCompleteDialog();
                                    }
                                    playPauseAnimation(false);
                                }
                            }, DELAY_TIME);
                        } else {
                            isSayWordFinish = false;
                            currentTryCount++;
                            tts.initialize("Spell the Word " + UtilityFunctions.addComma(spellingDetails.get(binding.viewPager.getCurrentItem()).getWord()), EnglishSpellingActivity.this);
                        }
                    }

                    @Override
                    public void onErrorOccurred(String errorMessage) {

                    }
                }).
                get().
                initialize(message, this);
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

    static class STTAsyncTask extends AsyncTask<ConversionCallback, Void, SpeechToTextConverterSpelling> {
        @Override
        protected SpeechToTextConverterSpelling doInBackground(ConversionCallback... conversionCallbacks) {
            return SpeechToTextBuilder.spellingBuilder(conversionCallbacks[0]);
        }
    }


    private void destroyedEngines() {
        binding.playPause.setChecked(false);
        playPauseAnimation(false);
        if (tts != null)
            tts.destroy();
        if (stt != null)
            stt.destroy();
        if (ttsHelper != null)
            ttsHelper.destroy();
        if (mediaPlayer != null)
            mediaPlayer.release();
        try {
            var current = (SpellingFragment) fragments.get(binding.viewPager.getCurrentItem());
            current.getAnimationView().setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        checkLogIsEnable();
        destroyedEngines();
        checkProgressData();
        UtilityFunctions.checkProgressAvailable(progressDataBase, "English" + "Spelling", UtilityFunctions.getDBNameSpelling(spellings, this), new Date(),
                timeSpend + Integer.parseInt(binding.timeText.getText().toString()), false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        destroyedEngines();
    }

    private void checkLogIsEnable() {
        if (PrefConfig.readIdInPref(getApplicationContext(), "IS_LOG_ENABLE").equals("true"))
            saveLog();
    }

    private void saveLog() {
        Log.d(TAG, "saveLog: Called " + logs);
        UtilityFunctions.saveLog(logDatabase, logs);
        logs="";
    }


    private void setToolbar() {
        binding.toolBar.titleText.setText(getResources().getString(R.string.spelling));
        binding.toolBar.imageViewBack.setOnClickListener(v -> onBackPressed());
        binding.toolBar.getRoot().inflateMenu(R.menu.log_menu);
        binding.toolBar.getRoot().setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.action_log) {
                startActivity(new Intent(getApplicationContext(), LogActivity.class));

                return true;
            }
            return super.onOptionsItemSelected(item);

        });

    }

    private void displayCompleteDialog() {

        HintDialog hintDialog = new HintDialog(EnglishSpellingActivity.this);
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

    public void restartEngine() {
        destroyedEngines();
        try {
            startSpeaking();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        isSpeaking=true;
        binding.playPause.setChecked(true);
    }

    private void timer() {
        boolean isTimerRunning = false;
        Observable.interval(60, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    public void accept(Long x) throws Exception {
                        // update your view here

                        binding.timerProgress.setMax(15);
                        binding.timerProgress.setProgress(Integer.parseInt((x + 1) + ""));
                        binding.timeText.setText((x + 1) + "");
                        Log.i("task", x + "");
                    }
                })
                .takeUntil(aLong -> aLong == TIMER_VALUE)
                .doOnComplete(() ->
                        // do whatever you need on complete
                        Log.i("TSK", "task")
                ).subscribe();
    }

    private void checkProgressData() {
        progressData = UtilityFunctions.checkProgressAvailable(progressDataBase, "English" + "Spelling", UtilityFunctions.getDBNameSpelling(spellings, this),
                new Date(), 0, true);

        try {
            if (progressData != null) {
                timeSpend = progressData.get(0).time_spend;
            }
        } catch (Exception e) {
            timeSpend = 0;
        }

    }
}