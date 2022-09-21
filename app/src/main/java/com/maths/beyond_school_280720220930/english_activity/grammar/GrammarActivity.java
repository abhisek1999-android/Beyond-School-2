package com.maths.beyond_school_280720220930.english_activity.grammar;

import static com.maths.beyond_school_280720220930.utils.Constants.EXTRA_GRAMMAR_CATEGORY;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.maths.beyond_school_280720220930.LogActivity;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.databinding.ActivityGrammarBinding;
import com.maths.beyond_school_280720220930.english_activity.grammar.fragments.IndentificationNoun.IdentifyingNounsFragment;
import com.maths.beyond_school_280720220930.english_activity.grammar.fragments.Irregular_plural_noun.IrregularPluralNounFragment;
import com.maths.beyond_school_280720220930.english_activity.grammar.fragments.common_and_proper_noun.CommonAndProperNounFragment;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

public class GrammarActivity extends AppCompatActivity {

    private static final String TAG = "GrammarActivity";
    private final Context context = GrammarActivity.this;
    private ActivityGrammarBinding binding;
    private Fragment currentFragment;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGrammarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        currentFragment = getFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_grammar, currentFragment)
                .commit();
        setToolbar();
        textButtonClick();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Fragment getFragment() {
        if (getIntent().getStringExtra(EXTRA_GRAMMAR_CATEGORY).equals(
                getResources().getString(R.string.grammar_1)
        )) {
            return new IdentifyingNounsFragment();
        } else if (getIntent().getStringExtra(EXTRA_GRAMMAR_CATEGORY).equals(
                getResources().getString(R.string.grammar_2)
        )) {
            return new IrregularPluralNounFragment();
        } else {
            return new CommonAndProperNounFragment();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onDestroyEngine() {
        if (getIntent().getStringExtra(EXTRA_GRAMMAR_CATEGORY).equals(
                getResources().getString(R.string.grammar_1)
        )) {
            ((IdentifyingNounsFragment) currentFragment).destroyEngine();
        } else if (getIntent().getStringExtra(EXTRA_GRAMMAR_CATEGORY).equals(
                getResources().getString(R.string.grammar_2)
        )) {
            ((IrregularPluralNounFragment) currentFragment).destroyEngine();
        } else {
            ((CommonAndProperNounFragment) currentFragment).destroyEngine();
        }
    }

    private void textButtonClick() {
        binding.giveTestButton.setOnClickListener(v -> {
            UtilityFunctions.simpleToast(context, "TODO : Implement Test");
        });
    }

    private void setToolbar() {
        binding.toolBar.imageViewBack.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                onBackPressed();
            }
        });
        binding.toolBar.titleText.setText(getResources().getString(R.string.grammar));
        binding.textViewCategory.setText(getIntent().
                getStringExtra(EXTRA_GRAMMAR_CATEGORY)
                .replace("Grammar", ""));

        binding.toolBar.getRoot().inflateMenu(R.menu.log_menu);
        binding.toolBar.getRoot().setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_log) {
                startActivity(new Intent(getApplicationContext(), LogActivity.class));

                return true;
            }
            return super.onOptionsItemSelected(item);

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onDestroyEngine();
    }
}