package com.maths.beyond_school_280720220930.database.english;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.maths.beyond_school_280720220930.database.english.grammer.GrammarDao;
import com.maths.beyond_school_280720220930.database.english.grammer.GrammarList;
import com.maths.beyond_school_280720220930.database.english.grammer.model.GrammarModelTypeConverter;
import com.maths.beyond_school_280720220930.database.english.grammer.model.GrammarType;
import com.maths.beyond_school_280720220930.database.english.grammer.model.GrammarTypeTypeConverter;
import com.maths.beyond_school_280720220930.database.english.spelling_common_words.SpellingCommonWordDao;
import com.maths.beyond_school_280720220930.database.english.spelling_common_words.SpellingCommonWordList;
import com.maths.beyond_school_280720220930.database.english.spelling_common_words.model.SpellingCategoryModelConverter;
import com.maths.beyond_school_280720220930.database.english.spelling_common_words.model.SpellingModel;
import com.maths.beyond_school_280720220930.database.english.spelling_common_words.model.SpellingsDetailsConverter;
import com.maths.beyond_school_280720220930.database.english.spelling_objects.SpellingList;
import com.maths.beyond_school_280720220930.database.english.spelling_objects.SpellingObjectsDao;
import com.maths.beyond_school_280720220930.database.english.spelling_objects.model.SpellingModelConverter;
import com.maths.beyond_school_280720220930.database.english.spelling_objects.model.SpellingType;
import com.maths.beyond_school_280720220930.database.english.spelling_objects.model.SpellingTypeConverter;
import com.maths.beyond_school_280720220930.database.english.vocabulary.VocabularyDao;
import com.maths.beyond_school_280720220930.database.english.vocabulary.VocabularyList;
import com.maths.beyond_school_280720220930.database.english.vocabulary.model.VocabularyDetailsConverter;
import com.maths.beyond_school_280720220930.database.english.vocabulary.model.VocabularyModel;
import com.maths.beyond_school_280720220930.database.english.vocabulary.model.VocabularyModelConverter;

@Database(entities = {VocabularyModel.class,
        SpellingType.class, GrammarType.class,
        SpellingModel.class
}, version = 1)
@TypeConverters({VocabularyDetailsConverter.class, VocabularyModelConverter.class,
        SpellingTypeConverter.class, SpellingModelConverter.class,
        GrammarModelTypeConverter.class, GrammarTypeTypeConverter.class,
        SpellingCategoryModelConverter.class, SpellingsDetailsConverter.class
})
abstract public class EnglishGradeDatabase extends RoomDatabase {

    public abstract VocabularyDao englishDao();

    public abstract SpellingObjectsDao spellingDao();

    public abstract GrammarDao grammarDao();

    public abstract SpellingCommonWordDao spellingCommonWordDao();


    private static EnglishGradeDatabase INSTANCE;

    public static EnglishGradeDatabase getDbInstance(Context context) {

        if (INSTANCE == null) {

            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), EnglishGradeDatabase.class, "english_db")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback(context))
                    .allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomCallback(Context context) {
        return new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                new PopulateDbAsyncTask(INSTANCE, context).execute();
            }
        };
    }

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private final VocabularyDao vocabularyDao;
        private final SpellingObjectsDao spellingObjectsDao;
        private final GrammarDao grammarDao;
        private final SpellingCommonWordDao spellingCommonWordDao;
        //        private final ExpressionDao expressionDao;
        @SuppressLint("StaticFieldLeak")
        private final Context context;


        public PopulateDbAsyncTask(EnglishGradeDatabase db, Context context) {
            this.vocabularyDao = db.englishDao();
            this.spellingObjectsDao = db.spellingDao();
            this.grammarDao = db.grammarDao();
            spellingCommonWordDao = db.spellingCommonWordDao();
            this.context = context;
//            this.expressionDao=db.expressionDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {


//            expressionDao.insert(ExpressionList.GradeOneExpression.englishListGrade1());

            vocabularyDao.insert(VocabularyList.GradeOneVocabulary.englishListGrade1());
            spellingObjectsDao.insertAll(SpellingList.getSpellingList(context));
            grammarDao.insertAll(GrammarList.Noun.getGrammarList(context));
            grammarDao.insertAll(GrammarList.Verb.getGrammarList(context));
            spellingCommonWordDao.insert(SpellingCommonWordList.SpellingGradeOne.getSpellingList());

            return null;
        }
    }

}
