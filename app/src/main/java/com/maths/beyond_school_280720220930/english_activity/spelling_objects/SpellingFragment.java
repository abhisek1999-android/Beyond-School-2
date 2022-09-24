package com.maths.beyond_school_280720220930.english_activity.spelling_objects;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.english.spelling_objects.model.SpellingModel;
import com.maths.beyond_school_280720220930.databinding.FragmentSpellingBinding;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.N)
public class SpellingFragment extends Fragment {

    private final SpellingModel spellingModel;
    private final int currentPage;

    public SpellingFragment(SpellingModel spellingModel, int currentPage) {
        super(R.layout.fragment_spelling);
        this.spellingModel = spellingModel;
        this.currentPage = currentPage;
    }

    private FragmentSpellingBinding binding;


    public TextView getAnswerTextView() {
        return binding.otpViewWord;
    }

    private TextView getWordTextView() {
        return binding.textViewWord;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentSpellingBinding.bind(view);
        UtilityFunctions.loadImage(spellingModel.getImageLink(), binding.imageViewSpelling, binding.loadingAnimation);
        binding.otpViewWord.setText(spellingModel.getWord().replaceAll("[A-Za-z]", "_"));
        binding.textViewWord.setText(UtilityFunctions.addSpaceAnswer(spellingModel.getWord()));

        var viewPager = (ViewPager2) requireActivity().findViewById(R.id.view_pager);
        binding.imageButtonPrev.setVisibility((currentPage == 1) ? View.INVISIBLE : View.VISIBLE);
        binding.imageButtonNext.setVisibility((currentPage == Objects.requireNonNull(viewPager.getAdapter()).getItemCount()) ? View.INVISIBLE : View.VISIBLE);
        var activity = (SpellingActivity) requireActivity();

        binding.imageButtonNext.setOnClickListener(v -> {
            binding.otpViewWord.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color));
            binding.otpViewWord.setText(spellingModel.getWord().replaceAll("[A-Za-z]", "_"));
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            activity.restartEngine();
            activity.setButtonText();
        });
        binding.imageButtonPrev.setOnClickListener(v -> {
            binding.otpViewWord.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color));
            binding.otpViewWord.setText(spellingModel.getWord().replaceAll("[A-Za-z]", "_"));
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            activity.restartEngine();
            activity.setButtonText();
        });
        binding.progress.setText(getResources()
                .getString(R.string.current_by_all,
                        String.valueOf(currentPage),
                        String.valueOf(Objects.requireNonNull(viewPager.getAdapter()).getItemCount())));
    }
}
