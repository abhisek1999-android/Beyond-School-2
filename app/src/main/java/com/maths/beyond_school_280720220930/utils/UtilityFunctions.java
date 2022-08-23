package com.maths.beyond_school_280720220930.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.database.english.model.VocabularyModel;
import com.maths.beyond_school_280720220930.database.log.LogDatabase;
import com.maths.beyond_school_280720220930.database.log.LogEntity;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
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

    public static String getQuestionsFromVocabularyCategories(VocabularyCategories categories) {
        switch (categories) {
            case bathroom:
                return getRandomItem(new String[]{"Can you name this object? ", " Try naming this object ", " Can you tell what is this object called?"});
            case body_parts:
                return getRandomItem(new String[]{"Can you name this body part? ", " Try naming this body part; Can you name this one?"});
            case colors:
                return getRandomItem(new String[]{" Can you name this color? ", " Which color is this?", " Try naming this color ", " Which color do you see here?"});
            case animals:
                return getRandomItem(new String[]{" Can you name this animal? ", " Try naming this animal", " Can you identify this?"});
            case fruits:
                return getRandomItem(new String[]{"Can you name this fruit? ", "Name this fruit; Can you identify this fruit?"});
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static VocabularyModel getVocabularyDetailsFromType(List<VocabularyModel> models, VocabularyCategories type) {
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
}
