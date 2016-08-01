package com.jinchao.express.activity;

import android.os.Bundle;
import android.view.View;

import com.jinchao.express.R;
import com.jinchao.express.base.BaseActivity;
import com.jinchao.express.widget.NavigationLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 95 on 2016/8/1.
 */
@ContentView(R.layout.activity_about)
public class AboutActivity extends BaseActivity {
    @ViewInject(R.id.navigation)
    private NavigationLayout navigationLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationLayout.setCenterText(getResources().getString(R.string.about_us));
        navigationLayout.setLeftTextOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutActivity.this.finish();
            }
        });
    }
}
