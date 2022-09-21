package com.maths.beyond_school_280720220930.english_activity.grammar.fragments.IndentificationNoun.sub_fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.english.grammer.model.GrammarModel;
import com.maths.beyond_school_280720220930.databinding.FragmentIndentifyingNounsRowBinding;
import com.maths.beyond_school_280720220930.english_activity.grammar.GrammarActivity;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.Objects;

public class RowItemFragment extends Fragment {

    private final int pos;
    private final GrammarModel grammarModel;


    public RowItemFragment(GrammarModel grammarModel, int pos) {
        super(R.layout.fragment_indentifying_nouns_row);
        this.pos = pos;
        this.grammarModel = grammarModel;
    }

    private SentenceButtonClick listener = null;


    public void setSentenceButtonClick(SentenceButtonClick listener) {
        this.listener = listener;
    }

    private FragmentIndentifyingNounsRowBinding binding;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentIndentifyingNounsRowBinding.bind(view);
        UtilityFunctions.loadImage(grammarModel.getImage(), binding.imageViewSpelling, binding.loadingAnimation);


        ViewPager2 viewPager = requireActivity().findViewById(R.id.view_pager_identifying_nouns);

        binding.textViewDes.setText(grammarModel.getDescription());


        binding.imageButtonPrev.setVisibility((pos == 1) ? View.INVISIBLE : View.VISIBLE);
        binding.imageButtonNext.setVisibility((pos == Objects.requireNonNull(viewPager.getAdapter()).getItemCount()) ? View.INVISIBLE : View.VISIBLE);

        var activity = (GrammarActivity) getActivity();

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


    public interface SentenceButtonClick {
        void onSentenceButtonClick(int pos);
    }
}
