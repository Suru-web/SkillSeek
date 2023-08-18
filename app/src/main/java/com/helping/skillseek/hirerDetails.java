package com.helping.skillseek;

public class hirerDetails {
    String id;
    String name;
    String username;
    String email;
    String address;
    String imageDownloadUrl;

    public hirerDetails() {

    }

    public hirerDetails(String id, String name, String username, String email, String address,String imageDownloadUrl) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.address = address;
        this.imageDownloadUrl = imageDownloadUrl;
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

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getImageDownloadUrl() {
        return imageDownloadUrl;
    }
}
