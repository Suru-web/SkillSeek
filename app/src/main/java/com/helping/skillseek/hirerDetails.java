package com.helping.skillseek;

public class hirerDetails {
    String id;
    String name;
    String username;
    String email;
    String address;
    String downloadUrl;
    String phoneNumber;

    public hirerDetails() {

    }

    public hirerDetails(String id, String name, String username, String email, String address,String downloadUrl,String phoneNumber) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.address = address;
        this.downloadUrl = downloadUrl;
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
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

    public String getdownloadUrl() {
        return downloadUrl;
    }
}
