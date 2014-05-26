package com.jone.app.ui.wallpaper;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * Created by jone_admin on 14-1-20.
 */
public class LiveWallpaper extends WallpaperService
{
    // 实现WallpaperService必须实现的抽象方法
    public Engine onCreateEngine()
    {
        // 返回自定义的Engine
        return new MyEngine();
    }

    class MyEngine extends Engine
    {
        // 记录程序界面是否可见
        private boolean mVisible;
        // 记录当前当前用户动作事件的发生位置
        private float mTouchX = -1;
        private float mTouchY = -1;
        // 记录当前圆圈的绘制位置

        //左上角坐标
        private float cx1 = 15;
        private float cy1 = 20;

        //右下角坐标
        private float cx2 = 300;
        private float cy2 = 380;

        //右上角坐标
        private float cx3 = 300;
        private float cy3 = 20;

        //左下角坐标
        private float cx4 = 15;
        private float cy4 = 380;

        // 定义画笔
        private Paint mPaint = new Paint();
        // 定义一个Handler
        Handler mHandler = new Handler();
        // 定义一个周期性执行的任务
        private final Runnable drawTarget = new Runnable()
        {
            public void run()
            {
                // 动态地绘制图形
                drawFrame();
            }
        };

        @Override
        public void onCreate(SurfaceHolder surfaceHolder)
        {
            super.onCreate(surfaceHolder);
            // 初始化画笔
            mPaint.setColor(0xffffffff);
            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(2);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStyle(Paint.Style.STROKE);
            // 设置处理触摸事件
            setTouchEventsEnabled(true);
        }

        @Override
        public void onDestroy()
        {
            super.onDestroy();
            // 删除回调
            mHandler.removeCallbacks(drawTarget);
        }

        @Override
        public void onVisibilityChanged(boolean visible)
        {
            mVisible = visible;
            // 当界面可见时候，执行drawFrame()方法。
            if (visible)
            {
                // 动态地绘制图形
                drawFrame();
            }
            else
            {
                // 如果界面不可见，删除回调
                mHandler.removeCallbacks(drawTarget);
            }
        }

        public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
                                     float yStep, int xPixels, int yPixels)
        {
            drawFrame();
        }


        public void onTouchEvent(MotionEvent event)
        {
            // 如果检测到滑动操作
            if (event.getAction() == MotionEvent.ACTION_MOVE)
            {
                mTouchX = event.getX();
                mTouchY = event.getY();
            }
            else
            {
                mTouchX = -1;
                mTouchY = -1;
            }
            super.onTouchEvent(event);
        }

        // 定义绘制图形的工具方法
        private void drawFrame()
        {
            // 获取该壁纸的SurfaceHolder
            final SurfaceHolder holder = getSurfaceHolder();
            Canvas c = null;
            try
            {
                // 对画布加锁
                c = holder.lockCanvas();
                if (c != null)
                {
                    c.save();
                    // 绘制背景色
                   c.drawColor(0xff000000);
                    // 在触碰点绘制圆圈
                    drawTouchPoint(c);

                    // 绘制圆圈
                    c.drawCircle(cx1, cy1, 80, mPaint);
                    c.drawCircle(cx2, cy2, 40, mPaint);
                    c.drawCircle(cx3, cy3, 50, mPaint);
                    c.drawCircle(cx4, cy4, 60, mPaint);
                    c.restore();
                }
            }
            finally
            {
                if (c != null)
                    holder.unlockCanvasAndPost(c);
            }
            mHandler.removeCallbacks(drawTarget);
            // 调度下一次重绘
            if (mVisible)
            {
                cx1 += 6;
                cy1 += 8;
                // 如果cx1、cy1移出屏幕后从左上角重新开始
                if (cx1 > 320)
                    cx1 = 15;
                if (cy1 > 400)
                    cy1 = 20;


                cx2 -= 6;
                cy2 -= 8;
                // 如果cx2、cy2移出屏幕后从右下角重新开始
                if (cx2 <15)
                    cx2 = 300;
                if (cy2 <20)
                    cy2 = 380;


                cx3 -= 6;
                cy3 += 8;
                // 如果cx3、cy3移出屏幕后从右上角重新开始
                if (cx3 <0)
                    cx3 = 300;
                if (cy3 >400)
                    cy3 = 20;


                cx4 += 6;
                cy4 -= 8;
                // 如果cx4、cy4移出屏幕后从左下角重新开始
                if (cx4 >320)
                    cx4 = 15;
                if (cy4 <0)
                    cy4 = 380;

                // 指定0.1秒后重新执行mDrawCube一次
                mHandler.postDelayed(drawTarget, 100);
            }
        }

        // 在屏幕触碰点绘制圆圈
        private void drawTouchPoint(Canvas c)
        {
            if (mTouchX >= 0 && mTouchY >= 0)
            {
                c.drawCircle(mTouchX, mTouchY, 40, mPaint);
            }
        }
    }
}