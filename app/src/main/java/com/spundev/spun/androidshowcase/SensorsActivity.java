package com.spundev.spun.androidshowcase;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class SensorsActivity extends AppCompatActivity implements SensorEventListener {

    // TextViews
    TextView mLightSensorTextView;
    TextView mGravitySensorTextView;
    TextView mPressureSensorTextView;
    TextView mProximitySensorTextView;
    TextView mAmbientTemperatureSensorTextView;
    private SensorManager mSensorManager;
    // Sensors
    private Sensor mLightSensor;
    private Sensor mGravitySensor;
    private Sensor mPressureSensor;
    private Sensor mProximitySensor;
    private Sensor mAmbientTemperatureSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // Sensors
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mGravitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mPressureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mAmbientTemperatureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        // TextViews
        mLightSensorTextView = (TextView) findViewById(R.id.light_sensor_textview);
        mGravitySensorTextView = (TextView) findViewById(R.id.gravity_sensor_textview);
        mPressureSensorTextView = (TextView) findViewById(R.id.pressure_sensor_textview);
        mProximitySensorTextView = (TextView) findViewById(R.id.proximity_sensor_textview);
        mAmbientTemperatureSensorTextView = (TextView) findViewById(R.id.ambient_temperature_sensor_textview);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mLightSensor != null) {
            mSensorManager.registerListener(this, mLightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (mGravitySensor != null) {
            mSensorManager.registerListener(this, mGravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (mPressureSensor != null) {
            mSensorManager.registerListener(this, mPressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (mProximitySensor != null) {
            mSensorManager.registerListener(this, mProximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (mAmbientTemperatureSensor != null) {
            mSensorManager.registerListener(this, mAmbientTemperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_LIGHT) {
            float lux = event.values[0];
            mLightSensorTextView.setText(String.valueOf(lux));

        } else if (sensor.getType() == Sensor.TYPE_GRAVITY) {
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];
            String output = String.valueOf(axisX) + " | " + String.valueOf(axisY) + " | " + String.valueOf(axisZ);
            mGravitySensorTextView.setText(output);

        } else if (sensor.getType() == Sensor.TYPE_PRESSURE) {
            float millibar = event.values[0];
            mPressureSensorTextView.setText(String.valueOf(millibar));

        } else if (sensor.getType() == Sensor.TYPE_PROXIMITY) {
            float cm = event.values[0];
            mProximitySensorTextView.setText(String.valueOf(cm));

        } else if (sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float celsius = event.values[0];
            mAmbientTemperatureSensorTextView.setText(String.valueOf(celsius));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Si la precisión cambia.
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sensors, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
