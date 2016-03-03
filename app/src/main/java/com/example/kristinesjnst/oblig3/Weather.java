package com.example.kristinesjnst.oblig3;

public class Weather {
    public int id;
    public String station_name;
    public String station_position;
    public String timestamp;
    public double temperature;
    public double pressure;
    public double humidity;

    public Weather(int id, String station_name, String station_position, String timestamp,
                   double temperature, double pressure, double humidity) {
        super();
        this.id = id;
        this.station_name = station_name;
        this.station_position = station_position;
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
    }
}