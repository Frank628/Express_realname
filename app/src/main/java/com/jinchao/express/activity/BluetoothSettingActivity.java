package com.jinchao.express.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jinchao.express.Constants;
import com.jinchao.express.R;
import com.jinchao.express.base.BaseActivity;
import com.jinchao.express.utils.SharePrefUtil;
import com.jinchao.express.widget.NavigationLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 95 on 2016/7/30.
 */
@ContentView(R.layout.bluetooth_setting)
public class BluetoothSettingActivity extends BaseActivity {
    @ViewInject(R.id.device_forget) LinearLayout device_forget;
    @ViewInject(R.id.long_time) LinearLayout long_time;
    @ViewInject(R.id.device_remember) LinearLayout device_remember;
    @ViewInject(R.id.rt) LinearLayout rt;
    @ViewInject(R.id.textview_time) TextView textview_time;
    @ViewInject(R.id.textview_remember) TextView textview_remember;
    @ViewInject(R.id.navigation)
    NavigationLayout navigationLayout;
    private String[] bluetooth_time = new String[] { "0", "15", "30" };
    private String[] remember_bluetooth = new String[] { "是", "否" };
    private TimeRadioOnClick timeRadioOnClick;
    private RememberRadioOnClick rememberRadioOnClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationLayout.setCenterText(getResources().getString(R.string.bluetooth_settings));
        navigationLayout.setLeftTextOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothSettingActivity.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean isRememberBluetooth= SharePrefUtil.getBoolean(this, Constants.IS_REMEMBER_BLUTOOTH,false);
        textview_remember.setText(isRememberBluetooth?"是":"否");
        if (isRememberBluetooth){
            rememberRadioOnClick = new RememberRadioOnClick(0);
            rt.setVisibility(View.VISIBLE);
        }else{
            rememberRadioOnClick = new RememberRadioOnClick(1);
            rt.setVisibility(View.GONE);
        }
        int time =SharePrefUtil.getInt(this,Constants.BluetoothSetting_long_time,15);
        textview_time.setText(time + "分钟");
        switch (time) {
            case 0:
                timeRadioOnClick = new TimeRadioOnClick(0);
                break;

            case 15:
                timeRadioOnClick = new TimeRadioOnClick(1);
                break;

            case 30:
                timeRadioOnClick = new TimeRadioOnClick(2);
                break;

        }

    }

    @Event(value = R.id.device_forget)
    private void forgetClick(View view){
        confirm("提示", "确认忘记蓝牙设备?");
    }
    @Event(value = R.id.long_time)
    private void longtimeClick(View view){
        AlertDialog ad = new AlertDialog.Builder(this).setTitle("蓝牙最长连接时间")
                .setSingleChoiceItems(bluetooth_time, timeRadioOnClick.getIndex(), timeRadioOnClick).create();
        ad.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface arg0) {
                onResume();
            }
        });
        ad.show();
    }
    @Event(value = R.id.device_remember)
    private void devicerememberClick(View view){
        AlertDialog ad2 = new AlertDialog.Builder(this).setTitle("默认记住蓝牙")
                .setSingleChoiceItems(remember_bluetooth, rememberRadioOnClick.getIndex(), rememberRadioOnClick)
                .create();
        ad2.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface arg0) {
                onResume();
            }
        });
        ad2.show();
    }
    /**
     * 确认框
     *
     * @param title
     * @param msg
     */
    protected void confirm(String title, String msg) {
        AlertDialog.Builder  builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setTitle(title);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SharePrefUtil.saveString(BluetoothSettingActivity.this, "mac", null);
                Toast.makeText(BluetoothSettingActivity.this, "已忘记蓝牙设备", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    /**
     * 点击单选框事件
     *
     * @author xmz
     *
     */
    class TimeRadioOnClick implements DialogInterface.OnClickListener {
        private int index;

        public TimeRadioOnClick(int index) {
            this.index = index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public void onClick(DialogInterface dialog, int whichButton) {
            setIndex(whichButton);
            SharePrefUtil.saveInt(BluetoothSettingActivity.this, Constants.BluetoothSetting_long_time, Integer.parseInt(bluetooth_time[index]));
            dialog.dismiss();
        }
    }

    /**
     * 点击单选框事件
     *
     * @author xmz
     *
     */
    class RememberRadioOnClick implements DialogInterface.OnClickListener {
        private int index;

        public RememberRadioOnClick(int index) {
            this.index = index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public void onClick(DialogInterface dialog, int whichButton) {
            setIndex(whichButton);
            if (index == 1) {
                SharePrefUtil.saveString(BluetoothSettingActivity.this, "mac", null);
                SharePrefUtil.saveInt(BluetoothSettingActivity.this, Constants.BluetoothSetting_long_time, 0);
            }
            SharePrefUtil.saveBoolean(BluetoothSettingActivity.this, Constants.IS_REMEMBER_BLUTOOTH, remember_bluetooth[index].equals("是")?true:false);
            dialog.dismiss();
        }
    }
}
