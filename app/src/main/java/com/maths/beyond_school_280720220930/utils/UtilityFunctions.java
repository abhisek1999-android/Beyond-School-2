package com.maths.beyond_school_280720220930.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.database.english.vocabulary.model.VocabularyCategoryModel;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.database.grade_tables.Grades_data;
import com.maths.beyond_school_280720220930.database.log.LogDatabase;
import com.maths.beyond_school_280720220930.database.log.LogEntity;
import com.maths.beyond_school_280720220930.database.process.ProgressDataBase;
import com.maths.beyond_school_280720220930.database.process.ProgressM;
import com.maths.beyond_school_280720220930.dialogs.HintDialog;

import java.text.DateFormat;
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

    public static void loadImage(String url, android.widget.ImageView imageView, View progress) {
        Glide.with(imageView.getContext())
                .load(url)
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


    public  static Map<String, List<String>> phonetics(){

        Map<String,List<String>> words=new HashMap<>();
        words.put("a", Arrays.asList(new String[]{"a","ya","yay"}));
        words.put("b", Arrays.asList(new String[]{"b","be","bee"}));
        words.put("c", Arrays.asList(new String[]{"c","see","sea"}));
        words.put("d", Arrays.asList(new String[]{"d","de","dee","thee"}));
        words.put("e", Arrays.asList(new String[]{"e","ee","eh"}));
        words.put("f", Arrays.asList(new String[]{"f","eff"}));
        words.put("g", Arrays.asList(new String[]{"g","gee","jee"}));
        words.put("h", Arrays.asList(new String[]{"h","aitch","itch","hedge","hatch","edge"}));
        words.put("i", Arrays.asList(new String[]{"i","eye","aye"}));
        words.put("j",Arrays.asList(new String[]{"j","jay", "je","joy"}));
        words.put("k",Arrays.asList(new String[]{"k","kay", "ke"}));
        words.put("l",Arrays.asList(new String[]{"l","ell", "yell","hell","el"}));
        words.put("m", Arrays.asList(new String[]{"m","am","yam","em"}));
        words.put("n", Arrays.asList(new String[]{"n","yen"}));
        words.put("o",Arrays.asList(new String[]{"o","oh", "vow", "waw"}));
        words.put("p",Arrays.asList(new String[]{"p","pee", "pay", "pie"}));
        words.put("q",Arrays.asList(new String[]{"q","cue", "queue"}));
        words.put("r",Arrays.asList(new String[]{"r","are", "err","year"}));
        words.put("s",Arrays.asList(new String[]{"s","ess","es","ass", "yes","as"}));
        words.put("t",Arrays.asList(new String[]{"t","tee", "tea","it","ti"}));
        words.put("u",Arrays.asList(new String[]{"u","you"}));
        words.put("v",Arrays.asList(new String[]{"v","vee", "wee"}));
        words.put("w",Arrays.asList(new String[]{"w","double you"}));
        words.put("x",Arrays.asList(new String[]{"x","ex", "aex"}));
        words.put("y",Arrays.asList(new String[]{"y","why"}));
        words.put("z",Arrays.asList(new String[]{"z","zed", "zee", "jed"}));
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
            case "animals_1":
                return VocabularyCategories.animals_1;
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

    public enum Spellings {
        Most_Common_Words_1,
        Most_Common_Words_2,
        Words_with_short_a_sounds,
        Words_with_L_blends,
        Words_with_double_consonants,
        Words_with_Long_o_sound,
        Words_with_sh
    }

    public static Spellings getSpellingsFromString(String spelling) {
        switch (spelling) {
            case "Spelling Words with short ‘a’ sounds":
                return Spellings.Words_with_short_a_sounds;
            case "Spelling Words with ‘L’ blends":
                return Spellings.Words_with_L_blends;
            case "Spelling Words with double consonants":
                return Spellings.Words_with_double_consonants;
            case "Spelling Words with Long ‘o’ sound":
                return Spellings.Words_with_Long_o_sound;
            case "Spelling Words with ‘sh’":
                return Spellings.Words_with_sh;
            case "Spelling Most Common Words 2":
                return Spellings.Most_Common_Words_2;
            default:
                return Spellings.Most_Common_Words_1;
        }
    }

    public static String getDBNameSpelling(Spellings spellings, Context context) {
        switch (spellings) {
            case Most_Common_Words_1:
                return context.getString(R.string.spelling1);
            case Most_Common_Words_2:
                return context.getString(R.string.spelling1_1);
            case Words_with_short_a_sounds:
                return context.getString(R.string.spelling2);
            case Words_with_L_blends:
                return context.getString(R.string.spelling3);
            case Words_with_double_consonants:
                return context.getString(R.string.spelling4);
            case Words_with_Long_o_sound:
                return context.getString(R.string.spelling5);
            case Words_with_sh:
                return context.getString(R.string.spelling6);
            default:
                return "";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static VocabularyCategoryModel getVocabularyDetailsFromType(List<VocabularyCategoryModel> models, VocabularyCategories type) {
        var filterList = models.stream().filter(model -> model.getCategory().equals(type.name())).collect(Collectors.toList());
        if (filterList.size() > 0) {
            return filterList.get(0);
        } else {
            return null;
        }
    }


    public static void sendDataToAnalytics(FirebaseAnalytics mFirebaseAnalytics, String uid, String kidsId, String kidsName, String type,
                                           int age, String result, String detected, Boolean tag, int timeTaken, String question, String subject) {
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
        mFirebaseAnalytics.logEvent(subject, resultBundle);
    }


    public static void saveLog(LogDatabase db, String log) {
        var saveLog = new LogEntity(log, String.valueOf(new Date().getTime()));
        var dao = db.logDao();
        dao.insertNotes(saveLog);
    }


    public static MediaPlayer playClapSound(Activity activity) {
        var m = MediaPlayer.create(activity, R.raw.clap_sound);
        m.setVolume(0.2f, 0.2f);
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String formatTime(long diff) {


        Duration duration = Duration.ofMillis(diff);
        long h = duration.toHours();
        long m = duration.toMinutes() % 60;
        long s = duration.getSeconds() % 60;
        String timeInHms = String.format("%d h:%d m:%d s", h, m, s);
        return String.format("Time taken :%s", timeInHms);
    }


    // Calculates the age of kids
    @RequiresApi(api = Build.VERSION_CODES.O)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = ((Activity) mContext).getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            View decorView = window.getDecorView();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
            window.setStatusBarColor(Color.TRANSPARENT);
        }
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
        Log.i("CHAPTER", chapter);
        Log.i("DB_DATA", dbData + "");
        for (int i = 0; i < dbData.size(); i++) {
            if (dbData.get(i).chapter.equals(subSub)) {

                try {
                    database.gradesDao().updateIsComplete(true, dbData.get(i).chapter);
                    database.gradesDao().update(true, dbData.get(i + 1).chapter);
                    Log.i("DB_DATA", dbData.get(i + 1).chapter);
                    break;
                } catch (Exception e) {

                    Log.i("DB_DATA_EXP", e.getMessage());
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

}
