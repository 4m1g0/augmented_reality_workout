package es.udc.apm.museos.view.activity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

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

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run(){
                        findRSSI();
                    }
                });
            }
        };

        registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        timer.schedule(task, 1, 10000);
    }

    private void findRSSI() {
        Log.d("TEST", "Buscando RSSI");
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("TEST", "receive");
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                int  rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d("TEST", "Name: " + name  + device.getName() + " RSSID: " + device.getAddress() + "  RSSI: " + rssi + "dBm");
            }
        }
    };

    public void discoveryClick(View view) {
        if (BTAdapter != null)
        {
            Log.d("TEST", "start discovery");
            BTAdapter.startDiscovery();
        }

    }
}
