package com.maths.beyond_school_280720220930;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.maths.beyond_school_280720220930.SP.PrefConfig;
import com.maths.beyond_school_280720220930.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_280720220930.database.grade_tables.GradeDatabase;
import com.maths.beyond_school_280720220930.database.grade_tables.Grades_data;
import com.maths.beyond_school_280720220930.databinding.ActivityGradeBinding;
import com.maths.beyond_school_280720220930.model.KidsData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GradeActivity extends AppCompatActivity {
    GradeDatabase database;
    Grades_data data;
    Button next;
    TextView grade, desc;
    ActivityGradeBinding binding;
    String grade1;
    String[] desc1 = {""};
    List<String> list1, list2, list4;
    String[] array;
    ArrayAdapter adapter;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private FirebaseFirestore kidsDb = FirebaseFirestore.getInstance();
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGradeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpTextLayoutGrade();

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.textInputLayoutGrade.getEditText().getText().toString().equals(" "))
                    return;
                else{
                Intent intent = new Intent(GradeActivity.this, NameAgeActivity.class);
                intent.putExtra("grade", binding.textInputLayoutGrade.getEditText().getText().toString());
                startActivity(intent);
                finish();}
            }
        });

    }
    private void setUpTextLayoutGrade() {
        array = getResources().getStringArray(R.array.grades);
        adapter = new ArrayAdapter(this, R.layout.list_item, array);
        AutoCompleteTextView editText = Objects.requireNonNull((AutoCompleteTextView) binding.textInputLayoutGrade.getEditText());
        editText.setAdapter(adapter);

//TODO Must be added
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            binding.textInputLayoutGrade.getEditText().setText(binding.textInputLayoutGrade.getAdapter().getItem(9).toString(), false);
//        }

    }


}