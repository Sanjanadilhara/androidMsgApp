package com.example.msgapplication.cusViews;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SentMessageItem extends Drawable {

    private Paint paint;
    private Shader shader;

    private int[] colors;
    private float[] positions;
    private View parentView;
//    private static final String COLOR_SHADER_SRC =
//            "layout(color) uniform half4 iColor;\n"+
//                    "half4 main(float2 fragCoord) {\n" +
//                    "return iColor;\n" +
//                    "}";
//    private RuntimeShader shader;
    public SentMessageItem(View view, int screenHeight) {
        colors=new int[2];
        colors[0]=Color.BLUE;colors[1]=Color.RED;
        positions=new float[2];
        positions[0]=0.0f;positions[1]=1.0f;
        paint=new Paint();

        int[] coords=new int[2];
        view.getLocationOnScreen(coords);
        shader=new LinearGradient(0, -(coords[1]), 0, screenHeight, colors, positions, Shader.TileMode.CLAMP);
        System.out.println("coords: "+coords[1]+", "+ screenHeight);
        paint.setShader(shader);

    }

    @Override
    public void draw(@NonNull Canvas canvas) {

        canvas.drawRoundRect(0, 0, getBounds().width(), getBounds().height(), 5, 5, paint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
