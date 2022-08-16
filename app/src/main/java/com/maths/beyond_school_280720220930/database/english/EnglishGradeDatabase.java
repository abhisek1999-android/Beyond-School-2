package com.maths.beyond_school_280720220930.database.english;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {EnglishModel.class}, version = 1)
@TypeConverters({VocabularyDetailsConverter.class, VocabularyModelConverter.class})
abstract public class EnglishGradeDatabase extends RoomDatabase {

    public abstract EnglishDao englishDao();

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

        private EnglishDao englishDao;

        public PopulateDbAsyncTask(EnglishGradeDatabase db) {
            this.englishDao = db.englishDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            englishDao.insert(EnglishList.GradeOneVocabulary.englishListGrade1());
            englishDao.insert(EnglishList.GradeTwoVocabulary.englishListGrade2());
            englishDao.insert(EnglishList.GradeThreeVocabulary.englishListGrade3());
            return null;
        }
    }

}
