package com.maths.beyond_school_280720220930.database.log;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Log {

    @ColumnInfo(name="log_content")
    public String   log_content;

    @PrimaryKey(autoGenerate = true)
    public int  log_id;
}
