package com.ctrl.music.locationmarker;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends ActionBarActivity {
    private static final String TAG = "MainActivity";
    private static final int TWO_MINUTES = 1000 * 60 * 2;

    private static final String locationProvider = LocationManager.NETWORK_PROVIDER;
    // private static final String locationProvider = LocationManager.GPS_PROVIDER;

    // motion sensor
    private SensorManager sensorManager;
    private List<Sensor> sensorList;

    // Control
    private TextView textViewAInformation;
    private TextView textViewBInformation;
    private TextView textViewCInformation;

    // Async task
    private OrienationAsyncTask orienationTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* List all the sensor, and the information add to the list */
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        textViewAInformation = (TextView) findViewById(R.id.textViewAInformation);
        textViewBInformation = (TextView) findViewById(R.id.textViewBInformation);
        textViewCInformation = (TextView) findViewById(R.id.textViewCInformation);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();

        stopMotionListening(Sensor.TYPE_ACCELEROMETER);
        stopMotionListening(Sensor.TYPE_ORIENTATION);

        if (orienationTask != null) {
            orienationTask.cancel(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        startMotionListening(Sensor.TYPE_ACCELEROMETER);
        startMotionListening(Sensor.TYPE_ORIENTATION);

        /* start a new async job to display some information */
        orienationTask = new OrienationAsyncTask();
        orienationTask.execute();
    }

    private class OrienationAsyncTask extends
            AsyncTask<Integer, Integer, String> {

        public OrienationAsyncTask() {
        }

        @Override
        protected String doInBackground(Integer... params) {
            Sensor sensorTmp;
            sensorTmp = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (isCancelled()) {
                    break;
                }
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }

    public boolean startMotionListening(int type) {
        Sensor sensorTmp;
        SensorEventListener listenerTmp = null;

        sensorTmp = sensorManager.getDefaultSensor(type);

        if (sensorTmp != null) {
            if (type == Sensor.TYPE_ACCELEROMETER) {
                listenerTmp = accelerationListener;
            } else if (type == Sensor.TYPE_ORIENTATION) {
                listenerTmp = orienationListener;
            }

            if (listenerTmp != null) {
                sensorManager.registerListener(listenerTmp, sensorTmp, SensorManager.SENSOR_DELAY_NORMAL);
            }

            return true;
        }
        return false;
    }

    public boolean stopMotionListening(int type) {
        Sensor sensorTmp;
        SensorEventListener listenerTmp = null;

        if (type == Sensor.TYPE_ACCELEROMETER) {
            listenerTmp = accelerationListener;
        } else if (type == Sensor.TYPE_ORIENTATION) {
            listenerTmp = orienationListener;
        }

        sensorManager.unregisterListener(listenerTmp);

        return true;
    }

    public SensorEventListener accelerationListener = new SensorEventListener() {
        @Override
        public final void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Do something here if sensor accuracy changes.
        }

        @Override
        public final void onSensorChanged(SensorEvent event) {
            // In this example, alpha is calculated as t / (t + dT),
            // where t is the low-pass filter's time-constant and
            // dT is the event delivery rate.

            /*
            final float alpha = 0.8f;

            // Isolate the force of gravity with the low-pass filter.
            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            // Remove the gravity contribution with the high-pass filter.
            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];
            */
            textViewAInformation.setText("x:" + event.values[0] + "\r\ny:" + event.values[1] + "\r\nz:" + event.values[2]);
        }
    };

    public SensorEventListener orienationListener = new SensorEventListener() {
        @Override
        public final void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Do something here if sensor accuracy changes.
        }

        @Override
        public final void onSensorChanged(SensorEvent event) {
            textViewBInformation.setText("x:" + event.values[0] + "\r\ny:" + event.values[1] + "\r\nz:" + event.values[2]);
        }
    };

    public boolean startLocationListening() {
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                //makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);

        return true;
    }

    /**
     * Determines whether one Location reading is better than the current Location fix
     *
     * @param location            The new Location that you want to evaluate
     * @param currentBestLocation The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

}
