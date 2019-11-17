package com.api.mapview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.LinkedList;

/**
 * Created by Ryan.
 */
public class MapView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private HandlerThread mHandlerThread;
    private volatile Matrix mMatrix;
    private DrawHelper mDrawHelper;
    private LinkedList<MapLayer> mLayers;


    public MapView(Context context) {
        super(context);
        init();
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        getHolder().addCallback(this);
        mLayers = new LinkedList<>();
        mMatrix = new Matrix();
    }

    public void loadMap(Bitmap bitmap) {
        addLayer(new BasisMapLayer(this, bitmap));
    }

    public void addLayer(MapLayer layer) {
        mLayers.add(layer);
    }

    public Matrix getMapMatrix() {
        return mMatrix;
    }

    public void refresh() {
        if (mDrawHelper != null) {
            mDrawHelper.refresh();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mHolder = surfaceHolder;
        mHandlerThread = new HandlerThread("map_view");
        mHandlerThread.start();

        mDrawHelper = new DrawHelper(mHolder, new Handler(mHandlerThread.getLooper()));
        mDrawHelper.setDrawableList(mLayers);
        refresh();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mHolder = null;
        mDrawHelper.release();
        if (mHandlerThread != null) {
            mHandlerThread.quit();
        }
    }
}
