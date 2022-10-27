package com.maths.beyond_school_280720220930;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.maths.beyond_school_280720220930.adapters.ChaptersRecyclerAdapter;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeData;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.databinding.AcitivityVocabularyBinding;
import com.maths.beyond_school_280720220930.databinding.ActivityViewCurriculumBinding;
import com.maths.beyond_school_280720220930.english_activity.vocabulary2.VocabularyActivity;
import com.maths.beyond_school_280720220930.retrofit.ApiClient;
import com.maths.beyond_school_280720220930.retrofit.ApiInterface;
import com.maths.beyond_school_280720220930.retrofit.model.grade.GradeModel;
import com.maths.beyond_school_280720220930.utils.Constants;
import com.maths.beyond_school_280720220930.utils.typeconverters.GradeConverter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ViewCurriculum extends AppCompatActivity {

    private static final String TAG = "ViewCurriculum";
    private GradeDatabase gradeDatabase;
    private ActivityViewCurriculumBinding binding;
    private List<GradeData> chapterList;
    private ChaptersRecyclerAdapter chaptersRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewCurriculumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        gradeDatabase = GradeDatabase.getDbInstance(this);


        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked) {
                    setRecyclerViewData(checkedRadioButton.getText().toString().trim());
                }
            }
        });

        binding.buttonLayout.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),HomeScreen.class));
        });

        setRecyclerViewData("Vocabulary");

    }


//    private void getNewData() {
//        Retrofit retrofit = ApiClient.getClient();
//        var api = retrofit.create(ApiInterface.class);
//        api.getGradeData("grade1").enqueue(new retrofit2.Callback<>() {
//            private Call<GradeModel> call;
//            private Response<GradeModel> response;
//
//            @Override
//            public void onResponse(@NonNull retrofit2.Call<GradeModel> call, @NonNull retrofit2.Response<GradeModel> response) {
//                this.call = call;
//                this.response = response;
//                if (response.body() != null) {
//
//                    Log.d(TAG, "onResponse: " + response.code());
//                    Log.d(TAG, "onResponse: " + response.body().getEnglish().toString());
//                    var list = response.body().getEnglish();
//                    mapToGradeModel(list);
//                } else {
//                    Toast.makeText(ViewCurriculum.this, "Something wrong occurs", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(retrofit2.Call<com.maths.beyond_school_280720220930.retrofit.model.grade.GradeModel> call, Throwable t) {
//                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
//            }
//        });
//    }
//


    private void setRecyclerViewData(String subject) {

        chapterList = gradeDatabase.gradesDaoUpdated().getSubjectData(subject);

        if (chapterList.size()==0)
            binding.noDataLayout.setVisibility(View.VISIBLE);
        else
            binding.noDataLayout.setVisibility(View.GONE);

        binding.contentRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        chaptersRecyclerAdapter = new ChaptersRecyclerAdapter(chapterList, ViewCurriculum.this, gradeData -> {
            var intent = new Intent(ViewCurriculum.this, VocabularyActivity.class);
            intent.putExtra(Constants.EXTRA_VOCABULARY_CATEGORY, gradeData.getChapter_name());
            startActivity(intent);
        });
        binding.contentRecyclerView.setAdapter(chaptersRecyclerAdapter);
    }

    private void mapToGradeModel(List<GradeModel.EnglishModel> list) {
        list.forEach(subject -> {
            var mapper = new GradeConverter(subject.getSubject());
            var chapterList = mapper.mapToList(subject.getChapters());
            gradeDatabase.gradesDaoUpdated().insertNotes(chapterList);
        });
    }


}