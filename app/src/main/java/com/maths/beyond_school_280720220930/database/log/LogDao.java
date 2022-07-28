package com.maths.beyond_school_280720220930.database.log;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.maths.beyond_school_280720220930.database.process.ProgressM;

import java.util.List;

@Dao
public interface LogDao {
    @Query("SELECT * FROM log")
    List<Log> getAllProgress();

    @Insert
    void insertNotes(Log...progresses);

    @Query("DELETE FROM log")
    void deleteAll();

    @Delete
    void delete(Log progress);
}
