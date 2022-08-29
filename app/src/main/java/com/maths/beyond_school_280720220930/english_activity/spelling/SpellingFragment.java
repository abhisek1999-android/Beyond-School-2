package com.maths.beyond_school_280720220930.english_activity.spelling;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.english.spelling.model.SpellingDetail;
import com.maths.beyond_school_280720220930.databinding.FragmentSpellingBinding;

import java.util.Objects;

public class SpellingFragment extends Fragment {


    private final SpellingDetail spellingDetail;
    private final int pos;
    private FragmentSpellingBinding binding;

    public SpellingFragment(SpellingDetail spellingDetail, int pos) {
        super(R.layout.fragment_spelling);
        this.spellingDetail = spellingDetail;
        this.pos = pos;
    }

    public TextView getTextView() {
        return binding.otpViewWord;
    }

    public LottieAnimationView getAnimationView() {
        return binding.animationVoice;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentSpellingBinding.bind(view);
        binding.textViewDes.setText(spellingDetail.getDescription());
        binding.textViewWord.setText(spellingDetail.getWord());

        var viewPager = (ViewPager2) requireActivity().findViewById(R.id.view_pager);
        binding.imageButtonPrev.setVisibility((pos == 1) ? View.INVISIBLE : View.VISIBLE);
        binding.imageButtonNext.setVisibility((pos == Objects.requireNonNull(viewPager.getAdapter()).getItemCount()) ? View.INVISIBLE : View.VISIBLE);

        binding.imageButtonNext.setOnClickListener(v -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        });
        binding.imageButtonPrev.setOnClickListener(v -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        });
//        binding.otpViewWord.

        
        binding.progress.setText(getResources()
                .getString(R.string.current_by_all,
                        String.valueOf(pos),
                        String.valueOf(Objects.requireNonNull(viewPager.getAdapter()).getItemCount())));
    }
}
