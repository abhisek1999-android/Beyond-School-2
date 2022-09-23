package com.maths.beyond_school_280720220930.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.model.AnimData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AnimationUtil {


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<AnimData> getAnimList(int num1, int num2, int digit, String type) {

        switch (type) {
            case "addition":
                if (digit == 1)
                    return oneDigitAddition(num1, num2);
                else
                    return type_three_addition(num1, num2);
            case "subtraction":
                if (digit == 1)
                    return oneDigitSubtraction(num1, num2);
                else
                    return performSubtraction(num1, num2);
            case "division":
                if (digit == 1)
                    return oneDigitDivision(num1, num2);
                else
                    return performDivision(num1, num2);
            case "multiplication":
                return performMultiplication(num1, num2);
            default:
                return null;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<AnimData> type_three_addition(int num1, int num2) {

        List<String> desc = new ArrayList<>();
        desc.add("First, add tens of both numbers ");
        desc.add("Then, add ones of both numbers ");
        desc.add("Lastly add");

        List<AnimData> list = new ArrayList<>();
        List<Integer> last_num = new ArrayList<>();
        String last_exp = "";
        String[] num1_arr = String.valueOf(num1).split("");
        String[] num2_arr = String.valueOf(num2).split("");


        for (int i = 0; i < num1_arr.length; i++) {

            int firstDigit = Integer.parseInt(num1_arr[i]) * smallest(num1_arr.length - i);
            int secondDigit = Integer.parseInt(num2_arr[i]) * smallest(num1_arr.length - i);
            last_num.add(firstDigit + secondDigit);
            list.add(new AnimData(desc.get(i), firstDigit + "_" + secondDigit + "_" + (firstDigit + secondDigit), "addition"));
            num1 = num1 - firstDigit;
            num2 = num2 - secondDigit;
        }

        for (int i = 0; i < last_num.size(); i++) {

            last_exp += last_num.get(i) + "_";
        }

        last_exp += last_num.stream().mapToInt(Integer::intValue).sum();
        list.add(new AnimData("At the end add, " + last_exp.split("_")[0] + " and " + last_exp.split("_")[1] + ". Voila!!  " + last_exp.split("_")[2] + "  is your answer.", last_exp, "addition"));

        return list;

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<AnimData> oneDigitAddition(int num1, int num2) {

        List<String> desc = new ArrayList<>();
        desc.add("You can add the two numbers on your fingers. Count ," + num2 + " more from " + num1 + ". That's it, " + (num1 + num2) + "  is your answer");

        List<AnimData> list = new ArrayList<>();
        list.add(new AnimData(desc.get(0), num1 + "_" + num2 + "_" + (num1 + num2), "addition"));

        return list;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<AnimData> oneDigitDivision(int num1, int num2) {

        List<String> desc = new ArrayList<>();
        desc.add("Try reading table of " + num2 + " until you get " + num1 + ". That's it, " + (num1 / num2) + " is your answer");

        List<AnimData> list = new ArrayList<>();
        list.add(new AnimData(desc.get(0), num1 + "_" + num2 + "_" + (num1 / num2), "division"));

        return list;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<AnimData> performMultiplication(int num1, int num2) {

        List<String> desc = new ArrayList<>();
        desc.add("Start with table of" + num2 + ". When you reach " + num1 + "×" + num2 + " you will get your answer. And your answer is " + (num1 * num2));

        List<AnimData> list = new ArrayList<>();
        list.add(new AnimData(desc.get(0), num1 + "_" + num2 + "_" + (num1 * num2), "multiplication"));

        return list;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<AnimData> oneDigitSubtraction(int num1, int num2) {

        List<String> desc = new ArrayList<>();
        desc.add("You can subtract two 1 digit numbers on your fingers. Take away, " + num2 + " from " + num1 + " to get your answer. That's it, " + (num1 - num2) + "  is your answer");

        List<AnimData> list = new ArrayList<>();
        list.add(new AnimData(desc.get(0), num1 + "_" + num2 + "_" + (num1 - num2), "subtraction"));

        return list;

    }


    public static List<AnimData> animGrammar(String grammarType, Context context) {

        List<AnimData> list = new ArrayList<>();

        int len = grammarType.split(" ").length;
        if (grammarType.split(" ")[len - 1].toLowerCase(Locale.ROOT).equals("noun")) {
            if (grammarType.equals(context.getResources().getString(R.string.grammar_1))) {
                list.add(new AnimData("Nouns are words for people,places, or things.\nExample -  girl, doctor, office, town, bag, shirt. ", "none", "none"));
                list.add(new AnimData("An idea or feeling can also be a noun. They are called Abstract nouns as we cannot see or touch them.\nExample - Happiness, sad, friendship, respect.", "", ""));
                list.add(new AnimData("Plural nouns describe more than one person, place, or thing.\nExample - car - cars , pen - pens, man - men, orange - oranges.", "none", "none"));
            } else if (grammarType.equals(context.getResources().getString(R.string.grammar_3))) {
                list.add(new AnimData("Common nouns name general objects, not specific " +
                        "objects.\nFor example - car is a common" +
                        " noun.", "none", "none"));
                list.add(new AnimData("A name for a specific object is a proper noun." +
                        " Always capitalize proper nouns.\nExample - My favorite color is Pink." +
                        " ", "none", "none"));
                list.add(new AnimData("The days of the week are proper nouns. " +
                        "Always capitalize first letter of them.\nExample - Monday, Tuesday.",
                        "none", "none"));
                list.add(new AnimData("The months of the year " +
                        "are proper nouns,Capitalize them." +
                        "\nExample - March, June.", "none", "none"));
                list.add(new AnimData("Don't capitalize the seasons" +
                        ", like summer, fall, and winter!",
                        "none", "none"));

            }
        } else if (grammarType.split(" ")[len - 1].toLowerCase(Locale.ROOT).equals("verb")) {
            if (grammarType.equals(context.getResources().getString(R.string.grammar_4))) {
                list.add(new AnimData("A verb is a word that shows action.\nExample - eat, run, dance, sing," +
                        "play, read, write, is, am, are, was, were . "
                        , "", ""));
                list.add(new AnimData("Let’s see how they are used in a sentence." +
                        "\nExample - David swims in water. ‘swim’ is the verb here.",
                        "none", "none"));
            } else {
                list.add(new AnimData("Hi, Kids. Let us learn about Present Tense verbs.", "none", "none"));
                list.add(new AnimData("When you change a verb to talk about the past" +
                        ",present, or future, you change it's tense." +
                        " The tense of a verb tells when an action happens." +
                        " Verbs can be past, present," +
                        " or future tense.",
                        "none", "none"));
                list.add(new AnimData("Present tense verbs are actions that are happening now. " +
                        "To write most verbs in the present tense, add -s.\nExample - write + s =" +
                        " writes - Sally writes a letter. ",
                        "none", "none"));
            }
        }
        return list;
    }


    @SuppressLint("SuspiciousIndentation")
    public static List<AnimData> performSubtraction(int num1, int num2) {

        List<AnimData> list = new ArrayList<>();
        List<Integer> last_num = new ArrayList<>();
        String last_exp = "";
        String[] num1_arr = String.valueOf(num1).split("");
        String[] num2_arr = String.valueOf(num2).split("");

        if (Integer.parseInt(num1_arr[num1_arr.length - 1]) < Integer.parseInt(num2_arr[num2_arr.length - 1])) {

            List<String> desc = new ArrayList<>();
            desc.add("First, Subtract tens place of both number");
            desc.add("Then,  Subtract the ones place of both numbers.Reverse the oder if second one is grater then first one");
            desc.add("At the end subtract the differences. ");

            for (int i = 0; i < num1_arr.length; i++) {

                int firstDigit = Integer.parseInt(num1_arr[i]) * smallest(num1_arr.length - i);
                int secondDigit = Integer.parseInt(num2_arr[i]) * smallest(num1_arr.length - i);
                last_num.add(Math.abs(firstDigit - secondDigit));
                if (firstDigit < secondDigit)
                    list.add(new AnimData(desc.get(i), secondDigit + "_" + firstDigit + "_" + (Math.abs(firstDigit - secondDigit)), "subtraction"));
                else
                    list.add(new AnimData(desc.get(i), firstDigit + "_" + secondDigit + "_" + (Math.abs(firstDigit - secondDigit)), "subtraction"));
                num1 = num1 - firstDigit;
                num2 = num2 - secondDigit;
            }

            for (int i = 0; i < last_num.size(); i++) {
                last_exp += last_num.get(i) + "_";
            }

            last_exp += last_num.get(0) - last_num.get(1);
            list.add(new AnimData(desc.get(desc.size() - 1) + " Voila!! " + (last_num.get(0) - last_num.get(1)) + " is your answer", last_exp, "subtraction"));
            return list;
        } else {


            List<String> desc = new ArrayList<>();
            desc.add("First, We can write second digit like this.");
            desc.add("Then,  Subtract this with the first digit. Still we have remaining" + Integer.parseInt(num2_arr[num2_arr.length - 1]));
            desc.add("At the end subtract the differences. ");
            int res = num1;

            int firstDigit = num2;
            int secondDigit = Integer.parseInt(num2_arr[num2_arr.length - 1]);
            list.add(new AnimData(desc.get(0), firstDigit + "_" + secondDigit + "_" + (firstDigit - secondDigit), "subtraction"));
            last_num.add(firstDigit - secondDigit);
            last_num.add(secondDigit);

            for (int i = 0; i < last_num.size(); i++) {

                if (i == last_num.size() - 1)
                    list.add(new AnimData(desc.get(i + 1) + " Voila!! " + (res - last_num.get(i)) + " is your answer.", res + "_" + last_num.get(i) + "_" + (res - last_num.get(i)), "subtraction"));
                else
                    list.add(new AnimData(desc.get(i + 1), res + "_" + last_num.get(i) + "_" + (res - last_num.get(i)), "subtraction"));
                res = num1 - last_num.get(i);
            }

            return list;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<AnimData> performDivision(int num1, int num2) {

        List<AnimData> list = new ArrayList<>();
        List<Integer> last_num = new ArrayList<>();
        String last_exp = "";
        String[] num1_arr = String.valueOf(num1).split("");
        String[] num2_arr = String.valueOf(num2).split("");

        List<String> desc = new ArrayList<>();
        desc.add("First, Divide tens of the first number with the second number");
        desc.add("Then,  Divide ones of the first number with the second number");
        desc.add("At the end add the results. ");

        for (int i = 0; i < num1_arr.length; i++) {

            int firstDigit = Integer.parseInt(num1_arr[i]) * smallest(num1_arr.length - i);
            last_num.add(firstDigit / num2);
            list.add(new AnimData(desc.get(i), firstDigit + "_" + num2 + "_" + (Math.abs(firstDigit / num2)), "division"));
            num1 = num1 - firstDigit;

        }

        for (int i = 0; i < last_num.size(); i++) {
            last_exp += last_num.get(i) + "_";
        }

        last_exp += last_num.stream().mapToInt(Integer::intValue).sum();
        ;
        list.add(new AnimData(desc.get(desc.size() - 1) + " Voila!!  " + last_exp.split("_")[2] + " is your answer", last_exp, "addition"));
        return list;


    }

    public static int smallest(int n) {
        int num = 1;

        if (n == 2)
            return 10;
        else if (n == 3)
            return 100;
        else if (n == 4)
            return 1000;
        else if (n == 5)
            return 10000;
        else
            return num;

    }


    int placeValue(int N, int num) {
        int total = 1, value = 0, rem = 0;
        while (true) {
            rem = N % 10;
            N = N / 10;

            if (rem == num) {
                value = total * rem;
                break;
            }
            total = total * 10;
        }
        return value;
    }

}
