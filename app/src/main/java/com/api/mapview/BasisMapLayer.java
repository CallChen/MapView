package com.api.mapview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

/**
 * Created by Ryan.
 */
public class BasisMapLayer extends MapLayer {
    private Picture mPicture;
    private Paint mPaint;


    public BasisMapLayer(MapView mapView, final Bitmap bitmap) {
        super(mapView);
        mPaint = new Paint();

//        Log.d("ryan", "mapview:" + width + " " + height);
        mPicture = new Picture();
        Canvas canvas = mPicture.beginRecording(bitmap.getWidth(), bitmap.getHeight());
        canvas.drawBitmap(bitmap,0,0,mPaint);
        mPicture.endRecording();
        bitmap.recycle();
        mapView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                Log.d("ryan", "mapview1:" + getMapView().getMeasuredWidth() + " " +  getMapView().getMeasuredHeight());
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mPicture == null) return;
        int width = getMapView().getWidth();
        int height = getMapView().getHeight();
        float scale = calculateInSampleSize(mPicture.getWidth(), mPicture.getHeight(),width, height);
        Log.d("ryan", "mapview:" + width + " " + height);

        canvas.drawPicture(mPicture, new RectF(
                0,
                0,
                mPicture.getWidth() * (1 + scale),
                mPicture.getHeight() * (1 + scale)));
//        mPicture.draw(canvas);
    }

    private float calculateInSampleSize(int width,int height,int reqWidth, int reqHeight) {
        Log.d("ryan", "width:" + width+" height:" + height+" reqWidth:" + reqWidth+" reqHeight:" + reqHeight);
        float inSampleSize = 1;
        // 计算出实际宽高和目标宽高的比率
        float heightRatio =(float) height / (float) reqHeight;
        float widthRatio = (float) width / (float) reqWidth;
        // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
        // 一定都会大于等于目标的宽和高。
        inSampleSize = Math.abs(heightRatio) < Math.abs(widthRatio) ? heightRatio : widthRatio;

        Log.d("ryan", "inSampleSize:" + inSampleSize);
        return inSampleSize;
    }
}
