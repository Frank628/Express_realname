package com.jinchao.express.base;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.jinchao.express.utils.network.NetWorkManager;

import org.xutils.x;

/**
 * Created by OfferJiShu01 on 2016/7/5.
 */
public class BaseFragment extends Fragment implements NetWorkManager.NetConnectChangeListener{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NetWorkManager.getInstance().regist(this);
        return x.view().inject(this,inflater,container);
    }

    public void showDialog(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        builder.setMessage(str);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.create().show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        NetWorkManager.getInstance().unregist(this);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    @Override
    public void change(NetWorkManager.NetWorkInfo netWorkInfo) {
        Toast.makeText(getActivity(),netWorkInfo.simiProvider+""+netWorkInfo.netState+"",Toast.LENGTH_SHORT).show();
        System.out.println(netWorkInfo.netState+"");
    }
}
