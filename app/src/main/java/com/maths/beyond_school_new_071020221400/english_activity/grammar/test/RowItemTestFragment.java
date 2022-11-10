package com.maths.beyond_school_new_071020221400.english_activity.grammar.test;

import android.app.ActionBar;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.maths.beyond_school_new_071020221400.R;
import com.maths.beyond_school_new_071020221400.database.english.grammer.model.GrammarModel;
import com.maths.beyond_school_new_071020221400.databinding.FragmentIndentifyingNounsRowBinding;
import com.maths.beyond_school_new_071020221400.utils.UtilityFunctions;

import java.util.Objects;

public class RowItemTestFragment extends Fragment {

    private final int pos;
    private final GrammarModel grammarModel;
    private final String request;


    public RowItemTestFragment(GrammarModel grammarModel, int pos, String request) {
        super(R.layout.fragment_indentifying_nouns_row);
        this.pos = pos;
        this.grammarModel = grammarModel;
        this.request = request;
    }

    private FragmentIndentifyingNounsRowBinding binding;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentIndentifyingNounsRowBinding.bind(view);
        UtilityFunctions.loadImage(grammarModel.getImage(), binding.imageViewSpelling, binding.loadingAnimation);
        binding.imageViewSpelling.setVisibility(grammarModel.getImage() == null ? View.GONE : View.VISIBLE);

        ViewPager2 viewPager = requireActivity().findViewById(R.id.view_pager_identifying_nouns);
        if (request.equals(getResources().getString(R.string.grammar_3)))
            binding.textViewDesGrammar.setText(grammarModel.getWord());
        else
            binding.textViewDesGrammar.setText(Html.fromHtml(grammarModel.getDescription(), Html.FROM_HTML_MODE_COMPACT));

        //set margin to parent view
        var param = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                requireActivity().getResources()
                        .getDimensionPixelSize(R.dimen.layout_size_grammar));
        param.setMarginStart(requireActivity().getResources()
                .getDimensionPixelSize(R.dimen.grid_2));
        param.setMarginEnd(requireActivity().getResources()
                .getDimensionPixelSize(R.dimen.grid_2));

        binding.rowParent.setLayoutParams(param);

        binding.imageButtonPrev.setVisibility(View.GONE);
        binding.imageButtonNext.setVisibility(View.GONE);


        binding.progress.setText(getResources()
                .getString(R.string.current_by_all,
                        String.valueOf(pos),
                        String.valueOf(Objects.requireNonNull(viewPager.getAdapter()).getItemCount())));
    }
}
