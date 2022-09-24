package com.maths.beyond_school_280720220930.database.grade_tables;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface GradesDao {

    /*@Query("SELECT * FROM grades ORDER BY timestamp DESC")
    List<ProgressM> getAllProgress();*/

    //@Query("SELECT * FROM grades where :val = true")
    @RawQuery(observedEntities = Grades_data.class)
    List<Grades_data> valus(SupportSQLiteQuery query);


    /*@Query("SELECT * FROM Grades_data WHERE date=:date ORDER BY timestamp DESC")
    List<ProgressM> getAllProgressByDate(String date);

    @Query("SELECT date,SUM(correct) AS total_correct,SUM(wrong) AS total_wrong FROM Grades_data GROUP BY date ORDER BY timestamp DESC")
    List<ProgressDate> getSumOFData();


    @Query("SELECT `table` ,COUNT(`table`) AS count,SUM(correct) AS total_correct,SUM(time_to_complete) AS total_time,SUM(wrong) AS total_wrong FROM Grades_data WHERE date=:date GROUP BY `table`")
    List<ProgressTableWise> getSumOFTableDataByDate(String date);*/


    @Query("UPDATE grades SET unlock=:is_lock WHERE chapter = :chapter")
    void update(boolean is_lock, String chapter);

    @Query("UPDATE grades SET is_complete=:is_lock WHERE chapter = :chapter")
    void updateIsComplete(boolean is_lock, String chapter);


    @Query("SELECT * FROM grades WHERE chapter LIKE '%'||:chapter||'%'")
    List<Grades_data> getChapter(String chapter);



    @Insert
    void insertNotes(Grades_data... progresses);

    @Delete
    void delete(Grades_data progress);


}
