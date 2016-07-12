package com.jinchao.express;

import android.app.Application;

import org.xutils.x;

/**
 * Created by OfferJiShu01 on 2016/7/8.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
    }
}
