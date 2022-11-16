package com.maths.beyond_school_280720220930;


import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_TITLE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.adapters.ChaptersRecyclerAdapter;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.databinding.ActivityViewCurriculumBinding;
import com.maths.beyond_school_280720220930.english_activity.grammar.GrammarActivity;
import com.maths.beyond_school_280720220930.english_activity.spelling.EnglishSpellingActivity;
import com.maths.beyond_school_280720220930.retrofit.ApiClient;
import com.maths.beyond_school_280720220930.retrofit.ApiInterface;
import com.maths.beyond_school_280720220930.retrofit.model.grade.GradeModel;
import com.maths.beyond_school_280720220930.utils.Constants;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;
import com.maths.beyond_school_280720220930.utils.typeconverters.GradeConverter;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Retrofit;

public class ViewCurriculum extends AppCompatActivity {

    private static final String TAG = "ViewCurriculum";
    private GradeDatabase gradeDatabase;
    private ActivityViewCurriculumBinding binding;
    private ChaptersRecyclerAdapter chaptersRecyclerAdapter;
    private String[] eng;
    private List<String> engChapters;
    private String defaultSubject = "";
    private String kidsGrade = "";
    private int noOfDays = 0;
    private String paymentStatus;
    private int trialPeriodDay=1;
    private int paymentAmount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewCurriculumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        gradeDatabase = GradeDatabase.getDbInstance(this);
        kidsGrade = PrefConfig.readIdInPref(getApplicationContext(), getResources().getString(R.string.kids_grade)).toLowerCase(Locale.ROOT);

        eng = gradeDatabase.gradesDaoUpdated().getChapterNames();
        binding.gradeInfo.setText("For " + kidsGrade.substring(0, 1).toUpperCase() + kidsGrade.substring(1));

        noOfDays = PrefConfig.readIntInPref(ViewCurriculum.this, getResources().getString(R.string.noOfdays), 0);
        trialPeriodDay= PrefConfig.readIntInPref(ViewCurriculum.this, getResources().getString(R.string.trial_period), 0);
        paymentStatus = PrefConfig.readIdInPref(ViewCurriculum.this, getResources().getString(R.string.payment_status));
        paymentAmount=PrefConfig.readIntDInPref(ViewCurriculum.this,getResources().getString(R.string.plan_value));
        try{
        engChapters = Arrays.asList(eng);
        defaultSubject = eng[0];}catch (Exception e){}


        try {
            defaultSubject = getIntent().getStringExtra("subSubject");
            Log.d(TAG, "onCreate: " + defaultSubject + "," + Arrays.binarySearch(eng, defaultSubject) + "," + eng[1] + "," + eng[0]);
        } catch (Exception e) {
            defaultSubject = eng[0];
        }

        addRadioButtons();
        // TODO: Radio group should be dynamic
        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
            boolean isChecked = checkedRadioButton.isChecked();
            if (isChecked) {
                setRecyclerViewData(checkedRadioButton.getText().toString().trim());
            }
        });

        binding.buttonLayout.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), TabbedHomePage.class));
            finish();
        });

        //  getNewData();
    }


    public void addRadioButtons() {
        binding.radioGroup.setOrientation(LinearLayout.HORIZONTAL);
        //


        for (String st : eng) {
            RadioButton rdbtn = new RadioButton(this);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 15, 10, 15);
            rdbtn.setLayoutParams(params);
            rdbtn.setPadding(15, 20, 15, 20);
            rdbtn.setTypeface(ResourcesCompat.getFont(this, R.font.jellee_roman));
            rdbtn.setTextColor(getResources().getColor(R.color.primary));
            rdbtn.setId(engChapters.indexOf(st));
            rdbtn.setText(st);
            rdbtn.setButtonDrawable(R.color.transparent);
            rdbtn.setBackgroundResource(R.drawable.radio_selector);
            binding.radioGroup.addView(rdbtn);
            rdbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((RadioGroup) view.getParent()).check(view.getId());

                }
            });
        }

        setRecyclerViewData(defaultSubject);
        ((RadioButton) binding.radioGroup.getChildAt(engChapters.indexOf(defaultSubject))).setChecked(true);
    }


    private void getNewData() {
        Retrofit retrofit = ApiClient.getClient();
        var api = retrofit.create(ApiInterface.class);
        api.getGradeData(PrefConfig.readIdInPref(this, getResources().getString(R.string.kids_grade)).toLowerCase().replace(" ", "")).enqueue(new retrofit2.Callback<>() {


            @Override
            public void onResponse(@NonNull retrofit2.Call<GradeModel> call, @NonNull retrofit2.Response<GradeModel> response) {
                if (response.body() != null) {

                    Log.d(TAG, "onResponse: " + response.code());
                    Log.d(TAG, "onResponse: " + response.body().getEnglish().toString());
                    var list = response.body().getEnglish();
                    mapToGradeModel(list);
                } else {
                    Toast.makeText(ViewCurriculum.this, "Something wrong occurs", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<com.maths.beyond_school_280720220930.retrofit.model.grade.GradeModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
//


    private void setRecyclerViewData(String subject) {

        binding.contentRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        chaptersRecyclerAdapter = new ChaptersRecyclerAdapter(ViewCurriculum.this, gradeData -> {



            if (paymentStatus.equals("active")){

                if (gradeData.isUnlock()) {
                    Intent intent;
                    Log.d(TAG, "setRecyclerViewData: " + gradeData.getRequest());
                    if (gradeData.getSubject().equals("Spelling_CommonWords")) {
                        intent = new Intent(this, EnglishSpellingActivity.class);
                        intent.putExtra(Constants.EXTRA_SPELLING_DETAIL, gradeData.getChapter_name());
                    } else {
                        intent = new Intent(this, GrammarActivity.class);
                        intent.putExtra(Constants.EXTRA_GRAMMAR_CATEGORY, gradeData.getChapter_name());
                    }
                    intent.putExtra(Constants.EXTRA_ONLINE_FLAG, true);
                    intent.putExtra(Constants.EXTRA_CATEGORY_ID, gradeData.getId());
                    intent.putExtra(EXTRA_TITLE, gradeData.getSubject());
                    startActivity(intent);

                } else {

                    UtilityFunctions.displayCustomDialog(ViewCurriculum.this, "Chapter Locked", "Hey, Please complete previous level to unlock.");
                }
            }

            else {

                if (noOfDays<trialPeriodDay){

                    if (gradeData.isUnlock()) {
                        Intent intent;
                        Log.d(TAG, "setRecyclerViewData: " + gradeData.getRequest());
                        if (gradeData.getSubject().equals("Spelling_CommonWords")) {
                            intent = new Intent(this, EnglishSpellingActivity.class);
                            intent.putExtra(Constants.EXTRA_SPELLING_DETAIL, gradeData.getChapter_name());
                        } else {
                            intent = new Intent(this, GrammarActivity.class);
                            intent.putExtra(Constants.EXTRA_GRAMMAR_CATEGORY, gradeData.getChapter_name());
                        }
                        intent.putExtra(Constants.EXTRA_ONLINE_FLAG, true);
                        intent.putExtra(Constants.EXTRA_CATEGORY_ID, gradeData.getId());
                        intent.putExtra(EXTRA_TITLE, gradeData.getSubject());
                        startActivity(intent);

                    } else {

                        UtilityFunctions.displayCustomDialog(ViewCurriculum.this, "Chapter Locked", "Hey, Please complete previous level to unlock.");
                    }
                }
                else{

                   UtilityFunctions.displayCustomDialogSubscribe(ViewCurriculum.this,"Subscribe","Hey you don't have any subscription plan. Please subscribe to continue.","Subscribe @ Rs "+paymentAmount+"/ Month");
                }
            }

        });
        binding.contentRecyclerView.setAdapter(chaptersRecyclerAdapter);
        getLiveData(subject);
    }

    private void displaySubscriptionDialog() {

    }

    private void getLiveData(String subject) {
        gradeDatabase.gradesDaoUpdated().getSubjectData(subject).observe(this, gradeData -> {
            chaptersRecyclerAdapter.submitList(gradeData);
            if (gradeData.size() == 0) binding.noDataLayout.setVisibility(View.VISIBLE);
            else binding.noDataLayout.setVisibility(View.GONE);
        });
    }

    private void mapToGradeModel(List<GradeModel.EnglishModel> list) {
        list.forEach(subject -> {
            var mapper = new GradeConverter(subject.getSubject());
            var chapterList = mapper.mapToList(subject.getChapters());
           // gradeDatabase.gradesDaoUpdated().insertNotes(chapterList);
        });
    }


}