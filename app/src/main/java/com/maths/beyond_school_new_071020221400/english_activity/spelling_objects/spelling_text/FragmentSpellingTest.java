package com.maths.beyond_school_new_071020221400.english_activity.spelling_objects.spelling_text;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.maths.beyond_school_new_071020221400.R;
import com.maths.beyond_school_new_071020221400.database.english.spelling_objects.model.SpellingModel;
import com.maths.beyond_school_new_071020221400.databinding.FragmentSpellingTestBinding;
import com.maths.beyond_school_new_071020221400.utils.UtilityFunctions;

import java.util.Objects;

public class FragmentSpellingTest extends Fragment {
    private static final String TAG = "FragmentSpellingTest";
    private final SpellingModel spellingModel;
    private final int currentPage;
    private FragmentSpellingTestBinding binding;


    public FragmentSpellingTest(SpellingModel spellingModel, int currentPage) {
        super(R.layout.fragment_spelling_test);
        this.spellingModel = spellingModel;
        this.currentPage = currentPage;
    }

    public TextView getTextView() {
        return binding.otpViewWord;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentSpellingTestBinding.bind(view);
        UtilityFunctions.loadImage(spellingModel.getImageLink(), binding.imageViewSpelling, binding.loadingAnimation);
        binding.otpViewWord.setText(spellingModel.getWord().
                replaceAll("[A-Za-z]", "_"));
        var viewPager = (ViewPager2) requireActivity().findViewById(R.id.viewPager_test);
        binding.progress.setText(getResources()
                .getString(R.string.current_by_all,
                        String.valueOf(currentPage),
                        String.valueOf(Objects.requireNonNull(viewPager.
                                getAdapter()).getItemCount())));
    }
}
