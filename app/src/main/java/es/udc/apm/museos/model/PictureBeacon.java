package es.udc.apm.museos.model;

import java.io.Serializable;

/**
 * Created by 4m1g0 on 11/05/17.
 */

public class PictureBeacon implements Serializable {

    public PictureBeacon(int x, int y, String id) {
        this.x = x;
        this.y = y;
        this.id = id;

    }


    public int x;
    public int y;
    public String id;
}
