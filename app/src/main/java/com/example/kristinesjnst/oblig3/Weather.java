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
    public Weather() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStationName(String station_name) {
        this.station_name = station_name;
    }

    public void setStationPosition(String station_position) {
        this.station_position = station_position;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public int getId(){
        return id;
    }
    public String getStation_name(){
        return station_name;
    }
    public String getStation_position(){
        return station_position;
    }
    public String getTimestamp(){
        return timestamp;
    }
    public double getTemperature(){
        return temperature;
    }
    public double getPressure(){
        return pressure;
    }
    public double getHumidity(){
        return humidity;
    }

    @Override
    public String toString() {
        return "Weather{ " +
                "Id: " + id +
                "Station name: " + station_name +
                "Station position: " + station_position +
                "Timestamp: " + timestamp +
                "Temperature: " + temperature +
                "Pressure: " + pressure +
                "Humidity: " + humidity + "}";
    }
}