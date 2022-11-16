package com.maths.beyond_school_280720220930.test.data;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Keep
public class GradeModelNew {


    String subject;

    @SerializedName("subsubjects")
    List<SubSubject> subSubjects;


    @Override
    public String toString() {
        return "GradeModelNew{" +
                "subject='" + subject + '\'' +
                ", subSubject=" + subSubjects +
                '}';
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<SubSubject> getEnglish() {
        return subSubjects;
    }

    public void setEnglish(List<SubSubject> english) {
        this.subSubjects = english;
    }

    public GradeModelNew(String subject, List<SubSubject> english) {
        this.subject = subject;
        this.subSubjects = english;

    }

    @Keep
    public static class SubSubject {
        String name;
        Meta meta;

        public SubSubject(String name, Meta meta) {
            this.name = name;
            this.meta = meta;
        }

        @Override
        public String toString() {
            return "SubSubject{" +
                    "name='" + name + '\'' +
                    ", meta=" + meta +
                    '}';
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Meta getMeta() {
            return meta;
        }

        public void setMeta(Meta meta) {
            this.meta = meta;
        }
    }


    @Keep
    public static class Meta {
        String hint;
        String question;
        String screen_type;

        public Meta(String hint, String question, String screen_type) {
            this.hint = hint;
            this.question = question;
            this.screen_type = screen_type;
        }

        @Override
        public String toString() {
            return "Meta{" +
                    "hint='" + hint + '\'' +
                    ", question='" + question + '\'' +
                    ", screen_type='" + screen_type + '\'' +
                    '}';
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

        public String getScreen_type() {
            return screen_type;
        }

        public void setScreen_type(String screen_type) {
            this.screen_type = screen_type;
        }
    }

    @Keep
    public static class Content {
        String word;
        String description;
        String image;
        String extra;

        public Content(String word, String description, String image, String extra) {
            this.word = word;
            this.description = description;
            this.image = image;
            this.extra = extra;
        }

        @Override
        public String toString() {
            return "Content{" +
                    "word='" + word + '\'' +
                    ", description='" + description + '\'' +
                    ", image='" + image + '\'' +
                    ", extra='" + extra + '\'' +
                    '}';
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }
    }

}
