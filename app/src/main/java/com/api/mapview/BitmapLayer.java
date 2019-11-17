package com.api.mapview;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;

/**
 * Created by Ryan.
 */
public class BitmapLayer extends MapLayer {
    private Bitmap mBitmap;
    private PointF mPoint;
    float[] position = new float[]{0f,0f};
    private Paint mPaint;

    public BitmapLayer(MapView mapView, Bitmap bitmap) {
        super(mapView);
        this.mBitmap = bitmap;
        this.mPaint = new Paint();
    }

    public void setPosition(PointF position) {
        mPoint = position;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Matrix matrix = getMatrix();
        if (mPoint != null) {
            position[0] = mPoint.x;
            position[1] = mPoint.y;
            matrix.getValues(position);
        }
        canvas.save();
//        canvas.translate(position[0],position[1]);
        canvas.drawBitmap(mBitmap,matrix,mPaint);
        canvas.restore();
    }

}
