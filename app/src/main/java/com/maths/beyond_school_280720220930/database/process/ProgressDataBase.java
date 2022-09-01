package com.maths.beyond_school_280720220930.database.process;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {ProgressM.class},version =1)
public abstract class ProgressDataBase extends RoomDatabase {

    public abstract ProgressDao progressDao();

    private static ProgressDataBase INSTANCE;

    public static  ProgressDataBase getDbInstance(Context context){

        if (INSTANCE==null){

            INSTANCE= Room.databaseBuilder(context.getApplicationContext(),ProgressDataBase.class,"progress_db")
                    .allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

}
