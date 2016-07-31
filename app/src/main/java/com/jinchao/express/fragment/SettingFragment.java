package com.jinchao.express.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jinchao.express.Constants;
import com.jinchao.express.R;
import com.jinchao.express.activity.BluetoothSettingActivity;
import com.jinchao.express.base.BaseFragment;
import com.jinchao.express.utils.SharePrefUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.regex.Pattern;

/**
 * Created by OfferJiShu01 on 2016/7/12.
 */
@ContentView(R.layout.fragment_setting)
public class SettingFragment extends BaseFragment {
    private DeviceRadioOnClick deviceRadioOnClick;
    private NetRadioOnClick netRadioOnClick;
    @ViewInject(R.id.textview_device) TextView tv_device;
    @ViewInject(R.id.textview_net) TextView tv_net;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
    }


    private void initData(){
        String device=SharePrefUtil.getString(getActivity(),Constants.DEVICE_WAY,"自动");
        tv_device.setText(device);
        switch (device) {
            case "自动":
                deviceRadioOnClick = new DeviceRadioOnClick(0);
                break;
            case "蓝牙":
                deviceRadioOnClick = new DeviceRadioOnClick(1);
                break;
            case "OTG":
                deviceRadioOnClick = new DeviceRadioOnClick(2);
                break;
            case "NFC":
                deviceRadioOnClick = new DeviceRadioOnClick(3);
                break;
        }
        String net =SharePrefUtil.getString(getActivity(),Constants.NET_WAY,"自动");
        tv_net.setText(net);
        switch (net) {
            case "自动":
                netRadioOnClick = new NetRadioOnClick(0);
                break;
            case "移动":
                netRadioOnClick = new NetRadioOnClick(1);
                break;
            case "电信":
                netRadioOnClick = new NetRadioOnClick(2);
                break;
            case "联通":
                netRadioOnClick = new NetRadioOnClick(3);
                break;
            case "手动":
                netRadioOnClick = new NetRadioOnClick(4);
                break;
            default:
                break;
        }
    }
    @Event(value = R.id.device_setting)
    private void devicesettingClick(View view){
        AlertDialog ad = new AlertDialog.Builder(getActivity()).setTitle("选择设备连接方式").setSingleChoiceItems(Constants.DEVICE_AREA, deviceRadioOnClick.getIndex(), deviceRadioOnClick).create();
        ad.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface arg0) {
                initData();
            }
        });
        ad.show();
    }
    @Event(value = R.id.net_setting)
    private void netClick(View view){
        AlertDialog ad = new AlertDialog.Builder(getActivity()).setTitle("选择网络连接方式").setSingleChoiceItems(Constants.NET_AREA, netRadioOnClick.getIndex(), netRadioOnClick).create();
        ad.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface arg0) {
                initData();
            }
        });
        ad.show();
    }
    @Event(value = R.id.bt_setting)
    private void bluetoothsettingClick(View view){
        Intent intent =new Intent(getActivity(), BluetoothSettingActivity.class);
        startActivity(intent);
    }
    class DeviceRadioOnClick implements DialogInterface.OnClickListener {
        private int index;
        public DeviceRadioOnClick(int index) {
            this.index = index;
        }
        public void setIndex(int index) {
            this.index = index;
        }
        public int getIndex() {
            return index;
        }
        public void onClick(DialogInterface dialog, int whichButton) {
            setIndex(whichButton);
            SharePrefUtil.saveString(getActivity(), Constants.DEVICE_WAY,Constants.DEVICE_AREA[index]);
            dialog.dismiss();
        }
    }

    class NetRadioOnClick implements DialogInterface.OnClickListener {
        private int index;

        public NetRadioOnClick(int index) {
            this.index = index;
        }
        public void setIndex(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public void onClick(DialogInterface dialog, int whichButton) {
            setIndex(whichButton);
            SharePrefUtil.saveString(getActivity(), Constants.NET_WAY,Constants.NET_AREA[index]);
            dialog.dismiss();
            if (Constants.NET_AREA[index].equals("手动")) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                View view= LayoutInflater.from(SettingFragment.this.getActivity()).inflate(R.layout.manual_input_ip_layout,null);
                builder.setView(view);
                builder.setTitle("请输入解码服务器IP");
                builder.setCancelable(true);
                final AlertDialog ipDialog=builder.create();
                ipDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        ipDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                        ipDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setEnabled(false);
                    }
                });
                final EditText manualIPET=(EditText) view.findViewById(R.id.manualIPET);
                final SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
                manualIPET.setText(preferences.getString("ManualIP",""));
                Button okBTN=(Button) view.findViewById(R.id.okBTN);
                Button cancelBTN=(Button)view.findViewById(R.id.cancelBTN);
                okBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String IP_ADDRESS_PATTERN="^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
                        Pattern pattern=Pattern.compile(IP_ADDRESS_PATTERN);
                                    String manualInputIP=manualIPET.getText().toString().trim();
                                    boolean matchResult=pattern.matcher(manualInputIP).matches();
                                    preferences.edit().putString("ManualIP",manualInputIP).commit();
                                    if(matchResult==false){
                            Toast.makeText(SettingFragment.this.getActivity(),"输入的IP可能不正确",Toast.LENGTH_LONG).show();
                        }
                        ipDialog.dismiss();
                    }
                });
                cancelBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ipDialog.dismiss();
                    }
                });
                ipDialog.show();
            }
        }
    }
}
