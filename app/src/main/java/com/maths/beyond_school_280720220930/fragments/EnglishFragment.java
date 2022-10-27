package com.maths.beyond_school_280720220930.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;
import com.maths.beyond_school_280720220930.R;
import com.maths.beyond_school_280720220930.ViewCurriculum;
import com.maths.beyond_school_280720220930.databinding.FragmentEnglishBinding;


public class EnglishFragment extends Fragment {

    private FragmentEnglishBinding binding;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding=FragmentEnglishBinding.bind(view);

        binding.buttonLayout.setOnClickListener(v->{
            startActivity(new Intent(getContext(), ViewCurriculum.class));
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_english, container, false);
    }
}