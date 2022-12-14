package com.maths.beyond_school_new_071020221400.database.english.spelling_common_words;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.maths.beyond_school_new_071020221400.database.english.spelling_common_words.model.SpellingModel;

@Dao
public interface SpellingCommonWordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SpellingModel spellingModel);

    @Update
    void update(SpellingModel spellingModel);

    @Query("SELECT * FROM spelling_common_word_table WHERE grade = :grade")
    SpellingModel getSpellingModel(int grade);
}
