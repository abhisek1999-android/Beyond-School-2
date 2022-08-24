package com.maths.beyond_school_280720220930.database.english.spelling;

import com.maths.beyond_school_280720220930.database.english.spelling.model.SpellingDetail;
import com.maths.beyond_school_280720220930.database.english.spelling.model.SpellingModel;

import java.util.ArrayList;
import java.util.List;

public abstract class SpellingList {
    abstract static class SpellingGradeOne {
        public static List<SpellingModel> getSpellingList() {
            return null;
        }


        public static List<SpellingDetail> getSpellingDetailMostCommonWords() {
            var list = new ArrayList<SpellingDetail>();
            
            return list;
        }
    }
}
