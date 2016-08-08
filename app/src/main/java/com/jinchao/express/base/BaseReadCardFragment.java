package com.jinchao.express.base;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;

import com.caihua.cloud.common.entity.Server;
import com.caihua.cloud.common.enumerate.ConnectType;
import com.caihua.cloud.common.enumerate.NetType;
import com.caihua.cloud.common.reader.IDReader;
import com.jinchao.express.Constants;
import com.jinchao.express.fragment.DeviceListDialogFragment;
import com.jinchao.express.utils.SharePrefUtil;

/**
 * Created by OfferJiShu01 on 2016/8/4.
 */
public class BaseReadCardFragment extends BaseFragment  implements DeviceListDialogFragment.DeviceListDialogFragmentListener {
    protected boolean isReading = false;// NFC用
    protected ProgressDialog dialog;
    protected IDReader idReader;
    protected DeviceListDialogFragment deviceListDialogFragment;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deviceListDialogFragment = DeviceListDialogFragment.newInstance();
        deviceListDialogFragment.setDeviceListDialogFragmentListener(this);
    }

    @Override
    public void onConnect(String mac) {
        if (SharePrefUtil.getBoolean(getActivity(), Constants.REMEMBER_BLUETOOTH,false)) {
            SharePrefUtil.saveString(getActivity(), "mac", mac);
        }
        if (mac != null) {
            showProcessDialog("正在读卡中，请稍后");
            int delayMillis = SharePrefUtil.getInt(getActivity(),Constants.BluetoothSetting_long_time,15);
            idReader.connect(ConnectType.BLUETOOTH, mac, delayMillis);
        }
    }

    protected void showErrorDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(msg);
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    protected void showProcessDialog(String msg) {
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(msg);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        dialog.setCancelable(false);// 设置是否可以通过点击Back键取消
        dialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        dialog.show();
    }
    protected void hideProcessDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }
    public int HasOTGDeviceConnected() {
        // TODO Auto-generated method stub
        UsbManager mUsbManager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
        if (!mUsbManager.getDeviceList().isEmpty()) {
            return 0;
        } else if (mUsbManager.getAccessoryList() != null) {
            return 1;
        }
        return -1;
    }


    @Override
    public void onResume() {
        super.onResume();
        String net="自动";
        switch (net) {
            case "自动":
                idReader.SetNetType(NetType.publicNetwork);
                break;
            case "移动":
                idReader.SetNetType(NetType.privateNetwork, new Server("221.181.64.41", 2005));
                break;
            case "电信":
                idReader.SetNetType(NetType.privateNetwork, new Server("61.155.106.65", 2005));
                break;
            case "联通":
                idReader.SetNetType(NetType.privateNetwork, new Server("61.51.110.49", 2005));
                break;
            case "手动":
                idReader.SetNetType(NetType.privateNetwork, new Server(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("ManualIP",""),2005));
                break;
            default:
                break;
        }
    }
}
