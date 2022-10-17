package com.maths.beyond_school_new_071020221400.retrofit;

public class ResponseClass<T> {

    T subjectContent;

    public ResponseClass(T subjectContent) {
        this.subjectContent = subjectContent;
    }

    public T getSubjectContent() {
        return subjectContent;
    }
}
