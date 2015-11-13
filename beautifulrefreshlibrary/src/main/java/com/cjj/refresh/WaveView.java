package com.cjj.refresh;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cjj on 2015/8/5.
 * 绘制贝塞尔来绘制波浪形
 */
public class WaveView extends View {
    private int waveHeight;//曲线高度
    private int headHeight;//真是高度
    Path path;
    Paint paint;

    public WaveView(Context context) {
        this(context, null, 0);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        path = new Path();
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
    }

    public int getHeadHeight() {
        return headHeight;
    }

    public void setHeadHeight(int headHeight) {
        this.headHeight = headHeight;
    }
    public int getWaveHeight() {
        return waveHeight;
    }
    public void setWaveHeight(int waveHeight) {
        this.waveHeight = waveHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //重置画笔
        path.reset();
        path.lineTo(0, headHeight);
        //绘制贝塞尔曲线

        //quadTo(x1，y1,x2,y2) //x1,y1是控制点的坐标值,,, y1,y2是终点的坐标
        path.quadTo(getMeasuredWidth() / 2, waveHeight + headHeight, getMeasuredWidth(), headHeight);
        path.lineTo(getMeasuredWidth(), 0);

/*        path.lineTo(150, 200);
        path.quadTo(0, 300, 150, 400);

        path.quadTo(250,500,350,400);

        path.quadTo(500,300,350,200);
        path.quadTo(250,100,150,200);*/
        canvas.drawPath(path, paint);
    }

}
