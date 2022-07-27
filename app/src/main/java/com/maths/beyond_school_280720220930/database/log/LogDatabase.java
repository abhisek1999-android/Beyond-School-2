package com.maths.beyond_school_280720220930.database.log;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Log.class},version =1)
public abstract class LogDatabase extends RoomDatabase {

    public abstract LogDao logDao();

    private static LogDatabase INSTANCE;

    public static  LogDatabase getDbInstance(Context context){

        if (INSTANCE==null){

            INSTANCE= Room.databaseBuilder(context.getApplicationContext(),LogDatabase.class,"log_db")
                    .allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}
