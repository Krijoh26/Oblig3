package com.example.kristinesjnst.oblig3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "WeatherDB.db";
    private static final int DATABASE_VERSION = 10;
    public static final String WEATHER_TABLE = "WeatherTable";

    public static final String KEY_PRIMARY_ID = "_id";
    public static final String KEY_ID = "id";
    public static final String KEY_STATION_NAME = "station_name";
    public static final String KEY_STATION_POSITION = "station_position";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_TEMPERATURE = "temperature";
    public static final String KEY_PRESSURE = "pressure";
    public static final String KEY_HUMIDITY = "humidity";


    /**
     * Oppretter string for opprettning av tabell
     */
    public static final String WEATHER_TABLE_CREATE = "CREATE TABLE "
            + WEATHER_TABLE
            + " (" + KEY_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_ID + " INTEGER, "
            + KEY_STATION_NAME + " TEXT, "
            + KEY_STATION_POSITION + " TEXT, "
            + KEY_TIMESTAMP + " TEXT, "
            + KEY_TEMPERATURE + " INTEGER, "
            + KEY_PRESSURE + " INTEGER, "
            + KEY_HUMIDITY + " INTEGER );";




    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Oppretter tabell
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WEATHER_TABLE_CREATE);
    }

    /**
     *  Oppdaterer tabell
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WEATHER_TABLE);
        onCreate(db);
    }
}