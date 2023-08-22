package com.helping.skillseek;

public class hireeDetailsForFB {
    private String id;
    private String name;
    private String skill;
    private String imageURL;

    public hireeDetailsForFB() {
        // Required default constructor for Firebase
    }

    public hireeDetailsForFB(String id, String name, String skill,String imageURL) {
        this.id = id;
        this.name = name;
        this.skill = skill;
        this.imageURL =imageURL;
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

    public String getImageURL() {
        return imageURL;
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

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}