package com.maths.beyond_school_280720220930.extras;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UtilityFunctions {

    public Boolean matchingSeq(String str1, String str2) {


        if (str1.equals(""))
            return false;

            String st1=str1.replace(" ","");
            Log.i("Strings",st1+","+str2);
            while(st1.length()>0){

                if (st1.startsWith(str2)){
                    st1=st1.replace(str2,"");
                }
                else{
                    return false;
                }

        }


        return true;
    }
    public String greeting(){
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            return "Good Morning";
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            return "Good Afternoon";
        }else if(timeOfDay >= 16 && timeOfDay < 21){
           return  "Good Evening";
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            return  "Good Night";
        }
        return "";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String formatTime(long diff){


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
        return  0;

    }
    }
