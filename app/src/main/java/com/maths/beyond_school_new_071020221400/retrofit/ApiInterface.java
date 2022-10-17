package com.maths.beyond_school_new_071020221400.retrofit;


import com.maths.beyond_school_new_071020221400.retrofit.model.ResponseSubjects;
import com.maths.beyond_school_new_071020221400.retrofit.noun.NounResponse;
import com.maths.beyond_school_new_071020221400.retrofit.noun.SubjectContent;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("english.json")
    Call<ResponseSubjects> getTodos();

    @GET("subjects/Indefinite_Noun.json")
    Call<ResponseClass<SubjectContent>> getIndefiniteNoun();

    @GET("subjects/Irregular_Plural_Noun.json")
    Call<ResponseClass<SubjectContent>> getIrregularPluralNoun();



}
