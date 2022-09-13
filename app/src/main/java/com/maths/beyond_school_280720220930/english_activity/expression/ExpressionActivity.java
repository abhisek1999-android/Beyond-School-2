package com.maths.beyond_school_280720220930.english_activity.expression;

import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_SPELLING_DETAIL;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.english.EnglishGradeDatabase;
import com.maths.beyond_school_280720220930.database.english.expression.ExpressionDao;
import com.maths.beyond_school_280720220930.database.log.LogDatabase;
import com.maths.beyond_school_280720220930.database.process.ProgressDataBase;
import com.maths.beyond_school_280720220930.database.process.ProgressM;
import com.maths.beyond_school_280720220930.databinding.ActivityExpressionBinding;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.ArrayList;
import java.util.List;

public class ExpressionActivity extends AppCompatActivity {

    private ActivityExpressionBinding binding;

    private List<ProgressM> progressData;
    private ProgressDataBase progressDataBase;
    private LogDatabase logDatabase;
    private ExpressionDao expressionDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityExpressionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        logDatabase = LogDatabase.getDbInstance(this);
        progressDataBase = ProgressDataBase.getDbInstance(this);
        progressData = new ArrayList<>();

        setToolbar();
        setData();
    }

    private void setToolbar() {
    }

    private void setData(){
        if (getIntent().hasExtra(EXTRA_SPELLING_DETAIL)) {
//            spellings = UtilityFunctions.getSpellingsFromString(getIntent().getStringExtra(EXTRA_SPELLING_DETAIL).trim());
//            expressionDao = EnglishGradeDatabase.getDbInstance(this).expressionDao());
//            setViews();
//            buttonClick();
//            binding.giveTestButton.setOnClickListener((view) -> {
//                navigateToTest();
//            });
        } else {
            UtilityFunctions.simpleToast(this, "No data found");
        }
    }
}