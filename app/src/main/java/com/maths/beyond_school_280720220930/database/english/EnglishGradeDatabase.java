package com.maths.beyond_school_280720220930.database.english;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.maths.beyond_school_280720220930.database.english.vocabulary.VocabularyDao;
import com.maths.beyond_school_280720220930.database.english.vocabulary.VocabularyList;
import com.maths.beyond_school_280720220930.database.english.vocabulary.model.VocabularyModel;
import com.maths.beyond_school_280720220930.database.english.vocabulary.model.VocabularyDetailsConverter;
import com.maths.beyond_school_280720220930.database.english.vocabulary.model.VocabularyModelConverter;

@Database(entities = {VocabularyModel.class}, version = 1)
@TypeConverters({VocabularyDetailsConverter.class, VocabularyModelConverter.class})
abstract public class EnglishGradeDatabase extends RoomDatabase {

    public abstract VocabularyDao englishDao();

    private static EnglishGradeDatabase INSTANCE;

    public static EnglishGradeDatabase getDbInstance(Context context) {

        if (INSTANCE == null) {

            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), EnglishGradeDatabase.class, "english_db")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private VocabularyDao vocabularyDao;

        public PopulateDbAsyncTask(EnglishGradeDatabase db) {
            this.vocabularyDao = db.englishDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            vocabularyDao.insert(VocabularyList.GradeOneVocabulary.englishListGrade1());
            vocabularyDao.insert(VocabularyList.GradeTwoVocabulary.englishListGrade2());
            vocabularyDao.insert(VocabularyList.GradeThreeVocabulary.englishListGrade3());
            return null;
        }
    }

}
