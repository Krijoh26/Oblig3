package com.example.kristinesjnst.oblig3;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FragmentList fragmentList;
    Weather weather = new Weather();
    WeatherDataSource weatherDataSource = new WeatherDataSource(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        try {
            weatherDataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

//       ListView listView = (ListView) findViewById(R.id.idlv);
//
//        List<Weather> values = weatherDataSource.getAllWeather();
//
//
//        ArrayAdapter<Weather> adapter = new ArrayAdapter<Weather>(this, R.layout.list_fragment, values);
//        listView.setAdapter(adapter);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentList = (FragmentList)fragmentManager.findFragmentById(R.id.list_fragment);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.download:
                fragmentList.getWeatherFromUrl();
                return true;
            case R.id.showSaved:
                showSavedData();

            case R.id.quit:
                this.finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSavedData() {
        WeatherDataSource source = new WeatherDataSource(this);
        ArrayList<Weather> weatherDatas = source.getAllDataFromDb();

        ((TextView) findViewById(R.id.idView)).setText(String.valueOf(weatherDatas.get(3)));
    }
}