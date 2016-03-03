package com.example.kristinesjnst.oblig3;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import com.google.gson.Gson;

public class FragmentList extends Fragment implements OnItemClickListener {

    private SimpleCursorAdapter cursorAdapter = null;
    private WeatherDataSource weatherDataSource = null;
    private ListView listView;
    private int currentPosition = 0;
    private static final String POSITION = "position";
    private boolean isRunning = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null)
            currentPosition = savedInstanceState.getInt(POSITION);

        listView = (ListView) getActivity().findViewById(R.id.idlv);

        String[] from = new String[]{SQLiteHelper.KEY_ID,
                SQLiteHelper.KEY_STATION_NAME,
                SQLiteHelper.KEY_STATION_POSITION,
                SQLiteHelper.KEY_TIMESTAMP,
                SQLiteHelper.KEY_TEMPERATURE,
                SQLiteHelper.KEY_PRESSURE,
                SQLiteHelper.KEY_HUMIDITY};
        int[] to = new int[]{R.id.idView, R.id.stationNameView, R.id.stationPositionView, R.id.timestampView, R.id.temperatureView, R.id.pressureView, R.id.humidityView};

        cursorAdapter = new SimpleCursorAdapter(this.getActivity(), R.layout.list_fragment, null, from, to, 0);
        listView.setAdapter(cursorAdapter);

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(POSITION, currentPosition);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onStart() {
        super.onStart();
        weatherDataSource = new WeatherDataSource(this.getActivity());
        try {
            weatherDataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.updateWeatherList();
    }

    private void updateWeatherList() {
        Cursor cursor = weatherDataSource.getAllWeather();
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onStop() {
        super.onStop();
        weatherDataSource.close();
    }

    public  void getWeather() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String myURL = "http://kark.hin.no/~wfa/fag/android/2016/weather/vdata.php?id=0";
                HttpURLConnection httpURLConnection;
                WeatherDataSource weatherDataSource;

                try {
                    URL url = new URL(myURL);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");

                    int responseCode = httpURLConnection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Gson gson = new Gson();
                        Weather weather = gson.fromJson(new InputStreamReader(httpURLConnection.getInputStream()), Weather.class);
                        weatherDataSource = new WeatherDataSource(getActivity());
                        weatherDataSource.open();
                        weatherDataSource.createWeatherData(weather.id,
                                weather.station_name,
                                weather.station_position,
                                weather.timestamp,
                                weather.temperature,
                                weather.pressure,
                                weather.humidity);
                        weatherDataSource.close();
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

        isRunning = true;
        thread.start();
    }

    public boolean isRunning() {
        return isRunning;
    }
}