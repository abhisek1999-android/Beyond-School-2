package com.maths.beyond_school_280720220930.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.maths.beyond_school_280720220930.model.AnimData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AnimationUtil {



    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<AnimData> type_three_addition(int num1, int num2){

        List<String> desc=new ArrayList<>();
        desc.add("First, add tens of both numbers ");
        desc.add("Then, add once of both numbers ");
        desc.add("Lastly add");




        List<AnimData> list=new ArrayList<>();
        List<Integer> last_num=new ArrayList<>();
        String last_exp="";
        String[] num1_arr=String.valueOf(num1).split("");
        String[] num2_arr=String.valueOf(num2).split("");


        for (int i=0;i<num1_arr.length;i++){

            int firstDigit= Integer.parseInt(num1_arr[i])*smallest(num1_arr.length-i);
            int secondDigit=Integer.parseInt(num2_arr[i])*smallest(num1_arr.length-i);
            last_num.add(firstDigit+secondDigit);
            list.add(new AnimData(desc.get(i),firstDigit+"_"+secondDigit+"_"+(firstDigit+secondDigit)));
            num1=num1-firstDigit;
            num2=num2-secondDigit;
        }

        for (int i=0;i<last_num.size();i++) {

            last_exp+=last_num.get(i)+"_";
        }

        last_exp+= last_num.stream().mapToInt(Integer::intValue).sum();
        list.add(new AnimData("At the end add, "+last_exp.split("_")[0]+"and"+last_exp.split("_")[1]+". Woahla!! "+last_exp.split("_")[2]+" is your answer.",last_exp));

        return list;

    }

    public static int smallest(int n){
        int num=1;

        if (n==2)
            return 10;
        else if(n==3)
            return 100;
        else if (n==4)
            return 1000;
        else if (n==5)
            return 10000;
        else
            return num;

    }


    int placeValue(int N, int num)
    {
        int total = 1, value = 0, rem = 0;
        while (true)
        {
            rem = N % 10;
            N = N / 10;

            if (rem == num)
            {
                value = total * rem;
                break;
            }
            total = total * 10;
        }
        return value;
    }

}
