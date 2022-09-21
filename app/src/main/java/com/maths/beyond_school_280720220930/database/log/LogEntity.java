package com.maths.beyond_school_280720220930.database.log;


import androidx.annotation.Keep;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Keep
@Entity
public class LogEntity {

    @ColumnInfo(name = "log_content")
    private String log_content;

    @ColumnInfo(name = "timestamp")
    private String timestamp;

    @PrimaryKey(autoGenerate = true)
    private int log_id;

    public LogEntity(String log_content, String timestamp) {
        this.log_content = log_content;
        this.timestamp = timestamp;
    }

    public String getLog_content() {
        return log_content;
    }

    public void setLog_content(String log_content) {
        this.log_content = log_content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
    }
}
