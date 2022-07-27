package com.maths.beyond_school_280720220930.database.process;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProgressDao {

    @Query("SELECT * FROM progressM")
    List<ProgressM> getAllProgress();

    @Insert
    void insertNotes(ProgressM...progresses);

    @Delete
    void delete(ProgressM progress);


}
