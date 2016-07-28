package com.jinchao.express;

import android.app.Application;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.jinchao.express.location.LocationService;
import com.jinchao.express.location.MyLocation;

import org.greenrobot.eventbus.EventBus;
import org.xutils.x;

/**
 * Created by OfferJiShu01 on 2016/7/8.
 */
public class MyApplication extends Application {
    public static MyApplication myApplication=null;
    public LocationService locationService;
    public MyLocation myLocation=new MyLocation();
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication=this;
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        locationService = new LocationService(getApplicationContext());
        locationService.registerListener(BDListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
    }

    private BDLocationListener BDListener=new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                myLocation.setCity(location.getCity()==null?"":location.getCity());
                myLocation.setAddress(location.getAddrStr()==null?"":location.getAddrStr());
                myLocation.setStreet(location.getStreet()==null?"":location.getStreet());
                myLocation.setProvince(location.getProvince()==null?"":location.getProvince());
                myLocation.setLat(location.getLatitude());
                myLocation.setLog(location.getLongitude());
                EventBus.getDefault().post(myLocation);
                locationService.stop();
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                } else if (location.getLocType() == BDLocation.TypeServerError) {
//						服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//						网络不同导致定位失败，请检查网络是否通畅
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//						无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机
                }
            }
        }
    };
}
