package com.maths.beyond_school_280720220930.retrofit.model.grade;

import java.util.List;

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

    private class English {
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


        private class Sub_Subjects {

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

            private class Blocks {
                String blockId;
                String status;
                String name;
                String sub_subject;
                String sub_subject_id;

                @Override
                public String toString() {
                    return "Blocks{" +
                            "blockId='" + blockId + '\'' +
                            ", status='" + status + '\'' +
                            ", name='" + name + '\'' +
                            ", sub_subject='" + sub_subject + '\'' +
                            ", sub_subject_id='" + sub_subject_id + '\'' +
                            '}';
                }

                public Blocks(String blockId, String status, String name, String sub_subject, String sub_subject_id) {
                    this.blockId = blockId;
                    this.status = status;
                    this.name = name;
                    this.sub_subject = sub_subject;
                    this.sub_subject_id = sub_subject_id;
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
            }
        }
    }
}
