package com.example.kristinesjnst.oblig3;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.sql.SQLException;

public class WeatherDataSource {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    private String[] weatherColumns = {
            SQLiteHelper.KEY_PRIMARY_ID,
            SQLiteHelper.KEY_ID,
            SQLiteHelper.KEY_STATION_NAME,
            SQLiteHelper.KEY_STATION_POSITION,
            SQLiteHelper.KEY_TIMESTAMP,
            SQLiteHelper.KEY_TEMPERATURE,
            SQLiteHelper.KEY_PRESSURE,
            SQLiteHelper.KEY_HUMIDITY};

    /**
     * Konstruktør
     */
    public WeatherDataSource(FragmentList parentFragment) {
        dbHelper = new SQLiteHelper(parentFragment.getActivity());
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * Lagrer data i databasen
     */
    public boolean createWeatherData (int id, String station_name, String station_position, String timestamp, double temperature, double pressure, double humidity)  {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

        return result >= 0;
    }
    /**
     * Henter ut data fra databasen med bruk av Id
     */
    public Cursor getDataWithStationId(int station_id) {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbHelper.getReadableDatabase();
        String whereClause = SQLiteHelper.KEY_ID + " = ?";
        String[] whereArgs = new String[] {String.valueOf(station_id)};

        Cursor cursor = database.query(SQLiteHelper.WEATHER_TABLE, weatherColumns, whereClause, whereArgs, null, null, null);

        return cursor;
    }

    /**
     * Sletter lokalt lagrede data
     */
    public void deleteAllStoredData(){
        try {
            open();
            database.delete(SQLiteHelper.WEATHER_TABLE, null, null);
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
