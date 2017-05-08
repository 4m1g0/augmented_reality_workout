package es.udc.apm.museos.view.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.Timer;

import es.udc.apm.museos.R;

public class MapActivity extends AppCompatActivity {
    Timer timer = new Timer();
    private Handler mTimerHandler = new Handler();
    private BluetoothManager BTManager;
    private BluetoothAdapter BTAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        BTManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
            BTAdapter = BTManager.getAdapter();
        else
            BTAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!BTAdapter.isEnabled())
            BTAdapter.enable();

    }

}
