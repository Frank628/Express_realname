package com.jinchao.express.fragment;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcB;
import android.os.Bundle;
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

import com.jinchao.express.R;
import com.jinchao.express.activity.ScanActivity;
import com.jinchao.express.base.BaseFragment;
import com.jinchao.express.utils.CommonUtils;
import com.jinchao.express.view.ContactsPop;
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
    @ViewInject(R.id.edt_yundanhao) EditText edt_yundanhao;
    @ViewInject(R.id.ib_addcustom) ImageButton ib_addcustom;
    @ViewInject(R.id.root) LinearLayout root;
    public static final int BAR_SCAN_RESULT=100;
    private Tag tagNFC;
    private NfcB nfcB;
    private NfcAdapter nfcAdapter;

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
        ContactsPop pop=new ContactsPop(getActivity(),CommonUtils.getWindowWidth(getActivity())-CommonUtils.dip2px(getActivity(),6)-width,CommonUtils.getWindowHeight(getActivity())-CommonUtils.getStatusBarHeight(getActivity()),location[1]-CommonUtils.getStatusBarHeight(getActivity()),ib_addcustom.getMeasuredHeight());
        pop.showPopupWindow(root,width+CommonUtils.dip2px(getActivity(),6),CommonUtils.getStatusBarHeight(getActivity()));
    }
    @Event(value = R.id.btn_readcard)
    private void readCardClick(View view){
        nfcAdapter=NfcAdapter.getDefaultAdapter(getActivity());
        if (nfcAdapter==null){
            Toast.makeText(getActivity(),"设备不支持NFC！",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!nfcAdapter.isEnabled()){
            Toast.makeText(getActivity(),"请在系统设置中先启用NFC功能！",Toast.LENGTH_SHORT).show();
            return;
        }
        processIntent(getActivity().getIntent());
    }
    private void processIntent(Intent intent){
        tagNFC=intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);


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
}
