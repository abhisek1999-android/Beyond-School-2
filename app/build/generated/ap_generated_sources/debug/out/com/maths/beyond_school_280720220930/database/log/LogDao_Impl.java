package com.maths.beyond_school_280720220930.database.log;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class LogDao_Impl implements LogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<LogEntity> __insertionAdapterOfLogEntity;

  private final EntityDeletionOrUpdateAdapter<LogEntity> __deletionAdapterOfLogEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public LogDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLogEntity = new EntityInsertionAdapter<LogEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `LogEntity` (`log_content`,`timestamp`,`log_id`) VALUES (?,?,nullif(?, 0))";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, LogEntity value) {
        if (value.getLog_content() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getLog_content());
        }
        if (value.getTimestamp() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTimestamp());
        }
        stmt.bindLong(3, value.getLog_id());
      }
    };
    this.__deletionAdapterOfLogEntity = new EntityDeletionOrUpdateAdapter<LogEntity>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `LogEntity` WHERE `log_id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, LogEntity value) {
        stmt.bindLong(1, value.getLog_id());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM LogEntity";
        return _query;
      }
    };
  }

  @Override
  public void insertNotes(final LogEntity... progresses) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfLogEntity.insert(progresses);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final LogEntity progress) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfLogEntity.handle(progress);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public List<LogEntity> getAllProgress() {
    final String _sql = "SELECT * FROM LogEntity";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfLogContent = CursorUtil.getColumnIndexOrThrow(_cursor, "log_content");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final int _cursorIndexOfLogId = CursorUtil.getColumnIndexOrThrow(_cursor, "log_id");
      final List<LogEntity> _result = new ArrayList<LogEntity>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final LogEntity _item;
        final String _tmpLog_content;
        _tmpLog_content = _cursor.getString(_cursorIndexOfLogContent);
        final String _tmpTimestamp;
        _tmpTimestamp = _cursor.getString(_cursorIndexOfTimestamp);
        _item = new LogEntity(_tmpLog_content,_tmpTimestamp);
        final int _tmpLog_id;
        _tmpLog_id = _cursor.getInt(_cursorIndexOfLogId);
        _item.setLog_id(_tmpLog_id);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
