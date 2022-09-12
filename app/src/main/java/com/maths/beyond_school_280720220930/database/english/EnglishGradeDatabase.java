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

import com.maths.beyond_school_280720220930.database.english.spelling.SpellingDao;
import com.maths.beyond_school_280720220930.database.english.spelling.SpellingList;
import com.maths.beyond_school_280720220930.database.english.spelling.model.SpellingModelConverter;
import com.maths.beyond_school_280720220930.database.english.spelling.model.SpellingType;
import com.maths.beyond_school_280720220930.database.english.spelling.model.SpellingTypeConverter;
import com.maths.beyond_school_280720220930.database.english.vocabulary.VocabularyDao;
import com.maths.beyond_school_280720220930.database.english.vocabulary.VocabularyList;
import com.maths.beyond_school_280720220930.database.english.vocabulary.model.VocabularyDetailsConverter;
import com.maths.beyond_school_280720220930.database.english.vocabulary.model.VocabularyModel;
import com.maths.beyond_school_280720220930.database.english.vocabulary.model.VocabularyModelConverter;

@Database(entities = {VocabularyModel.class, SpellingType.class}, version = 1)
@TypeConverters({VocabularyDetailsConverter.class, VocabularyModelConverter.class,
        SpellingTypeConverter.class, SpellingModelConverter.class
})
abstract public class EnglishGradeDatabase extends RoomDatabase {

    public abstract VocabularyDao englishDao();

    public abstract SpellingDao spellingDao();

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
        private final SpellingDao spellingDao;
        @SuppressLint("StaticFieldLeak")
        private final Context context;

        public PopulateDbAsyncTask(EnglishGradeDatabase db, Context context) {
            this.vocabularyDao = db.englishDao();
            this.spellingDao = db.spellingDao();
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            vocabularyDao.insert(VocabularyList.GradeOneVocabulary.englishListGrade1());

//            Adding Spellings
            spellingDao.insertAll(SpellingList.getSpellingList(context));
            return null;
        }
    }

}
