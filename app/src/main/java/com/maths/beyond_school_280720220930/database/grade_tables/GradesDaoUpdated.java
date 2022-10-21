package com.maths.beyond_school_280720220930.database.grade_tables;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface GradesDaoUpdated {

    @Query("SELECT * FROM grade")
    List<GradeData> getChapter();

    @Insert
    void insertNotes(GradeData... progresses);

    @Delete
    void delete(GradeData progress);

}
