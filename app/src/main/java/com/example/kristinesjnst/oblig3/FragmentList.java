package com.example.kristinesjnst.oblig3;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FragmentList extends Fragment implements OnItemClickListener {

    private boolean isRunning = false;
    private ArrayList<Weather> weatherArrayList = new ArrayList<>();
    private WeatherDataSource weatherDataSource = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onStart() {
        super.onStart();
        weatherDataSource = new WeatherDataSource(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void getWeather() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String myURL = "http://kark.hin.no/~wfa/fag/android/2016/weather/vdata.php?id=0";
                HttpURLConnection httpURLConnection;

                try {
                    URL url = new URL(myURL);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");

                    int responseCode = httpURLConnection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Gson gson = new Gson();
                        weatherArrayList.add(gson.fromJson(new InputStreamReader(httpURLConnection.getInputStream()), Weather.class));
                        for(Weather weather : weatherArrayList) {
                            weatherDataSource.createWeatherData(weather.id, weather.station_name, weather.station_position, weather.timestamp,
                                    weather.temperature, weather.pressure, weather.humidity);
                        }

                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
        isRunning = true;

    }

    public boolean isRunning() {
        return isRunning;
    }
}