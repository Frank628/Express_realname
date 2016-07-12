package com.jinchao.express.base;

import android.app.Activity;
import android.os.Bundle;

import org.xutils.x;

/**
 * Created by OfferJiShu01 on 2016/7/8.
 */
public class BaseActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }
}
