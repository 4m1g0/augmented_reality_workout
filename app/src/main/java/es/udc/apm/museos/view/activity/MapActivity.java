package es.udc.apm.museos.view.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.util.List;

import es.udc.apm.museos.R;
import es.udc.apm.museos.model.PictureBeacon;
import es.udc.apm.museos.presenter.MapPresenter;
import es.udc.apm.museos.presenter.MapPresenterBluetooth;
import es.udc.apm.museos.view.MapCanvas;
import es.udc.apm.museos.view.MapView;

@EActivity
public class MapActivity extends AppCompatActivity implements MapView {
    private static final String TAG = "MapActivity";
    private MapPresenter mapPresenter;
    private MapCanvas map;

    @Override
    protected void onResume() {
        super.onResume();

        mapPresenter.startDiscovery();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mapPresenter.stopDiscovery();
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapActivity.this, ARCameraActivity_.class);
                startActivity(i);
            }
        });
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
        // TODO: Implement this with a text view informing about the situation
        Log.e(TAG, "ERROR: not fully supported");
    }

    @Override
    public void updateMap(List<PictureBeacon> picturesList) {
        map.updateMarkers(picturesList);
    }
}
