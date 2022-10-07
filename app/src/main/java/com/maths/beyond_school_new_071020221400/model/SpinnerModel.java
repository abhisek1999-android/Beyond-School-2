package com.maths.beyond_school_new_071020221400.model;

import androidx.annotation.Keep;

@Keep
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
