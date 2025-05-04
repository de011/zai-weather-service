package com.zaiweather.model;

public class WeatherResponse {
    private int temperatureDegrees;
    private double windSpeed;

    public WeatherResponse(int temperatureDegrees, double windSpeed) {
        this.temperatureDegrees = temperatureDegrees;
        this.windSpeed = windSpeed;
    }

    public int getTemperatureDegrees() {
        return temperatureDegrees;
    }

    public double getWindSpeed() {
        return windSpeed;
    }
}
