package com.maths.beyond_school_280720220930.english_activity.vocabulary.practice;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.english.model.VocabularyDetails;
import com.maths.beyond_school_280720220930.databinding.FragmentVocabularyTestBinding;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.Objects;

public class VocabularyTestFragment extends Fragment {


    private static final String TAG = VocabularyTestFragment.class.getSimpleName();
    private final VocabularyDetails vocabulary;
    private final int currentPage;
    private FragmentVocabularyTestBinding binding = null;

    public VocabularyTestFragment(VocabularyDetails vocabulary, int currentPage) {
        super(R.layout.fragment_vocabulary_test);
        this.vocabulary = vocabulary;
        this.currentPage = currentPage;
    }

    public LottieAnimationView getAnimationView() {
        return binding.animationVoice;
    }

    public TextView getTextView() {
        return binding.textViewQuestion;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentVocabularyTestBinding.bind(view);
        UtilityFunctions.loadImage(vocabulary.getImageLink(), binding.imageViewObject, binding.loadingAnimation);
        var viewPager = (ViewPager2) requireActivity().findViewById(R.id.viewPager_test);
        binding.progress.setText(getResources()
                .getString(R.string.current_by_all,
                        String.valueOf(currentPage),
                        String.valueOf(Objects.requireNonNull(viewPager.getAdapter()).getItemCount())));
    }
}
