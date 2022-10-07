package com.maths.beyond_school_new_071020221400.english_activity.grammar;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.maths.beyond_school_new_071020221400.R;
import com.maths.beyond_school_new_071020221400.database.english.grammer.model.GrammarModel;
import com.maths.beyond_school_new_071020221400.databinding.FragmentIndentifyingNounsRowBinding;
import com.maths.beyond_school_new_071020221400.utils.UtilityFunctions;

import java.util.Locale;
import java.util.Objects;

public class RowItemFragment extends Fragment {

    private final int pos;
    private final GrammarModel grammarModel;
    private final String category;


    public RowItemFragment(GrammarModel grammarModel, int pos, String category) {
        super(R.layout.fragment_indentifying_nouns_row);
        this.pos = pos;
        this.grammarModel = grammarModel;
        this.category = category;
    }

    private FragmentIndentifyingNounsRowBinding binding;

    public TextView getTextView() {
        return binding.textViewDes;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentIndentifyingNounsRowBinding.bind(view);
        UtilityFunctions.loadImage(grammarModel.getImage(), binding.imageViewSpelling, binding.loadingAnimation);


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
        if (category.equals(getResources().getString(R.string.grammar_3))) {
            var text = "<font color='#64c1c7'>" + grammarModel.getWord() + "</font><br>" + grammarModel.getDescription();
            binding.textViewDes.setText(
                    Html.fromHtml(text,
                            Html.FROM_HTML_MODE_COMPACT)
            );
        } else {
            binding.textViewDes.setText(Html.fromHtml(
                    grammarModel.getDescription().replace(grammarModel.getWord().toLowerCase(Locale.ROOT),
                            "<font color='#64c1c7'>" + grammarModel.getWord().toLowerCase(Locale.ROOT) + "</font>")
                    , Html.FROM_HTML_MODE_COMPACT
            ));
        }
    }
}