package com.maths.beyond_school_280720220930.retrofit.model.content_new;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;
import com.maths.beyond_school_280720220930.retrofit.model.content.ContentModel;

import java.io.Serializable;
import java.util.List;

@Keep
public class ContentModelNew implements Serializable {
    @SerializedName("sub-subject")
    private String sub_subject;
    @SerializedName("sub-subject_id")
    private String sub_subject_id;
    private String created_at;
    private ContentModel.Meta meta;
    private String name;
    private List<ContentModel.Content> learning;
    private List<ContentModel.Content> exercise;

    public ContentModelNew(String sub_subject, String sub_subject_id, String created_at,
                           ContentModel.Meta meta, String name, List<ContentModel.Content> learning,
                           List<ContentModel.Content> exercise) {
        this.sub_subject = sub_subject;
        this.sub_subject_id = sub_subject_id;
        this.created_at = created_at;
        this.meta = meta;
        this.name = name;
        this.learning = learning;
        this.exercise = exercise;
    }

    public String getSub_subject() {
        return sub_subject;
    }

    public void setSub_subject(String sub_subject) {
        this.sub_subject = sub_subject;
    }

    public String getSub_subject_id() {
        return sub_subject_id;
    }

    public void setSub_subject_id(String sub_subject_id) {
        this.sub_subject_id = sub_subject_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public ContentModel.Meta getMeta() {
        return meta;
    }

    public void setMeta(ContentModel.Meta meta) {
        this.meta = meta;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ContentModel.Content> getLearning() {
        return learning;
    }

    public void setLearning(List<ContentModel.Content> learning) {
        this.learning = learning;
    }

    public List<ContentModel.Content> getExercise() {
        return exercise;
    }

    public void setExercise(List<ContentModel.Content> exercise) {
        this.exercise = exercise;
    }

    @Override
    public String toString() {
        return "ContentModelNew{" +
                "sub_subject='" + sub_subject + '\'' +
                ", sub_subject_id='" + sub_subject_id + '\'' +
                ", created_at='" + created_at + '\'' +
                ", meta=" + meta +
                ", name='" + name + '\'' +
                ", learning=" + learning +
                ", exercise=" + exercise +
                '}';
    }
}
