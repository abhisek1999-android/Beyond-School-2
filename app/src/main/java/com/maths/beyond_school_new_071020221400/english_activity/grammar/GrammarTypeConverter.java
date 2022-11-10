package com.maths.beyond_school_new_071020221400.english_activity.grammar;

import com.maths.beyond_school_new_071020221400.database.english.grammer.model.GrammarModel;
import com.maths.beyond_school_new_071020221400.retrofit.model.content.ContentModel;
import com.maths.beyond_school_new_071020221400.utils.TypeConverter;

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
