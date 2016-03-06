package com.example.kristinesjnst.oblig3;

public class Weather {
    public int id;
    public String station_name;
    public String station_position;
    public String timestamp;
    public double temperature;
    public double pressure;
    public double humidity;

    public Weather() {
    }

    public void setId(int id) {
        this.id = id;
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

}