package com.maths.beyond_school_new_071020221400.database.log;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LogDao {
    @Query("SELECT * FROM LogEntity")
    List<LogEntity> getAllProgress();

    @Insert
    void insertNotes(LogEntity...progresses);

    @Query("DELETE FROM LogEntity")
    void deleteAll();

    @Delete
    void delete(LogEntity progress);
}
