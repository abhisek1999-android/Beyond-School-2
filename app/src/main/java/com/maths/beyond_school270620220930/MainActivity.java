package com.maths.beyond_school270620220930;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
GridLayout gridLayout;
CardView[] card =new CardView[19];
private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID," id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "name");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

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