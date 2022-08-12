package com.maths.beyond_school_280720220930.database.english;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface EnglishDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(EnglishModel englishModel);

    @Update
    public void update(EnglishModel englishModel);

    @Query("SELECT * FROM english_table WHERE grade = :grade")
    public EnglishModel getEnglishModel(String grade);
}
