package com.maths.beyond_school_280720220930.database.grade_tables;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GradesDaoUpdated {

    @Query("SELECT * FROM grade")
    List<GradeData> getChapter();

    @Query("SELECT * FROM grade WHERE subject=:subject")
    LiveData<List<GradeData>> getSubjectData(String subject);

    @Query("SELECT * FROM grade WHERE subject=:subject")
    List<GradeData> getSubjectDataNL(String subject);

    @Query("SELECT COUNT(*) FROM grade WHERE subject=:subject")
    int getSubjectCount(String subject);

    @Query("SELECT COUNT(*) FROM grade WHERE subject=:subject and is_completed=1")
    int getSubjectCompleteCount(String subject);

    @Query("UPDATE grade SET unlock=:is_lock , unlock_ex=:is_lock WHERE chapter_name = :chapter AND subject=:subSubject")
    void update(boolean is_lock, String chapter,String subSubject);

    @Query("UPDATE grade SET is_completed=:is_lock WHERE chapter_name = :chapter")
    void updateIsComplete(boolean is_lock, String chapter);

    @Query("UPDATE grade SET is_completed_ex=:is_lock WHERE chapter_name = :chapter")
    void updateIsCompleteEX(boolean is_lock, String chapter);

    @Query("SELECT * FROM grade WHERE subject=:subject LIMIT 2")
    LiveData<List<GradeData>> getSubjectDataLimited(String subject);

    @Query("SELECT * FROM grade WHERE subject=:subject AND unlock=1 AND is_completed=0 LIMIT 1 ")
    List<GradeData> getSubjectDataIncompleteFirst(String subject);


    @Query("SELECT * FROM grade WHERE id=:id ")
    List<GradeData> getSubjectDataViaID(String id);

    @Query("SELECT DISTINCT(subject) FROM grade")
    String[] getChapterNames();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNotes(List<GradeData> gradeData);

    @Delete
    void delete(GradeData progress);


    @Query("DELETE FROM grade")
    void deleteAll();

    @Query("SELECT DISTINCT(sub_subject) FROM grade WHERE subject=:subject AND sub_subject NOT LIKE ''")
    List<String> getSubSubjects(String subject);

    @Query("SELECT * FROM grade WHERE subject=:subject AND sub_subject=:subSubject")
    List<GradeData> getDataFromSubject(String subject,String subSubject);
}
