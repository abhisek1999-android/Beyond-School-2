package com.maths.beyond_school_new_071020221400.retrofit.noun;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.util.List;


@Keep
public class SubjectContent {
    String chapter_name;
    MetaData meta;
    List<Content> content;

    public SubjectContent(String chapter_name, MetaData meta, List<Content> content) {
        this.chapter_name = chapter_name;
        this.meta = meta;
        this.content = content;
    }

    public String getChapter_name() {
        return chapter_name;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }

    public MetaData getMeta() {
        return meta;
    }

    public void setMeta(MetaData meta) {
        this.meta = meta;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    @NonNull
    @Override
    public String toString() {
        return "SubjectContent{" +
                "chapter_name='" + chapter_name + '\'' +
                ", meta=" + meta +
                ", content=" + content +
                '}';
    }
}
