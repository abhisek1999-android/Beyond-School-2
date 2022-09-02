package com.maths.beyond_school_280720220930.english_activity.spelling;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

    public TextView getWordTextView() {
        return binding.textViewWord;
    }

    public TextView getTextView() {
        return binding.otpViewWord;
    }

    public LottieAnimationView getAnimationView() {
        return binding.animationVoice;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentSpellingBinding.bind(view);
        binding.textViewDes.setText(spellingDetail.getWord());
        binding.textViewWord.setText(spellingDetail.getDescription());

        var viewPager = (ViewPager2) requireActivity().findViewById(R.id.view_pager);
        binding.imageButtonPrev.setVisibility((pos == 1) ? View.INVISIBLE : View.VISIBLE);
        binding.imageButtonNext.setVisibility((pos == Objects.requireNonNull(viewPager.getAdapter()).getItemCount()) ? View.INVISIBLE : View.VISIBLE);
        var activity = (EnglishSpellingActivity) requireActivity();

        binding.imageButtonNext.setOnClickListener(v -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            activity.restartEngine();
        });
        binding.imageButtonPrev.setOnClickListener(v -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            activity.restartEngine();
        });
//        binding.otpViewWord.


        binding.progress.setText(getResources()
                .getString(R.string.current_by_all,
                        String.valueOf(pos),
                        String.valueOf(Objects.requireNonNull(viewPager.getAdapter()).getItemCount())));
    }
}
