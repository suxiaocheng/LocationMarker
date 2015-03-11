package com.ctrl.music.locationmarker;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class SettingActivity extends ActionBarActivity {
    private static final String TAG = "MainActivity";
    private ScrollView sensorInfo;
    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    private StringBuffer sensorStringBuffer;
    private TextView sensorInfoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sensorInfo = (ScrollView) findViewById(R.id.scrollViewSensorInformation);
        sensorInfoText = (TextView) findViewById(R.id.textViewSensorInformation);
        if(sensorInfo != null) {

            sensorStringBuffer = new StringBuffer();

            /* List all the sensor, and the information add to the list */
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

            Iterator<Sensor> list = sensorList.listIterator();
            for (; list.hasNext(); ) {
                Sensor sensorTmp;
                sensorTmp = list.next();
                sensorStringBuffer.append(sensorTmp.toString() + "\r\n");
            }
            sensorInfoText.setText(sensorStringBuffer);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
