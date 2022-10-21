package com.maths.beyond_school_280720220930.retrofit;


import com.maths.beyond_school_280720220930.retrofit.model.content.ContentModel;
import com.maths.beyond_school_280720220930.retrofit.model.grade.GradeModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("{grade}.json")
    Call<GradeModel> getGradeData(@Path("grade") String grade);


    @GET("subjects/grade1/english/vocabulary/{vocabSubject}.json")
    Call<ContentModel> getVocabularySubject(@Path("vocabSubject") String vocabSubject);
}
