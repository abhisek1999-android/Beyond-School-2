package com.maths.beyond_school_new_071020221400.database.english.expression;

import androidx.annotation.NonNull;

import com.maths.beyond_school_new_071020221400.database.english.expression.model.ExpressionCategoryModel;
import com.maths.beyond_school_new_071020221400.database.english.expression.model.ExpressionDetails;
import com.maths.beyond_school_new_071020221400.database.english.expression.model.ExpressionModel;

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
                            "Used to ask How a person is called or addressed. Example, My name is Emily. or, I am Jenny. or You can call me Mary.",
                            Arrays.asList(new String[]{"My name is mName","I am mName","You can call me mName"})
            ));
            vocabularyDetail.add(
                    new ExpressionDetails(
                            "Where are you from?",
                            " Used to ask in which country or region were you born or raised. Example, I am from India.",
                            Arrays.asList(new String[]{"I am from mCountry"})
                    )
            );
            vocabularyDetail.add(
                    new ExpressionDetails(
                            "Where do you live? ",
                            "Used to ask where the person lives. Example, I live in Lucknow.",
                            Arrays.asList(new String[]{"I live in mCity"})
                    )
            );

            vocabularyDetail.add(
                    new ExpressionDetails(
                            "How old are you?",
                            "Used to ask the age of a person. Example, I am 6 years old. or, I am 7.  ",
                            Arrays.asList(new String[]{"I am mAge years old","I am mAge"})
                    )
            );

            vocabularyDetail.add(
                    new ExpressionDetails(
                            "How many people are there in your family? ",
                            "Used to ask the number of people in your family. Example, There are 5 people in my family. ",
                            Arrays.asList(new String[]{"There are mPeople people in my family"})
                    )
            );


            vocabularyDetail.add(
                    new ExpressionDetails(
                            "With whom do you live?",
                            "Used to ask who all live with you in you home. Example, I live with my parents.",
                            Arrays.asList(new String[]{" I live with my parents"})
                    )
            );

            vocabularyDetail.add(
                    new ExpressionDetails(
                            "Do you have any sibling?",
                            "Used to ask how many brothers and sisters a person has. Example. Yes, I have a sister. or, I don’t have any sibling.",
                            Arrays.asList(new String[]{"I have a sister","I don’t have any sibling"})
                    )
            );

            vocabularyDetail.add(
                    new ExpressionDetails(
                            "When is your birthday?",
                            "Used to ask when a person was born. Example. My birthday is on 6th October.",
                            Arrays.asList(new String[]{"My birthday is on"})
                    )
            );

            vocabularyDetail.add(
                    new ExpressionDetails(
                            "What is your hobby? ",
                            "Used to ask what activity a person does in free time for fun. Example. I love listening to music.",
                            Arrays.asList(new String[]{"I love"})
                    )
            );


     vocabularyDetail.add(
                    new ExpressionDetails(
                            "In which class do you study?",
                            " Example. I study in class 1",
                            Arrays.asList(new String[]{"I study in"})
                    )
            );

            return vocabularyDetail;
        }


    }
}


