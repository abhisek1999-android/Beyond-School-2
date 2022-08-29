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
import com.maths.beyond_school_280720220930.LearningActivity;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.database.english.vocabulary.model.VocabularyCategoryModel;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.database.grade_tables.Grades_data;
import com.maths.beyond_school_280720220930.database.log.LogDatabase;
import com.maths.beyond_school_280720220930.database.log.LogEntity;
import com.maths.beyond_school_280720220930.dialogs.HintDialog;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    public static String addSpace(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            sb.append(s.charAt(i));
            if (i != s.length() - 1)
                sb.append(" ");
        }
        return sb.toString();
    }

    public static String addSpaceAnswer(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            sb.append(s.charAt(i)).append(" ");
        }
        return sb.toString();
    }

    public enum VocabularyCategories {
        bathroom, body_parts, colors, animals, fruits,
        vegetables, cloth, feeling, insect, kitchen,
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
        switch (number % 100) {
            case 11:
            case 12:
            case 13:
                return number + "th";
            default:
                return number + suffixes[number % 10];
        }
    }


    public static String getQuestionTitleVocabulary(VocabularyCategories categories) {
        switch (categories) {
            case bathroom:
                return "Let us learn about Bathroom and objects we use there.";
            case body_parts:
                return "Here we will learn about our different body parts.";
            case colors:
                return "It’s the turn of colors now. Let us learn them.";
            case animals:
                return "Let us learn and know some animals. ";
            case fruits:
                return "I am sure, you all love eating fruits. Let us try to learn different fruits and their names.";
            case vegetables:
                return "Vegetables are good for health. We will learn about some of them now.";
            case cloth:
                return "We all wear different types of clothes to look nice. Let us know about them.";
            case feeling:
                return "Let us learn about feelings and emotions we all undergo.";
            case insect:
                return "Now we will discover different types of insects.";
            case kitchen:
                return "Let us learn and know some of the objects used by our mothers in Kitchen.";
            default:
                return "Let us learn and know about " + UtilityFunctions.vocabularyCategoriesToString(categories);
        }
    }

    public static String getQuestionsFromVocabularyCategories(VocabularyCategories categories) {
        switch (categories) {
            case bathroom:
                return getRandomItem(new String[]{"Can you name this object? ", " Try naming this object ", " Can you tell what is this object called?"});
            case body_parts:
                return getRandomItem(new String[]{"Can you name this body part? ", " Try naming this body part", "Can you name this one?"});
            case colors:
                return getRandomItem(new String[]{" Can you name this color? ", " Which color is this?", " Try naming this color ", " Which color do you see here?"});
            case animals:
                return getRandomItem(new String[]{" Can you name this animal? ", " Try naming this animal", " Can you identify this?"});
            case fruits:
                return getRandomItem(new String[]{"Can you name this fruit? ", "Name this fruit", "Can you identify this fruit?"});
            case vegetables:
                return getRandomItem(new String[]{"Can you name this vegetable? ", "Name this vegetable", "Can you identify this vegetable?"});
            case cloth:
                return getRandomItem(new String[]{"Can you name this object?", "Name this ?", "Can you identify this?"});
            case feeling:
                return getRandomItem(new String[]{"What is this feeling called? ", "Name this ?", "Can you identify this?"});
            case insect:
                return getRandomItem(new String[]{"What is this insect called?", "Name this ?"});
            case kitchen:
                return getRandomItem(new String[]{" What is this object called?", "Name this object", "Can you identify this?"});
            default:
                return getRandomItem(new String[]{"Name this object", "Can you identify this object?"});
        }
    }

    public static String getDbName(VocabularyCategories categories, Context context) {
        switch (categories) {
            case bathroom:
                return context.getString(R.string.vocab1);
            case body_parts:
                return context.getString(R.string.vocab2);
            case colors:
                return context.getString(R.string.vocab3);
            case animals:
                return context.getString(R.string.vocab4);
            case fruits:
                return context.getString(R.string.vocab5);
            case vegetables:
                return context.getString(R.string.vocab6);
            case cloth:
                return context.getString(R.string.vocab8);
            case feeling:
                return context.getString(R.string.vocab9);
            case insect:
                return context.getString(R.string.vocab10);
            case kitchen:
                return context.getString(R.string.vocab11);
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
            case bathroom:
                return "Bathroom";
            case body_parts:
                return "Body Parts";
            case colors:
                return "Colors";
            case animals:
                return "Animals";
            case fruits:
                return "Fruits";
            case vegetables:
                return "Vegetables";
            case cloth:
                return "Clothes";
            case feeling:
                return "Feelings";
            case insect:
                return "Insects";
            case kitchen:
                return "Kitchen";
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
            case "bathroom":
                return VocabularyCategories.bathroom;
            case "body_parts":
                return VocabularyCategories.body_parts;
            case "colors":
                return VocabularyCategories.colors;
            case "fruits":
                return VocabularyCategories.fruits;
            case "vegetables":
                return VocabularyCategories.vegetables;
            case "cloth":
                return VocabularyCategories.cloth;
            case "feeling":
                return VocabularyCategories.feeling;
            case "insect":
                return VocabularyCategories.insect;
            case "kitchen":
                return VocabularyCategories.kitchen;
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
                return VocabularyCategories.animals;
        }
    }

    public static VocabularyCategories getVocabularyCategoryFromAdapter(String category) {
        switch (category) {
            case "bathroom":
                return VocabularyCategories.bathroom;
            case "bodyparts":
                return VocabularyCategories.body_parts;
            case "colors":
                return VocabularyCategories.colors;
            case "fruits":
                return VocabularyCategories.fruits;
            case "vegetables":
                return VocabularyCategories.vegetables;
            case "clothes":
                return VocabularyCategories.cloth;
            case "feelings":
                return VocabularyCategories.feeling;
            case "insects":
                return VocabularyCategories.insect;
            case "kitchen":
                return VocabularyCategories.kitchen;
            case "livingroom":
                return VocabularyCategories.living_room;
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
                return VocabularyCategories.animals;
        }
    }

    public enum Spellings {
        Most_Common_Words,
        Words_with_short_a_sounds,
        Spelling_Words_with_L_blends,
        Spelling_Words_with_double_consonants,
        Spelling_Words_with_Long_o_sound,
        Spelling_Words_with_sh
    }

    public static Spellings getSpellingsFromString(String spelling) {
        switch (spelling) {
            case "Spelling Words with short ‘a’ sounds":
                return Spellings.Words_with_short_a_sounds;
            case "Spelling Words with ‘L’ blends":
                return Spellings.Spelling_Words_with_L_blends;
            case "Spelling Words with double consonants":
                return Spellings.Spelling_Words_with_double_consonants;
            case "Spelling Words with Long ‘o’ sound":
                return Spellings.Spelling_Words_with_Long_o_sound;
            case "Spelling Words with ‘sh’":
                return Spellings.Spelling_Words_with_sh;
            default:
                return Spellings.Most_Common_Words;
        }
    }

    public static String getDBNameSpelling(Spellings spellings, Context context) {
        switch (spellings) {
            case Most_Common_Words:
                return context.getString(R.string.spelling1);
            case Words_with_short_a_sounds:
                return context.getString(R.string.spelling2);
            case Spelling_Words_with_L_blends:
                return context.getString(R.string.spelling3);
            case Spelling_Words_with_double_consonants:
                return context.getString(R.string.spelling4);
            case Spelling_Words_with_Long_o_sound:
                return context.getString(R.string.spelling5);
            case Spelling_Words_with_sh:
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
    public int calculateAge(String inputDate) {

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


    public static void updateDbUnlock(GradeDatabase database, String grade, String chapter, String subSub) {

        List<Grades_data> dbData = new ArrayList<>();
        dbData = database.gradesDao().valus(new SimpleSQLiteQuery("SELECT * FROM grades where " + grade.replaceAll(" ", "").toLowerCase() + " =1 and chapter LIKE '%" + chapter + "%'"));
        Log.i("CHAPTER", chapter);
        Log.i("DB_DATA", dbData + "");
        for (int i = 0; i < dbData.size(); i++) {
            if (dbData.get(i).chapter.equals(subSub)) {

                try {
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

   public static void displayCustomDialog(Context context,String title, String body) {

        HintDialog hintDialog = new HintDialog(context);
        hintDialog.setCancelable(true);
        hintDialog.setAlertTitle(title);
        hintDialog.setAlertDesciption(body);

        hintDialog.displayAnim();
        hintDialog.setOnActionListener(viewId->{

            switch (viewId.getId()){

                case R.id.closeButton:
                    hintDialog.dismiss();
                    break;


            }
        });

        hintDialog.show();

    }

}
