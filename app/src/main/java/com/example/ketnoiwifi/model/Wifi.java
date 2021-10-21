package com.example.ketnoiwifi.model;

public class Wifi {
    private String ssid;
    private String password;
    private Boolean available;

    public Wifi(String ssid, String password, Boolean available) {
        this.ssid = ssid;
        this.password = password;
        this.available = available;
    }

    public Wifi() {
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

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
