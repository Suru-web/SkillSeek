package com.helping.skillseek;

public class hireeDetailsForFB {
    private String id;
    private String name;
    private String skill;

    public hireeDetailsForFB() {
        // Required default constructor for Firebase
    }

    public hireeDetailsForFB(String id, String name, String skill) {
        this.id = id;
        this.name = name;
        this.skill = skill;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }
}