package com.maths.beyond_school_new_071020221400.database.english.expression;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.maths.beyond_school_new_071020221400.database.english.expression.model.ExpressionModel;

@Dao
public interface ExpressionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(ExpressionModel expressionModel);

    @Update
    public void update(ExpressionModel expressionModel);

    @Query("SELECT * FROM expression_table WHERE grade = :grade")
    public ExpressionModel getEnglishModel(int grade);
}
