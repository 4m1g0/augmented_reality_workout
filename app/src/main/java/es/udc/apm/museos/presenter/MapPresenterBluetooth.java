package es.udc.apm.museos.presenter;

import android.Manifest;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import org.androidannotations.annotations.EBean;

import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import es.udc.apm.museos.view.MapView;

/**
 * Created by 4m1g0 on 11/05/17.
 */

@EBean
public class MapPresenterBluetooth implements MapPresenter {
    Timer timer = new Timer();
    private Handler mTimerHandler = new Handler();
    private BluetoothManager BTManager;
    private BluetoothAdapter BTAdapter;

    @Override
    public void initializeDiscovery(MapView view, Context context) {
        /*
        The presenter should never know about the context nor even use the com.Android.* classes
        but it seems there is no way to implement bluetooth without breaking some pattern, so until
        someone can tell me how to do it in an other way I'll do it the easiest...
         */
        BTManager = (BluetoothManager)  context.getSystemService(Context.BLUETOOTH_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
            BTAdapter = BTManager.getAdapter();
        else
            BTAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!BTAdapter.isEnabled())
            BTAdapter.enable();



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

        context.registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        timer.schedule(task, 1, 10000);
    }

    @Override
    public void startDiscovery() {
        if (BTAdapter != null)
        {
            Log.d("TEST", "start discovery");
            BTAdapter.startDiscovery();
        }
    }

    // This broadcast receiver may be in the model and communicate to this presenter through a observer
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

    private void findRSSI() {
        Log.d("TEST", "Buscando RSSI");
    }
}
