package com.example.ketnoiwifi.model;

public class Password {
    private int id;
    private String password;

    public Password(int id,String password) {
        this.id = id;
        this.password = password;
    }

    public Password() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
