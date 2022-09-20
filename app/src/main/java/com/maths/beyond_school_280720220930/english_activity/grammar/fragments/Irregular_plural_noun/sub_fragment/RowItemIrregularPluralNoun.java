package com.maths.beyond_school_280720220930.english_activity.grammar.fragments.Irregular_plural_noun.sub_fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.english.grammer.model.GrammarModel;
import com.maths.beyond_school_280720220930.databinding.FragmentGrammarOtherBinding;
import com.maths.beyond_school_280720220930.english_activity.grammar.fragments.Irregular_plural_noun.IrregularPluralNounFragment;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.Objects;

public class RowItemIrregularPluralNoun extends Fragment {

    private final int pos;
    private final GrammarModel grammarModel;
    private int request;


    public RowItemIrregularPluralNoun(GrammarModel grammarModel, int pos) {
        super(R.layout.fragment_grammar_other);
        this.pos = pos;
        this.grammarModel = grammarModel;
        request = 0;
    }

    public LottieAnimationView getLottieAnimationView() {
        return binding.animationVoice;
    }


    private FragmentGrammarOtherBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentGrammarOtherBinding.bind(view);
        UtilityFunctions.loadImage(grammarModel.getImage(), binding.imageViewSpelling, binding.loadingAnimation);


        ViewPager2 viewPager = requireActivity().findViewById(R.id.view_pager_identifying_nouns);

        binding.textViewWord.setText(grammarModel.getDescription());

        binding.imageButtonPrev.setVisibility((pos == 1) ? View.INVISIBLE : View.VISIBLE);
        binding.imageButtonNext.setVisibility((pos == Objects.requireNonNull(viewPager.getAdapter()).getItemCount()) ? View.INVISIBLE : View.VISIBLE);

        var identifyingNounsFragment = (IrregularPluralNounFragment) getParentFragment();

        binding.imageButtonNext.setOnClickListener(v -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            Objects.requireNonNull(identifyingNounsFragment).restartEngine();
        });
        binding.imageButtonPrev.setOnClickListener(v -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            Objects.requireNonNull(identifyingNounsFragment).restartEngine();
        });
        binding.progress.setText(getResources()
                .getString(R.string.current_by_all,
                        String.valueOf(pos),
                        String.valueOf(Objects.requireNonNull(viewPager.getAdapter()).getItemCount())));
    }

}
