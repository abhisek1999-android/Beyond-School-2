package com.maths.beyond_school_280720220930.english_activity.vocabulary2;

import com.maths.beyond_school_280720220930.database.english.vocabulary.model.VocabularyDetails;
import com.maths.beyond_school_280720220930.retrofit.model.content.ContentModel;
import com.maths.beyond_school_280720220930.utils.TypeConverter;

import java.util.List;
import java.util.stream.Collectors;

public class VocabularyTypeConverter implements TypeConverter<VocabularyDetails, ContentModel.Content> {
    @Override
    public VocabularyDetails mapTo(ContentModel.Content content) {
        return new VocabularyDetails(content.getWord(), content.getDescription(), content.getImage());
    }

    @Override
    public ContentModel.Content mapFrom(VocabularyDetails vocabularyDetails) {
        return new ContentModel.Content();
    }

    public List<VocabularyDetails> mapToList(List<ContentModel.Content> contents) {
        return contents.stream().map(this::mapTo).collect(Collectors.toList());
    }
}
