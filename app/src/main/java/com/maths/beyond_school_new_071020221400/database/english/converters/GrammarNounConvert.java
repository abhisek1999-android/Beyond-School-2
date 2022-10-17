package com.maths.beyond_school_new_071020221400.database.english.converters;

import com.maths.beyond_school_new_071020221400.database.english.grammer.model.GrammarModel;
import com.maths.beyond_school_new_071020221400.retrofit.noun.Content;
import com.maths.beyond_school_new_071020221400.utils.TypeConverter;

import java.util.ArrayList;
import java.util.List;

public class GrammarNounConvert implements TypeConverter<GrammarModel, Content> {
    @Override
    public GrammarModel mapTo(Content content) {
        return new GrammarModel(
                content.getWord(),
                content.getExtra(),
                content.getDescription(),
                content.getImage()
        );
    }

    @Override
    public Content mapFrom(GrammarModel grammarModel) {
        return new Content(
                grammarModel.getWord(),
                grammarModel.getDescription(),
                grammarModel.getImage(),
                grammarModel.getExtra()
        );
    }

    public List<GrammarModel> mapToList(List<Content> contents) {
        var list = new ArrayList<GrammarModel>();
        contents.forEach(
                content -> list.add(mapTo(content))
        );
        return list;
    }
}
