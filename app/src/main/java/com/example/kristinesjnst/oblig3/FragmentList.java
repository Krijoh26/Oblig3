package com.example.kristinesjnst.oblig3;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

public class FragmentList extends Fragment implements View.OnClickListener {

    private boolean isRunning = false;
    private WeatherDataSource weatherDataSource;
    private String station_name = "null";
    private int downloadTime = 120;
    private int stationId = 0;
    private int interval = 1;
    private Switch downloadSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    /**
     * Aktiverer cookies og setter retained fragment
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        setRetainInstance(true);
    }
    /**
     * Setter nedlastning og visning av data på onclickListener
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().findViewById(R.id.btnDownload).setOnClickListener(this);
        getActivity().findViewById(R.id.btnShowData).setOnClickListener(this);

    }

    /**
     * Tegner graf
     */
    public void generateGraphView() {
        RadioButton radioButton;
        GraphView graph = (GraphView)this.getActivity().findViewById(R.id.graphView);
        graph.removeAllSeries();
        radioButton = (RadioButton)getActivity().findViewById(R.id.radiobutton_temperature);
        if(radioButton.isChecked()) {
            LineGraphSeries<DataPoint> series = generateLineGraphDataFromDB("temperature");
            graph.addSeries(series);
            series.setDrawDataPoints(true);
            series.setDataPointsRadius(10);
            series.setColor(Color.RED);
        }
        radioButton = (RadioButton)getActivity().findViewById(R.id.radiobutton_humidity);
        if(radioButton.isChecked()){
            LineGraphSeries<DataPoint> series = generateLineGraphDataFromDB("humidity");
            graph.addSeries(series);
            series.setDrawDataPoints(true);
            series.setDataPointsRadius(10);
            series.setColor(Color.GREEN);
        }
        radioButton = (RadioButton)getActivity().findViewById(R.id.radiobutton_pressure);
        if(radioButton.isChecked()){
            LineGraphSeries<DataPoint> series = generateLineGraphDataFromDB("pressure");
            graph.addSeries(series);
            series.setDrawDataPoints(true);
            series.setDataPointsRadius(10);
            series.setColor(Color.YELLOW);
        }
        graph.setTitle(station_name);
    }
    /**
     * Henter data fra database som settes inn i grafen
     */
    private LineGraphSeries<DataPoint> generateLineGraphDataFromDB(String dataType) {
        int count = 0;
        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        Cursor cursor = weatherDataSource.getDataWithStationId(getStationId());

        while(cursor.moveToNext()) {
            station_name = cursor.getString(cursor.getColumnIndex("station_name"));

            double temperature = cursor.getDouble(cursor.getColumnIndex(dataType));
            double x = count++;
            double y = temperature;
            DataPoint point = new DataPoint(x, y);
            dataPoints.add(point);

        }
        count = 0;
        DataPoint[] points = new DataPoint[dataPoints.size()];
        for (DataPoint point: dataPoints) {
            points[count] = point;
            count++;
        }
        return new LineGraphSeries<>(points);
    }


    @Override
    public void onStart() {
        super.onStart();
        weatherDataSource = new WeatherDataSource(this);
        try {
            weatherDataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        weatherDataSource.deleteAllStoredData();
        weatherDataSource.close();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnDownload:
                connectToServer();
                break;
            case R.id.btnShowData:
                generateGraphView();
                break;
        }
    }

    public int getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(int downloadTime) {
        this.downloadTime = downloadTime;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    /**
     * Starter ny tråd for nedlastning og setter nedlastningstid
     */

    private void connectToServer() {

        if (!isRunning) {
            isRunning = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long startTime = System.currentTimeMillis();
                    long timeElapsed;

                    do {
                        getWeatherFromUrl();
                        long timeEnd = System.currentTimeMillis();
                        timeElapsed = timeEnd - startTime;
                    }
                    while (isRunning && timeElapsed < 1000 * getDownloadTime());
                    downloadSwitch = (Switch) getActivity().findViewById(R.id.btnDownload);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(downloadSwitch.isChecked())
                                downloadSwitch.setChecked(false);
                        }
                    });
                }
            }).start();

        } else {
            isRunning = false;
        }
    }

    /**
     * Laster ned fra URL og legger det inn i databasen
     */
    public void getWeatherFromUrl() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String myURL = "http://kark.hin.no/~wfa/fag/android/2016/weather/vdata.php?id=" + getStationId();
                HttpURLConnection httpURLConnection;

                    try {

                        URL url = new URL(myURL);
                        httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");

                        int responseCode = httpURLConnection.getResponseCode();

                        if (responseCode == HttpURLConnection.HTTP_OK) {

                            Gson gson = new Gson();
                            Weather weather = gson.fromJson(new InputStreamReader(httpURLConnection.getInputStream()), Weather.class);
                            weatherDataSource.createWeatherData(weather.getId(), weather.getStation_name(), weather.getStation_position(), weather.getTimestamp(),
                                    weather.getTemperature(), weather.getPressure(), weather.getHumidity());
                        }


                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
//            }
        });

        thread.start();
        try {
            thread.join();
            Thread.sleep(1000 * getInterval());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}