package com.maths.beyond_school_280720220930.extras;

import android.util.Log;

import java.util.Calendar;

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

//    out ="32323212"
//    ans = "32"
//
//    def fun(out,ans):
//            while len(out)>0:
//            if out.startswith(ans):
//              out = out.replace(ans,"")
//            else:
//              return False
//    return True
//
//    print(fun(out,ans))

}
