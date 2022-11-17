package com.maths.beyond_school_280720220930.english_activity.grammar;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.english.grammer.model.GrammarModel;
import com.maths.beyond_school_280720220930.databinding.FragmentIndentifyingNounsRowBinding;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.Locale;
import java.util.Objects;

public class RowItemFragment extends Fragment {

    private final int pos;
    private final GrammarModel grammarModel;
    private final String category;
    private final boolean isOnline;


    public RowItemFragment(GrammarModel grammarModel, int pos, String category, boolean isOnline) {
        super(R.layout.fragment_indentifying_nouns_row);
        this.pos = pos;
        this.grammarModel = grammarModel;
        this.category = category;
        this.isOnline = isOnline;
    }

    private FragmentIndentifyingNounsRowBinding binding;

    public TextView getTextView() {
        return binding.textViewDesGrammar;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding = FragmentIndentifyingNounsRowBinding.bind(view);
//        UtilityFunctions.loadImage(grammarModel.getImage(), binding.imageViewSpelling, binding.loadingAnimation);

        try {
            if (grammarModel.getImage().contains(".svg"))
                UtilityFunctions.loadImageSvg(grammarModel.getImage(), binding.imageViewSpelling, binding.loadingAnimation);
            else
                UtilityFunctions.loadImage(grammarModel.getImage(), binding.imageViewSpelling, binding.loadingAnimation);
        } catch (Exception e) {
        }

        binding.imageViewSpelling.setVisibility(grammarModel.getImage() == null
                || grammarModel.getImage().equals("null") ? View.GONE : View.VISIBLE);


        ViewPager2 viewPager = requireActivity().findViewById(R.id.view_pager_identifying_nouns);
        setTextView();


        binding.imageButtonPrev.setVisibility((pos == 1) ? View.INVISIBLE : View.VISIBLE);
        binding.imageButtonNext.setVisibility((pos == Objects.requireNonNull(viewPager.getAdapter()).getItemCount()) ? View.INVISIBLE : View.VISIBLE);

        var activity = (GrammarActivity) getActivity();

        binding.imageButtonNext.setOnClickListener(v -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            if (activity != null) {
                activity.restartEngine();
            }
            setTextView();

        });
        binding.imageButtonPrev.setOnClickListener(v -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            if (activity != null) {
                activity.restartEngine();
            }
            setTextView();
        });
        binding.progress.setText(getResources()
                .getString(R.string.current_by_all,
                        String.valueOf(pos),
                        String.valueOf(Objects.requireNonNull(viewPager.getAdapter()).getItemCount())));
    }

    private void setTextView() {
        if (isOnline) {
            binding.textViewDesGrammar.setText(Html.fromHtml(grammarModel.getDescription(), Html.FROM_HTML_MODE_COMPACT));
            return;
        }
        if (category.equals(getResources().getString(R.string.grammar_3))) {
            var text = "<font color='#64c1c7'>" + grammarModel.getWord() + "</font><br>" + grammarModel.getDescription();
            binding.textViewDesGrammar.setText(
                    Html.fromHtml(text,
                            Html.FROM_HTML_MODE_COMPACT)
            );
        } else {
            binding.textViewDesGrammar.setText(Html.fromHtml(
                    grammarModel.getDescription().replace(grammarModel.getWord().toLowerCase(Locale.ROOT),
                            "<font color='#64c1c7'>" + grammarModel.getWord().toLowerCase(Locale.ROOT) + "</font>")
                    , Html.FROM_HTML_MODE_COMPACT
            ));
        }
    }
}