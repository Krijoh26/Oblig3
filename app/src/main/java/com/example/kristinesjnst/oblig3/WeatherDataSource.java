package com.example.kristinesjnst.oblig3;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.w3c.dom.Comment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WeatherDataSource {
    public SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    private String[] weatherColumns = {
            SQLiteHelper.KEY_ID,
            SQLiteHelper.KEY_STATION_NAME,
            SQLiteHelper.KEY_STATION_POSITION,
            SQLiteHelper.KEY_TIMESTAMP,
            SQLiteHelper.KEY_TEMPERATURE,
            SQLiteHelper.KEY_PRESSURE,
            SQLiteHelper.KEY_HUMIDITY};


    public WeatherDataSource(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean createWeatherData (int id, String station_name, String station_position, String timestamp, double temperature, double pressure, double humidity)  {
        dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.KEY_ID, id);
        values.put(SQLiteHelper.KEY_STATION_NAME, station_name);
        values.put(SQLiteHelper.KEY_STATION_POSITION, station_position);
        values.put(SQLiteHelper.KEY_TIMESTAMP, timestamp);
        values.put(SQLiteHelper.KEY_TEMPERATURE, temperature);
        values.put(SQLiteHelper.KEY_PRESSURE, pressure);
        values.put(SQLiteHelper.KEY_HUMIDITY, humidity);

        long result = database.insert(SQLiteHelper.WEATHER_TABLE, null, values);

        if(result >= 0)
            return true;
        else
            return false;
    }

    public List<Weather> getAllWeather() {
        List<Weather> list = new ArrayList<Weather>();

        Cursor cursor = database.query(SQLiteHelper.WEATHER_TABLE, weatherColumns, null, null, null, null, null);
       cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Weather weather = cursorToComment(cursor);
            list.add(weather);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    private Weather cursorToComment(Cursor cursor) {
        Weather weather = new Weather();
        weather.setId(cursor.getInt(0));
        weather.setStationName(cursor.getString(1));
        weather.setStationPosition(cursor.getString(2));
        weather.setTimestamp(cursor.getString(3));
        weather.setTemperature(cursor.getInt(4));
        weather.setPressure(cursor.getInt(5));
        weather.setHumidity(cursor.getInt(6));
        return weather;
    }

    private Weather cursorToWeather(Cursor cursor) {
        Weather weather = new Weather();
        weather.setId(cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteHelper.KEY_ID)));
        weather.setStationName(cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.KEY_STATION_NAME)));
        weather.setStationPosition(cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.KEY_STATION_POSITION)));
        weather.setTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.KEY_TIMESTAMP)));
        weather.setTemperature(cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteHelper.KEY_TEMPERATURE)));
        weather.setPressure(cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteHelper.KEY_PRESSURE)));
        weather.setHumidity(cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteHelper.KEY_HUMIDITY)));

        return weather;
    }
}
