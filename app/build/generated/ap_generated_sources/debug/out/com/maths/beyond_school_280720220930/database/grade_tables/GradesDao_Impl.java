package com.maths.beyond_school_280720220930.database.grade_tables;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.maths.beyond_school_280720220930.database.converter.Converters;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class GradesDao_Impl implements GradesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Grades_data> __insertionAdapterOfGrades_data;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<Grades_data> __deletionAdapterOfGrades_data;

  public GradesDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGrades_data = new EntityInsertionAdapter<Grades_data>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `grades` (`subject`,`sub_sub`,`chapter`,`grade`,`url`,`progress_id`) VALUES (?,?,?,?,?,nullif(?, 0))";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Grades_data value) {
        if (value.subject == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.subject);
        }
        if (value.subsubject == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.subsubject);
        }
        stmt.bindLong(3, value.chapter);
        final String _tmp;
        _tmp = __converters.fromArrayList(value.grade);
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, _tmp);
        }
        if (value.url == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.url);
        }
        stmt.bindLong(6, value.progress_id);
      }
    };
    this.__deletionAdapterOfGrades_data = new EntityDeletionOrUpdateAdapter<Grades_data>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `grades` WHERE `progress_id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Grades_data value) {
        stmt.bindLong(1, value.progress_id);
      }
    };
  }

  @Override
  public void insertNotes(final Grades_data... progresses) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfGrades_data.insert(progresses);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Grades_data progress) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfGrades_data.handle(progress);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Grades_data> valus(final ArrayList<String> grade) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT ");
    _stringBuilder.append("*");
    _stringBuilder.append(" FROM grades WHERE grade=");
    final int _inputSize = grade.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : grade) {
      if (_item == null) {
        _statement.bindNull(_argIndex);
      } else {
        _statement.bindString(_argIndex, _item);
      }
      _argIndex ++;
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfSubject = CursorUtil.getColumnIndexOrThrow(_cursor, "subject");
      final int _cursorIndexOfSubsubject = CursorUtil.getColumnIndexOrThrow(_cursor, "sub_sub");
      final int _cursorIndexOfChapter = CursorUtil.getColumnIndexOrThrow(_cursor, "chapter");
      final int _cursorIndexOfGrade = CursorUtil.getColumnIndexOrThrow(_cursor, "grade");
      final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
      final int _cursorIndexOfProgressId = CursorUtil.getColumnIndexOrThrow(_cursor, "progress_id");
      final List<Grades_data> _result = new ArrayList<Grades_data>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Grades_data _item_1;
        final String _tmpSubject;
        _tmpSubject = _cursor.getString(_cursorIndexOfSubject);
        final String _tmpSubsubject;
        _tmpSubsubject = _cursor.getString(_cursorIndexOfSubsubject);
        final int _tmpChapter;
        _tmpChapter = _cursor.getInt(_cursorIndexOfChapter);
        final ArrayList<String> _tmpGrade;
        final String _tmp;
        _tmp = _cursor.getString(_cursorIndexOfGrade);
        _tmpGrade = __converters.fromString(_tmp);
        final String _tmpUrl;
        _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
        _item_1 = new Grades_data(_tmpSubject,_tmpSubsubject,_tmpChapter,_tmpGrade,_tmpUrl);
        _item_1.progress_id = _cursor.getInt(_cursorIndexOfProgressId);
        _result.add(_item_1);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
