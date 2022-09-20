package com.maths.beyond_school_280720220930.english_activity.grammar.fragments.IndentificationNoun.sub_fragment;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.english.grammer.model.GrammarModel;
import com.maths.beyond_school_280720220930.databinding.FragmentIndentifyingNounsRowBinding;
import com.maths.beyond_school_280720220930.english_activity.grammar.fragments.IndentificationNoun.IdentifyingNounsFragment;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.Locale;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentIndentifyingNounsRowBinding.bind(view);
        UtilityFunctions.loadImage(grammarModel.getImage(), binding.imageViewSpelling, binding.loadingAnimation);
        var currentWord = grammarModel.getWord().toLowerCase(Locale.ROOT);
        var splitSentence = grammarModel.getDescription().split(currentWord.toLowerCase(Locale.ROOT), 2);

        ViewPager2 viewPager = requireActivity().findViewById(R.id.view_pager_identifying_nouns);
        try {
            binding.textViewStart.setText(splitSentence[0].trim());
            binding.textViewNounWord.setText(currentWord.trim());
            binding.textViewEnd.setText(splitSentence[1].trim());
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        handleTextViewClick();

        binding.imageButtonPrev.setVisibility((pos == 1) ? View.INVISIBLE : View.VISIBLE);
        binding.imageButtonNext.setVisibility((pos == Objects.requireNonNull(viewPager.getAdapter()).getItemCount()) ? View.INVISIBLE : View.VISIBLE);

        var identifyingNounsFragment = (IdentifyingNounsFragment) getParentFragment();

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

    private void handleTextViewClick() {
        binding.textViewStart.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSentenceButtonClick(1);
            }
        });
        binding.textViewNounWord.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSentenceButtonClick(2);
            }
        });

        binding.textViewEnd.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSentenceButtonClick(3);
            }
        });

    }


    public interface SentenceButtonClick {
        void onSentenceButtonClick(int pos);
    }
}
