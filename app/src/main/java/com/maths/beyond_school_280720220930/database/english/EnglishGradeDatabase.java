package com.maths.beyond_school_280720220930.database.english;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;

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
            var dao = INSTANCE.englishDao();
            var vocabularyDetail = new ArrayList<VocabularyDetails>();
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "bath",
                            "You fill it with water and use to wash the body.",
                            "https://www.anglomaniacy.pl/img/xv-bath.png.pagespeed.ic.uB6miierqO.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "brush",
                            "A device used to brush and tidy hair.",
                            "https://www.anglomaniacy.pl/img/xv-brush.png.pagespeed.ic.0X3s6eyE6w.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "comb",
                            "A flat device with narrow pointed teeth.",
                            "https://www.anglomaniacy.pl/img/xv-comb.png.pagespeed.ic.4TNQjOLNjJ.webp"
                    )
            );
            vocabularyDetail.add(
                    new VocabularyDetails(
                            "Mirror",
                            "When you look at it you can see yourself reflected in it.",
                            "https://www.anglomaniacy.pl/img/xv-mirror.png.pagespeed.ic.wR4A4ITISX.webp"
                    )
            );
            var listVocabulary = new ArrayList<VocabularyModel>();
            listVocabulary.add(
                    new VocabularyModel(
                            "Bathroom",
                            vocabularyDetail
                    )
            );
            var EnglishModel = new EnglishModel(
                    1,
                    listVocabulary
            );

            dao.insert(EnglishModel);
        }
    };

}
