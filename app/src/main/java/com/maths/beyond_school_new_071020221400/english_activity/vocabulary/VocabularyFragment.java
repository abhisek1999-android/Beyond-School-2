package com.maths.beyond_school_new_071020221400.english_activity.vocabulary;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
import com.maths.beyond_school_new_071020221400.R;
import com.maths.beyond_school_new_071020221400.database.english.vocabulary.model.VocabularyDetails;
import com.maths.beyond_school_new_071020221400.databinding.FragmentVocabularyBinding;
import com.maths.beyond_school_new_071020221400.utils.UtilityFunctions;

import java.util.Objects;

public class VocabularyFragment extends Fragment {


    private static final String TAG = VocabularyFragment.class.getSimpleName();
    private final VocabularyDetails vocabulary;

    private FragmentVocabularyBinding binding = null;
    private final int currentPage;
    private final OnRootClick onRootClick;


    public VocabularyFragment(VocabularyDetails vocabulary, int currentPage, OnRootClick onRootClick) {
        super(R.layout.fragment_vocabulary);
        this.vocabulary = vocabulary;
        this.currentPage = currentPage;
        this.onRootClick = onRootClick;
    }

    public LottieAnimationView getAnimationView() {
        return binding.animationVoice;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentVocabularyBinding.bind(view);
        UtilityFunctions.loadImage(vocabulary.getImageLink(), binding.imageViewObject, binding.loadingAnimation);
        var viewPager = (ViewPager2) requireActivity().findViewById(R.id.view_pager);
        binding.textViewDescription.setText(vocabulary.getDefinition());
        binding.textViewItem.setText(vocabulary.getWord());
        binding.imageButtonPrev.setVisibility((currentPage == 1) ? View.INVISIBLE : View.VISIBLE);
        binding.imageButtonNext.setVisibility((currentPage == Objects.requireNonNull(viewPager.getAdapter()).getItemCount()) ? View.INVISIBLE : View.VISIBLE);

        var activity = (EnglishActivity) requireActivity();

        binding.imageButtonNext.setOnClickListener(v -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            activity.restartEngine();
        });
        binding.imageButtonPrev.setOnClickListener(v -> {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            activity.restartEngine();
        });
        binding.progress.setText(getResources()
                .getString(R.string.current_by_all,
                        String.valueOf(currentPage),
                        String.valueOf(Objects.requireNonNull(viewPager.getAdapter()).getItemCount())));


        binding.getRoot().setOnClickListener(v -> {

            onRootClick.onRootClick();
        });

    }

    interface OnRootClick {
        void onRootClick();
    }

}
