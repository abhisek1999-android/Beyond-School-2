package com.maths.beyond_school270620220930.extras;

import android.util.Log;

public class UtilityFunctions {

    public Boolean matchingSeq(String str1, String str2) {


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
