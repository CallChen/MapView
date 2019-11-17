package com.api.mapview;

import android.graphics.Canvas;
import android.graphics.Matrix;

/**
 * Created by Ryan.
 */
public abstract class MapLayer implements Drawable {

    private MapView mMapView;
    private boolean mVisibility = true;

    public MapLayer(MapView mapView) {
        this.mMapView = mapView;
    }

    @Override
    public void draw(Canvas canvas) {
        if (isVisibility()) {
            onDraw(canvas);
        }
    }

    public MapView getMapView() {
        return mMapView;
    }

    protected Matrix getMatrix() {
        return mMapView.getMapMatrix();
    }

    protected abstract void onDraw(Canvas canvas);

    public boolean isVisibility() {
        return mVisibility;
    }

    public void setVisibility(boolean visibility) {
        mVisibility = visibility;
    }
}
