package com.jinchao.express.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jinchao.express.R;
import com.jinchao.express.activity.ScanActivity;
import com.jinchao.express.base.BaseFragment;
import com.jinchao.express.utils.CommonUtils;
import com.jinchao.express.widget.IDCardView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by OfferJiShu01 on 2016/7/5.
 */
@ContentView(R.layout.fragment_caiji)
public class CaiJiFragment extends BaseFragment {

    @ViewInject(R.id.btn_scan) ImageButton btn_scan;
    @ViewInject(R.id.idcard) IDCardView idCardView;
    public static final int BAR_SCAN_RESULT=100;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) idCardView.getLayoutParams();
        params.width= CommonUtils.getWindowWidth(getActivity())-CommonUtils.dip2px(getActivity(),35);
        params.height=(CommonUtils.getWindowWidth(getActivity())-CommonUtils.dip2px(getActivity(),35))*377/600;
        idCardView.setLayoutParams(params);
    }


    @Event(value = R.id.btn_scan)
    private void scanClick(View view){
        Intent intent=new Intent(getActivity(), ScanActivity.class);
        startActivityForResult(intent,BAR_SCAN_RESULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case BAR_SCAN_RESULT:

                break;
            default:
                break;
        }
    }
}
