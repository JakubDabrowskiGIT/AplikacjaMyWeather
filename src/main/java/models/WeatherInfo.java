package models;

public class WeatherInfo {
    private double temp;
    private int humidity;
    private int pressure;
    private int clouds;


    public WeatherInfo(double temp, int humidity, int pressure, int clouds) {
        this.temp = temp;
        this.humidity = humidity;
        this.pressure = pressure;
        this.clouds = clouds;
    }

    public double getTemp() {
        return temp;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public int getClouds() {
        return clouds;
    }
}
