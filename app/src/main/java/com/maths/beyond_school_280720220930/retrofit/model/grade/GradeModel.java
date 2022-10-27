package com.maths.beyond_school_280720220930.retrofit.model.grade;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class GradeModel {
    private List<EnglishModel> english;

    public List<EnglishModel> getEnglish() {
        return english;
    }

    @Override
    public String toString() {
        return "GradeModel{" +
                "english=" + english +
                '}';
    }

    public static class EnglishModel {
        private String subject;

        private List<Chapter> chapters;

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }


        public List<Chapter> getChapters() {
            return chapters;
        }

        public void setChapters(List<Chapter> chapters) {
            this.chapters = chapters;
        }

        @Override
        public String toString() {
            return "EnglishModel{" +
                    "subject='" + subject + '\'' +
                    ", chapters=" + chapters +
                    '}';
        }

        public static class Chapter {
            private String id;
            private String chapter_name;
            private int unlock;
            private int isFetchRequired;

            private String request;

            public Chapter() {
            }

            public Chapter(String id, String chapter_name, int unlock, int isFetchRequired) {
                this.id = id;
                this.chapter_name = chapter_name;
                this.unlock = unlock;
                this.isFetchRequired = isFetchRequired;
            }

            public String getRequest() {
                return request;
            }

            public void setRequest(String request) {
                this.request = request;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getChapter_name() {
                return chapter_name;
            }

            public void setChapter_name(String chapter_name) {
                this.chapter_name = chapter_name;
            }

            public int getUnlock() {
                return unlock;
            }

            public void setUnlock(int unlock) {
                this.unlock = unlock;
            }

            public int getIsFetchRequired() {
                return isFetchRequired;
            }

            public void setIsFetchRequired(int isFetchRequired) {
                this.isFetchRequired = isFetchRequired;
            }

            @Override
            public String toString() {
                return "Chapter{" +
                        "id=" + id +
                        ", chapter_name='" + chapter_name + '\'' +
                        ", unlock=" + unlock +
                        ", isFetchRequired=" + isFetchRequired +
                        '}';
            }
        }
    }

}
