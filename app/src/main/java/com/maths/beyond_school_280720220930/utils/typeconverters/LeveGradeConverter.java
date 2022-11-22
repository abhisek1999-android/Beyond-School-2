package com.maths.beyond_school_280720220930.utils.typeconverters;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.maths.beyond_school_280720220930.database.grade_tables.GradeData;
import com.maths.beyond_school_280720220930.retrofit.model.grade.GradeModelNew;
import com.maths.beyond_school_280720220930.utils.TypeConverter;
import com.maths.beyond_school_280720220930.utils.UtilityFunctions;

import java.util.List;
import java.util.stream.Collectors;

public class LeveGradeConverter implements TypeConverter<GradeData, GradeModelNew.English.Sub_Subjects.Blocks> {

    private final String superSubject;
    private  String subject;


    public LeveGradeConverter(String superSubject,String subject) {
        this.superSubject = superSubject;
        this.subject=subject;
    }

    @Override
    public GradeData mapTo(GradeModelNew.English.Sub_Subjects.Blocks blocks) {
        return new GradeData(
                blocks.getBlockId(),
                blocks.getName(),
                subject,
                "",
                superSubject,
                blocks.getSub_subject(),
                blocks.getSub_subject_id(),
                new MetaConverter().mapTo(blocks.getMeta()),
                UtilityFunctions.intToBoolean(blocks.isUnlock()),
                UtilityFunctions.intToBoolean(blocks.isUnlock_ex()),
                false,
                false
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<GradeData> mapToList(List<GradeModelNew.English.Sub_Subjects.Blocks> blocks) {
        return blocks.stream().map(this::mapTo).collect(Collectors.toList());
    }

    @Override
    public GradeModelNew.English.Sub_Subjects.Blocks mapFrom(GradeData gradeData) {
        return null;
    }

    public static class MetaConverter implements TypeConverter<GradeData.Meta, GradeModelNew.English.Sub_Subjects.Blocks.Meta> {
        @Override
        public GradeData.Meta mapTo(GradeModelNew.English.Sub_Subjects.Blocks.Meta meta) {
            return new GradeData.Meta(meta.getScreen_type(), meta.getHint(), meta.getQuestion());
        }

        @Override
        public GradeModelNew.English.Sub_Subjects.Blocks.Meta mapFrom(GradeData.Meta meta) {
            return new GradeModelNew.English.Sub_Subjects.Blocks.Meta();
        }
    }
}
