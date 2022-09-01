package com.maths.beyond_school_280720220930.english_activity.spelling.spelling_test;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.english.spelling.model.SpellingDetail;
import com.maths.beyond_school_280720220930.databinding.FragmentSpellingTestBinding;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.Locale;
import java.util.Objects;

public class SpellingTestFragment extends Fragment {


    private FragmentSpellingTestBinding binding;
    private final SpellingDetail spellingDetail;
    private final int pos;

    public SpellingTestFragment(SpellingDetail spellingDetail, int pos) {
        super(R.layout.fragment_spelling_test);
        this.spellingDetail = spellingDetail;
        this.pos = pos;
    }


    public TextView getTextView() {
        return binding.textViewPart1;
    }

    public LottieAnimationView getAnimationView() {
        return binding.animationVoice;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentSpellingTestBinding.bind(view);

        var split = spellingDetail.getDescription().toLowerCase(Locale.ROOT).split(spellingDetail.getWord().toLowerCase(Locale.ROOT), 2);

        binding.textViewPart1.setText(Html.fromHtml(UtilityFunctions.capitalize(split[0]) + "<font color='#64c1c7'>" +
                spellingDetail.getWord().replaceAll("[A-Za-z]", "_") + "</font>"
                + split[1]));

        var viewPager = (ViewPager2) requireActivity().findViewById(R.id.viewPager_test);
        binding.progress.setText(getResources()
                .getString(R.string.current_by_all,
                        String.valueOf(pos),
                        String.valueOf(Objects.requireNonNull(viewPager.getAdapter()).getItemCount())));
    }

    //function to capitalize first letter of each word

}
