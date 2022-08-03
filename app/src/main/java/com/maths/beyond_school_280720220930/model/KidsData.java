package com.maths.beyond_school_280720220930.model;

public class KidsData {

    String name,kids_id,profile_url,age;

    public KidsData(){}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKids_id() {
        return kids_id;
    }

    public void setKids_id(String kids_id) {
        this.kids_id = kids_id;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public KidsData(String name, String kids_id, String profile_url, String age) {
        this.name = name;
        this.kids_id = kids_id;
        this.profile_url = profile_url;
        this.age = age;
    }
}
