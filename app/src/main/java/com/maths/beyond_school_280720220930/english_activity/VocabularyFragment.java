package com.maths.beyond_school_280720220930.english_activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.database.english.model.VocabularyDetails;
import com.maths.beyond_school_280720220930.databinding.FragmentVocabularyBinding;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

public class VocabularyFragment extends Fragment {


    private static final String TAG = VocabularyFragment.class.getSimpleName();
    private final VocabularyDetails vocabulary;


    public VocabularyFragment(VocabularyDetails vocabulary) {
        super(R.layout.fragment_vocabulary);
        this.vocabulary = vocabulary;
    }

    private FragmentVocabularyBinding binding;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentVocabularyBinding.bind(view);
        UtilityFunctions.loadImage(vocabulary.getImageLink(), binding.imageViewObject);
        binding.textViewDescription.setText(getResources().getString(R.string.name_with_des, vocabulary.getWord(), vocabulary.getDefinition()));
        binding.textViewItem.setText(vocabulary.getWord());
    }
}
