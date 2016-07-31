package com.jinchao.express.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jinchao.express.R;
import com.jinchao.express.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by OfferJiShu01 on 2016/7/28.
 */
@ContentView(R.layout.activity_splash)
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent =new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);

    }
}
