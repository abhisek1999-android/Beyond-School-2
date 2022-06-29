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
CardView card[]=new CardView[19];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridLayout=findViewById(R.id.mainGrid);
        for (int i = 0; i <19 ; i++) {
            card[i]=(CardView) gridLayout.getChildAt(i);
            card[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(MainActivity.this,select_action.class);
        switch (view.getId()){
            case R.id.card2:{
                intent.putExtra("value",2);
                break;
            }
            case R.id.card3:{
                intent.putExtra("value",3);
                break;
            }
            case R.id.card4:{
                intent.putExtra("value",4);
                break;
            }
            case R.id.card5:{
                intent.putExtra("value",5);
                break;
            }
            case R.id.card6:{
                intent.putExtra("value",6);
                break;
            }
            case R.id.card7:{
                intent.putExtra("value",7);
                break;
            }
            case R.id.card8:{
                intent.putExtra("value",8);
                break;
            }
            case R.id.card9:{
                intent.putExtra("value",9);
                break;
            }
            case R.id.card10:{
                intent.putExtra("value",10);
                break;
            }
            case R.id.card11:{
                intent.putExtra("value",11);
                break;
            }
            case R.id.card12:{
                intent.putExtra("value",12);
                break;
            }
            case R.id.card13:{
                intent.putExtra("value",13);
                break;
            }
            case R.id.card14:{
                intent.putExtra("value",14);
                break;
            }
            case R.id.card15:{
                intent.putExtra("value",15);
                break;
            }
            case R.id.card16:{
                intent.putExtra("value",16);
                break;
            }
            case R.id.card17:{
                intent.putExtra("value",17);
                break;
            }
            case R.id.card18:{
                intent.putExtra("value",18);
                break;
            }
            case R.id.card19:{
                intent.putExtra("value",19);
                break;
            }
            case R.id.card20:{
                intent.putExtra("value",20);
                break;
            }


        }
        startActivity(intent);
    }

}