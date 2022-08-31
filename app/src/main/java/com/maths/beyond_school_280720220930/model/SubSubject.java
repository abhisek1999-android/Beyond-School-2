package com.maths.beyond_school_280720220930.model;

public class SubSubject {

    String subSubject;
    int total;
    int completed;
    int resource;

    public SubSubject(){}

    public String getSubSubject() {
        return subSubject;
    }

    public void setSubSubject(String subSubject) {
        this.subSubject = subSubject;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public SubSubject(String subSubject, int total, int completed, int resource) {
        this.subSubject = subSubject;
        this.total = total;
        this.completed = completed;
        this.resource=resource;
    }
}
