package com.maths.beyond_school_280720220930.database.english.spelling;

import com.maths.beyond_school_280720220930.database.english.spelling.model.SpellingCategoryModel;
import com.maths.beyond_school_280720220930.database.english.spelling.model.SpellingDetail;
import com.maths.beyond_school_280720220930.database.english.spelling.model.SpellingModel;

import java.util.ArrayList;
import java.util.List;

public abstract class SpellingList {
    public abstract static class SpellingGradeOne {

        public static SpellingModel getSpellingList() {
            var mostCommonWord = getSpellingDetailMostCommonWords();
            var wordsWithShortASound = getSpellingDetailWordsWithShortASound();
            var wordsWithShortLSound = getSpellingDetailWordsWithShortLSound();
            var wordsWithDoubleConsonant = getSpellingDetailWordsWithDoubleConsonant();
            var wordsWithLongOSound = getSpellingDetailWordsWithLongOSound();
            var wordsWithSh = getSpellingDetailWordsWithSh();

            var spellingCategoryModelsList = new ArrayList<SpellingCategoryModel>();
            spellingCategoryModelsList.add(new SpellingCategoryModel("most common word", mostCommonWord));
            spellingCategoryModelsList.add(new SpellingCategoryModel("words with short a sound", wordsWithShortASound));
            spellingCategoryModelsList.add(new SpellingCategoryModel("words with short l sound", wordsWithShortLSound));
            spellingCategoryModelsList.add(new SpellingCategoryModel("words with double consonant", wordsWithDoubleConsonant));
            spellingCategoryModelsList.add(new SpellingCategoryModel("words with long o sound", wordsWithLongOSound));
            spellingCategoryModelsList.add(new SpellingCategoryModel("words with sh", wordsWithSh));

            return new SpellingModel(1, spellingCategoryModelsList);
        }


        private static List<SpellingDetail> getSpellingDetailMostCommonWords() {
            var list = new ArrayList<SpellingDetail>();
            list.add(new SpellingDetail(
                    "The",
                    "She ran to the park."
            ));
            list.add(new SpellingDetail(
                    "Of",
                    "Take care of yourself."
            ));
            list.add(new SpellingDetail(
                    "And",
                    "Pink and Blue are his favorite colors."
            ));
            list.add(new SpellingDetail(
                    "A",
                    "Rita is a good girl."
            ));
            list.add(new SpellingDetail(
                    "To",
                    "Rohan went to the market."
            ));
            list.add(new SpellingDetail(
                    "In",
                    "I see a house in the picture."
            ));
            list.add(new SpellingDetail(
                    "Is",
                    "She is our English teacher."
            ));
            list.add(new SpellingDetail(
                    "You",
                    "He wants to meet you."
            ));
            list.add(new SpellingDetail(
                    "That",
                    "That bridge is very beautiful."
            ));
            list.add(new SpellingDetail(
                    "It",
                    "It is raining again. "
            ));
            list.add(new SpellingDetail(
                    "He",
                    "He is a good man."
            ));
            list.add(new SpellingDetail(
                    "Was",
                    "It was so cold that day."
            ));
            list.add(new SpellingDetail(
                    "For",
                    "She thanked him for his help."
            ));
            list.add(new SpellingDetail(
                    "On",
                    "There is an apple on the table."
            ));
            list.add(new SpellingDetail(
                    "Are",
                    "You are very brave."
            ));
            list.add(new SpellingDetail(
                    "As",
                    "She is as tall as her brother."
            ));
            list.add(new SpellingDetail(
                    "With",
                    "He went with his friends to the party."
            ));
            list.add(new SpellingDetail(
                    "His",
                    "He found his keys."
            ));
            list.add(new SpellingDetail(
                    "They",
                    "They live in that house."
            ));
            list.add(new SpellingDetail(
                    "I",
                    "I love playing cricket."
            ));
            return list;
        }

        private static List<SpellingDetail> getSpellingDetailWordsWithShortASound() {
            var list = new ArrayList<SpellingDetail>();

            list.add(new SpellingDetail(
                    "At",
                    "I am at Jim’s house."
            ));
            list.add(new SpellingDetail(
                    "Am",
                    "I am going to the market."
            ));
            list.add(new SpellingDetail(
                    "Cat",
                    "She has a white cat."
            ));
            list.add(new SpellingDetail(
                    "Mat",
                    "Wipe your feet on the mat."
            ));
            list.add(new SpellingDetail(
                    "Can",
                    "I can play football."
            ));
            list.add(new SpellingDetail(
                    "Map",
                    "I viewed a map online."
            ));
            list.add(new SpellingDetail(
                    "Tap",
                    "A light tap sounded at the door."
            ));
            list.add(new SpellingDetail(
                    "Hat",
                    "She was wearing a red hat."
            ));
            list.add(new SpellingDetail(
                    "That",
                    ""
            ));
            list.add(new SpellingDetail(
                    "",
                    ""
            ));
            return list;
        }

        private static List<SpellingDetail> getSpellingDetailWordsWithShortLSound() {
            var list = new ArrayList<SpellingDetail>();
            list.add(new SpellingDetail(
                    "Clap",
                    "You cannot clap with one hand."
            ));
            list.add(new SpellingDetail(
                    "Flap",
                    "A bird can flap its wings."
            ));
            list.add(new SpellingDetail(
                    "Flag",
                    "This is India’s flag."
            ));
            list.add(new SpellingDetail(
                    "Plop",
                    "The stone fell into the water with a plop."
            ));
            list.add(new SpellingDetail(
                    "Plug",
                    "Pull the plug and let the water drain away."
            ));
            list.add(new SpellingDetail(
                    "Flip",
                    "Let’s flip a coin for a toss."
            ));
            list.add(new SpellingDetail(
                    "Slip",
                    "She was about to slip on the floor."
            ));
            list.add(new SpellingDetail(
                    "Slid",
                    "She slid a coin into the drawer."
            ));
            list.add(new SpellingDetail(
                    "",
                    ""
            ));
            return list;
        }

        private static List<SpellingDetail> getSpellingDetailWordsWithDoubleConsonant() {
            var list = new ArrayList<SpellingDetail>();
            list.add(new SpellingDetail(
                    "Bell",
                    "She rang the bell again."
            ));
            list.add(new SpellingDetail(
                    "Shell",
                    "A snail's shell is spiral in form."
            ));
            list.add(new SpellingDetail(
                    "Hill",
                    "We climbed up the hill."
            ));
            list.add(new SpellingDetail(
                    "Bitter",
                    "Cocoa beans have a bitter flavor."
            ));
            list.add(new SpellingDetail(
                    "Stuff",
                    "We should do more stuff together."
            ));
            list.add(new SpellingDetail(
                    "Pass",
                    "They pass the library every morning on their way to school."
            ));
            list.add(new SpellingDetail(
                    "Class",
                    "Fred often comes late for class."
            ));
            list.add(new SpellingDetail(
                    "Fell",
                    "The old woman got hurt when she fell."
            ));
            list.add(new SpellingDetail(
                    "Smell",
                    "I can smell some delicious food from far away."
            ));
            return list;
        }

        private static List<SpellingDetail> getSpellingDetailWordsWithLongOSound() {
            var list = new ArrayList<SpellingDetail>();
            list.add(new SpellingDetail(
                    "Home",
                    "If it gets boring, I'll go home."
            ));
            list.add(new SpellingDetail(
                    "Hole",
                    "They are digging a hole."
            ));
            list.add(new SpellingDetail(
                    "Rope",
                    "It was a small rope."
            ));
            list.add(new SpellingDetail(
                    "Joke",
                    "It was just a joke."
            ));
            list.add(new SpellingDetail(
                    "Bone",
                    "He broke a bone in his left arm."
            ));
            list.add(new SpellingDetail(
                    "Poke",
                    "Be careful not to poke someone in the eye with your umbrella."
            ));
            list.add(new SpellingDetail(
                    "So",
                    "I am so hungry."
            ));
            list.add(new SpellingDetail(
                    "No",
                    "There are no toys left in the store."
            ));
            list.add(new SpellingDetail(
                    "Go",
                    "I have to go to sleep."
            ));
            list.add(new SpellingDetail(
                    "Snow",
                    "The snow will soon disappear."
            ));
            list.add(new SpellingDetail(
                    "Boat",
                    "Get in the boat."
            ));
            list.add(new SpellingDetail(
                    "Toe",
                    "He hurt his toe."
            ));
            list.add(new SpellingDetail(
                    "",
                    ""
            ));
            return list;
        }

        private static List<SpellingDetail> getSpellingDetailWordsWithSh() {
            var list = new ArrayList<SpellingDetail>();
            list.add(new SpellingDetail(
                    "Ship",
                    "We saw a large ship."
            ));
            list.add(new SpellingDetail(
                    "Shop",
                    "I went to a coffee shop."
            ));
            list.add(new SpellingDetail(
                    "Shut",
                    "Please shut the door."
            ));
            list.add(new SpellingDetail(
                    "Dish",
                    "This dish is very tasty."
            ));
            list.add(new SpellingDetail(
                    "Fish",
                    "The boy caught a fish."
            ));
            list.add(new SpellingDetail(
                    "Wish",
                    "I made a wish on my birthday."
            ));
            list.add(new SpellingDetail(
                    "Rush",
                    "Don’t rush."
            ));
            list.add(new SpellingDetail(
                    "Shoes",
                    "She bought new shoes."
            ));
            list.add(new SpellingDetail(
                    "Shed",
                    "They shed tears of joy."
            ));
            return list;
        }
    }
}
