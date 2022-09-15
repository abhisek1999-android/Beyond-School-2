package com.maths.beyond_school_280720220930.model;

import androidx.annotation.Keep;

@Keep
public class table_values {
    int val;
    String name;

    public table_values(int val, String name) {
        this.val = val;
        this.name = name;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
