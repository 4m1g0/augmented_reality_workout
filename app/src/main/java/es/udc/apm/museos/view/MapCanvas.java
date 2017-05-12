package es.udc.apm.museos.view;

import android.content.Context;
import android.graphics.Canvas;

import es.udc.apm.museos.R;

public class MapCanvas extends android.support.v7.widget.AppCompatImageView {
    public MapCanvas(Context context) {
        super(context);

        setScaleType(ScaleType.FIT_XY);
        setBackgroundResource(R.drawable.icon_map);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.getWidth();
        canvas.getHeight();
    }
}
