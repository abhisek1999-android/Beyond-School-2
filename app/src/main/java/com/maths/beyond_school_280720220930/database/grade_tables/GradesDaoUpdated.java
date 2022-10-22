package com.maths.beyond_school_280720220930.database.grade_tables;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GradesDaoUpdated {

    @Query("SELECT * FROM grade")
    List<GradeData> getChapter();

    @Insert
    void insertNotes(List<GradeData> gradeData);

    @Delete
    void delete(GradeData progress);

}
