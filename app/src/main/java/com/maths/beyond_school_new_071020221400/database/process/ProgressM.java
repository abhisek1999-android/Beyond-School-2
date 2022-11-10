package com.maths.beyond_school_new_071020221400.database.process;

import androidx.annotation.Keep;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Keep
@Entity
public class ProgressM {

    @ColumnInfo(name="date")
    public String date;

    @ColumnInfo(name="sub_id")
    public String  sub_id;

    @ColumnInfo(name="time_to_complete")
    public long  time_to_complete;

    @ColumnInfo(name="correct")
    public long   correct;

    @ColumnInfo(name="time")
    public String  time;

    @ColumnInfo(name="wrong")
    public long    wrong;

    @ColumnInfo(name="timestamp")
    public  long  timestamp;

    @ColumnInfo(name="is_complete")
    public String   is_completed;
    @ColumnInfo(name="time_spend")
    public long   time_spend;

    @ColumnInfo(name="subject")
    public String  subject;

    @ColumnInfo(name="chapter")
    public String  chapter;

    @PrimaryKey(autoGenerate = true)
    public int  progress_id;

}
