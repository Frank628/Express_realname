package com.jinchao.express.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcB;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.caihua.cloud.common.User;
import com.caihua.cloud.common.entity.Server;
import com.caihua.cloud.common.enumerate.ConnectType;
import com.caihua.cloud.common.enumerate.NetType;
import com.caihua.cloud.common.reader.IDReader;
import com.jinchao.express.R;
import com.jinchao.express.activity.ScanActivity;
import com.jinchao.express.base.BaseFragment;
import com.jinchao.express.location.MyLocation;
import com.jinchao.express.utils.CommonUtils;
import com.jinchao.express.utils.SharePrefUtil;
import com.jinchao.express.view.ContactsPop;
import com.jinchao.express.widget.IDCardView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.DbManager;
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
    @ViewInject(R.id.edt_yundanhao) EditText edt_yundanhao;
    @ViewInject(R.id.ib_addcustom) ImageButton ib_addcustom;
    @ViewInject(R.id.root) LinearLayout root;
    public static final int BAR_SCAN_RESULT=100;
    private ContactsPop pop;
    private IDReader idReader;
    protected boolean isReading = false;// NFC用
    private ProgressDialog dialog;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) idCardView.getLayoutParams();
        params.width= CommonUtils.getWindowWidth(getActivity())-CommonUtils.dip2px(getActivity(),35);
        params.height=(CommonUtils.getWindowWidth(getActivity())-CommonUtils.dip2px(getActivity(),35))*377/600;
        idCardView.setLayoutParams(params);
        idReader = new IDReader(getActivity(), mHandler);
    }
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case IDReader.CONNECT_SUCCESS:
                    break;
                case IDReader.CONNECT_FAILED:
                    hideProcessDialog();
                    if (idReader.strErrorMsg != null) {
//                        mTextView_errorinfo.setText("连接失败：" + idReader.strErrorMsg);
                    }
                    isReading = false;
                    if (idReader.getConnectType() == ConnectType.BLUETOOTH) {
                        SharePrefUtil.saveString(getActivity(),"mac",null);
                    }
                    break;
                case IDReader.READ_CARD_SUCCESS:
//                    BeepManager.playsuccess(getActivity());
                    showIDCardInfo(false, (User) msg.obj);
                    break;

                case IDReader.READ_CARD_FAILED:
//                    BeepManager.playfail(getActivity());
                    showIDCardInfo(false, null);
                    break;
                default:
                    break;
            }
        }
    };
    private void showIDCardInfo(boolean isClear,User user){//显示、清空身份证内容
        hideProcessDialog();
        if (isClear){
            idCardView.clearIDCard();
            return;
        }
        if (user==null){
            Toast.makeText(getActivity(),"读卡失败！",Toast.LENGTH_SHORT).show();
        }else{
            idCardView.setIDCard(user.name.trim(),user.sexL.trim(),user.nationL.trim(),
                    user.brithday.trim().substring(0,4),user.brithday.trim().substring(4,6),user.brithday.trim().substring(6,8),
                    user.address.trim(),user.id.trim(), BitmapFactory.decodeByteArray(user.headImg, 0, user.headImg.length));
        }
        isReading = false;
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

    @Event(value = R.id.btn_scan)
    private void scanClick(View view){
        Intent intent=new Intent(getActivity(), ScanActivity.class);
        startActivityForResult(intent,BAR_SCAN_RESULT);
    }
    @Event(value = R.id.ib_addcustom)
    private void addCustomClick(View view){
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        ib_addcustom.measure(w, h);
        int width = ib_addcustom.getMeasuredWidth();
        int[] location = new int[2];
        ib_addcustom.getLocationInWindow(location);
        pop=new ContactsPop(getActivity(),CommonUtils.getWindowWidth(getActivity())-CommonUtils.dip2px(getActivity(),6)-width,CommonUtils.getWindowHeight(getActivity())-CommonUtils.getStatusBarHeight(getActivity()),location[1]-CommonUtils.getStatusBarHeight(getActivity()),ib_addcustom.getMeasuredHeight());
        pop.showPopupWindow(root,width+CommonUtils.dip2px(getActivity(),6),CommonUtils.getStatusBarHeight(getActivity()));
    }
    @Event(value = R.id.btn_readcard)
    private void readCardClick(View view){


    }
    @Event(value = R.id.btn_ensure)
    private void ensureClick(View view){
        DbManager dbManager =CommonUtils.getDbManager();


    }
    public void onNewIntent(Intent intent){
        Parcelable parcelable = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (parcelable == null) {
            return;
        }
        // 正在处理上一次读取的数据，不再读取数据
        if (isReading) {
            return;
        }
        showIDCardInfo(true, null);
        isReading = true;
        showProcessDialog("正在读卡中，请稍后");
        idReader.connect(ConnectType.NFC, parcelable);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MyLocation event) {
        if (pop!=null){
            pop.setLocationAddress(event);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==getActivity().RESULT_OK) {
            switch (requestCode) {
                case BAR_SCAN_RESULT:
                    edt_yundanhao.setText(data.getStringExtra("yundanhao"));
                    break;
                default:
                    break;
            }
        }
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
}
