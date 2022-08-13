package com.maths.beyond_school_280720220930.model;

public class SpinnerModel {
    boolean isHeader;
    int name;

    public SpinnerModel(boolean isHeader, int name) {
        this.isHeader = isHeader;
        this.name = name;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

}
