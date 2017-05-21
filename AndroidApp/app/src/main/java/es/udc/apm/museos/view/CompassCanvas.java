package es.udc.apm.museos.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

import es.udc.apm.museos.R;

public class CompassCanvas extends android.support.v7.widget.AppCompatImageView {
    private float mOrientation = 0;

    public CompassCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CompassCanvas(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.rotate(-mOrientation*360/(2*3.14159f), getWidth()/2, getHeight()/2);
        Drawable d = ContextCompat.getDrawable(getContext(), R.drawable.orientation);
        d.setBounds(0, 0, getWidth(), getHeight());
        d.draw(canvas);
        invalidate();
    }

    public void setOrientation(float angle) {
        mOrientation = angle;
        invalidate();
    }
}
