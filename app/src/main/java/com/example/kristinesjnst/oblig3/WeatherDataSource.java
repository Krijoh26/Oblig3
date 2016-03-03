package com.example.kristinesjnst.oblig3;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

public class WeatherDataSource {
    public SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private FragmentList parent;


    public WeatherDataSource(FragmentList parentFragment) {
        parent = parentFragment;
        dbHelper = new SQLiteHelper(parent.getActivity());
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean createWeatherData (int id, String station_name, String station_position, String timestamp, double temperature, double pressure, double humidity)  {
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
}
