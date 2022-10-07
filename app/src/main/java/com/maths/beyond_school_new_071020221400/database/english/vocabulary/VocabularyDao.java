package com.maths.beyond_school_new_071020221400.database.english.vocabulary;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.maths.beyond_school_new_071020221400.database.english.vocabulary.model.VocabularyModel;

@Dao
public interface VocabularyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(VocabularyModel vocabularyModel);

    @Update
    public void update(VocabularyModel vocabularyModel);

    @Query("SELECT * FROM english_table WHERE grade = :grade")
    public VocabularyModel getEnglishModel(int grade);
}
