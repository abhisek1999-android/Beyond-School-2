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

  private final EntityInsertionAdapter<Log> __insertionAdapterOfLog;

  private final EntityDeletionOrUpdateAdapter<Log> __deletionAdapterOfLog;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public LogDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLog = new EntityInsertionAdapter<Log>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Log` (`log_content`,`timestamp`,`log_id`) VALUES (?,?,nullif(?, 0))";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Log value) {
        if (value.log_content == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.log_content);
        }
        if (value.timestamp == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.timestamp);
        }
        stmt.bindLong(3, value.log_id);
      }
    };
    this.__deletionAdapterOfLog = new EntityDeletionOrUpdateAdapter<Log>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Log` WHERE `log_id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Log value) {
        stmt.bindLong(1, value.log_id);
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM log";
        return _query;
      }
    };
  }

  @Override
  public void insertNotes(final Log... progresses) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfLog.insert(progresses);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Log progress) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfLog.handle(progress);
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
  public List<Log> getAllProgress() {
    final String _sql = "SELECT * FROM log";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfLogContent = CursorUtil.getColumnIndexOrThrow(_cursor, "log_content");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final int _cursorIndexOfLogId = CursorUtil.getColumnIndexOrThrow(_cursor, "log_id");
      final List<Log> _result = new ArrayList<Log>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Log _item;
        _item = new Log();
        _item.log_content = _cursor.getString(_cursorIndexOfLogContent);
        _item.timestamp = _cursor.getString(_cursorIndexOfTimestamp);
        _item.log_id = _cursor.getInt(_cursorIndexOfLogId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
