package com.weather.projectsky;

public class ForecastEntry {
    private String date;
    private double temperature;
    private String weatherDescription;

    public ForecastEntry(String date, double temperature, String weatherDescription) {
        this.date = date;
        this.temperature = temperature;
        this.weatherDescription = weatherDescription;
    }

    public String getDate() {
        return date;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }
}
