package com.maths.beyond_school_280720220930.database.english.spelling;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.maths.beyond_school_280720220930.database.english.spelling.model.SpellingModel;

@Dao
public interface SpellingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SpellingModel spellingModel);

    @Update
    void update(SpellingModel spellingModel);

    @Query("SELECT * FROM spelling_table WHERE grade = :grade")
    SpellingModel getSpellingModel(int grade);
}
