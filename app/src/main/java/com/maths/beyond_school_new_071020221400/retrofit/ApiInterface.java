package com.maths.beyond_school_new_071020221400.retrofit;


import com.maths.beyond_school_new_071020221400.retrofit.model.content.ContentModel;
import com.maths.beyond_school_new_071020221400.retrofit.model.grade.GradeModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("{grade}.json")
    Call<GradeModel> getGradeData(@Path("grade") String grade);


    @GET("subjects/{grade1}/{subject}/{topic}/{topic_detail}.json")
    Call<ContentModel> getVocabularySubject(
            @Path("grade1") String grade,
            @Path("subject") String subject,
            @Path("topic") String topic,
            @Path("topic_detail") String vocabSubject
    );
}
