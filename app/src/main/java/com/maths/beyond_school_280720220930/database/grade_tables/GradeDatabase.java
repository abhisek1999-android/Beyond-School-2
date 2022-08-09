package com.maths.beyond_school_280720220930.database.grade_tables;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.maths.beyond_school_280720220930.database.converter.Converters;
import com.maths.beyond_school_280720220930.database.process.ProgressDao;
import com.maths.beyond_school_280720220930.database.process.ProgressDataBase;
import com.maths.beyond_school_280720220930.database.process.ProgressM;

@Database(entities = {Grades_data.class},version =1)
@TypeConverters(Converters.class)
public abstract class GradeDatabase extends RoomDatabase {

    public abstract GradesDao gradesDao();

    private static GradeDatabase INSTANCE;

    public static  GradeDatabase getDbInstance(Context context){

        if (INSTANCE==null){

            INSTANCE= Room.databaseBuilder(context.getApplicationContext(),GradeDatabase.class,"grade_db")
                    .allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}
