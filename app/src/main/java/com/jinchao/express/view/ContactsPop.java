package com.jinchao.express.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.jinchao.express.R;
import com.jinchao.express.utils.CommonUtils;
import com.jinchao.express.widget.ArrowRectangleView;

/**
 * Created by OfferJiShu01 on 2016/7/14.
 */
public class ContactsPop extends PopupWindow {
    public ContactsPop(Context context, int width, int height,int arrowoffset,int ibHeight) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView=inflater.inflate(R.layout.pop_contacts,null);
        ArrowRectangleView arrow= (ArrowRectangleView) contentView.findViewById(R.id.sanjiao);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) arrow.getLayoutParams();
        params.setMargins(0,arrowoffset+(ibHeight- CommonUtils.dip2px(context,20))/2,0,0);
        arrow.setLayoutParams(params);
        this.setContentView(contentView);
        this.setWidth(width);
        this.setHeight(height);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
    }


    public void showPopupWindow(View parent,int x,int y){
        if (!this.isShowing()) {
            this.showAtLocation(parent, Gravity.TOP,x,y);
        } else {
            this.dismiss();
        }
    }


}