package com.example.weatherapp;

public class WeatherModal {
    private String time;
    private String temp_c;
    private String icon;
    private String windSpeed;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemp() {
        return temp_c;
    }

    public void setTemp(String temp) {
        this.temp_c = temp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public WeatherModal(String time, String temp, String icon, String windSpeed) {
        this.time = time;
        this.temp_c = temp;
        this.icon = icon;
        this.windSpeed = windSpeed;
    }
}
