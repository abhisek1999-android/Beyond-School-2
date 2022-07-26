package com.maths.beyond_school_280720220930.database;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
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
        return "INSERT OR ABORT INTO `ProgressM` (`date`,`table`,`time_to_complete`,`correct`,`time`,`wrong`,`is_complete`,`progress_id`) VALUES (?,?,?,?,?,?,?,nullif(?, 0))";
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
        if (value.time_to_complete == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.time_to_complete);
        }
        if (value.correct == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.correct);
        }
        if (value.time == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.time);
        }
        if (value.wrong == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.wrong);
        }
        if (value.is_completed == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.is_completed);
        }
        stmt.bindLong(8, value.progress_id);
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
    final String _sql = "SELECT * FROM progressM";
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
      final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "is_complete");
      final int _cursorIndexOfProgressId = CursorUtil.getColumnIndexOrThrow(_cursor, "progress_id");
      final List<ProgressM> _result = new ArrayList<ProgressM>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ProgressM _item;
        _item = new ProgressM();
        _item.date = _cursor.getString(_cursorIndexOfDate);
        _item.table = _cursor.getString(_cursorIndexOfTable);
        _item.time_to_complete = _cursor.getString(_cursorIndexOfTimeToComplete);
        _item.correct = _cursor.getString(_cursorIndexOfCorrect);
        _item.time = _cursor.getString(_cursorIndexOfTime);
        _item.wrong = _cursor.getString(_cursorIndexOfWrong);
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
}
