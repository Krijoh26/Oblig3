package com.example.kristinesjnst.oblig3;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.sql.SQLException;

public class WeatherDataSource {
    public SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private FragmentList parent;

    private String[] weatherColumns = {
            SQLiteHelper.KEY_PRIMARY_ID,
            SQLiteHelper.KEY_ID,
            SQLiteHelper.KEY_STATION_NAME,
            SQLiteHelper.KEY_STATION_POSITION,
            SQLiteHelper.KEY_TIMESTAMP,
            SQLiteHelper.KEY_TEMPERATURE,
            SQLiteHelper.KEY_PRESSURE,
            SQLiteHelper.KEY_HUMIDITY};


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

        if(result >= 0)
            return true;
        else
            return false;
    }

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

        close();
        return cursor;
    }

    public void deleteAllStoredData(){
        database.execSQL("DROP TABLE IF EXISTS " + SQLiteHelper.WEATHER_TABLE);
        database.execSQL(SQLiteHelper.WEATHER_TABLE_CREATE);
    }

    public Cursor getAllWeather (){
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbHelper.getReadableDatabase();
        return database.query(SQLiteHelper.WEATHER_TABLE, weatherColumns, null, null, null, null, null);
    }
}
