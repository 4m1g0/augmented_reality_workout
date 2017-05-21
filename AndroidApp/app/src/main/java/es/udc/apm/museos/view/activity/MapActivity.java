package es.udc.apm.museos.view.activity;

import android.Manifest;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.util.List;

import es.udc.apm.museos.R;
import es.udc.apm.museos.model.PictureBeacon;
import es.udc.apm.museos.presenter.MapPresenter;
import es.udc.apm.museos.presenter.MapPresenterBluetooth;
import es.udc.apm.museos.view.CompassCanvas;
import es.udc.apm.museos.view.MapCanvas;
import es.udc.apm.museos.view.MapView;

@EActivity
public class MapActivity extends AppCompatActivity implements MapView, SensorEventListener {
    private static final String TAG = "MapActivity";
    private MapPresenter mapPresenter;
    private MapCanvas map;
    SensorManager mSensorManager;
    float[] mGravity, mGeomagnetic;
    float azimut;
    CompassCanvas compass;
    Sensor accelerometer,magnetometer;

    @Override
    protected void onResume() {
        super.onResume();

        mapPresenter.startDiscovery();

        // is sensor does not exists, this bindings do nothing
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mapPresenter.stopDiscovery();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ConstraintLayout mainLayout = (ConstraintLayout) findViewById(R.id.mainMapLayout);

        map = new MapCanvas(this);
        mainLayout.addView(map, -1, -1);

        mapPresenter.initializeDiscovery(this, getApplicationContext());

        ImageButton button = (ImageButton) findViewById(R.id.arButton);
        button.bringToFront();

        compass = (CompassCanvas) findViewById(R.id.buttonCompass);
        compass.bringToFront();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapActivity.this, ARCameraActivity_.class);
                startActivity(i);
            }
        });

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Bean
    protected void setMapPresenter(MapPresenterBluetooth mapPresenter) {
        this.mapPresenter = mapPresenter;
    }

    @Override
    public void requestBluetoothPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
    }

    @Override
    public void showErrorAndFinish(String error) {
        Log.e(TAG, "ERROR: " + error);

        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(error)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    finish();
                    System.exit(0);
                })
                .setOnDismissListener(dialog -> {
                    finish();
                    System.exit(0);
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void showNotFullySupported() {
        TextView errorTv = (TextView) findViewById(R.id.map_textview);
        errorTv.setVisibility(View.VISIBLE);
        errorTv.bringToFront();
    }

    @Override
    public void updateMap(List<PictureBeacon> picturesList) {
        map.updateMarkers(picturesList);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimut = orientation[0]; // orientation contains: azimut, pitch and roll
            }
        }

        compass.setOrientation(azimut);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
