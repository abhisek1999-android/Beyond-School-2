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

    @Query("SELECT COUNT(*) FROM grade WHERE subject=:subject")
    int getSubjectCount(String subject);


    @Query("SELECT * FROM grade WHERE subject=:subject LIMIT 2")
    LiveData<List<GradeData>> getSubjectDataLimited(String subject);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNotes(List<GradeData> gradeData);

    @Delete
    void delete(GradeData progress);


    @Query("DELETE FROM grade")
    void deleteAll();

}
