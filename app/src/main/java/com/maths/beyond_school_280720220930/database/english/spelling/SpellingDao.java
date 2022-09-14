package com.maths.beyond_school_280720220930.database.english.spelling;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.maths.beyond_school_280720220930.database.english.spelling.model.SpellingType;

import java.util.List;

@Dao
public interface SpellingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SpellingType spellingType);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SpellingType> spellingTypes);

    @Delete
    void delete(SpellingType spellingType);


    @Query("SELECT * FROM spelling_table")
    List<SpellingType> getAllSpelling();
}
