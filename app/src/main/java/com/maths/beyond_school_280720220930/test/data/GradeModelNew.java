package com.maths.beyond_school_280720220930.test.data;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class GradeModelNew {
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

    @Keep
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

        @Keep
        public static class Chapter {
            private String id;
            private String chapter_name;
            private int unlock;
            private List<Lesson> lessons;

            public Chapter(String id, String chapter_name, int unlock, List<Lesson> lessons) {
                this.id = id;
                this.chapter_name = chapter_name;
                this.unlock = unlock;
                this.lessons = lessons;
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

            public List<Lesson> getLessons() {
                return lessons;
            }

            public void setLessons(List<Lesson> lessons) {
                this.lessons = lessons;
            }

            @Override
            public String toString() {
                return "Chapter{" +
                        "id='" + id + '\'' +
                        ", chapter_name='" + chapter_name + '\'' +
                        ", unlock=" + unlock +
                        ", lessons=" + lessons +
                        '}';
            }
        }
    }

    @Keep
    public static class Lesson {
        private String id;
        private String lesson_name;
        private int unlock;

        public Lesson(String id, String lesson_name, int unlock) {
            this.id = id;
            this.lesson_name = lesson_name;
            this.unlock = unlock;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLesson_name() {
            return lesson_name;
        }

        public void setLesson_name(String lesson_name) {
            this.lesson_name = lesson_name;
        }

        public int getUnlock() {
            return unlock;
        }

        public void setUnlock(int unlock) {
            this.unlock = unlock;
        }

        @Override
        public String toString() {
            return "Lesson{" +
                    "id='" + id + '\'' +
                    ", lesson_name='" + lesson_name + '\'' +
                    ", unlock=" + unlock +
                    '}';
        }
    }
}
