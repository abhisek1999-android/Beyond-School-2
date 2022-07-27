package com.maths.beyond_school_280720220930.database.process;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.maths.beyond_school_280720220930.model.ProgressDate;

import java.util.List;

@Dao
public interface ProgressDao {

    @Query("SELECT * FROM progressM ORDER BY timestamp DESC")
    List<ProgressM> getAllProgress();

    @Query("SELECT * FROM progressM WHERE date=:date ORDER BY timestamp DESC")
    List<ProgressM> getAllProgressByDate(String date);

    @Query("SELECT date,SUM(correct) AS total_correct,SUM(wrong) AS total_wrong FROM progressM GROUP BY date")
    List<ProgressDate> getSumOFData();

    @Insert
    void insertNotes(ProgressM...progresses);

    @Delete
    void delete(ProgressM progress);


}
