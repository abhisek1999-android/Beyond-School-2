package com.maths.beyond_school_280720220930.english_activity.vocabulary2;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.maths.beyond_school_280720220930.database.english.vocabulary.model.VocabularyDetails;
import com.maths.beyond_school_280720220930.databinding.ActivityEnglishBinding;
import com.maths.beyond_school_280720220930.english_activity.vocabulary.EnglishViewPager;
import com.maths.beyond_school_280720220930.english_activity.vocabulary.VocabularyFragment;
import com.maths.beyond_school_280720220930.retrofit.ApiClient;
import com.maths.beyond_school_280720220930.retrofit.ApiInterface;
import com.maths.beyond_school_280720220930.retrofit.model.content.ContentModel;
import com.maths.beyond_school_280720220930.utils.CollectionUtils;
import com.maths.beyond_school_280720220930.utils.Constants;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class VocabularyActivity extends AppCompatActivity implements VocabularyFragment.OnRootClick {

    private static final String TAG = "VocabularyActivity";

    private ActivityEnglishBinding binding;
    private static String chapterName;

    private List<VocabularyDetails> vocabularyList;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEnglishBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chapterName = getIntent().getStringExtra(Constants.EXTRA_VOCABULARY_CATEGORY);
        getSubjectData();
    }

    private void getSubjectData() {
        Retrofit retrofit = ApiClient.getClient();
        var api = retrofit.create(ApiInterface.class);
        api.getVocabularySubject(chapterName).enqueue(new retrofit2.Callback<>() {
            @Override
            public void onResponse(Call<ContentModel> call, Response<ContentModel> response) {
                Log.d(TAG, "onResponse: " + response.code());
                Log.d(TAG, "onResponse: " + response.body().getContent().toString());
                var contentModel = response.body();
                var list = contentModel.getContent();
                var converter = new VocabularyTypeConverter();
                vocabularyList = converter.mapToList(list);
                setViewPager();
            }

            @Override
            public void onFailure(Call<ContentModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void setViewPager() {

        binding.textViewCategory.setText(chapterName);
        List<Fragment> fragments = getFragments();
        var pagerAdapter = new EnglishViewPager(
                fragments,
                getSupportFragmentManager(),
                getLifecycle()
        );

        binding.viewPager.setAdapter(pagerAdapter);
//        binding.viewPager.setPageTransformer((page, position) -> {
//            updatePagerHeightForChild(page, binding.viewPager);
//        });
        binding.viewPager.setUserInputEnabled(false);
//        try {
//            binding.playPause.setChecked(true);
//            isSpeaking = binding.playPause.isChecked();
//            initTTS();
//            intSTT();
//            initMediaPlayer();
//            playPauseAnimation(true);
//            helperTTS(UtilityFunctions.getQuestionTitleVocabulary(category), false, REQUEST_FOR_QUESTION);
//        } catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    @NonNull
    private List<Fragment> getFragments() {
        fragmentList = CollectionUtils.mapWithIndex(vocabularyList.stream(),
                (index, item) -> new VocabularyFragment(item, index + 1, this)).collect(Collectors.toList());
        return fragmentList;
    }

    @Override
    public void onRootClick() {

    }
}
