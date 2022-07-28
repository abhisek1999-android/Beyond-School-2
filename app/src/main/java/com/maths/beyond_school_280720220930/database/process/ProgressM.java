package com.maths.beyond_school_280720220930.database.process;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ProgressM {

    @ColumnInfo(name="date")
    public String date;

    @ColumnInfo(name="table")
    public String  table;

    @ColumnInfo(name="time_to_complete")
    public String  time_to_complete;

    @ColumnInfo(name="correct")
    public int   correct;

    @ColumnInfo(name="time")
    public String  time;

    @ColumnInfo(name="wrong")
    public int    wrong;

    @ColumnInfo(name="timestamp")
    public  long  timestamp;

    @ColumnInfo(name="is_complete")
    public String   is_completed;

    @PrimaryKey(autoGenerate = true)
    public int  progress_id;

}
