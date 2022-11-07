package com.maths.beyond_school_280720220930.utils.typeconverters;

import com.maths.beyond_school_280720220930.database.grade_tables.GradeData;
import com.maths.beyond_school_280720220930.retrofit.model.grade.GradeModel;
import com.maths.beyond_school_280720220930.utils.TypeConverter;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.List;
import java.util.stream.Collectors;

public class GradeConverter implements TypeConverter<GradeData, GradeModel.EnglishModel.Chapter> {

    private final String subject;

    public GradeConverter(String subject) {
        this.subject = subject;
    }

    @Override
    public GradeData mapTo(GradeModel.EnglishModel.Chapter chapter) {
        return new GradeData(
                chapter.getId(),
                chapter.getChapter_name(),
                subject,
                chapter.getRequest(),
                "english",
                UtilityFunctions.intToBoolean(chapter.getUnlock()),
                UtilityFunctions.intToBoolean(chapter.getIsFetchRequired()),
                false
        );
    }


    @Override
    public GradeModel.EnglishModel.Chapter mapFrom(GradeData gradeData) {
        return new GradeModel.EnglishModel.Chapter();
    }

    public List<GradeData> mapToList(List<GradeModel.EnglishModel.Chapter> chapters) {
        return chapters.stream().map(this::mapTo).collect(Collectors.toList());
    }
}
