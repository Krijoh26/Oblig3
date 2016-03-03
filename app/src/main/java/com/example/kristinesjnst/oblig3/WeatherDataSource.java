package com.example.kristinesjnst.oblig3;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

public class WeatherDataSource {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private Activity parent;
    private String[] allColumns;

    public String[] weatherColumns = {
            SQLiteHelper.KEY_ID,
            SQLiteHelper.KEY_STATION_NAME,
            SQLiteHelper.KEY_STATION_POSITION,
            SQLiteHelper.KEY_TIMESTAMP,
            SQLiteHelper.KEY_TEMPERATURE,
            SQLiteHelper.KEY_PRESSURE,
            SQLiteHelper.KEY_HUMIDITY
    };

    public WeatherDataSource(Activity parent) {
        this.parent = parent;
        dbHelper = new SQLiteHelper(parent);
        allColumns = new String[] {"station_name","station_position", "timestamp", "temperature", "pressure", "humidity"};
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Cursor getAllWeather() {
        Cursor cursor = database.query(SQLiteHelper.WEATHER_TABLE, weatherColumns, null, null, null, null, null);
        return cursor;
    }

    public Cursor getWeatherByStationName(String stationName) {
        String selectQuery = "select * from WeatherTable where station_name = ?";
        String[] selectParams = new String[] {stationName};
        Cursor cursor = database.rawQuery(selectQuery, selectParams);
        return cursor;
    }

    public Cursor getWeatherById(int id) {
        String selectQuery = "select * from WeatherTable where id = ?";
        String[] selectParams = new String[] {String.valueOf(id)};
        Cursor cursor = database.rawQuery(selectQuery, selectParams);
        return cursor;
    }

//    public Weather cursorToWeather(Cursor cursor) {
//        Weather weather = new Weather();
//
//        int keyIndex = cursor.getColumnIndexOrThrow(WeatherDBTable.WEATHER_COL_ID);
//        int stationNameIndex = cursor.getColumnIndexOrThrow(WeatherDBTable.WEATHER_COL_STATION_NAME);
//        int stationPositionIndex = cursor.getColumnIndexOrThrow(WeatherDBTable.WEATHER_COL_STATION_POSITION);
//        int timestampIndex = cursor.getColumnIndexOrThrow(WeatherDBTable.WEATHER_COL_TIMESTAMP);
//        int temperatureIndex = cursor.getColumnIndexOrThrow(WeatherDBTable.WEATHER_COL_TEMPERATURE);
//        int pressureIndex = cursor.getColumnIndexOrThrow(WeatherDBTable.WEATHER_COL_PRESSURE);
//        int humidityIndex = cursor.getColumnIndexOrThrow(WeatherDBTable.WEATHER_COL_HUMIDITY);
//
//        weather.setId(cursor.getInt(keyIndex));
//        weather.setStation_name(cursor.getString(stationNameIndex));
//        weather.setStation_position(cursor.getString(stationPositionIndex));
//        weather.setTimestamp(cursor.getString(timestampIndex));
//        weather.setTemperature(cursor.getString(temperatureIndex));
//        weather.setPressure(cursor.getString(pressureIndex));
//        weather.setHumidity(cursor.getString(humidityIndex));
//
//        return  weather;
//    }

    public void createWeatherData (int id, String station_name, String station_position, String timestamp, double temperature,
                                          double pressure, double humidity)  {

        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.KEY_ID, id);
        values.put(SQLiteHelper.KEY_STATION_NAME, station_name);
        values.put(SQLiteHelper.KEY_STATION_POSITION, station_position);
        values.put(SQLiteHelper.KEY_TIMESTAMP, timestamp);
        values.put(SQLiteHelper.KEY_TEMPERATURE, temperature);
        values.put(SQLiteHelper.KEY_PRESSURE, pressure);
        values.put(SQLiteHelper.KEY_HUMIDITY, humidity);

        database.insert(SQLiteHelper.WEATHER_TABLE, null, values);


    }
}
