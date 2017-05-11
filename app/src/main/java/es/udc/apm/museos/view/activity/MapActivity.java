package es.udc.apm.museos.view.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import es.udc.apm.museos.R;
import es.udc.apm.museos.presenter.MapPresenter;
import es.udc.apm.museos.presenter.MapPresenterBluetooth;
import es.udc.apm.museos.view.MapView;

@EActivity
public class MapActivity extends AppCompatActivity implements MapView {

    private MapPresenter mapPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapPresenter.initializeDiscovery(this, getApplicationContext());
    }

    @Bean
    protected void setMapPresenter(MapPresenterBluetooth mapPresenter) {
        this.mapPresenter = mapPresenter;
    }

    public void discoveryClick(View view) {
        mapPresenter.startDiscovery();
    }

    @Override
    public void requestBluetoothPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
    }
}
