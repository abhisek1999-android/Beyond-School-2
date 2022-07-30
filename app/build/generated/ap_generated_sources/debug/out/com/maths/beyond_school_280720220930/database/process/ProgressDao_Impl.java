package com.maths.beyond_school_280720220930.database.process;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.maths.beyond_school_280720220930.model.ProgressDate;
import com.maths.beyond_school_280720220930.model.ProgressTableWise;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ProgressDao_Impl implements ProgressDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ProgressM> __insertionAdapterOfProgressM;

  private final EntityDeletionOrUpdateAdapter<ProgressM> __deletionAdapterOfProgressM;

  public ProgressDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfProgressM = new EntityInsertionAdapter<ProgressM>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `ProgressM` (`date`,`table`,`time_to_complete`,`correct`,`time`,`wrong`,`timestamp`,`is_complete`,`progress_id`) VALUES (?,?,?,?,?,?,?,?,nullif(?, 0))";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ProgressM value) {
        if (value.date == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.date);
        }
        if (value.table == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.table);
        }
        stmt.bindLong(3, value.time_to_complete);
        stmt.bindLong(4, value.correct);
        if (value.time == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.time);
        }
        stmt.bindLong(6, value.wrong);
        stmt.bindLong(7, value.timestamp);
        if (value.is_completed == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.is_completed);
        }
        stmt.bindLong(9, value.progress_id);
      }
    };
    this.__deletionAdapterOfProgressM = new EntityDeletionOrUpdateAdapter<ProgressM>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `ProgressM` WHERE `progress_id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ProgressM value) {
        stmt.bindLong(1, value.progress_id);
      }
    };
  }

  @Override
  public void insertNotes(final ProgressM... progresses) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfProgressM.insert(progresses);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final ProgressM progress) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfProgressM.handle(progress);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<ProgressM> getAllProgress() {
    final String _sql = "SELECT * FROM progressM ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfTable = CursorUtil.getColumnIndexOrThrow(_cursor, "table");
      final int _cursorIndexOfTimeToComplete = CursorUtil.getColumnIndexOrThrow(_cursor, "time_to_complete");
      final int _cursorIndexOfCorrect = CursorUtil.getColumnIndexOrThrow(_cursor, "correct");
      final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
      final int _cursorIndexOfWrong = CursorUtil.getColumnIndexOrThrow(_cursor, "wrong");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_complete");
      final int _cursorIndexOfProgressId = CursorUtil.getColumnIndexOrThrow(_cursor, "progress_id");
      final List<ProgressM> _result = new ArrayList<ProgressM>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ProgressM _item;
        _item = new ProgressM();
        _item.date = _cursor.getString(_cursorIndexOfDate);
        _item.table = _cursor.getString(_cursorIndexOfTable);
        _item.time_to_complete = _cursor.getLong(_cursorIndexOfTimeToComplete);
        _item.correct = _cursor.getLong(_cursorIndexOfCorrect);
        _item.time = _cursor.getString(_cursorIndexOfTime);
        _item.wrong = _cursor.getLong(_cursorIndexOfWrong);
        _item.timestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        _item.is_completed = _cursor.getString(_cursorIndexOfIsCompleted);
        _item.progress_id = _cursor.getInt(_cursorIndexOfProgressId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<ProgressM> getAllProgressByDate(final String date) {
    final String _sql = "SELECT * FROM progressM WHERE date=? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (date == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, date);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfTable = CursorUtil.getColumnIndexOrThrow(_cursor, "table");
      final int _cursorIndexOfTimeToComplete = CursorUtil.getColumnIndexOrThrow(_cursor, "time_to_complete");
      final int _cursorIndexOfCorrect = CursorUtil.getColumnIndexOrThrow(_cursor, "correct");
      final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
      final int _cursorIndexOfWrong = CursorUtil.getColumnIndexOrThrow(_cursor, "wrong");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_complete");
      final int _cursorIndexOfProgressId = CursorUtil.getColumnIndexOrThrow(_cursor, "progress_id");
      final List<ProgressM> _result = new ArrayList<ProgressM>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ProgressM _item;
        _item = new ProgressM();
        _item.date = _cursor.getString(_cursorIndexOfDate);
        _item.table = _cursor.getString(_cursorIndexOfTable);
        _item.time_to_complete = _cursor.getLong(_cursorIndexOfTimeToComplete);
        _item.correct = _cursor.getLong(_cursorIndexOfCorrect);
        _item.time = _cursor.getString(_cursorIndexOfTime);
        _item.wrong = _cursor.getLong(_cursorIndexOfWrong);
        _item.timestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        _item.is_completed = _cursor.getString(_cursorIndexOfIsCompleted);
        _item.progress_id = _cursor.getInt(_cursorIndexOfProgressId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<ProgressDate> getSumOFData() {
    final String _sql = "SELECT date,SUM(correct) AS total_correct,SUM(wrong) AS total_wrong FROM progressM GROUP BY date ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfTotalCorrect = CursorUtil.getColumnIndexOrThrow(_cursor, "total_correct");
      final int _cursorIndexOfTotalWrong = CursorUtil.getColumnIndexOrThrow(_cursor, "total_wrong");
      final List<ProgressDate> _result = new ArrayList<ProgressDate>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ProgressDate _item;
        _item = new ProgressDate();
        final String _tmpDate;
        _tmpDate = _cursor.getString(_cursorIndexOfDate);
        _item.setDate(_tmpDate);
        final long _tmpTotal_correct;
        _tmpTotal_correct = _cursor.getLong(_cursorIndexOfTotalCorrect);
        _item.setTotal_correct(_tmpTotal_correct);
        final long _tmpTotal_wrong;
        _tmpTotal_wrong = _cursor.getLong(_cursorIndexOfTotalWrong);
        _item.setTotal_wrong(_tmpTotal_wrong);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<ProgressTableWise> getSumOFTableDataByDate(final String date) {
    final String _sql = "SELECT `table` ,COUNT(`table`) AS count,SUM(correct) AS total_correct,SUM(time_to_complete) AS total_time,SUM(wrong) AS total_wrong FROM progressM WHERE date=? GROUP BY `table`";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (date == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, date);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTable = CursorUtil.getColumnIndexOrThrow(_cursor, "table");
      final int _cursorIndexOfCount = CursorUtil.getColumnIndexOrThrow(_cursor, "count");
      final int _cursorIndexOfTotalCorrect = CursorUtil.getColumnIndexOrThrow(_cursor, "total_correct");
      final int _cursorIndexOfTotalTime = CursorUtil.getColumnIndexOrThrow(_cursor, "total_time");
      final int _cursorIndexOfTotalWrong = CursorUtil.getColumnIndexOrThrow(_cursor, "total_wrong");
      final List<ProgressTableWise> _result = new ArrayList<ProgressTableWise>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ProgressTableWise _item;
        _item = new ProgressTableWise();
        final String _tmpTable;
        _tmpTable = _cursor.getString(_cursorIndexOfTable);
        _item.setTable(_tmpTable);
        final long _tmpCount;
        _tmpCount = _cursor.getLong(_cursorIndexOfCount);
        _item.setCount(_tmpCount);
        final long _tmpTotal_correct;
        _tmpTotal_correct = _cursor.getLong(_cursorIndexOfTotalCorrect);
        _item.setTotal_correct(_tmpTotal_correct);
        final long _tmpTotal_time;
        _tmpTotal_time = _cursor.getLong(_cursorIndexOfTotalTime);
        _item.setTotal_time(_tmpTotal_time);
        final long _tmpTotal_wrong;
        _tmpTotal_wrong = _cursor.getLong(_cursorIndexOfTotalWrong);
        _item.setTotal_wrong(_tmpTotal_wrong);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
