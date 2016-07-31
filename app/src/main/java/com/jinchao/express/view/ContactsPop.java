package com.jinchao.express.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jinchao.express.MyApplication;
import com.jinchao.express.R;
import com.jinchao.express.base.CommonAdapter;
import com.jinchao.express.base.ViewHolder;
import com.jinchao.express.location.MyLocation;
import com.jinchao.express.utils.CommonUtils;
import com.jinchao.express.widget.ArrowRectangleView;
import com.jinchao.express.widget.SideBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by OfferJiShu01 on 2016/7/14.
 */
public class ContactsPop extends PopupWindow {
    private ListView lv;
    private TextView tv_location;
    public interface OnContactClickListener{
       void onClick(String str);
    };
    private OnContactClickListener onContactClickListener;
    public ContactsPop(final Context context, int width, int height, int arrowoffset, int ibHeight,OnContactClickListener onContactClickListener) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView=inflater.inflate(R.layout.pop_contacts,null);
        initView(contentView,context,arrowoffset,ibHeight);
        this.onContactClickListener=onContactClickListener;
        this.setContentView(contentView);
        this.setWidth(width);
        this.setHeight(height);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
        initData(context);
    }
    private void initView(View v,final Context context, int arrowoffset, int ibHeight){
        ArrowRectangleView arrow= (ArrowRectangleView) v.findViewById(R.id.sanjiao);
        tv_location= (TextView) v.findViewById(R.id.tv_location);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) arrow.getLayoutParams();
        params.setMargins(0,arrowoffset+(ibHeight- CommonUtils.dip2px(context,20))/2,0,0);
        arrow.setLayoutParams(params);
        lv = (ListView) v.findViewById(R.id.lv);
        SideBar sideBar = (SideBar) v.findViewById(R.id.sidebar);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
//                Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData(Context context){
        List<String> list =new ArrayList<String>();
        for(int i=0;i<100;i++){
            list.add("测试"+i);
        }
        CommonAdapter<String> adapter =new CommonAdapter<String>(context,list,R.layout.item_contacts_test) {
            @Override
            public void convert(ViewHolder helper, String item, int position) {
                helper.setText(R.id.tv_name,item);
                if (position<=3){
                    helper.setText(R.id.mi,"<100米");
                }else if(position<=10){
                    helper.setText(R.id.mi,"<500米");
                }else if(position<=20){
                    helper.setText(R.id.mi,"<1000米");
                }else if(position<=50){
                    helper.setText(R.id.mi,"<2000米");
                }
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 String str =(String)((ListView)parent).getItemAtPosition(position);
                onContactClickListener.onClick(str);
                ContactsPop.this.dismiss();
            }
        });
    }

    public void setLocationAddress(MyLocation myLocation){
        if (tv_location!=null){
            tv_location.setText(myLocation.getAddress());
        }
    }
    public void showPopupWindow(View parent,int x,int y){
        if (!this.isShowing()) {
            MyApplication.myApplication.locationService.start();
            this.showAtLocation(parent, Gravity.TOP,x,y);
        } else {
            this.dismiss();
        }
    }


}