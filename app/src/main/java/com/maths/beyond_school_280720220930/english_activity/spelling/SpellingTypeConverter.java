package com.maths.beyond_school_280720220930.english_activity.spelling;

import com.maths.beyond_school_280720220930.database.english.grammer.model.GrammarModel;
import com.maths.beyond_school_280720220930.database.english.spelling_common_words.model.SpellingDetail;
import com.maths.beyond_school_280720220930.retrofit.model.content.ContentModel;
import com.maths.beyond_school_280720220930.utils.TypeConverter;

import java.util.List;
import java.util.stream.Collectors;

public class SpellingTypeConverter implements TypeConverter<SpellingDetail, ContentModel.Content> {
    @Override
    public SpellingDetail mapTo(ContentModel.Content content) {
        return new SpellingDetail(
                content.getWord(),
                content.getDescription()
        );
    }

    @Override
    public ContentModel.Content mapFrom(SpellingDetail vocabularyDetails) {
        return new ContentModel.Content();
    }

    public List<SpellingDetail> mapToList(List<ContentModel.Content> contents) {
        return contents.stream().map(this::mapTo).collect(Collectors.toList());
    }
}
