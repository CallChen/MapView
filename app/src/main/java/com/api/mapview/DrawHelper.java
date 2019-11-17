package com.api.mapview;

import android.graphics.Canvas;
import android.os.Handler;
import android.view.SurfaceHolder;

import java.util.List;

/**
 * Created by Ryan.
 */
public class DrawHelper implements Runnable{

    private Handler mHandler;
    private SurfaceHolder mHolder;
    private List<? extends Drawable> mDrawableList;

    public DrawHelper(SurfaceHolder holder,Handler handler) {
        this.mHandler = handler;
        this.mHolder = holder;
    }

    public void setDrawableList(List<? extends Drawable> list) {
        this.mDrawableList = list;
    }

    @Override
    public void run() {
        Canvas canvas = null;
        try {
            canvas = mHolder.lockCanvas();
            //执行具体的绘制操作
            draw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (canvas != null) {
                mHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void refresh() {
        if (mHandler != null) {
            mHandler.post(this);
        }
    }

    private void draw(Canvas canvas) {
        if (mDrawableList != null) {
            for (int i = 0; i < mDrawableList.size(); i++) {
                mDrawableList.get(i).draw(canvas);
            }
        }
    }

    public void release() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }
}
