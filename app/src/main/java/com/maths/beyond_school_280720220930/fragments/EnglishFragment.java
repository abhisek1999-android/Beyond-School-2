package com.maths.beyond_school_280720220930.fragments;

import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_CATEGORY_ID;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_DATA;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_FLAG_HAVE_DATA;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_GRAMMAR_CATEGORY;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_IS_OPEN_FROM_LEARN;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_ONLINE_FLAG;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_OPEN_TYPE;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_SPELLING_DETAIL;
import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_TITLE;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.TabbedHomePage;
import com.maths.beyond_school_280720220930.ViewCurriculum;
import com.maths.beyond_school_280720220930.adapters.SectionSubSubjectRecyclerAdapter;
import com.maths.beyond_school_280720220930.adapters.SubjectRecyclerAdapter;
import com.maths.beyond_school_280720220930.adapters.SubjectRecyclerAdapterUpdated;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeData;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.database.grade_tables.Grades_data;
import com.maths.beyond_school_280720220930.databinding.FragmentEnglishBinding;
import com.maths.beyond_school_280720220930.dialogs.HintDialog;
import com.maths.beyond_school_280720220930.english_activity.grammar.GrammarActivity;
import com.maths.beyond_school_280720220930.english_activity.grammar.test.GrammarTestActivity;
import com.maths.beyond_school_280720220930.english_activity.spelling.EnglishSpellingActivity;
import com.maths.beyond_school_280720220930.english_activity.spelling.spelling_test.SpellingTest;
import com.maths.beyond_school_280720220930.extras.CustomProgressDialogue;
import com.maths.beyond_school_280720220930.model.SectionSubSubject;
import com.maths.beyond_school_280720220930.model.SubSubject;
import com.maths.beyond_school_280720220930.retrofit.ApiClientContent;
import com.maths.beyond_school_280720220930.retrofit.ApiInterfaceContent;
import com.maths.beyond_school_280720220930.retrofit.model.content_new.ContentModelNew;
import com.maths.beyond_school_280720220930.utils.Constants;
import com.maths.beyond_school_280720220930.utils.ScreenType;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EnglishFragment extends Fragment {

    private FragmentEnglishBinding binding;
    private static final String TAG = "EnglishFragment";
    private List<String> mathSub;
    private List<String> engSub;
    private String kidsGrade = "";
    private String kidsName = "";
    private GradeDatabase gradeDatabase;
    private List<Grades_data> subMathsData;
    private List<Grades_data> subEngData;
    private SubjectRecyclerAdapter subjectRecyclerAdapter;
    String[] math = {"Addition", "Subtraction", "Multiplication Tables", "Division"};
    String[] eng = {"Vocabulary"};
    private List<SubSubject> subMathList;
    private List<SubSubject> subEngList;
    private List<SectionSubSubject> sectionList;
    private int[] resMath = {R.drawable.ic_addition, R.drawable.ic_sub, R.drawable.ic_mul, R.drawable.ic_division};
    private int[] resEng = {R.drawable.ic_vocab_p, R.drawable.ic_spell_p, R.drawable.ic_spell_p, R.drawable.ic_spell_p};

    private String[] tableList;
    private SectionSubSubjectRecyclerAdapter sectionSubSubjectRecyclerAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private int startIndex = 0;
    private FirebaseFirestore kidsDb = FirebaseFirestore.getInstance();
    ActionBarDrawerToggle toggle;
    private int REQUEST_RECORD_AUDIO = 1;
    private List<String> chapterListEng;
    private List<String> chapterListMath;
    private FirebaseFirestore firebaseFirestore;
    private String subscriptionId = "";
    private CustomProgressDialogue customProgressDialogue;
    private BottomSheetBehavior mBottomSheetBehavior;
    private List<GradeData> subjectDataNew;
    private SubjectRecyclerAdapterUpdated subjectRecyclerAdapterUpdated;
    private String userType = "";
    private int noOfDays;
    private int trialPeriodDay;
    private String paymentStatus;
    private int paymentAmount;
    private String createAt;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentEnglishBinding.bind(view);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        subscriptionId = PrefConfig.readIdInPref(getContext(), getResources().getString(R.string.subscription_id));
        subjectDataNew = new ArrayList<>();
        startIndex = PrefConfig.readIntDInPref(getContext(), getResources().getString(R.string.alter_maths_value));
        userType = PrefConfig.readIdInPref(getContext(), getResources().getString(R.string.user_type));

        noOfDays = PrefConfig.readIntInPref(getContext(), getResources().getString(R.string.noOfdays), 0);
        trialPeriodDay = PrefConfig.readIntInPref(getContext(), getResources().getString(R.string.trial_period), 0);
        paymentStatus = PrefConfig.readIdInPref(getContext(), getResources().getString(R.string.payment_status));
        paymentAmount = PrefConfig.readIntDInPref(getContext(), getResources().getString(R.string.plan_value));
        createAt = PrefConfig.readIdInPref(getContext(), getResources().getString(R.string.created_at));
        customProgressDialogue = new CustomProgressDialogue(getContext());
        mathSub = new ArrayList<>();
        engSub = new ArrayList<>();

        subMathsData = new ArrayList<>();
        subEngData = new ArrayList<>();

        subMathList = new ArrayList<>();
        subEngList = new ArrayList<>();
        sectionList = new ArrayList<>();

        tableList = getResources().getStringArray(R.array.table_name);
        subjectRecyclerAdapter = new SubjectRecyclerAdapter(subMathsData, getContext());

        kidsGrade = PrefConfig.readIdInPref(getContext(), getResources().getString(R.string.kids_grade));

        kidsName = PrefConfig.readIdInPref(getContext(), getResources().getString(R.string.kids_name));
        gradeDatabase = GradeDatabase.getDbInstance(getContext());

        mathSub = Arrays.asList(math);
        engSub = Arrays.asList(eng);


        //TODO must take care
        setSubSubjectProgress();
        // uiChnages();

    }


    private void uiChnages() {

        userType = PrefConfig.readIdInPref(getContext(), getResources().getString(R.string.user_type));
        binding.evaluationTestTile.setVisibility(View.GONE);

//        binding.introTile.setOnClickListener(v -> {
//
//            startActivity(new Intent(getContext(), TestActivity.class));
//        });

        if (!userType.equals("old_user")) {
            binding.completeProgressTile.setVisibility(View.GONE);
        } else {
            binding.completeProgressTile.setVisibility(View.VISIBLE);
            binding.startExeButtonLayout.setVisibility(View.GONE);
        }


        int trialPeriod = PrefConfig.readIntInPref(getContext(), getResources().getString(R.string.trial_period), 0);
        int noOfDays = PrefConfig.readIntInPref(getContext(), getResources().getString(R.string.noOfdays), 0);
        if (PrefConfig.readIdInPref(getContext(), getResources().getString(R.string.payment_status)).equals("active")) {
            binding.trialPeriodText.setText("Your plan is activated");
            binding.trialPeriodText.setVisibility(View.GONE);
        } else if ((trialPeriod - UtilityFunctions.diffDate(createAt, new Date().toString())) == trialPeriod - 1) {
            binding.trialPeriodText.setText("Your trial period ends today ");
            binding.trialPeriodText.setVisibility(View.VISIBLE);
        } else if (UtilityFunctions.diffDate(createAt, new Date().toString()) < trialPeriod) {
            binding.trialPeriodText.setText("Your trial period ends in " + (trialPeriod - UtilityFunctions.diffDate(createAt, new Date().toString())) + " days");
            binding.trialPeriodText.setVisibility(View.VISIBLE);

        } else if (!PrefConfig.readIdInPref(getContext(), getResources().getString(R.string.payment_status)).equals("active")) {
            binding.trialPeriodText.setVisibility(View.VISIBLE);
            binding.trialPeriodText.setText("You don't have any valid plans");
            binding.trialPeriodText.setTextColor(getResources().getColor(R.color.red));
            binding.trialPeriodText.setCompoundDrawableTintList(ContextCompat.getColorStateList(getContext(), R.color.sweet_red));

        }


        binding.startExeButtonLayout.setOnClickListener(v -> {
            PrefConfig.writeIdInPref(getContext(), "old_user", getResources().getString(R.string.user_type));
            binding.nestedScrollView.setSmoothScrollingEnabled(true);
            binding.nestedScrollView.scrollTo(0, 0);
            binding.startExeButtonLayout.setVisibility(View.GONE);
            binding.completeProgressTile.setVisibility(View.VISIBLE);
            displayTrialPeriodDialog(false);
            //startActivity(new Intent(getContext(), ViewCurriculum.class));
        });

        binding.gotoViewCurriculumOne.setOnClickListener(v -> {

            if (!PrefConfig.readIdInPref(getContext(), getResources().getString(R.string.user_type)).equals("old_user"))
                displayTrialPeriodDialog(true);
            else
                startActivity(new Intent(getContext(), ViewCurriculum.class));

            PrefConfig.writeIdInPref(getContext(), "old_user", getResources().getString(R.string.user_type));
        });

        binding.gotoViewCurriculumTwo.setOnClickListener(v -> {
            if (!PrefConfig.readIdInPref(getContext(), getResources().getString(R.string.user_type)).equals("old_user"))
                displayTrialPeriodDialog(true);
            else
                startActivity(new Intent(getContext(), ViewCurriculum.class));


            PrefConfig.writeIdInPref(getContext(), "old_user", getResources().getString(R.string.user_type));
        });
        dateChecking();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //  Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_english, container, false);
    }


    private void displayTrialPeriodDialog(boolean gotoViewCurriculum) {
        int trialPeriod = PrefConfig.readIntInPref(getContext(), getResources().getString(R.string.trial_period), 0);
        HintDialog hintDialog = new HintDialog(getContext());
        hintDialog.setCancelable(true);
        hintDialog.setAlertTitle("Trial Period");
        hintDialog.setAlertDesciption(String.valueOf(Html.fromHtml("Your trial period is started from today and it will be end in <font color='#64c1c7'>" + trialPeriod + " Days.</font>", Html.FROM_HTML_MODE_COMPACT)));
        hintDialog.hideActionButton();
        hintDialog.hideCloseButton();
        if (gotoViewCurriculum)
            hintDialog.displaySubscribeButton("View Curriculum");
        hintDialog.setOnActionListener(viewId -> {

            switch (viewId.getId()) {

                case R.id.closeButton:
                    hintDialog.dismiss();
                    break;
                case R.id.gotoTextView:
                    if (gotoViewCurriculum)
                        startActivity(new Intent(getContext(), ViewCurriculum.class));
                    hintDialog.dismiss();
                    break;
            }
        });

        hintDialog.show();

    }


    private void dateChecking() {

        Log.d(TAG, "dateChecking: ");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (!PrefConfig.readIdInPref(getContext(), getResources().getString(R.string.alter_maths)).equals(simpleDateFormat.format(new Date()))) {
            // Added functionality when we got a new date
            try {
                UtilityFunctions.checkUpdatePaymentStatus(getContext(), subscriptionId, firebaseFirestore, mAuth);

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (PrefConfig.readIdInPref(getContext(), getResources().getString(R.string.timer_time)).equals("")) {
                //displayAddProfileAlertDialog();
            }
            PrefConfig.writeIdInPref(getContext(), simpleDateFormat.format(new Date()), getResources().getString(R.string.alter_maths));
            if (PrefConfig.readIntDInPref(getContext(), getResources().getString(R.string.alter_maths_value)) == 0) {
                PrefConfig.writeIntDInPref(getContext(), 2, getResources().getString(R.string.alter_maths_value));
                startIndex = 2;
            } else {
                PrefConfig.writeIntDInPref(getContext(), 0, getResources().getString(R.string.alter_maths_value));
                startIndex = 0;
            }
            getUiData(true);
            //  customProgressDialogue.show();
        } else {

            Log.i("List_data sd", PrefConfig.readNormalListInPref(getContext(), getResources().getString(R.string.saved_english_value)) + "");

            if (PrefConfig.readNormalListInPref(getContext(), getResources().getString(R.string.saved_english_value)).size() == 0) {
                getUiData(true);
                if (PrefConfig.readIdInPref(getContext(), getResources().getString(R.string.timer_time)).equals("")) {
                    // displayAddProfileAlertDialog();
                }
            } else
                getUiData(false);
            if (startIndex == 0) {
                PrefConfig.writeIntDInPref(getContext(), 0, getResources().getString(R.string.alter_maths_value));
            } else {
                PrefConfig.writeIntDInPref(getContext(), 2, getResources().getString(R.string.alter_maths_value));
            }
        }


    }


    private void getUiData(boolean isNewCall) throws ArrayIndexOutOfBoundsException, NullPointerException {

        subMathsData.clear();
        subEngData.clear();
        chapterListEng = new ArrayList<>();
        chapterListMath = new ArrayList<>();
        chapterListMath.clear();
        chapterListEng.clear();

        subjectRecyclerAdapter.notifyDataSetChanged();
        Log.d(TAG, "getUiData: ");

        if (isNewCall) {

            getDataFromDatabase(isNewCall);
            customProgressDialogue.dismiss();
            Log.d(TAG, "getUiData: if");

        } else {
            getDataFromDatabase(isNewCall);
            chapterListEng.clear();
            chapterListMath.clear();
            Log.d(TAG, "getUiData: else");

        }
    }
    //getting data from local db

    private void getDataFromDatabase(boolean isNewCall) {

        subjectDataNew.clear();

        Log.d("TAG", "getDataFromDatabase:call ");
        if (isNewCall) {
            if (eng.length <= 2) {
                try {
                    for (String st : eng) {
                        subjectDataNew.add(gradeDatabase.gradesDaoUpdated().getSubjectDataIncompleteFirst(st).get(0));
                        chapterListEng.add(gradeDatabase.gradesDaoUpdated().getSubjectDataIncompleteFirst(st).get(0).getId());
                        Log.d("XXXXX", "getDataFromDatabase: IF SIZE " + subjectDataNew);
//            gradeDatabase.gradesDaoUpdated().getSubjectDataLimited(st).observe(this, grades_data -> {
//                Log.d("XXX", "getDataFromDatabase: "+grades_data.size());
//                subjectRecyclerAdapterUpdated.submitList(grades_data);
//            });
                    }
                } catch (Exception e) {
                    Log.d(TAG, "getDataFromDatabase: Database not found");
                }
            } else {
                List<Integer> ls = new ArrayList();
                chapterListEng.clear();

                ls = UtilityFunctions.getRandomTwoIntegerUpto(eng.length, 0);
                Log.d("LIST_SIZE", "getDataFromDatabase: " + ls.size());
                for (int index : ls) {
                    try {
                        subjectDataNew.add(gradeDatabase.gradesDaoUpdated().getSubjectDataIncompleteFirst(eng[index]).get(0));
                        chapterListEng.add(gradeDatabase.gradesDaoUpdated().getSubjectDataIncompleteFirst(eng[index]).get(0).getId());
                        Log.d("XXXXX", "getDataFromDatabase:  ELSE SIZE" + subjectDataNew);
                    } catch (Exception e) {
                    }
                }

            }
            PrefConfig.writeNormalListInPref(getContext(), chapterListEng, getResources().getString(R.string.saved_english_value));

        } else {
            try {
                chapterListEng = PrefConfig.readNormalListInPref(getContext(), getResources().getString(R.string.saved_english_value));
                for (String st : chapterListEng) {
                    subjectDataNew.add(gradeDatabase.gradesDaoUpdated().getSubjectDataViaID(st).get(0));
                    Log.d("XXXX", "getDataFromDatabase: OLD " + subjectDataNew);
//            gradeDatabase.gradesDaoUpdated().getSubjectDataLimited(st).observe(this, grades_data -> {
//                Log.d("XXX", "getDataFromDatabase: "+grades_data.size());
//                subjectRecyclerAdapterUpdated.submitList(grades_data);
//            });
                }

            } catch (Exception e) {

                chapterListEng.clear();

                if (eng.length <= 2) {
                    for (String st : eng) {
                        subjectDataNew.add(gradeDatabase.gradesDaoUpdated().getSubjectDataIncompleteFirst(st).get(0));
                        chapterListEng.add(gradeDatabase.gradesDaoUpdated().getSubjectDataIncompleteFirst(st).get(0).getId());
                        Log.d(TAG, "getDataFromDatabase:SIZE 2 " + subjectDataNew);
//            gradeDatabase.gradesDaoUpdated().getSubjectDataLimited(st).observe(this, grades_data -> {
//                Log.d("XXX", "getDataFromDatabase: "+grades_data.size());
//                subjectRecyclerAdapterUpdated.submitList(grades_data);
//            });
                    }
                } else {
                    List<Integer> ls = new ArrayList();
                    Log.d(TAG, "LIST_SIZE: " + ls.size());
                    ls = UtilityFunctions.getRandomTwoIntegerUpto(eng.length, 0);
                    for (int index : ls) {
                        subjectDataNew.add(gradeDatabase.gradesDaoUpdated().getSubjectDataIncompleteFirst(eng[index]).get(0));
                        chapterListEng.add(gradeDatabase.gradesDaoUpdated().getSubjectDataIncompleteFirst(eng[index]).get(0).getId());
                        Log.d(TAG, "getDataFromDatabase:ELSE SIZE " + subjectDataNew);
                    }

                }
                PrefConfig.writeNormalListInPref(getContext(), chapterListEng, getResources().getString(R.string.saved_english_value));

            }

        }

        binding.englishRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        subjectRecyclerAdapterUpdated = new SubjectRecyclerAdapterUpdated(getContext(), gradeData -> {

//            Intent intent;
//            Log.d(TAG, "setRecyclerViewData: " + gradeData.getRequest());
//            if (gradeData.getSubject().equals("Spelling_CommonWords")) {
//                intent = new Intent(getContext(), EnglishSpellingActivity.class);
//                intent.putExtra(Constants.EXTRA_SPELLING_DETAIL, gradeData.getChapter_name());
//            } else {
//                intent = new Intent(getContext(), GrammarActivity.class);
//                intent.putExtra(Constants.EXTRA_GRAMMAR_CATEGORY, gradeData.getChapter_name());
//            }
//            intent.putExtra(Constants.EXTRA_ONLINE_FLAG, true);
//            intent.putExtra(Constants.EXTRA_CATEGORY_ID, gradeData.getId());
//            intent.putExtra(EXTRA_TITLE, gradeData.getSubject());
//            startActivity(intent);

            if (paymentStatus.equals("active")) {
                if (!gradeData.isIs_completed()) {
                    navigateToNextScreen(gradeData, true);
                } else {
                    navigateToNextScreen(gradeData, false);
                }

            } else {

                if (UtilityFunctions.diffDate(createAt, new Date().toString()) < trialPeriodDay) {
                    if (!gradeData.isIs_completed()) {
                        navigateToNextScreen(gradeData, true);
                    } else {
                        navigateToNextScreen(gradeData, false);
                    }

                } else {
                    UtilityFunctions.displayCustomDialogSubscribe(getContext(), "Subscribe", "Hey you don't have any subscription plan. Please subscribe to continue.", "Subscribe @ Rs " + paymentAmount + "/ Month");
                }
            }
        });


        binding.englishRecyclerView.setAdapter(subjectRecyclerAdapterUpdated);
        subjectRecyclerAdapterUpdated.submitList(subjectDataNew);
    }


    private void navigateToNextScreen(GradeData gradeData, Boolean isLearn) {
        customProgressDialogue.show();
        var a = ApiClientContent.getClient().create(ApiInterfaceContent.class);
        a.getData(gradeData.getId(), gradeData.getSub_subject_id()).enqueue(new Callback<ContentModelNew>() {
            @Override
            public void onResponse(@NonNull Call<ContentModelNew> call, @NonNull Response<ContentModelNew> response) {
                if (response.body() != null) {
                    handleResponse(response.body(), gradeData, isLearn);
                    customProgressDialogue.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ContentModelNew> call, @NonNull Throwable t) {
                Log.d("AAA", "onFailure: " + t.getLocalizedMessage());
                customProgressDialogue.dismiss();
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void handleResponse(ContentModelNew body, GradeData gradeData, Boolean isLearn) {
        ScreenType screenType;
        try {
            screenType = ScreenType.valueOf(body.getMeta().getScreen_type());
        } catch (IllegalArgumentException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (body.getLearning().size() == 0) {
            Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show();
            return;
        }
        if (body.getExercise().size() == 0) {
            Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent;
        Constants.OpenType openType;
        switch (screenType) {
            case LETTER_BY_LETTER: {
                if (isLearn) {
                    intent = new Intent(getContext(), EnglishSpellingActivity.class);
                    openType = Constants.OpenType.LEARNING;
                } else {
                    intent = new Intent(getContext(), SpellingTest.class);
                    openType = Constants.OpenType.EXERCISE;
                }
                intent.putExtra(EXTRA_SPELLING_DETAIL, gradeData.getChapter_name());
            }
            break;
            case CHOSE_THE_CORRECT_WORD: {
                if (isLearn) {
                    intent = new Intent(getContext(), GrammarActivity.class);
                    openType = Constants.OpenType.LEARNING;
                } else {
                    intent = new Intent(getContext(), GrammarTestActivity.class);
                    openType = Constants.OpenType.EXERCISE;
                }
                intent.putExtra(EXTRA_GRAMMAR_CATEGORY, gradeData.getChapter_name());
            }
            break;
            default:
                Toast.makeText(getContext(), "No activity found for this screen type", Toast.LENGTH_SHORT).show();
                return;
        }

        Log.d(TAG, "handleResponse: " + openType);
        intent.putExtra(EXTRA_OPEN_TYPE, openType.name()); //TODO : u can check weather is intent in learning or exercise
        intent.putExtra(EXTRA_DATA, body);
        intent.putExtra(EXTRA_FLAG_HAVE_DATA, true);
        intent.putExtra(EXTRA_ONLINE_FLAG, true);
        intent.putExtra(EXTRA_IS_OPEN_FROM_LEARN, false);
        intent.putExtra(EXTRA_CATEGORY_ID, gradeData.getId());
        intent.putExtra(EXTRA_TITLE, gradeData.getSubject());
        startActivity(intent);
    }


    private void setSubSubjectProgress() {

        subMathList.clear();
        subEngList.clear();
        sectionList.clear();


        eng = gradeDatabase.gradesDaoUpdated().getChapterNames();

        if (eng.length != 0) {
            binding.noDataFound.setVisibility(View.GONE);
            binding.completeProgressTile.setVisibility(View.VISIBLE);
//        for (int i = 0; i < math.length; i++) {
//            int total = UtilityFunctions.gettingSubSubjectData(gradeDatabase, kidsGrade, math[i].split(" ")[0], true).size();
//            int completed = UtilityFunctions.gettingSubSubjectData(gradeDatabase, kidsGrade, math[i], false).size();
//            if (!math[i].equals("Multiplication Tables")) {
//                subMathList.add(new SubSubject(math[i], total, completed, resMath[i]));
//            } else {
//                Log.i("Mul_Data", total + "," + UtilityFunctions.gettingSubSubjectData(gradeDatabase, kidsGrade, math[i].split(" ")[0], false));
//                int mulUpto = PrefConfig.readIntInPref(getApplicationContext(), getResources().getString(R.string.multiplication_upto));
//                total = Integer.parseInt(UtilityFunctions.gettingSubSubjectData(gradeDatabase, kidsGrade, math[i].split(" ")[0], true).get(total - 1).chapter.split(" ")[3]);
//                if (mulUpto == 1)
//                    subMathList.add(new SubSubject(math[i], total, 0, resMath[i]));
//                else
//                    subMathList.add(new SubSubject(math[i], total, mulUpto, resMath[i]));
//            }
//        }
//
//        sectionList.add(new SectionSubSubject("Mathematics", subMathList));

            for (int i = 0; i < eng.length; i++) {
                int total = gradeDatabase.gradesDaoUpdated().getSubjectCount(eng[i]);
                //TODO: MUST BE UPDATE
                int completed = gradeDatabase.gradesDaoUpdated().getSubjectCompleteCount(eng[i]);
                //int completed = UtilityFunctions.gettingSubSubjectData(gradeDatabase, kidsGrade, eng[i], false).size();
                subEngList.add(new SubSubject(eng[i], total, completed, resEng[i]));
            }

            sectionList.add(new SectionSubSubject("", subEngList));


            binding.progressRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            sectionSubSubjectRecyclerAdapter = new SectionSubSubjectRecyclerAdapter(sectionList, getContext());
            binding.progressRecyclerView.setAdapter(sectionSubSubjectRecyclerAdapter);

            uiChnages();
        } else {
            binding.noDataFound.setVisibility(View.VISIBLE);
            binding.completeProgressTile.setVisibility(View.GONE);
            binding.gotoViewCurriculumOne.setVisibility(View.INVISIBLE);
            binding.gotoViewCurriculumTwo.setVisibility(View.INVISIBLE);
            binding.startExeButtonLayout.setVisibility(View.GONE);

        }

//        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
//        View mView = getLayoutInflater().inflate(R.layout.progress_report_dialog, null);
//
//        alert.setView(mView);
//        final AlertDialog alertDialog = alert.create();
//        alertDialog.setCancelable(true);
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        RecyclerView progressView = mView.findViewById(R.id.progressRecyclerView);
//        ImageView closeButton = mView.findViewById(R.id.closeButton);
//
//
//
//
//        try {
//            alertDialog.show();
//        } catch (Exception e) {
//
//        }
//
//        closeButton.setOnClickListener(v -> alertDialog.dismiss());

    }

    @Override
    public void onResume() {
        super.onResume();
        setSubSubjectProgress();
        paymentStatus = PrefConfig.readIdInPref(getContext(), getResources().getString(R.string.payment_status));
        userType = PrefConfig.readIdInPref(getContext(), getResources().getString(R.string.user_type));
    }
}