package com.maths.beyond_school_new_071020221400.database.process;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.maths.beyond_school_new_071020221400.model.ProgressDate;

import java.util.List;

@Dao
public interface ProgressDao {

    @Query("SELECT * FROM progressM ORDER BY timestamp DESC")
    List<ProgressM> getAllProgress();

    @Query("SELECT * FROM progressM WHERE date=:date ORDER BY timestamp DESC")
    List<ProgressM> getAllProgressByDate(String date);

    @Query("SELECT date,SUM(correct) AS total_correct,SUM(wrong) AS total_wrong FROM progressM GROUP BY date ORDER BY timestamp DESC")
    List<ProgressDate> getSumOFData();

    @Query("SELECT * FROM progressM WHERE chapter=:chapter")
    List<ProgressM> isAvailable(String chapter);

//    @Query("SELECT `table` ,COUNT(`table`) AS count,SUM(correct) AS total_correct,SUM(time_to_complete) AS total_time,SUM(wrong) AS total_wrong FROM progressM WHERE date=:date GROUP BY `table`")
//    List<ProgressTableWise> getSumOFTableDataByDate(String date);

    @Query("UPDATE progressM SET time_spend=:time WHERE chapter = :chapter")
    void update(long time, String chapter);
    @Query("UPDATE progressM SET correct=:correct , wrong=:wrong WHERE chapter = :chapter")
    void updateScore(long correct,long wrong, String chapter);

    @Query("SELECT time_spend FROM progressM WHERE sub_id =:sub_id AND chapter=:chapter")
    long getTimeSpend(String sub_id, String chapter);

    @RawQuery(observedEntities = ProgressM.class)
    long correctValues(SupportSQLiteQuery query);

    @Insert
    void insertNotes(ProgressM...progresses);

    @Delete
    void delete(ProgressM progress);


}
