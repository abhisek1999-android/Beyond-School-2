package com.maths.beyond_school_new_071020221400.utils;

import com.maths.beyond_school_new_071020221400.R;
import com.maths.beyond_school_new_071020221400.database.grade_tables.Grades_data;
import com.maths.beyond_school_new_071020221400.retrofit.Chapters;

import java.util.ArrayList;
import java.util.List;

public class GradeConverter implements TypeConverter<Grades_data, Chapters> {

    @Override
    public Grades_data mapTo(Chapters chapters) {
        return new Grades_data(
                chapters.getChapter(),
                chapters.getChapter_name(),
                contentToBoolean(chapters.getGrade1()),
                contentToBoolean(chapters.getGrade2()),
                contentToBoolean(chapters.getGrade3()),
                contentToBoolean(chapters.getGrade4()),
                contentToBoolean(chapters.getGrade5()),
                contentToBoolean(chapters.getUnlock()),
                false,
                "",
                contentToBoolean(chapters.getIsFetchRequired())
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
