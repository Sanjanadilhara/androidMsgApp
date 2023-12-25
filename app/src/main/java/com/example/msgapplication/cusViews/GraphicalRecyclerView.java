package com.example.msgapplication.cusViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msgapplication.R;

public class GraphicalRecyclerView extends RecyclerView {

    private int[] colors={Color.RED, Color.BLUE};
    private float[] positions={0.0f, 1.0f};

    public void setGradientColors(int col1, int col2){
        this.colors[0]=col1;
        this.colors[1]=col2;

    }

    public GraphicalRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean drawChild(Canvas canvas, View child, long drawingTime) {
        FrameLayout message=child.findViewById(R.id.sentMsgBackground);
        if(message!=null){
            Paint p=new Paint();
            p.setShader(new LinearGradient(0, 0, 0, getResources().getDisplayMetrics().heightPixels, colors, positions, Shader.TileMode.CLAMP));
            p.setAntiAlias(true);
            canvas.drawRoundRect(child.getLeft()+message.getLeft(), child.getTop()+message.getTop(), child.getLeft()+message.getRight(), child.getTop()+message.getBottom(), 15, 15,  p);
        }

        return super.drawChild(canvas, child, drawingTime);
    }
}
