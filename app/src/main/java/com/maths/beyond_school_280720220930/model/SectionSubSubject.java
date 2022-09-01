package com.maths.beyond_school_280720220930.model;

import java.util.Date;
import java.util.List;

public class SectionSubSubject {

    private String sectionName;
    private List<SubSubject> sectionItems;

    public SectionSubSubject(String sectionName, List<SubSubject> sectionItems) {
        this.sectionName = sectionName;
        this.sectionItems = sectionItems;
    }

    public List<SubSubject> getSectionItems() {
        return sectionItems;
    }

    public void setSectionItems(List<SubSubject> sectionItems) {
        this.sectionItems = sectionItems;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }



    @Override
    public String toString() {
        return "Section{" +
                "sectionName='" + sectionName + '\'' +
                ", sectionItems=" + sectionItems +
                '}';
    }
}
