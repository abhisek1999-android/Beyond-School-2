package com.maths.beyond_school_280720220930.english_activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.english.model.VocabularyDetails;
import com.maths.beyond_school_280720220930.databinding.FragmentVocabularyBinding;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.Objects;

public class VocabularyFragment extends Fragment {


    private static final String TAG = VocabularyFragment.class.getSimpleName();
    private final VocabularyDetails vocabulary;

    private FragmentVocabularyBinding binding = null;


    public VocabularyFragment(VocabularyDetails vocabulary) {
        super(R.layout.fragment_vocabulary);
        this.vocabulary = vocabulary;
    }

    public LottieAnimationView getAnimationView() {
        return binding.animationVoice;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentVocabularyBinding.bind(view);
        UtilityFunctions.loadImage(vocabulary.getImageLink(), binding.imageViewObject);
        binding.textViewDescription.setText(vocabulary.getDefinition());
        binding.textViewItem.setText(vocabulary.getWord());

        var viewPager = (ViewPager2) requireActivity().findViewById(R.id.view_pager);
        binding.progress.setText(getResources().getString(R.string.current_by_all, String.valueOf(viewPager.getCurrentItem() + 1), String.valueOf(Objects.requireNonNull(viewPager.getAdapter()).getItemCount())));
    }

}
