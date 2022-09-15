package com.maths.beyond_school_280720220930.model;

import androidx.annotation.Keep;

@Keep
public class BranchModel {
    int img;
    String sub;

    public BranchModel(int img, String sub) {
        this.img = img;
        this.sub=sub;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }
}
