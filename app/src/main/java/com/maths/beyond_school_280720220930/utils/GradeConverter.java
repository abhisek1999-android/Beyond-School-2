package com.maths.beyond_school_280720220930.utils;

import com.maths.beyond_school_280720220930.database.grade_tables.Grades_data;
import com.maths.beyond_school_280720220930.retrofit.Chapters;

import java.util.ArrayList;
import java.util.List;

public class GradeConverter implements TypeConverter<Grades_data, Chapters> {

    @Override
    public Grades_data mapTo(Chapters chapters) {
        return new Grades_data(
                chapters.getSubject(),
                chapters.getChapter_name(),
                contentToBoolean(chapters.getGrade1()),
                contentToBoolean(chapters.getGrade2()),
                contentToBoolean(chapters.getGrade3()),
                contentToBoolean(chapters.getGrade4()),
                contentToBoolean(chapters.getGrade5()),
                true,
                contentToBoolean(chapters.getIsCompleted()),
                null
        );
    }

    @Override
    public Chapters mapFrom(Grades_data grades_data) {
        return new Chapters();
    }

    public List<Grades_data> mapToList(List<Chapters> chapters) {
        var list = new ArrayList<Grades_data>();
        chapters.forEach(
                chapter -> list.add(mapTo(chapter))
        );
        return list;
    }

    private Boolean contentToBoolean(int content) {
        return content == 1;
    }
}
