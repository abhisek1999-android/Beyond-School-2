package com.maths.beyond_school_new_071020221400.retrofit.noun;

public class NounResponse {
    SubjectContent subjectContent;

    public NounResponse(SubjectContent subjectContent) {
        this.subjectContent = subjectContent;
    }

    public SubjectContent getSubjectContent() {
        return subjectContent;
    }

    public void setSubjectContent(SubjectContent subjectContent) {
        this.subjectContent = subjectContent;
    }
}
