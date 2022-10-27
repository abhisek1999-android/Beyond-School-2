package com.maths.beyond_school_280720220930.english_activity.grammar;

import com.maths.beyond_school_280720220930.database.english.grammer.model.GrammarModel;
import com.maths.beyond_school_280720220930.database.english.grammer.model.GrammarType;
import com.maths.beyond_school_280720220930.database.english.vocabulary.model.VocabularyDetails;
import com.maths.beyond_school_280720220930.retrofit.model.content.ContentModel;
import com.maths.beyond_school_280720220930.utils.TypeConverter;

import java.util.List;
import java.util.stream.Collectors;

public class GrammarTypeConverter implements TypeConverter<GrammarModel, ContentModel.Content> {
    @Override
    public GrammarModel mapTo(ContentModel.Content content) {
        return new GrammarModel(
                content.getWord(),
                content.getExtra(),
                content.getDescription(),
                content.getImage()
        );
    }

    @Override
    public ContentModel.Content mapFrom(GrammarModel vocabularyDetails) {
        return new ContentModel.Content();
    }

    public List<GrammarModel> mapToList(List<ContentModel.Content> contents) {
        return contents.stream().map(this::mapTo).collect(Collectors.toList());
    }
}
