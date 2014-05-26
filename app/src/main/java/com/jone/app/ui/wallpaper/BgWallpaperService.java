package com.jone.app.ui.wallpaper;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.jone.app.R;

import java.io.IOException;


/**
 * Created by jone_admin on 14-1-20.
 */
public class BgWallpaperService extends WallpaperService{
    @Override
    public Engine onCreateEngine() {
        return new MyEngine();
    }

    private static void setWallpaper(Context context, int resourceId){
        //设置壁纸为应用bg,需加权限"android.permission.SET_WALLPAPER"
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        try {
            wallpaperManager.setResource(resourceId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class MyEngine extends Engine{
        private Bitmap bgBitmap;
        private final Paint paint = new Paint(); // 创建画笔
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            paint.setAntiAlias(true);// 打开抗锯齿
            initImg();
        }

        /**
         * 该方法是应用程序第一次创建时要调用。可在这个方法里调用父类对应方法。
         * 该方法执行完毕后系统会立即调用onSurfaceChanged方法
         */
        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            super.onSurfaceCreated(holder);
            draw(holder);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if(bgBitmap != null && !bgBitmap.isRecycled()){
                bgBitmap.recycle();
                bgBitmap = null;
            }
            System.gc();
        }

        public void initImg() {
            bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg02);
        }

        private void draw(SurfaceHolder holder){
            Canvas canvas = holder.lockCanvas(null);
            if (canvas != null) {
                canvas.drawBitmap(bgBitmap, 0, 0, null);
            }

            holder.unlockCanvasAndPost(canvas);
        }
    }
}
