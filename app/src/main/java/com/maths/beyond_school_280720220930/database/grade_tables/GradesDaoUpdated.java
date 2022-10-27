package com.maths.beyond_school_280720220930.database.grade_tables;

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
    List<GradeData> getSubjectData(String subject);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNotes(List<GradeData> gradeData);

    @Delete
    void delete(GradeData progress);

}
