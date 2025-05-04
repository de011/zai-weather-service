package com.zaiweather.model;

/**
 * Represents a simplified weather response with temperature and wind speed.
 */
public class WeatherResponse {

    private int temperatureDegrees;
    private double windSpeed;

    /**
     * Default constructor required for deserialization.
     */
    public WeatherResponse() {
    }

    /**
     * Constructs a WeatherResponse with the given temperature and wind speed.
     *
     * @param temperatureDegrees Temperature in degrees Celsius
     * @param windSpeed Wind speed in m/s or km/h depending on API source
     */
    public WeatherResponse(int temperatureDegrees, double windSpeed) {
        this.temperatureDegrees = temperatureDegrees;
        this.windSpeed = windSpeed;
    }

    public int getTemperatureDegrees() {
        return temperatureDegrees;
    }

    public void setTemperatureDegrees(int temperatureDegrees) {
        this.temperatureDegrees = temperatureDegrees;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    @Override
    public String toString() {
        return "WeatherResponse{" +
                "temperatureDegrees=" + temperatureDegrees +
                ", windSpeed=" + windSpeed +
                '}';
    }
}
