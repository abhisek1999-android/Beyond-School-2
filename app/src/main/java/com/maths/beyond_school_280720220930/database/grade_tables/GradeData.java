package com.maths.beyond_school_280720220930.database.grade_tables;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "grade")
@Keep
public class GradeData {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String id;
    private String chapter_name;
    private String subject;
    private String request;
    private String super_subject;
    @Nullable
    private String sub_subject;
    @Nullable
    private String sub_subject_id;
    private boolean isFetchRequired;
    private boolean unlock;
    private boolean unlock_ex;
    private boolean is_completed;
    private boolean is_completed_ex;

    @Embedded
    private Meta meta;


    public GradeData(@NonNull String id, String chapter_name, String subject, String request, String super_subject,
                     @Nullable String sub_subject, @Nullable String sub_subject_id, Meta meta,
                     boolean unlock, boolean unlock_ex, boolean is_completed, boolean is_completed_ex) {
        this.id = id;
        this.chapter_name = chapter_name;
        this.subject = subject;
        this.request = request;
        this.super_subject = super_subject;
        this.sub_subject = sub_subject;
        this.sub_subject_id = sub_subject_id;
        this.unlock = unlock;
        this.unlock_ex = unlock_ex;
        this.is_completed = is_completed;
        this.is_completed_ex = is_completed_ex;
        this.meta = meta;
        isFetchRequired = false;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getChapter_name() {
        return chapter_name;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getSuper_subject() {
        return super_subject;
    }

    public void setSuper_subject(String super_subject) {
        this.super_subject = super_subject;
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

    public boolean isFetchRequired() {
        return isFetchRequired;
    }

    public void setFetchRequired(boolean fetchRequired) {
        isFetchRequired = fetchRequired;
    }

    public boolean isUnlock() {
        return unlock;
    }

    public void setUnlock(boolean unlock) {
        this.unlock = unlock;
    }

    public boolean isUnlock_ex() {
        return unlock_ex;
    }

    public void setUnlock_ex(boolean unlock_ex) {
        this.unlock_ex = unlock_ex;
    }

    public boolean isIs_completed() {
        return is_completed;
    }

    public void setIs_completed(boolean is_completed) {
        this.is_completed = is_completed;
    }

    public boolean isIs_completed_ex() {
        return is_completed_ex;
    }

    public void setIs_completed_ex(boolean is_completed_ex) {
        this.is_completed_ex = is_completed_ex;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    @Keep
    public static class Meta {

        @Nullable
        private String screen_type;
        @Nullable
        private String hint;

        @Nullable
        private String question;

        public Meta() {
            this.screen_type = "";
            this.hint = "";
            this.question = "";
        }

        public Meta(@Nullable String screen_type, @Nullable String hint, @Nullable String question) {
            this.screen_type = screen_type;
            this.hint = hint;
            this.question = question;
        }

        public String getScreen_type() {
            return screen_type;
        }

        public void setScreen_type(String screen_type) {
            this.screen_type = screen_type;
        }

        public String getHint() {
            return hint;
        }

        public void setHint(String hint) {
            this.hint = hint;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        @Override
        public String toString() {
            return "Meta{" +
                    "screen_type='" + screen_type + '\'' +
                    ", hint='" + hint + '\'' +
                    ", question='" + question + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GradeData{" +
                "id='" + id + '\'' +
                ", chapter_name='" + chapter_name + '\'' +
                ", subject='" + subject + '\'' +
                ", request='" + request + '\'' +
                ", super_subject='" + super_subject + '\'' +
                ", sub_subject='" + sub_subject + '\'' +
                ", sub_subject_id='" + sub_subject_id + '\'' +
                ", isFetchRequired=" + isFetchRequired +
                ", unlock=" + unlock +
                ", unlock_ex=" + unlock_ex +
                ", is_completed=" + is_completed +
                ", is_completed_ex=" + is_completed_ex +
                ", meta=" + meta +
                '}';
    }
}
