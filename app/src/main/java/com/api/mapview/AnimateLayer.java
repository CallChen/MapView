package com.api.mapview;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by Ryan.
 */
public class AnimateLayer extends MapLayer {

    private Paint mPaint;
    private float mRadius;
    private int mAlpha;
    private ValueAnimator mAnimation;


    public AnimateLayer(MapView mapView) {
        super(mapView);
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.FILL);
        mAlpha = 255;
        mAnimation = new ValueAnimator();
        mAnimation.setFloatValues(0,1);
        mAnimation.setInterpolator(new TimeInterpolator() {

            private final static int ACCURACY = 256;

            private int mLastI = 0;
            @Override
            public float getInterpolation(float input) {
//                if (input <= 0) {
//                    return 0;
//                } else if (input >= 1) {
//                    return 1;
//                }

                float t = input;
                // 近似求解t的值[0,1]
                for (int i = mLastI; i < ACCURACY; i++) {

                    t = 1.0f * i / ACCURACY;

                    double x = cubicCurves(t, 0, 0, 0.63, 0);

                    if (x >= input) {

                        mLastI = i;

                        break;

                    }

                }

                double value = cubicCurves(t, 0,0.21, 1, 1);

                if (value > 0.999d) {

                    value = 1;

                    mLastI = 0;

                }

                return (float) value;
            }

            private double cubicCurves(double t, double value0, double value1,

                                             double value2, double value3) {

                double value;

                double u = 1 - t;

                double tt = t * t;

                double uu = u * u;

                double uuu = uu * u;

                double ttt = tt * t;


                value = uuu * value0;

                value += 3 * uu * t * value1;

                value += 3 * u * tt * value2;

                value += ttt * value3;

                return value;

            }
        });
        mAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                Log.d("ryan", "onAnimationUpdate:" + value);
                mRadius = value * 100;
                mAlpha = Math.round((1 - value) * 255);
                getMapView().refresh();
            }
        });
        mAnimation.setDuration(2000);
        mAnimation.setRepeatMode(ValueAnimator.REVERSE);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("ryan", "onDraw:" + mRadius);
        mPaint.setAlpha(mAlpha);
        canvas.drawCircle(400,400,mRadius,mPaint);
        if (!mAnimation.isRunning()) {
            mAnimation.start();
        }
    }
}
