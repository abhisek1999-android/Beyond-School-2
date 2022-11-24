package com.maths.beyond_school_new_071020221400.database.grade_tables;

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

    @Query("SELECT * FROM grades")
   // @RawQuery(observedEntities = Grades_data.class)
    List<Grades_data> values();

    @Query("SELECT * FROM grades WHERE subject=:subSubject")
   // @RawQuery(observedEntities = Grades_data.class)
    List<Grades_data> valuesOnSubject(String subSubject);

    //@Query("SELECT * FROM grades")
    @RawQuery(observedEntities = Grades_data.class)
    List<Grades_data> values(SupportSQLiteQuery query);


    @Query("SELECT * FROM grades WHERE subject=:subject and is_completed=0 LIMIT 1")
    List<Grades_data> getFirstUnlockItem(String subject);

//    @Query("UPDATE grades SET is_completed = 1 WHERE chapter_name=:chapter")
//    List<Grades_data> updateComplete(String chapter);
//
//    @Query("UPDATE grades SET unlock = 1 WHERE chapter_name=:chapter")
//    List<Grades_data> updateUnlock(String chapter);


    /*@Query("SELECT * FROM Grades_data WHERE date=:date ORDER BY timestamp DESC")
    List<ProgressM> getAllProgressByDate(String date);

    @Query("SELECT date,SUM(correct) AS total_correct,SUM(wrong) AS total_wrong FROM Grades_data GROUP BY date ORDER BY timestamp DESC")
    List<ProgressDate> getSumOFData();


    @Query("SELECT `table` ,COUNT(`table`) AS count,SUM(correct) AS total_correct,SUM(time_to_complete) AS total_time,SUM(wrong) AS total_wrong FROM Grades_data WHERE date=:date GROUP BY `table`")
    List<ProgressTableWise> getSumOFTableDataByDate(String date);*/


    @Query("UPDATE grades SET unlock=:is_lock WHERE chapter_name = :chapter")
    void update(boolean is_lock, String chapter);

    @Query("UPDATE grades SET is_completed=:is_lock WHERE chapter_name = :chapter")
    void updateIsComplete(boolean is_lock, String chapter);


    @Query("SELECT * FROM grades WHERE chapter_name LIKE '%'||:chapter||'%'")
    List<Grades_data> getChapter(String chapter);

    @Query("SELECT DISTINCT(subject) FROM grades")
    String[] getChapterNames();

    @Query("SELECT COUNT(*) FROM grades WHERE subject=:subject")
    int getSubjectCount(String subject);

    @Query("SELECT COUNT(*) FROM grades WHERE subject=:subject and is_completed=1")
    int getSubjectCompleteCount(String subject);


    @Insert
    void insertNotes(Grades_data... progresses);

    @Delete
    void delete(Grades_data progress);


}
