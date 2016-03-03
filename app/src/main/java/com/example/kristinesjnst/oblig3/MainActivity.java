package com.example.kristinesjnst.oblig3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
                downloadData();
                return true;

            case R.id.quit:
                this.finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void downloadData() {
        FragmentList fragmentList = (FragmentList) getFragmentManager().findFragmentById(R.id.list_fragment);
        if(!fragmentList.isRunning()) {
            fragmentList.getWeather(30, 1);
        }
    }
}