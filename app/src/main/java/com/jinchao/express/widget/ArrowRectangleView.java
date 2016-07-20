package com.jinchao.express.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.jinchao.express.R;

/**
 * Created by OfferJiShu01 on 2016/7/19.
 */
public class ArrowRectangleView extends View {
    // arrow's width
    private int mArrowWidth = 20;
    // arrow's height
    private int mArrowHeight= 40;
    // background color
    private int mBackgroundColor = Color.WHITE;
    // arrow's horizontal offset relative to RIGHT side
    private int mArrowOffset = 0;

    public ArrowRectangleView(Context context) {
        this(context,null);
    }
    public ArrowRectangleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArrowRectangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a=context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArrowRectangleView,defStyleAttr,0);
        for (int i=0;i<a.getIndexCount();i++){
            int attr=a.getIndex(i);
            switch (attr){
                case R.styleable.ArrowRectangleView_arrow_width:
                    mArrowWidth = a.getDimensionPixelSize(attr, mArrowWidth);
                    break;
                case R.styleable.ArrowRectangleView_arrow_height:
                    mArrowHeight = a.getDimensionPixelSize(attr, mArrowHeight);
                    break;
                case R.styleable.ArrowRectangleView_background_color:
                    mBackgroundColor = a.getColor(attr, mBackgroundColor);
                    break;
                case R.styleable.ArrowRectangleView_arrow_offset:
                    mArrowOffset = a.getDimensionPixelSize(attr, mArrowOffset);
                    break;
            }
        }
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mBackgroundColor);
        paint.setStyle(Paint.Style.FILL);
        Path path = new Path();
        int startPoint = mArrowWidth;
        path.moveTo(startPoint, 0);
        path.lineTo(startPoint, mArrowHeight);
        path.lineTo(0 ,mArrowHeight/2);
        path.close();
        canvas.drawPath(path, paint);
    }
}
