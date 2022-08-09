package com.maths.beyond_school_280720220930;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.database.grade_tables.Grades_data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TopicsActivity extends AppCompatActivity {
    List<Grades_data> notes;
    GradeDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        database = GradeDatabase.getDbInstance(this);
        try {
            notes = database.gradesDao().valus(new ArrayList<>(Arrays.asList("GRADE 3")));
            Grades_data data = notes.get(0);
            Toast.makeText(this, "" + data.getUrl(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }
}