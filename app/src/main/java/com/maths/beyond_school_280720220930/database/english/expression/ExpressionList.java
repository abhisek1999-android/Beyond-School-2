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

            var vocabularyDetailCloth = getExpressionDetailsCloths();
            var vocabularyDetailCloth1 = getExpressionDetailsCloths1();



            var listVocabulary = new ArrayList<ExpressionCategoryModel>();
            listVocabulary.add(new ExpressionCategoryModel("bathroom_1", vocabularyDetailCloth));


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
                            "Cap",
                            "A kind of soft, flat hat, typically with a peak. It is used to cover our head.",
                            Arrays.asList(new String[]{"My name is"})
            ));
            vocabularyDetail.add(
                    new ExpressionDetails(
                            "Coat",
                            "A piece of outer clothing with long sleeves, usually worn to keep us warm.",
                            Arrays.asList(new String[]{"My name is"})
                    )
            );

            return vocabularyDetail;
        }

        private static ArrayList<ExpressionDetails> getExpressionDetailsCloths1() {
            var vocabularyDetail = new ArrayList<ExpressionDetails>();

            vocabularyDetail.add(
                    new ExpressionDetails(
                            "Shirt",
                            "A long piece of cloth or knitted material worn around the neck, head, or shoulders.",
                            Arrays.asList(new String[]{"My name is"})
                    )
            );
            vocabularyDetail.add(
                    new ExpressionDetails(
                            "Shoes",
                            "A protective covering for the human foot, often made of leather or canvas.",
                            Arrays.asList(new String[]{"My name is"})
                    ));

            return vocabularyDetail;
        }

    }
}


