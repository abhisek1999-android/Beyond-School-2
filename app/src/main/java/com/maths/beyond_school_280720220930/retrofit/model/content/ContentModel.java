package com.maths.beyond_school_280720220930.retrofit.model.content;

import androidx.annotation.Keep;

import org.checkerframework.checker.units.qual.K;

import java.io.Serializable;
import java.util.List;

@Keep
public class ContentModel implements Serializable {

    private Meta meta;
    private List<Content> content;

    public ContentModel(Meta meta, List<Content> content) {
        this.meta = meta;
        this.content = content;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        return "ContentModel{" +
                "meta=" + meta +
                ", content=" + content +
                '}';
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    @Keep
    public static class Meta implements Serializable {
        private String hint;
        private String question;
        private String screen_type;

        public Meta(String hint, String question, String screen_type) {
            this.hint = hint;
            this.question = question;
            this.screen_type = screen_type;
        }

        public String getHint() {
            return hint;
        }

        public String getQuestion() {
            return question;
        }

        public String getScreen_type() {
            return screen_type;
        }

        public void setHint(String hint) {
            this.hint = hint;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public void setScreen_type(String screen_type) {
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
    }

    @Keep
    public static final class Content implements Serializable{

        public Content() {
        }

        private String word;
        private String description;
        private String image;
        private String extra;

        public Content(String word, String description, String image, String extra) {
            this.word = word;
            this.description = description;
            this.image = image;
            this.extra = extra;
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

        @Override
        public String toString() {
            return "Content{" +
                    "word='" + word + '\'' +
                    ", description='" + description + '\'' +
                    ", image='" + image + '\'' +
                    ", extra='" + extra + '\'' +
                    '}';
        }
    }
}
