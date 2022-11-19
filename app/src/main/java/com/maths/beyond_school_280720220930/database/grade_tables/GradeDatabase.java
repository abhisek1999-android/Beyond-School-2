package com.maths.beyond_school_280720220930.database.grade_tables;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;

import com.maths.beyond_school_280720220930.database.converter.Converters;

@Database(entities = {GradeData.class, Grades_data.class}, version = 3)
@TypeConverters(Converters.class)
public abstract class GradeDatabase extends RoomDatabase {


    public abstract GradesDao gradesDao();

    public abstract GradesDaoUpdated gradesDaoUpdated();


    private static GradeDatabase INSTANCE;

    public static GradeDatabase getDbInstance(Context context) {

        if (INSTANCE == null) {

            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GradeDatabase.class, "grade_db").addMigrations(MIGRATION_2_3)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(androidx.sqlite.db.SupportSQLiteDatabase database) {
            database.execSQL("DELETE FROM grade_table");
            database.execSQL("ALTER TABLE grade ADD COLUMN is_completed_ex INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE grade ADD COLUMN unlock_ex INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE grade ADD COLUMN sub_subject TEXT  Default ''");
            database.execSQL("ALTER TABLE grade ADD COLUMN sub_subject_id TEXT  Default ''");
            database.execSQL("ALTER TABLE grade ADD COLUMN screen_type TEXT DEFAULT ''");
            database.execSQL("ALTER TABLE grade ADD COLUMN hint TEXT  DEFAULT ''");
            database.execSQL("ALTER TABLE grade ADD COLUMN question TEXT  DEFAULT ''");

        }
    };
}
