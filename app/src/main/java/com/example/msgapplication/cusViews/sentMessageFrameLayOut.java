package com.example.msgapplication.cusViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

public class sentMessageFrameLayOut extends FrameLayout {
    private Paint paint=new Paint();
    private int[] colors={Color.RED, Color.BLUE};
    private float[] positions={0.0f, 1.0f};
    public sentMessageFrameLayOut(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        int[] coords=new int[2];
        getLocationOnScreen(coords);
        paint.setShader(new LinearGradient(0, -(coords[1]), 0, getResources().getDisplayMetrics().heightPixels, colors, positions, Shader.TileMode.CLAMP));
//        paint.setColor(Color.BLUE);
//        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(0, 0, getWidth(), getHeight(), 15, 15, paint);

    }
}
