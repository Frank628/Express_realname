package com.jinchao.express.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import com.jinchao.express.R;
/**
 * Created by OfferJiShu01 on 2016/7/11.
 */
public class IDCardView extends RelativeLayout {
    public IDCardView(Context context) {
        super(context);

    }

    public IDCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_idcardview, this, true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setIDCard(String name, String gender, String nation, String year, String month, String day, String address, String cardnum, Drawable pic){

    }

}
