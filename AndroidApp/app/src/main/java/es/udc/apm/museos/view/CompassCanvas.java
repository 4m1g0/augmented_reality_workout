package es.udc.apm.museos.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import es.udc.apm.museos.R;

public class CompassCanvas extends android.support.v7.widget.AppCompatImageView {
    private float mOrientation = 0;
    private float tempOrientationSin = 0;
    private float tempOrientationCos = 0;
    private int count = 0;

    public CompassCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CompassCanvas(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float orientation = mOrientation;
        canvas.rotate(orientation, getWidth()/2, getHeight()/2);
        Drawable d = ContextCompat.getDrawable(getContext(), R.drawable.orientation);
        d.setBounds(0, 0, getWidth(), getHeight());
        d.draw(canvas);
        invalidate();
    }

    public void setOrientation(float angle) {
        // angle -pi to pi
        tempOrientationSin += Math.sin(angle);
        tempOrientationCos += Math.cos(angle);

        count++;

        if (count == 5) {
            float angleRad = (float)Math.atan2(tempOrientationSin, tempOrientationCos) + 3.14159f;
            float angleDeg = angleRad*360/(2*3.14159f);

            mOrientation = 360 - angleDeg;
            tempOrientationSin = 0;
            tempOrientationCos = 0;

            invalidate();
            count = 0;
        }
    }
}
