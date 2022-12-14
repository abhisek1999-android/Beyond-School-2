package com.maths.beyond_school_new_071020221400.database.log;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {LogEntity.class},version =1)
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
