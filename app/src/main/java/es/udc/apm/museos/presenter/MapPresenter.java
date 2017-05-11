package es.udc.apm.museos.presenter;

import android.content.Context;

import es.udc.apm.museos.view.MapView;

/**
 * Created by 4m1g0 on 11/05/17.
 */

public interface MapPresenter {
    void initializeDiscovery(MapView view, Context context);
    void startDiscovery();
}
