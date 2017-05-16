package es.udc.apm.museos.presenter;

import android.content.Context;

import es.udc.apm.museos.view.MapView;

public interface MapPresenter {
    void initializeDiscovery(MapView view, Context context);
    void startDiscovery();
    void stopDiscovery();
}
