package com.example.kristinesjnst.oblig3;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Switch;

import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
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
    private DownloadCallbacks downloadCallback;

    interface DownloadCallbacks {
        void onDownloadCancelled();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().findViewById(R.id.btnDownload).setOnClickListener(this);
        getActivity().findViewById(R.id.btnShowData).setOnClickListener(this);
        downloadSwitch = (Switch) getActivity().findViewById(R.id.btnDownload);
    }

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
        }
        radioButton = (RadioButton)getActivity().findViewById(R.id.radiobutton_humidity);
        if(radioButton.isChecked()){
            LineGraphSeries<DataPoint> series = generateLineGraphDataFromDB("humidity");
            graph.addSeries(series);
            series.setDrawDataPoints(true);
            series.setDataPointsRadius(10);
        }
        radioButton = (RadioButton)getActivity().findViewById(R.id.radiobutton_pressure);
        if(radioButton.isChecked()){
            LineGraphSeries<DataPoint> series = generateLineGraphDataFromDB("pressure");
            graph.addSeries(series);
            series.setDrawDataPoints(true);
            series.setDataPointsRadius(10);
        }
        graph.setTitle(station_name);
    }

    private LineGraphSeries<DataPoint> generateLineGraphDataFromDB(String dataType) {
        int count = 0;
        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        Cursor cursor = weatherDataSource.getAllWeather();

        //TODO:fix so the graph shows right graph

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
                //TODO:fix interval and download time
                DownloadAsyncTask task = new DownloadAsyncTask();
                task.execute();
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


    private class DownloadAsyncTask extends AsyncTask<Void, Void, String> {
        /**
         * NB! Man kan ikke oppdatere GUI fra doInBackGround():
         */
        @Override
        protected String doInBackground(Void... params) {
            return this.connectToServer();
        }

        @Override
        protected void onCancelled() {
            if (downloadCallback != null) {
                downloadCallback.onDownloadCancelled();
            }
        }

        private String connectToServer() {
            if (!isRunning) {
                    isRunning = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            long timeStart = System.currentTimeMillis();
                            long timeElapsed;
                            long runningTime = getDownloadTime() * 1000;
                            do {
                                getWeatherFromUrl();
                                timeElapsed = runningTime - timeStart;
                            }
                            while (isRunning && timeElapsed < runningTime);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (downloadSwitch.isActivated())
                                        downloadSwitch.toggle();
                                }
                            });
                        }
                    }).start();

                } else {
                    isRunning = false;
                    downloadSwitch.toggle();
                }
            return "0";
        }

        public void getWeatherFromUrl() {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String myURL = "http://kark.hin.no/~wfa/fag/android/2016/weather/vdata.php?id=" + getStationId();
                    HttpURLConnection httpURLConnection;
                    try {
                        Thread.sleep(1000*getInterval());

                        URL url = new URL(myURL);
                        httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");

                        int responseCode = httpURLConnection.getResponseCode();

                        if (responseCode == HttpURLConnection.HTTP_OK) {

                            Gson gson = new Gson();
                            Weather weather = gson.fromJson(new InputStreamReader(httpURLConnection.getInputStream()), Weather.class);
                            weatherDataSource.createWeatherData( weather.getId(), weather.getStation_name(), weather.getStation_position(), weather.getTimestamp(),
                                    weather.getTemperature(), weather.getPressure(), weather.getHumidity());
                        }

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
        }
    }

    /**
     * Set the callback to null so we don't accidentally leak the Activity instance.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        downloadCallback = null;
    }
}