package com.helping.skillseek.Objects;

public class hireeDetails {
    String id;
    String name;
    String username;
    String skill;
    String age;
    String downloadUrl;
    String phoneNumber;

    public hireeDetails(String id, String name, String uname, String[] skills, String age, String downloadUrl,String phoneNumber){

    }

    public hireeDetails(String id,String name, String username, String skill, String age,String downloadUrl,String phoneNumber) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.skill = skill;
        this.age = age;
        this.downloadUrl = downloadUrl;
        this.phoneNumber = phoneNumber;
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

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
