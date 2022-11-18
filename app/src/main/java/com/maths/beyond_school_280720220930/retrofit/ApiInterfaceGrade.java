package com.maths.beyond_school_280720220930.retrofit;


import com.maths.beyond_school_280720220930.retrofit.model.content.ContentModel;
import com.maths.beyond_school_280720220930.retrofit.model.content_new.ContentModelNew;
import com.maths.beyond_school_280720220930.retrofit.model.grade.GradeModelNew;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterfaceGrade {

    //@GET("getEnglishCourseData?grade=Grade 1")
    @GET("Exprement/demo_grade1.json")
    Call<GradeModelNew> getGradeData();


    @GET("subjects/{grade1}/{subject}/{topic}/{topic_detail}.json")
    Call<ContentModel> getVocabularySubject(@Path("grade1") String grade, @Path("subject") String subject, @Path("topic") String topic, @Path("topic_detail") String vocabSubject);

    @GET("getBlockData")
    Call<ContentModelNew> getData(@Query("blockID") String blockID, @Query("subsubjectId") String subSubjectId);
}
