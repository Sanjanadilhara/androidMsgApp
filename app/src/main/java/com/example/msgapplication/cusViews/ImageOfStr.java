package com.example.msgapplication.cusViews;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ImageOfStr extends Drawable {
    private Paint paint;
    private Paint background;
    private String str;
    public ImageOfStr(String value){
        this.str=value.toUpperCase();
        paint=new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setTextSize(55);

        this.background=new Paint();
        int temCol=Math.abs(str.hashCode())%5000;
        background.setColor(Color.rgb((temCol*10)%180, (temCol*566)%180, (temCol*1880)%180));
        background.setStyle(Paint.Style.FILL);

    }
    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect textBound=new Rect();
        paint.getTextBounds(this.str, 0, 1, textBound);

        canvas.drawRect(0, 0, getBounds().width(), getBounds().height(), background);
        canvas.drawText(this.str, 0, 1, (getBounds().width()/2f), (getBounds().height()/2f)+textBound.height()/2f, paint);

    }

    @Override
    public void setAlpha(int alpha) {
        this.background.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        background.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return this.background.getAlpha();
    }
}
