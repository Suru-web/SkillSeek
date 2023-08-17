package com.helping.skillseek;

public class hireeDetails {
    String id;
    String name;
    String username;
    String skill;
    String age;

    public hireeDetails(){

    }

    public hireeDetails(String id,String name, String username, String skill, String age) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.skill = skill;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getSkill() {
        return skill;
    }

    public String getAge() {
        return age;
    }

}
