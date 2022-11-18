package com.maths.beyond_school_280720220930.retrofit.model.grade;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

import java.util.List;

@Keep
public class GradeModelNew {

    List<English> english;

    @Override
    public String toString() {
        return "GradeModelNew{" +
                "english=" + english +
                '}';
    }

    public List<English> getEnglish() {
        return english;
    }

    public void setEnglish(List<English> english) {
        this.english = english;
    }

    public GradeModelNew(List<English> english) {
        this.english = english;
    }

    @Keep
    public static class English {
        String subject;
        List<Sub_Subjects> sub_subject;

        public English(String subject, List<Sub_Subjects> sub_subject) {
            this.subject = subject;
            this.sub_subject = sub_subject;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public List<Sub_Subjects> getSub_subject() {
            return sub_subject;
        }

        public void setSub_subject(List<Sub_Subjects> sub_subject) {
            this.sub_subject = sub_subject;
        }

        @Override
        public String toString() {
            return "English{" +
                    "subject='" + subject + '\'' +
                    ", sub_subject=" + sub_subject +
                    '}';
        }

        @Keep
        public static class Sub_Subjects {

            String sub_subject;
            List<Blocks> blocks;

            public Sub_Subjects(String sub_subject, List<Blocks> blocks) {
                this.sub_subject = sub_subject;
                this.blocks = blocks;
            }

            @Override
            public String toString() {
                return "Sub_Subjects{" +
                        "sub_subject='" + sub_subject + '\'' +
                        ", blocks=" + blocks +
                        '}';
            }

            public String getSub_subject() {
                return sub_subject;
            }

            public void setSub_subject(String sub_subject) {
                this.sub_subject = sub_subject;
            }

            public List<Blocks> getBlocks() {
                return blocks;
            }

            public void setBlocks(List<Blocks> blocks) {
                this.blocks = blocks;
            }

            @Keep
            public static class Blocks {
                String blockId;
                String status;
                String name;
                String sub_subject;
                String sub_subject_id;
                Meta meta;
                int unlock;
                int unlock_ex;

                @Override
                public String toString() {
                    return "Blocks{" +
                            "blockId='" + blockId + '\'' +
                            ", status='" + status + '\'' +
                            ", name='" + name + '\'' +
                            ", sub_subject='" + sub_subject + '\'' +
                            ", sub_subject_id='" + sub_subject_id + '\'' +
                            ", unlock=" + unlock +
                            ", unlock_ex=" + unlock_ex +
                            ", meta=" + meta +
                            '}';
                }

                public Blocks(String blockId, String status, String name, String sub_subject, String sub_subject_id, Meta meta, int unlock, int unlock_ex) {
                    this.blockId = blockId;
                    this.status = status;
                    this.name = name;
                    this.sub_subject = sub_subject;
                    this.sub_subject_id = sub_subject_id;
                    this.meta = meta;
                    this.unlock=unlock;
                    this.unlock_ex=unlock_ex;
                }

                public int isUnlock() {
                    return unlock;
                }

                public void setUnlock(int unlock) {
                    this.unlock = unlock;
                }

                public int isUnlock_ex() {
                    return unlock_ex;
                }

                public void setUnlock_ex(int unlock_ex) {
                    this.unlock_ex = unlock_ex;
                }

                public Meta getMeta() {
                    return meta;
                }

                public void setMeta(Meta meta) {
                    this.meta = meta;
                }

                public String getBlockId() {
                    return blockId;
                }

                public void setBlockId(String blockId) {
                    this.blockId = blockId;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
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


                @Keep
                public static class Meta {

                    private String screen_type;
                    private String hint;
                    private String question;

                    public Meta() {
                        this.screen_type = "";
                        this.hint = "";
                        this.question = "";
                    }

                    public Meta( String screen_type,  String hint, String question) {
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
            }
        }
    }
}
