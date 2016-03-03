package com.example.kristinesjnst.oblig3;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

public class FragmentList extends Fragment implements OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private boolean isRunning = false;
    private ArrayList<Weather> weatherItems = null;
    private WeatherDataSource weatherDataSource;
    private ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        listView = (ListView)getActivity().findViewById(R.id.idlv);


        weatherDataSource = new WeatherDataSource(this.getActivity());
//        weatherDataSource.getAllWeather(weatherItems);
//
//
//        ArrayAdapter<Weather> adapter = new ArrayAdapter<>(this.getActivity(), R.layout.list_fragment, weatherItems);
//        listView.setAdapter(adapter);

        try {
            weatherDataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void getWeatherFromUrl() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String myURL = "http://kark.hin.no/~wfa/fag/android/2016/weather/vdata.php?id=1";
                HttpURLConnection httpURLConnection;

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
                        weatherDataSource.createWeatherData(weather.getId(), weather.getStation_name(), weather.getStation_position(), weather.getTimestamp(),
                                weather.getTemperature(), weather.getPressure(), weather.getHumidity());
                        weatherDataSource.getAllWeather();
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

    public void listWeather(Weather weather) {
//        weatherItems = new Gson().fromJson(String.valueOf(serverResponse), Weather.class);


        ((TextView) listView.findViewById(R.id.idView)).setText(weather.getId());
        ((TextView) listView.findViewById(R.id.stationNameView)).setText(weather.getStation_name());
        ((TextView) listView.findViewById(R.id.stationPositionView)).setText(weather.getStation_position());
        ((TextView) listView.findViewById(R.id.timestampView)).setText(weather.getTimestamp());
        ((TextView) listView.findViewById(R.id.temperatureView)).setText(String.valueOf(weather.getTemperature()));
        ((TextView) listView.findViewById(R.id.pressureView)).setText(String.valueOf(weather.getPressure()));
        ((TextView) listView.findViewById(R.id.humidityView)).setText(String.valueOf(weather.getHumidity()));

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}