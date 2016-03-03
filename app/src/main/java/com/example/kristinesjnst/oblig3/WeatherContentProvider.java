package com.example.kristinesjnst.oblig3;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class WeatherContentProvider extends ContentProvider{

    private SQLiteHelper sqLiteHelper = null;
    public static final Uri CONTENT_URI = Uri.parse("content://com.example.kristinesjnst.oblig3.weathercontentprovider");


    @Override
    public boolean onCreate() {
        sqLiteHelper = new SQLiteHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        String groupBy = null;
        String having = null;

        String path = uri.getPath();
        Cursor cursor = null;

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(SQLiteHelper.WEATHER_TABLE);

        cursor = queryBuilder.query(db, projection, selection, selectionArgs, groupBy, having, sortOrder);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        long id = db.insert(SQLiteHelper.WEATHER_TABLE, null, values);
        Uri retval = Uri.parse(CONTENT_URI + "/"+String.valueOf(id));

        getContext().getContentResolver().notifyChange(uri, null);
        return retval;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        int noRows = db.delete(SQLiteHelper.WEATHER_TABLE, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);
        return noRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
