package com.example.kristinesjnst.oblig3;

import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private FragmentList fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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
            case R.id.downloadTime120sek:
                fragmentList.setDownloadTime(120);
                isChecked(item);
                return true;
            case R.id.downloadTime60sek:
                fragmentList.setDownloadTime(60);
                isChecked(item);
                return true;
            case R.id.downloadTime10sek:
                fragmentList.setDownloadTime(10);
                isChecked(item);
                return true;
            case R.id.downloadTime5sek:
                fragmentList.setDownloadTime(5);
                isChecked(item);
                return true;
            case R.id.nullgraderslia:
                fragmentList.setStationId(0);
                isChecked(item);
                return true;
            case R.id.iskaldtoppen:
                fragmentList.setStationId(1);
                isChecked(item);
                return true;
            case R.id.stranda:
                fragmentList.setStationId(2);
                isChecked(item);
                return true;
            case R.id.syden:
                fragmentList.setStationId(3);
                isChecked(item);
                return true;
            case R.id.nordpolen:
                fragmentList.setStationId(4);
                isChecked(item);
                return true;
            case R.id.interval1sek:
                fragmentList.setInterval(1);
                isChecked(item);
                return true;
            case R.id.interval5sek:
                fragmentList.setInterval(5);
                isChecked(item);
                return true;
            case R.id.delete:
                deleteDialog();
                return true;
            case R.id.quit:
                quitDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void isChecked(MenuItem item) {
        if(item.isChecked()) {
            item.setChecked(false);
        } else {
            item.setChecked(true);
        }
    }

    public void deleteDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Delete");
        alertDialog.setMessage("Are you sure you want to delete all data from database? ");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new WeatherDataSource(fragmentList).deleteAllStoredData();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void quitDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Quit");
        alertDialog.setMessage("Are you sure you want to quit? ");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}