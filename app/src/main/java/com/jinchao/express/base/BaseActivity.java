package com.jinchao.express.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.jinchao.express.utils.network.NetWorkManager;

import org.xutils.x;

/**
 * Created by OfferJiShu01 on 2016/7/8.
 */
public class BaseActivity extends FragmentActivity implements NetWorkManager.NetConnectChangeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        NetWorkManager.getInstance().regist(this);
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void change(NetWorkManager.NetWorkInfo netWorkInfo) {
//        Toast.makeText(this,netWorkInfo.simiProvider+""+netWorkInfo.netState+"",Toast.LENGTH_SHORT).show();
        System.out.println(netWorkInfo.netState+"");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetWorkManager.getInstance().unregist(this);
    }
}
