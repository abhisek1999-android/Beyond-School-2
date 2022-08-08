package com.maths.beyond_school_280720220930.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.maths.beyond_school_280720220930.R;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

public final class UtilityFunctions {

    //    Ayaan's Code
    // Extension Function To load image in imageview Using Glide Library
    public static void loadImage(String url, android.widget.ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(url)
                .error(R.drawable.cartoon_image_1)
                .into(imageView);
    }

    public static int getPendingIntentFlag() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) ? PendingIntent.FLAG_IMMUTABLE : PendingIntent.FLAG_ONE_SHOT;
    }

    public static void simpleToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // Extension Function to for Handler to run on UI Thread
    public static void runOnUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    //    Extension Function to get random number by passing digits number
    public static int getRandomNumber(int digits) {
        int number = (int) (Math.random() * Math.pow(10, digits));
        if (number != 0)
            return number;
        else
            return getRandomNumber(digits);
    }


    //     Abhishek's Code
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String formatTime(long diff) {


        Duration duration = Duration.ofMillis(diff);
        long h = duration.toHours();
        long m = duration.toMinutes() % 60;
        long s = duration.getSeconds() % 60;
        String timeInHms = String.format("%d h:%d m:%d s", h, m, s);
        return String.format("Time taken :%s", timeInHms);
    }

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
}
