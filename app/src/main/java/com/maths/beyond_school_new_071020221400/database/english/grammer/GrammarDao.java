package com.maths.beyond_school_new_071020221400.database.english.grammer;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.maths.beyond_school_new_071020221400.database.english.grammer.model.GrammarType;

import java.util.List;

@Dao
public interface GrammarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GrammarType grammarType);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<GrammarType> grammarTypeList);

    @Query("SELECT * FROM grammar_table")
    List<GrammarType> getAllGrammar();
}
