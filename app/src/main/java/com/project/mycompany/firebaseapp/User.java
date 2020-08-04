package com.project.mycompany.firebaseapp;

public class User {
    private String id;
    private String email;
    private String contact;
    private String password;

    public User(){

    }

    public User(String id ,String email, String contact, String password) {
        this.id=id;
        this.email = email;
        this.contact = contact;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getContact() {
        return contact;
    }

    public String getPassword() {
        return password;
    }
}
