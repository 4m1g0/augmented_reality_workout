package es.udc.apm.museos.model;

import java.io.Serializable;

public class PictureBeacon implements Serializable {

    public PictureBeacon(int x, int y, String id) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.rssi = null;
        this.lastSeen = 0L;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PictureBeacon))
            return false;

        PictureBeacon other = (PictureBeacon) obj;

        return x == other.x && y == other.y;
    }

    public int x;
    public int y;
    public String id;
    public Float rssi;
    public Long lastSeen;
}
