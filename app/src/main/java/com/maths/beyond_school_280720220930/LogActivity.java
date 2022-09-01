package com.maths.beyond_school_280720220930;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;


import com.maths.beyond_school_280720220930.adapters.LogAdapter;
import com.maths.beyond_school_280720220930.database.log.LogEntity;
import com.maths.beyond_school_280720220930.database.log.LogDatabase;
import java.util.List;

public class LogActivity extends AppCompatActivity {

    LogAdapter logAdapter;
    RecyclerView logRecyclerView;
    ImageView deleteAllLog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);


        logRecyclerView=findViewById(R.id.logRecyclerView);
        deleteAllLog=findViewById(R.id.deleteButton);
        logRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        logAdapter = new LogAdapter(getApplicationContext());
        logRecyclerView.setAdapter(logAdapter);
        loadNoteList();


        deleteAllLog.setOnClickListener(v->{

            try{
                LogDatabase db = LogDatabase.getDbInstance(this.getApplicationContext());
                db.logDao().deleteAll();
                loadNoteList();
            }catch (Exception e){

            }

        });

    }

    private void loadNoteList() {

        LogDatabase db = LogDatabase.getDbInstance(this.getApplicationContext());
        List<LogEntity> notesList = db.logDao().getAllProgress();
        logAdapter.setNotesList(notesList);

    }
}