package es.udc.apm.museos.presenter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import org.androidannotations.annotations.EBean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import es.udc.apm.museos.R;
import es.udc.apm.museos.model.PictureBeacon;
import es.udc.apm.museos.view.MapView;

@EBean
public class MapPresenterBluetooth implements MapPresenter {
    private Timer timer;
    private Handler mTimerHandler = new Handler();
    private BluetoothAdapter BTAdapter;
    private MapView view;
    private boolean discovering = false;

    private static final String TAG = "MapPresenterBluetooth";

    private List<PictureBeacon> picturesList = new ArrayList<>();

    @Override
    public void initializeDiscovery(MapView view, Context context) {
        /*
        The presenter should never know about the context nor even use the com.Android.* classes
        but it seems there is no way to implement bluetooth without breaking some pattern, so until
        someone can tell me how to do it in an other way I'll do it the easiest...
         */

        this.view = view;
        view.requestBluetoothPermission();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BluetoothManager BTManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            BTAdapter = BTManager.getAdapter();
        }
        else {
            BTAdapter = BluetoothAdapter.getDefaultAdapter();
        }

        readPictureList(context);

        if (BTAdapter == null){
            view.showNotFullySupported();
            return; // the application will still run.
        }

        // if the bluetooth is off we will not receive broadcasts
        if (!BTAdapter.isEnabled())
            BTAdapter.enable();

        context.registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }

    private void readPictureList(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("PictureBeacons.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            view.showErrorAndFinish(context.getString(R.string.ErrorReadFile));
        }

        try {
            JSONArray m_jArry = new JSONArray(json);

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo = m_jArry.getJSONObject(i);
                PictureBeacon picture = new PictureBeacon(
                        jo.getInt("x"),
                        jo.getInt("y"),
                        jo.getString("id")
                );
                
                picturesList.add(picture);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            view.showErrorAndFinish(context.getString(R.string.ErrorParsingFile));
        }
    }

    @Override
    public void startDiscovery() {
        if (discovering)
            return;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                mTimerHandler.post(() -> findRSSI());
            }
        };

        timer = new Timer();
        timer.schedule(task, 1, 30000);
        discovering = true;
    }

    @Override
    public void stopDiscovery() {
        timer.cancel();
        discovering = false;
    }

    // This broadcast receiver may be in the model and communicate to this presenter through a observer
    private final BroadcastReceiver receiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                int  rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d(TAG, "Name: " + device.getName()  + device.getName() + " RSSID: " + device.getAddress() + "  RSSI: " + rssi + "dBm");

                updateBeaconList(device, rssi);
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {
                Log.d(TAG, "Finished discovering");
            }
        }
    };

    private void updateBeaconList(BluetoothDevice device, int rssi) {
        for (PictureBeacon picture: picturesList) {
            if (!picture.id.equals(device.getAddress()))
                continue;

            picture.rssi = (picture.rssi * 2 +  rssi) / 3; // weighted mean
            Log.d(TAG, "FOUND: id: " + picture.id + " RSSI: " + rssi);
        }
    }

    private void findRSSI() {
        if (BTAdapter != null)
        {
            // if the bluetooth is off we will not receive broadcasts
            if (!BTAdapter.isEnabled())
                BTAdapter.enable();

            Log.d(TAG, "start discovery");
            BTAdapter.startDiscovery();
        }
    }
}
