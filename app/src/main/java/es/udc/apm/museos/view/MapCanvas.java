package es.udc.apm.museos.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.util.List;

import es.udc.apm.museos.R;
import es.udc.apm.museos.model.PictureBeacon;

public class MapCanvas extends android.support.v7.widget.AppCompatImageView {
    List<PictureBeacon> markerList;
    private static final int MARKER_WIDTH = 100;
    private static final int MARKER_HEIGHT = 160;

    public MapCanvas(Context context) {
        super(context);

        setScaleType(ScaleType.FIT_XY);
        setBackgroundResource(R.drawable.plano);
    }

    public void updateMarkers(List<PictureBeacon> markerList){
        this.markerList = markerList;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (markerList == null)
            return;

        float minRssi = Float.MAX_VALUE;

        for (PictureBeacon marker : markerList)
            if (marker.rssi < minRssi) minRssi = marker.rssi;

        for (PictureBeacon marker : markerList) {
            Drawable d;
            if (marker.rssi == Integer.MAX_VALUE)
                d = ContextCompat.getDrawable(getContext(), R.drawable.marker_disabled);
            else if (marker.rssi == minRssi)
                d = ContextCompat.getDrawable(getContext(), R.drawable.marker_near);
            else
                d = ContextCompat.getDrawable(getContext(), R.drawable.marker_normal);

            int x = (int) ((float)marker.x / (float)100 * (float)canvas.getWidth() - MARKER_WIDTH/2);
            int y = (int) ((float)marker.y / (float)100 * (float)canvas.getHeight() - MARKER_HEIGHT/2);
            d.setBounds(x, y, x + MARKER_WIDTH, y + MARKER_HEIGHT);
            d.draw(canvas);
        }
    }
}
