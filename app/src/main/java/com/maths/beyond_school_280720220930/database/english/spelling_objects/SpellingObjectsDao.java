package com.maths.beyond_school_280720220930.database.english.spelling_objects;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.maths.beyond_school_280720220930.database.english.spelling_objects.model.SpellingType;

import java.util.List;

@Dao
public interface SpellingObjectsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SpellingType spellingType);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SpellingType> spellingTypes);

    @Delete
    void delete(SpellingType spellingType);


    @Query("SELECT * FROM spelling_object_table")
    List<SpellingType> getAllSpelling();
}
