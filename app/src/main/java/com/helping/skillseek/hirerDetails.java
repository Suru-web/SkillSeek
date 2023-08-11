package com.helping.skillseek;

public class hirerDetails {
    String id;
    String name;
    String username;
    String email;
    String address;

    public hirerDetails() {

    }

    public hirerDetails(String id, String name, String username, String email, String address) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.address = address;
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
}
