package com.kaustubh.beyond_school;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
GridLayout gridLayout;
CardView[] card =new CardView[19];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridLayout=findViewById(R.id.mainGrid);
        for (int i = 0; i <gridLayout.getChildCount() ; i++) {
            card[i]=(CardView) gridLayout.getChildAt(i);
            card[i].setOnClickListener(this);
        }
    }
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(MainActivity.this,select_action.class);
        for (int i = 0; i <gridLayout.getChildCount() ; i++) {
            if (view.getId()==gridLayout.getChildAt(i).getId()){
                intent.putExtra("value",i+2);
                break;
            }
        }
        startActivity(intent);
    }

    public void buttonClick(View view) {
        startActivity(new Intent(getApplicationContext(),TestActivity.class));
    }
}