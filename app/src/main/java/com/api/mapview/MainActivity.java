package com.api.mapview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = findViewById(R.id.mapView);

//        mMapView.loadMap(getBitmap());
//        mMapView.addLayer(new BitmapLayer(mMapView, getBitmap()));
        try {
            mMapView.loadMap(BitmapFactory.decodeStream(getAssets().open("pic.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMapView.addLayer(new AnimateLayer(mMapView));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private Bitmap getBitmap(){

       BitmapFactory.Options opts = new BitmapFactory.Options();
        //设置为true,代表加载器不加载图片,而是把图片的宽高读出来
       opts.inJustDecodeBounds=true;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic, opts);

        int imageWidth = opts.outWidth;
       int imageHeight = opts.outHeight;
        Log.d("ryan", "picture：" + imageWidth + " " + imageHeight);

        //得到屏幕的宽高
       Display display=getWindowManager().getDefaultDisplay();
       DisplayMetrics displayMetrics=new DisplayMetrics();
       display.getMetrics(displayMetrics);
        //获得像素大小
       int screenWidth=displayMetrics.widthPixels;
       int screenHeight=displayMetrics.heightPixels;
//        int screenWidth = mMapView.getMeasuredWidth();
//        int screenHeight = mMapView.getMeasuredHeight();

       Log.d("ryan", "screen：" + screenWidth + " " + screenHeight);

       int scale = calculateInSampleSize(opts,screenWidth,screenHeight);
       opts.inJustDecodeBounds=false;
       opts.inSampleSize=scale;
       return BitmapFactory.decodeResource(getResources(), R.drawable.pic, opts);
   }

    private int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }

        Log.d("ryan", "inSampleSize:" + inSampleSize);
        return inSampleSize;
    }
}
