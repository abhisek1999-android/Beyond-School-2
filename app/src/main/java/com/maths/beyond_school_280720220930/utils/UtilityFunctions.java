package com.maths.beyond_school_280720220930.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.database.english.grammer.model.GrammarModel;
import com.maths.beyond_school_280720220930.database.english.vocabulary.model.VocabularyCategoryModel;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.database.grade_tables.Grades_data;
import com.maths.beyond_school_280720220930.database.log.LogDatabase;
import com.maths.beyond_school_280720220930.database.log.LogEntity;
import com.maths.beyond_school_280720220930.database.process.ProgressDataBase;
import com.maths.beyond_school_280720220930.database.process.ProgressM;
import com.maths.beyond_school_280720220930.dialogs.HintDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.stream.Collectors;

public final class UtilityFunctions {

    //    Ayaan's Code

    // Extension Function To load image in imageview Using Glide Library
    public static void loadImage(String url, android.widget.ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(url)
                .error(R.drawable.cartoon_image_1)
                .into(imageView);
    }


    public static boolean intToBoolean(int i) {
        return i != 0;
    }

    public static void loadImage(String url, android.widget.ImageView imageView, View progress) {
        Glide.with(imageView.getContext())
                .load(url)
                .timeout(10000)
                .error(R.drawable.cartoon_image_1)
                .listener(new com.bumptech.glide.request.RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
    }


    public static Map<String, List<String>> phonetics() {

        Map<String, List<String>> words = new HashMap<>();
        words.put("a", Arrays.asList(new String[]{"a", "ya", "yay"}));
        words.put("b", Arrays.asList(new String[]{"b", "be", "bee"}));
        words.put("c", Arrays.asList(new String[]{"c", "see", "sea"}));
        words.put("d", Arrays.asList(new String[]{"d", "de", "dee", "thee"}));
        words.put("e", Arrays.asList(new String[]{"e", "ee", "eh"}));
        words.put("f", Arrays.asList(new String[]{"f", "eff"}));
        words.put("g", Arrays.asList(new String[]{"g", "gee", "jee"}));
        words.put("h", Arrays.asList(new String[]{"h", "aitch", "itch", "hedge", "hatch", "edge"}));
        words.put("i", Arrays.asList(new String[]{"i", "eye", "aye"}));
        words.put("j", Arrays.asList(new String[]{"j", "jay", "je", "joy"}));
        words.put("k", Arrays.asList(new String[]{"k", "kay", "ke"}));
        words.put("l", Arrays.asList(new String[]{"l", "ell", "yell", "hell", "el"}));
        words.put("m", Arrays.asList(new String[]{"m", "am", "yam", "em"}));
        words.put("n", Arrays.asList(new String[]{"n", "yen"}));
        words.put("o", Arrays.asList(new String[]{"o", "oh", "vow", "waw"}));
        words.put("p", Arrays.asList(new String[]{"p", "pee", "pay", "pie"}));
        words.put("q", Arrays.asList(new String[]{"q", "cue", "queue"}));
        words.put("r", Arrays.asList(new String[]{"r", "are", "err", "year"}));
        words.put("s", Arrays.asList(new String[]{"s", "ess", "es", "ass", "yes", "as"}));
        words.put("t", Arrays.asList(new String[]{"t", "tee", "tea", "it", "ti"}));
        words.put("u", Arrays.asList(new String[]{"u", "you"}));
        words.put("v", Arrays.asList(new String[]{"v", "vee", "wee"}));
        words.put("w", Arrays.asList(new String[]{"w", "double you"}));
        words.put("x", Arrays.asList(new String[]{"x", "ex", "aex"}));
        words.put("y", Arrays.asList(new String[]{"y", "why"}));
        words.put("z", Arrays.asList(new String[]{"z", "zed", "zee", "jed"}));
        return words;
    }

    public static int getPendingIntentFlag() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) ? PendingIntent.FLAG_IMMUTABLE : PendingIntent.FLAG_ONE_SHOT;
    }

    public static void simpleToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // Extension Function to for Handler to run on UI Thread
    public static void runOnUiThread(Runnable runnable, long time) {
        new Handler(Looper.getMainLooper()).postDelayed(runnable, time);

    }

    public static void runOnUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);

    }

    public static Boolean checkString(String s1, String s2) {
        return Soundex.getCode(s1).equals(Soundex.getCode(s2));
    }


    //    Extension Function to get random number by passing digits number
    public static int getRandomNumber(int digits) {
        int number = (int) (Math.random() * Math.pow(10, digits));
        if (number != 0)
            return number;
        else
            return getRandomNumber(digits);
    }

    // Extension function to add space between String
    public static String addComma(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            sb.append("'").append(s.charAt(i)).append("' !");
        }
        return sb.toString();
    }

    public static String capitalize(String s) {
        if (s.length() == 0) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static String addSpaceAnswer(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            sb.append(s.charAt(i)).append(" ");
        }
        return sb.toString();
    }

    public static String replace(String str, int index, char ch) {
        char[] chars = str.toCharArray();
        chars[index] = ch;
        return new String(chars);
    }

    // Add space to each character of String accept end
    public static String addSpace(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length() - 1; i++) {
            sb.append(s.charAt(i)).append(" ");
        }
        sb.append(s.charAt(s.length() - 1));
        return sb.toString();
    }


    public enum VocabularyCategories {
        bathroom_1,
        bathroom_2,
        body_parts_1,
        body_parts_2,
        colors_1,
        colors_2,
        animals_1,
        animals_2,
        fruits_1,
        fruits_2,
        vegetables_1,
        vegetables_2,
        cloth_1,
        cloth_2,
        feeling_1,
        feeling_2,
        insect_1,
        insect_2,
        kitchen_1,
        kitchen_2,
        living_room, summer, town, transport, weather,
        school
    }

    public static String getRandomItem(String[] list) {
        Random random = new Random();
        return list[random.nextInt(list.length)];
    }

    public static String getCompliment(Boolean isCorrect) {
        return (isCorrect)
                ? getRandomItem(new String[]{"Fantastic !", "well done !", "great job !", "amazing !", "awesome !", "High-five !", "wow !", "That’s wonderful !!"})
                :
                getRandomItem(new String[]{"That’s incorrect ! ", "you can try again ! ", "That’s not correct !", "let’s try again !"});

    }

    public static String convertCardinalNumberToOrdinalNumber(int number) {
        String[] suffixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th"};
        try {
            switch (number % 100) {
                case 11:
                case 12:
                case 13:
                    return number + "th";
                default:
                    return number + suffixes[number % 10];
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static String getIntroForGrammar(Context context, String category) {
        var intro = "";
        if (context.getString(R.string.grammar_1).equals(category))
            intro = "Let’s learn how to identify nouns in a sentence." +
                    "Go through each word and see," +
                    "if it's a person, place, thing, or an emotion or idea.";
        else if (context.getString(R.string.grammar_2).equals(category))
            intro = "Some nouns have irregular plural forms. " +
                    "They turn into different words. Let us memorize them.";
        else if (context.getString(R.string.grammar_3).equals(category))
            intro = "Lets practice to identify some common and proper nouns";
        else if (context.getString(R.string.grammar_4).equals(category))
            intro = "Let us learn how to identify verbs in a sentence. ";
        else intro = "Let us learn how to identify Present tense in verbs in a sentence. ";

        return intro;
    }

    public static String[] getQuestionForGrammar(Context context, GrammarModel grammarModel, String category) {
        var question = new String[]{};
        if (context.getString(R.string.grammar_1).equals(category))
            question = new String[]{grammarModel.getDescription() + "..." + "'"
                    + grammarModel.getWord() + "'!" + "is noun here.",
                    "Now It's your turn to Click on the noun to identify it"};
        else if (context.getString(R.string.grammar_2).equals(category))
            question = new String[]{
                    grammarModel.getDescription().replace("→", ",")
                            + "..." + "'", "Now It's your turn to Click on " +
                    "the plural form to identify it"
            };
        else if (context.getString(R.string.grammar_3).equals(category)) {
            var list = new String[]{"Tell me which noun is this?",
                    "Please answer which noun is this ?"};
            question = new String[]{
                    "'" + grammarModel.getWord() + "' ."
                            + grammarModel.getDescription() + "..."
                    , UtilityFunctions.getRandomItem(list)
            };
        } else if (context.getString(R.string.grammar_4).equals(category))
            question = new String[]{
                    grammarModel.getDescription() + "..." + "'"
                            + grammarModel.getWord() + "'!" + "is verb here."
                    , "Now It's your turn to Click on the verb to identify it"
            };
        else question = new String[]{
                    grammarModel.getDescription() + "..." + "'"
                            + grammarModel.getWord() + "'!" + "is Present tense verb here."
                    , "Now It's your turn to Click on the Present tense verb to identify it"
            };
        return question;
    }

    public static String getQuestionForGrammarTest(Context context, String category) {
        var question = "";
        if (context.getString(R.string.grammar_1).equals(category))
            question = getRandomItem(new String[]{
                    "Can you identify the noun?",
                    "Try to identify the noun ?",
                    "Tell what is the noun here ?",
                    "Guess the noun here?"});
        else if (context.getString(R.string.grammar_2).equals(category))
            question = "Try to tell the plural form of this noun";
        else if (context.getString(R.string.grammar_3).equals(category)) {
            question = "Tell me which noun is this?";
        } else if (context.getString(R.string.grammar_4).equals(category))
            question = "Can you identify the verb?";
        else question = "Can you identify the Present tense verb?";
        return question;
    }


    public static String getQuestionTitleVocabulary(VocabularyCategories categories) {
        switch (categories) {
            case bathroom_1:
            case bathroom_2:
                return "Let us learn about Bathroom and objects we use there.";
            case body_parts_1:
            case body_parts_2:
                return "Here we will learn about our different body parts.";
            case colors_1:
            case colors_2:
                return "It’s the turn of colors now. Let us learn them.";
            case animals_1:
            case animals_2:
                return "Let us learn and know some animals. ";
            case fruits_1:
            case fruits_2:
                return "I am sure, you all love eating fruits. Let us try to learn different fruits and their names.";
            case vegetables_1:
            case vegetables_2:
                return "Vegetables are good for health. We will learn about some of them now.";
            case cloth_1:
            case cloth_2:
                return "We all wear different types of clothes to look nice. Let us know about them.";
            case feeling_1:
            case feeling_2:
                return "Let us learn about feelings and emotions we all undergo.";
            case insect_1:
            case insect_2:
                return "Now we will discover different types of insects.";
            case kitchen_1:
            case kitchen_2:
                return "Let us learn and know some of the objects used by our mothers in Kitchen.";
            default:
                return "Let us learn and know about " + UtilityFunctions.vocabularyCategoriesToString(categories);
        }
    }

    public static String getQuestionsFromVocabularyCategories(VocabularyCategories categories) {
        switch (categories) {
            case bathroom_1:
            case bathroom_2:
                return getRandomItem(new String[]{"Can you name this object? ", " Try naming this object ", " Can you tell what is this object called?"});
            case body_parts_1:
            case body_parts_2:
                return getRandomItem(new String[]{"Can you name this body part? ", " Try naming this body part", "Can you name this one?"});
            case colors_1:
            case colors_2:
                return getRandomItem(new String[]{" Can you name this color? ", " Which color is this?", " Try naming this color ", " Which color do you see here?"});
            case animals_1:
            case animals_2:
                return getRandomItem(new String[]{" Can you name this animal? ", " Try naming this animal", " Can you identify this?"});
            case fruits_1:
            case fruits_2:
                return getRandomItem(new String[]{"Can you name this fruit? ", "Name this fruit", "Can you identify this fruit?"});
            case vegetables_1:
            case vegetables_2:
                return getRandomItem(new String[]{"Can you name this vegetable? ", "Name this vegetable", "Can you identify this vegetable?"});
            case cloth_1:
            case cloth_2:
                return getRandomItem(new String[]{"Can you name this object?", "Name this ?", "Can you identify this?"});
            case feeling_1:
            case feeling_2:
                return getRandomItem(new String[]{"What is this feeling called? ", "Name this ?", "Can you identify this?"});
            case insect_1:
            case insect_2:
                return getRandomItem(new String[]{"What is this insect called?", "Name this ?"});
            case kitchen_1:
            case kitchen_2:
                return getRandomItem(new String[]{" What is this object called?", "Name this object", "Can you identify this?"});
            default:
                return getRandomItem(new String[]{"Name this object", "Can you identify this object?"});
        }
    }

    public static int getNinetyPercentage(int total) {
        return (int) Math.floor(total * 0.9);
    }

    public static String getDbName(VocabularyCategories categories, Context context) {
        switch (categories) {
            case bathroom_1:
                return context.getString(R.string.vocab1);
            case bathroom_2:
                return context.getString(R.string.vocab1_1);
            case body_parts_1:
                return context.getString(R.string.vocab2);
            case body_parts_2:
                return context.getString(R.string.vocab2_1);
            case colors_1:
                return context.getString(R.string.vocab3);
            case colors_2:
                return context.getString(R.string.vocab3_1);
            case animals_1:
                return context.getString(R.string.vocab4);
            case animals_2:
                return context.getString(R.string.vocab4_1);
            case fruits_1:
                return context.getString(R.string.vocab5);
            case fruits_2:
                return context.getString(R.string.vocab5_1);
            case vegetables_1:
                return context.getString(R.string.vocab6);
            case vegetables_2:
                return context.getString(R.string.vocab6_1);
            case cloth_1:
                return context.getString(R.string.vocab8);
            case cloth_2:
                return context.getString(R.string.vocab8_1);
            case feeling_1:
                return context.getString(R.string.vocab9);
            case feeling_2:
                return context.getString(R.string.vocab9_1);
            case insect_1:
                return context.getString(R.string.vocab10);
            case insect_2:
                return context.getString(R.string.vocab10_1);
            case kitchen_1:
                return context.getString(R.string.vocab11);
            case kitchen_2:
                return context.getString(R.string.vocab11_1);
            case living_room:
                return context.getString(R.string.vocab12);
            case school:
                return context.getString(R.string.vocab14);
            case summer:
                return context.getString(R.string.vocab15);
            case town:
                return context.getString(R.string.vocab16);
            case transport:
                return context.getString(R.string.vocab17);
            case weather:
                return context.getString(R.string.vocab18);
            default:
                return "";
        }
    }

    public static String vocabularyCategoriesToString(VocabularyCategories vocabularyCategories) {
        switch (vocabularyCategories) {
            case bathroom_1:
                return "Bathroom 1";
            case bathroom_2:
                return "Bathroom 2";
            case body_parts_1:
                return "Body Parts 1";
            case body_parts_2:
                return "Body Parts 2";
            case colors_1:
                return "Colors 1";
            case colors_2:
                return "Colors 2";
            case animals_1:
                return "Animals 1";
            case animals_2:
                return "Animals 2";
            case fruits_1:
                return "Fruits 1";
            case fruits_2:
                return "Fruits 2";
            case vegetables_1:
                return "Vegetables 1";
            case vegetables_2:
                return "Vegetables 2";
            case cloth_1:
                return "Cloth 1";
            case cloth_2:
                return "Cloth 2";
            case feeling_1:
                return "Feeling 1";
            case feeling_2:
                return "Feeling 2";
            case insect_1:
                return "Insect 1";
            case insect_2:
                return "Insect 2";
            case kitchen_1:
                return "Kitchen 1";
            case kitchen_2:
                return "Kitchen 2";
            case living_room:
                return "Living Room";
            case summer:
                return "Summer";
            case town:
                return "Town";
            case transport:
                return "Transport";
            case weather:
                return "Weather";
            case school:
                return "School";
            default:
                return "";
        }
    }

    public static int getGrade(String grade) {
        switch (grade) {
            case "GRADE 1":
                return 1;
            case "GRADE 2":
                return 2;
            default:
                return 3;
        }
    }

    public static VocabularyCategories getVocabularyFromString(String category) {
        switch (category) {
            case "bathroom_1":
                return VocabularyCategories.bathroom_1;
            case "bathroom_2":
                return VocabularyCategories.bathroom_2;
            case "body_parts_1":
                return VocabularyCategories.body_parts_1;
            case "body_parts_2":
                return VocabularyCategories.body_parts_2;
            case "colors_1":
                return VocabularyCategories.colors_1;
            case "colors_2":
                return VocabularyCategories.colors_2;
            case "fruits_1":
                return VocabularyCategories.fruits_1;
            case "fruits_2":
                return VocabularyCategories.fruits_2;
            case "vegetables_1":
                return VocabularyCategories.vegetables_1;
            case "vegetables_2":
                return VocabularyCategories.vegetables_2;
            case "animals_2":
                return VocabularyCategories.animals_2;
            case "cloth_1":
                return VocabularyCategories.cloth_1;
            case "cloth_2":
                return VocabularyCategories.cloth_2;
            case "feeling_1":
                return VocabularyCategories.feeling_1;
            case "feeling_2":
                return VocabularyCategories.feeling_2;
            case "insect_1":
                return VocabularyCategories.insect_1;
            case "insect_2":
                return VocabularyCategories.insect_2;
            case "kitchen_1":
                return VocabularyCategories.kitchen_1;
            case "kitchen_2":
                return VocabularyCategories.kitchen_2;
            case "living_room":
                return VocabularyCategories.living_room;
            case "summer":
                return VocabularyCategories.summer;
            case "town":
                return VocabularyCategories.town;
            case "transport":
                return VocabularyCategories.transport;
            case "weather":
                return VocabularyCategories.weather;
            case "school":
                return VocabularyCategories.school;
            default:
                return VocabularyCategories.animals_1;
        }
    }

    public static VocabularyCategories getVocabularyCategoryFromAdapter(String category) {
        switch (category) {
            case "bathroom_1":
                return VocabularyCategories.bathroom_1;
            case "bathroom_2":
                return VocabularyCategories.bathroom_2;
            case "bodyparts_1":
                return VocabularyCategories.body_parts_1;
            case "bodyparts_2":
                return VocabularyCategories.body_parts_2;
            case "colors_1":
                return VocabularyCategories.colors_1;
            case "colors_2":
                return VocabularyCategories.colors_2;
            case "fruits_1":
                return VocabularyCategories.fruits_1;
            case "fruits_2":
                return VocabularyCategories.fruits_2;
            case "vegetables_1":
                return VocabularyCategories.vegetables_1;
            case "vegetables_2":
                return VocabularyCategories.vegetables_2;
            case "clothes_1":
                return VocabularyCategories.cloth_1;
            case "clothes_2":
                return VocabularyCategories.cloth_2;
            case "feelings_1":
                return VocabularyCategories.feeling_1;
            case "feelings_2":
                return VocabularyCategories.feeling_2;
            case "insects_1":
                return VocabularyCategories.insect_1;
            case "insects_2":
                return VocabularyCategories.insect_2;
            case "kitchen_1":
                return VocabularyCategories.kitchen_1;
            case "kitchen_2":
                return VocabularyCategories.kitchen_2;
            case "livingroom":
                return VocabularyCategories.living_room;
            case "animal_2":
                return VocabularyCategories.animals_2;
            case "school":
                return VocabularyCategories.school;
            case "summer":
                return VocabularyCategories.summer;
            case "town":
                return VocabularyCategories.town;
            case "transport":
                return VocabularyCategories.transport;
            case "weather":
                return VocabularyCategories.weather;
            default:
                return VocabularyCategories.animals_1;
        }
    }


    // function to Shuffle array and return
    public static ArrayList shuffleArray(ArrayList ar) {
        Random rnd = new Random();
        for (int i = ar.size() - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            Object a = ar.get(index);
            ar.set(index, ar.get(i));
            ar.set(i, a);
        }
        return ar;
    }


    public static char getRandomAlphabet() {
        Random r = new Random();
        return (char) (r.nextInt(26) + 'a');
    }

    public static VocabularyCategoryModel getVocabularyDetailsFromType(List<VocabularyCategoryModel> models, VocabularyCategories type) {
        var filterList = models.stream().filter(model -> model.getCategory().equals(type.name())).collect(Collectors.toList());
        if (filterList.size() > 0) {
            return filterList.get(0);
        } else {
            return null;
        }
    }


    public static void attemptPhoneNumberLogin(FirebaseAnalytics mFirebaseAnalytics, String phoneNumber) {


        var kidsData = new Bundle();
        kidsData.putString("phoneNumber", phoneNumber);
        mFirebaseAnalytics.logEvent("phone_number_login_attempt", kidsData);
    }


    public static void sendDataToAnalytics(FirebaseAnalytics mFirebaseAnalytics, String uid, String kidsId, String kidsName, String type,
                                           int age, String result, String detected, Boolean tag, int timeTaken, String question, String subject
            , String parentsContactId
    ) {
        var resultBundle = new Bundle();
        resultBundle.putString("original_result", result);
        resultBundle.putString("detected_result", detected);
        resultBundle.putBoolean("is_correct", tag);
        resultBundle.putInt("timeTaken", timeTaken);
        resultBundle.putString("question", question);
        resultBundle.putString("parent_id", uid);
        resultBundle.putString("kids_id", kidsId);
        resultBundle.putString("kids_name", kidsName);
        resultBundle.putInt("kids_age", age);
        resultBundle.putString("type", type);
        resultBundle.putString("parents_contact_id", parentsContactId);
        mFirebaseAnalytics.logEvent(subject, resultBundle);


        Bundle itemJeggings = new Bundle();
        itemJeggings.putString(FirebaseAnalytics.Param.ITEM_ID, kidsId);
        itemJeggings.putString(FirebaseAnalytics.Param.ITEM_NAME, kidsName);
        itemJeggings.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, question);
        itemJeggings.putString(FirebaseAnalytics.Param.ITEM_VARIANT, detected);
        itemJeggings.putString(FirebaseAnalytics.Param.ITEM_BRAND, result);
        itemJeggings.putString(FirebaseAnalytics.Param.ITEM_CATEGORY2, parentsContactId);
        itemJeggings.putDouble(FirebaseAnalytics.Param.PRICE, age);


        Bundle activityInfoWithIndex = new Bundle(itemJeggings);
        activityInfoWithIndex.putLong(FirebaseAnalytics.Param.INDEX, 1);


        Bundle viewItemListParams = new Bundle();
        viewItemListParams.putString(FirebaseAnalytics.Param.ITEM_LIST_ID, tag + "");
        viewItemListParams.putString(FirebaseAnalytics.Param.ITEM_LIST_NAME, type);
        viewItemListParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS,
                new Parcelable[]{activityInfoWithIndex});
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, viewItemListParams);


    }


    public static void saveLog(LogDatabase db, String log) {
        var saveLog = new LogEntity(log, String.valueOf(new Date().getTime()));
        var dao = db.logDao();
        dao.insertNotes(saveLog);
    }


    public static MediaPlayer playClapSound(Activity activity) {
        var m = MediaPlayer.create(activity, R.raw.clap_sound);
        m.setVolume(0.3f, 0.3f);
        // set playback time to 1 sec
        m.setLooping(false);

        return m;
    }


    //     Abhishek's Code


    // This solves the problem of repeating digits..


    public static int getRandomIntegerUpto(int maximum, int minimum) {
        return ((int) (Math.random() * (maximum - minimum))) + minimum;
    }


    public static void saveDataLocally(Context context, String grade, String name, String dob, String imageUrl, String uuid) {
        PrefConfig.writeIdInPref(context, grade, context.getResources().getString(R.string.kids_grade));
        PrefConfig.writeIdInPref(context, name, context.getResources().getString(R.string.kids_name));
        PrefConfig.writeIdInPref(context, dob, context.getResources().getString(R.string.kids_dob));
        PrefConfig.writeIdInPref(context, imageUrl, context.getResources().getString(R.string.kids_profile_url));
        PrefConfig.writeIdInPref(context, uuid, context.getResources().getString(R.string.kids_id));
    }

    public static Boolean isDivisible(int num1, int num2) {

        if ((num1 % num2) == 0)
            return true;
        else
            return false;
    }

    public Boolean matchingSeq(String str1, String str2) {


        if (str1.equals(""))
            return false;

        String st1 = str1.replace(" ", "");
        Log.i("Strings", st1 + "," + str2);
        while (st1.length() > 0) {

            if (st1.startsWith(str2)) {
                st1 = st1.replace(str2, "");
            } else {
                return false;
            }
        }
        return true;
    }


    // Greet about morning, afternoon etc.
    public String greeting() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            return "Good Morning";
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            return "Good Afternoon";
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            return "Good Evening";
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            return "Good Night";
        }
        return "";
    }


    // Format milisec to h: m: s
    public static String formatTime(long diff) {


        Duration duration = Duration.ofMillis(diff);
        long h = duration.toHours();
        long m = duration.toMinutes() % 60;
        long s = duration.getSeconds() % 60;
        String timeInHms = String.format("%d h:%d m:%d s", h, m, s);
        return String.format("Time taken :%s", timeInHms);
    }


    // Calculates the age of kids
    public static int calculateAge(String inputDate) {

        LocalDate curDate = LocalDate.now();
        SimpleDateFormat as = new SimpleDateFormat("dd/mm/yyyy");
        SimpleDateFormat req = new SimpleDateFormat("yyyy-mm-dd");
        try {
            Date adate = as.parse(inputDate);
            LocalDate dob = LocalDate.parse(req.format(adate));

            if ((dob != null) && (curDate != null)) {
                return Period.between(dob, curDate).getYears();
            } else {
                return 0;
            }

        } catch (Exception e) {

        }
        return 0;
    }


    // Un-Mute audio streams
    public static void unMuteAudioStream(Context context) throws InterruptedException {
        Thread.sleep(500);

        AudioManager amanager = (AudioManager) ((Activity) context).getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);

        try {
            amanager.setStreamMute(AudioManager.STREAM_DTMF, false);
        } catch (Exception e) {
        }
        //
        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
        amanager.setStreamMute(AudioManager.STREAM_ACCESSIBILITY, false);


    }

    // Mute audio streams

    public static void muteAudioStream(Context context) {
        AudioManager amanager = (AudioManager) ((Activity) context).getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        try {
            amanager.setStreamMute(AudioManager.STREAM_DTMF, true);
        } catch (Exception e) {
        }
        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
        amanager.setStreamMute(AudioManager.STREAM_ACCESSIBILITY, true);

    }

    public void setStatusBarTransparent(Context mContext) {
        Window window = ((Activity) mContext).getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(Color.TRANSPARENT);
    }


    public static String numberToWords(long n) {
        long limit = 1000000000000L, curr_hun, t = 0;

        // If zero return zero
        if (n == 0)
            return ("Zero");

        // Array to store the powers of 10
        String multiplier[] = {"", "Trillion", "Billion",
                "Million", "Thousand"};

        // Array to store numbers till 20
        String first_twenty[] = {
                "", "One", "Two", "Three",
                "Four", "Five", "Six", "Seven",
                "Eight", "Nine", "Ten", "Eleven",
                "Twelve", "Thirteen", "Fourteen", "Fifteen",
                "Sixteen", "Seventeen", "Eighteen", "Nineteen"
        };

        // Array to store multiples of ten
        String tens[] = {"", "Twenty", "Thirty",
                "Forty", "Fifty", "Sixty",
                "Seventy", "Eighty", "Ninety"};

        // If number is less than 20, return without any
        // further computation
        if (n < 20L)
            return (first_twenty[(int) n]);
        String answer = "";
        for (long i = n; i > 0; i %= limit, limit /= 1000) {
            curr_hun = i / limit;

            // It might be possible that the current
            // multiplier is bigger than your number
            while (curr_hun == 0) {

                // Set i as the remainder obtained when n
                // was divided my the limit
                i %= limit;

                // Divide the limit by 1000, shifts the
                // multiplier
                limit /= 1000;

                // Get the current value in hundereds, as
                // English system works in hundreds
                curr_hun = i / limit;

                // Shift the multiplier
                ++t;
            }

            // If current hundered is greater that 99, Add
            // the hundreds' place
            if (curr_hun > 99)
                answer += (first_twenty[(int) curr_hun / 100]
                        + " Hundred ");

            // Bring the current hundered to tens
            curr_hun = curr_hun % 100;

            // If the value in tens belongs to [1,19], add
            // using the first_twenty
            if (curr_hun > 0 && curr_hun < 20)
                answer += (first_twenty[(int) curr_hun] + " ");

                // If curr_hun is now a multiple of 10, but not
                // 0 Add the tens' value using the tens array
            else if (curr_hun % 10 == 0 && curr_hun != 0)
                answer += (tens[(int) curr_hun / 10 - 1] + " ");

            else if (curr_hun > 20 && curr_hun < 100)
                answer
                        += (tens[(int) curr_hun / 10 - 1] + " "
                        + first_twenty[(int) curr_hun % 10]
                        + " ");

            // If Multiplier has not become less than 1000,
            // shift it
            if (t < 4)
                answer += (multiplier[(int) ++t] + " ");
        }
        return (answer);
    }


    // Function for unlocking
    public static void updateDbUnlock(GradeDatabase database, String grade, String chapter, String subSub) {

        List<Grades_data> dbData = new ArrayList<>();
        dbData = database.gradesDao().valus(new SimpleSQLiteQuery("SELECT * FROM grades where " + grade.replaceAll(" ", "").toLowerCase() + " =1 and chapter LIKE '%" + chapter + "%'"));
        Log.d("XXX", chapter);
        Log.d("XXX", dbData + "");
        for (int i = 0; i < dbData.size(); i++) {
            Log.d("XXX", "updateDbUnlock: called " + dbData.get(i).chapter.equals(subSub) + subSub);
            if (dbData.get(i).chapter.equals(subSub)) {
                Log.d("XXX", "updateDbUnlock: if");
                try {
                    database.gradesDao().updateIsComplete(true, dbData.get(i).chapter);
                    database.gradesDao().update(true, dbData.get(i + 1).chapter);
                    Log.d("XXX", dbData.get(i + 1).chapter);
                    break;
                } catch (Exception e) {
                    Log.e("XXX", e.getMessage());
                    break;
                }

            }

        }
    }


    // getting first false data
    public static Grades_data getFirstFalseData(GradeDatabase database, String grade, String chapter) {

        List<Grades_data> dbData = new ArrayList<>();
        dbData = database.gradesDao().valus(new SimpleSQLiteQuery("SELECT * FROM grades where " + grade.replaceAll(" ", "").toLowerCase() + " =1 and chapter LIKE '%" + chapter + "%' AND is_complete=0 LIMIT 1"));
        Log.i("CHAPTER", chapter);
        Log.i("DB_DATA", dbData + "");
        try {
            return dbData.get(0);
        } catch (Exception e) {

            return null;
        }

    }


    public static List<Grades_data> gettingSubSubjectData(GradeDatabase database, String grade, String chapter, boolean is_all_needed) {

        List<Grades_data> dbData = new ArrayList<>();
        if (is_all_needed)
            dbData = database.gradesDao().valus(new SimpleSQLiteQuery("SELECT * FROM grades where " + grade.replaceAll(" ", "").toLowerCase() + " =1 and chapter LIKE '%" + chapter + "%'"));
        else
            dbData = database.gradesDao().valus(new SimpleSQLiteQuery("SELECT * FROM grades where " + grade.replaceAll(" ", "").toLowerCase() + " =1 and chapter LIKE '%" + chapter + "%' AND is_complete=1"));

        Log.i("CHAPTER", chapter);
        Log.i("DB_DATA", dbData + "");
        try {
            return dbData;
        } catch (Exception e) {

            return null;
        }

    }


    public static long gettingCorrectValues(ProgressDataBase progressDataBase, String chapter, Boolean resultType) {

        long value = 0;

        if (resultType)
            value = progressDataBase.progressDao().correctValues(new SimpleSQLiteQuery("SELECT correct FROM progressM where chapter LIKE '%" + chapter + "%'"));
        else
            value = progressDataBase.progressDao().correctValues(new SimpleSQLiteQuery("SELECT wrong FROM progressM where chapter LIKE '%" + chapter + "%'"));

        Log.i("CHAPTER", chapter);
        Log.i("DB_DATA", value + "");
        try {
            return value;
        } catch (Exception e) {

            return 0;
        }

    }


    public static List<ProgressM> checkProgressAvailable(ProgressDataBase db, String subject, String chapter, Date timeStamp, long time_spend, boolean is_data_needed) {

        List<ProgressM> list = db.progressDao().isAvailable(chapter);

        if (!is_data_needed) {
            if (list.size() > 0) {
                updateProgressData(db, subject, chapter, time_spend);
            } else {

                addProgressData(db, subject, chapter, timeStamp, time_spend);
            }
        }
        return list;
    }

    public static void updateProgressData(ProgressDataBase db, String subject, String chapter, long time_spend) {

        try {
            db.progressDao().update(time_spend, chapter);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static void addProgressData(ProgressDataBase db, String subject, String chapter, Date timeStamp, long time_spend) {

        try {
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat timeFormatter = new SimpleDateFormat("hh:mm:ss a");
            long diff = date.getTime() - timeStamp.getTime();
            ProgressM progressM = new ProgressM();
            progressM.correct = 0;
            progressM.time_to_complete = diff;
            progressM.wrong = 0;
            progressM.time = timeFormatter.format(date) + "";
            progressM.is_completed = "Yes";
            progressM.subject = subject;
            progressM.chapter = chapter;
            progressM.time_spend = time_spend;
            progressM.date = formatter.format(date) + "";
            progressM.timestamp = date.getTime();
            db.progressDao().insertNotes(progressM);

        } catch (Exception e) {

        }

    }


    public static void displayCustomDialog(Context context, String title, String body) {

        HintDialog hintDialog = new HintDialog(context);
        hintDialog.setCancelable(true);
        hintDialog.setAlertTitle(title);
        hintDialog.setAlertDesciption(body);

        hintDialog.displayAnim();
        hintDialog.setOnActionListener(viewId -> {

            switch (viewId.getId()) {

                case R.id.closeButton:
                    hintDialog.dismiss();
                    break;
            }
        });

        hintDialog.show();

    }


    public static boolean checkGrade(String grade) {


        List<String> grades = new ArrayList();
        grades.add("GRADE 1");
        grades.add("GRADE 2");
        grades.add("GRADE 3");

        if (grades.contains(grade))
            return true;

        return false;

    }


    @SuppressLint("Range")
    public static void setEvent(Context context, TextInputLayout textInputLayout) throws ParseException {


        Cursor cur = context.getContentResolver().query(CalendarContract.Calendars.CONTENT_URI, null, null, null, null);
        try {


            PrefConfig.writeIdInPref(context, textInputLayout.getEditText().getText().toString(), context.getResources().getString(R.string.timer_time));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
            Date currDate = new Date();
            String datetime = dateFormatter.format(currDate) + " " + textInputLayout.getEditText().getText().toString().replace(" ", "").split("-")[0];
            String endTime = "2023/12/31 " + textInputLayout.getEditText().getText().toString().replace(" ", "").split("-")[1];
            //  String endTime="2023/08/12 06:30";
            var eventID = 0;
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(formatter.parse(datetime));

            Calendar endCal = Calendar.getInstance();
            endCal.setTime(formatter.parse(endTime));
            var eventdate = startCal.get(Calendar.YEAR) + "/" + startCal.get(Calendar.MONTH) + "/" + startCal.get(Calendar.DAY_OF_MONTH) + " " + startCal.get(Calendar.HOUR_OF_DAY) + ":" + startCal.get(Calendar.MINUTE);
            Log.e("event date", eventdate);
            // provide CalendarContract.Calendars.CONTENT_URI to
            // ContentResolver to query calendars

            if (cur.moveToFirst()) {

                if (isEventInCal(context, eventID + "")) {
                    return;
                }
                long calendarID = cur.getLong(cur.getColumnIndex(CalendarContract.Calendars._ID));
                ContentValues eventValues = new ContentValues();
                eventValues.put(CalendarContract.Events.DTSTART, ((startCal.getTimeInMillis())));
                //eventValues.put(CalendarContract.Events.DTEND, ((endCal.getTimeInMillis())));
                eventValues.put(CalendarContract.Events.DURATION, "+P30M");
                eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().toString());
                eventValues.put(CalendarContract.Events.CALENDAR_ID, calendarID);
                eventValues.put(CalendarContract.Events.TITLE, "It's time to study!");
                eventValues.put(CalendarContract.Events.RRULE, "FREQ=DAILY;COUNT=20;BYDAY=MO,TU,WE,TH,FR;WKST=MO");
                eventValues.put(CalendarContract.Events.DESCRIPTION, "Beyondschool is waiting for you, only 5 mins left to see you. Tap the link to open app \n https://www.beyondschool.live/app");
                eventValues.put(CalendarContract.Events.ALL_DAY, false);
                eventValues.put(CalendarContract.Events.HAS_ALARM, true);
                eventValues.put(CalendarContract.Events.CUSTOM_APP_PACKAGE, context.getPackageName());
                eventValues.put(CalendarContract.Events.CUSTOM_APP_URI, "myAppointment://1");

                Uri eventUri = context.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, eventValues);
                eventID = (int) ContentUris.parseId(eventUri);


                ContentValues reminder = new ContentValues();
                reminder.put(CalendarContract.Reminders.EVENT_ID, eventID);
                reminder.put(CalendarContract.Reminders.MINUTES, 5);

                reminder.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                context.getContentResolver().insert(CalendarContract.Reminders.CONTENT_URI, reminder);

                Toast.makeText(context, "Event Added", Toast.LENGTH_SHORT).show();
                PrefConfig.writeIntInPref(context, (int) eventID, context.getResources().getString(R.string.calender_event_id));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Error_Events", e.getMessage());
        } finally {
            if (cur != null) {
                cur.close();
            }
        }

    }

    public static boolean isEventInCal(Context context, String cal_meeting_id) {

        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://com.android.calendar/events"),
                new String[]{"_id"}, " _id = ? ",
                new String[]{cal_meeting_id}, null);

        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

}
