package com.maths.beyond_school_280720220930.database.english.expression;

import androidx.annotation.NonNull;

import com.maths.beyond_school_280720220930.database.english.expression.model.ExpressionCategoryModel;
import com.maths.beyond_school_280720220930.database.english.expression.model.ExpressionDetails;
import com.maths.beyond_school_280720220930.database.english.expression.model.ExpressionModel;
import com.maths.beyond_school_280720220930.database.english.vocabulary.model.VocabularyModel;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class ExpressionList {


    public abstract static class GradeOneExpression {
        public static ExpressionModel englishListGrade1() {

            var expressionIntroduction = getExpressionDetailsCloths();


            var listVocabulary = new ArrayList<ExpressionCategoryModel>();
            listVocabulary.add(new ExpressionCategoryModel("Introducing Yourself", expressionIntroduction));


            return new ExpressionModel(
                    1,
                    listVocabulary
            );
        }

        //    @NonNull
//    private static ArrayList<ExpressionDetails> getExpressionDetailsOpposites() {
//        var vocabularyDetail = new ArrayList<ExpressionDetails>();
//
//        return vocabularyDetail;
//    }

        @NonNull
        private static ArrayList<ExpressionDetails> getExpressionDetailsCloths() {
            var vocabularyDetail = new ArrayList<ExpressionDetails>();

            vocabularyDetail.add(
                    new ExpressionDetails(
                            "What is your name?",
                            "Used to ask How a person is called or addressed. eg. My name is Emily. OR I am Jenny. OR You can call me Mary. ",
                            Arrays.asList(new String[]{"My name is"})
            ));
            vocabularyDetail.add(
                    new ExpressionDetails(
                            "Where are you from?",
                            " Used to ask in which country or region were you born or raised? eg. I am from India.",
                            Arrays.asList(new String[]{"I am from"})
                    )
            );

            return vocabularyDetail;
        }


    }
}


