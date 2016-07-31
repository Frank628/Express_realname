package com.jinchao.express.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinchao.express.R;
import com.jinchao.express.base.BaseActivity;
import com.jinchao.express.dbentity.ExpressPackage;
import com.jinchao.express.utils.CommonUtils;
import com.jinchao.express.widget.IDCardView;
import com.jinchao.express.widget.NavigationLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by 95 on 2016/7/29.
 */
@ContentView(R.layout.activity_packagedetail)
public class PackageDetailActivity extends BaseActivity {
    @ViewInject(R.id.tv_yundanhao) TextView tv_yundanhao;
    @ViewInject(R.id.iv1) ImageView iv1;
    @ViewInject(R.id.iv2) ImageView iv2;
    @ViewInject(R.id.idcard) IDCardView idCardView;
    @ViewInject(R.id.navigation) NavigationLayout navigationLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationLayout.setCenterText(getResources().getString(R.string.package_detail));
        navigationLayout.setLeftTextOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageDetailActivity.this.finish();
            }
        });
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) idCardView.getLayoutParams();
        params.width= CommonUtils.getWindowWidth(this)-CommonUtils.dip2px(this,25);
        params.height=(CommonUtils.getWindowWidth(this)-CommonUtils.dip2px(this,25))*377/600;
        idCardView.setLayoutParams(params);
        ExpressPackage expresspackage= (ExpressPackage) getIntent().getSerializableExtra("package");
        initData(expresspackage);
    }
    private void initData(ExpressPackage expresspackage){
        tv_yundanhao.setText("运单号："+expresspackage.getYundanhao());
        x.image().bind(iv1,expresspackage.getPackagepic());
        x.image().bind(iv2,expresspackage.getExpresspic());
        idCardView.setIDCard(expresspackage.getName(),expresspackage.getGender(),expresspackage.getNation(),expresspackage.getBirth().trim().substring(0,4),expresspackage.getBirth().trim().substring(4,6),expresspackage.getBirth().trim().substring(6,8),expresspackage.getAddress(),expresspackage.getIdcard(), CommonUtils.base64ToBitmap(expresspackage.getPersonpic()));
    }
}
