package com.jinchao.express;

import android.os.Environment;

/**
 * Created by OfferJiShu01 on 2016/7/28.
 */
public class Constants {

    public static final String DB_NAME="config.db";
    public static final String DB_PATH= Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
    public static final String IS_REMEMBER_BLUTOOTH="IS_REMEMBER_BLUTOOTH";
    public static final String DEVICE_WAY="DEVICE_WAY";
    public static final String NET_WAY="NET_WAY";
    public static final String BluetoothSetting_long_time="BluetoothSetting_long_time";
    public static final String REMEMBER_BLUETOOTH="REMEMBER_BLUETOOTH";
    public static final String[] DEVICE_AREA={ "自动", "蓝牙", "OTG", "NFC" };
    public static final String[] NET_AREA={ "自动", "移动", "电信", "联通", "手动" };
}
