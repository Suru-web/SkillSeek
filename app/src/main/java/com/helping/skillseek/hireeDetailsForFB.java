package com.helping.skillseek;

public class hireeDetailsForFB {
    private String id;
    private String name;
    private String skill;
    private String downloadUrl;

    public hireeDetailsForFB() {
        // Required default constructor for Firebase
    }

    public hireeDetailsForFB(String id, String name, String skill,String downloadUrl) {
        this.id = id;
        this.name = name;
        this.skill = skill;
        this.downloadUrl =downloadUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getdownloadUrl() {
        return downloadUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public void setdownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}