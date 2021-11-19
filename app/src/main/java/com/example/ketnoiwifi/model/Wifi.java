package com.example.ketnoiwifi.model;

public class Wifi {
    private int id;
    private String ssid;
    private String password;

    public Wifi(int id,String ssid, String password) {
        this.id = id;
        this.ssid = ssid;
        this.password = password;
    }

    public Wifi() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
