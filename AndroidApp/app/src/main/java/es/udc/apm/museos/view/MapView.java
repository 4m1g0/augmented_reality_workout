package es.udc.apm.museos.view;

import java.util.List;

import es.udc.apm.museos.model.PictureBeacon;

public interface MapView {
    void requestBluetoothPermission();
    void showErrorAndFinish(String error);
    void showNotFullySupported();
    void updateMap(List<PictureBeacon> picturesList);
}
