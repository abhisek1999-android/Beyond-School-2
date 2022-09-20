package com.maths.beyond_school_280720220930.english_activity.grammar.fragments.common_and_proper_noun.sub_fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.english.grammer.model.GrammarModel;
import com.maths.beyond_school_280720220930.databinding.FragmentGrammarOtherBinding;
import com.maths.beyond_school_280720220930.english_activity.grammar.fragments.common_and_proper_noun.CommonAndProperNounFragment;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.Objects;

public class RowItemCommonAndProperNoun extends Fragment {


    private final int pos;
    private final GrammarModel grammarModel;
    private FragmentGrammarOtherBinding binding;
    private int request;

    public RowItemCommonAndProperNoun(GrammarModel grammarModel, int pos) {
        super(R.layout.fragment_grammar_other);
        this.pos = pos;
        this.grammarModel = grammarModel;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentGrammarOtherBinding.bind(view);

        UtilityFunctions.loadImage(grammarModel.getImage().trim(), binding.imageViewSpelling, binding.loadingAnimation);


        ViewPager2 viewPager = requireActivity().findViewById(R.id.view_pager_identifying_nouns);


        binding.textViewWord.setText(grammarModel.getWord());
        binding.textViewWord.setTextColor(
                ContextCompat.getColor(
                        requireContext(),
                        R.color.primary
                )
        );

        binding.imageButtonPrev.setVisibility((pos == 1) ? View.INVISIBLE : View.VISIBLE);
        binding.imageButtonNext.setVisibility((pos == Objects.requireNonNull(viewPager.getAdapter()).getItemCount()) ? View.INVISIBLE : View.VISIBLE);

        var parentFragment = (CommonAndProperNounFragment) getParentFragment();

        binding.imageButtonNext.setOnClickListener(v -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
//            Objects.requireNonNull(identifyingNounsFragment).restartEngine();
        });
        binding.imageButtonPrev.setOnClickListener(v -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
//            Objects.requireNonNull(identifyingNounsFragment).restartEngine();
        });
        binding.progress.setText(getResources()
                .getString(R.string.current_by_all,
                        String.valueOf(pos),
                        String.valueOf(Objects.requireNonNull(viewPager.getAdapter()).getItemCount())));
    }
}
