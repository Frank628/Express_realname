package com.jinchao.express.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcB;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.caihua.cloud.common.User;
import com.caihua.cloud.common.entity.Server;
import com.caihua.cloud.common.enumerate.ConnectType;
import com.caihua.cloud.common.enumerate.NetType;
import com.caihua.cloud.common.reader.IDReader;
import com.jinchao.express.Constants;
import com.jinchao.express.R;
import com.jinchao.express.activity.ScanActivity;
import com.jinchao.express.base.BaseFragment;
import com.jinchao.express.dbentity.ExpressPackage;
import com.jinchao.express.location.MyLocation;
import com.jinchao.express.utils.Base64Coder;
import com.jinchao.express.utils.CommonUtils;
import com.jinchao.express.utils.SharePrefUtil;
import com.jinchao.express.view.ContactsPop;
import com.jinchao.express.view.FacePop;
import com.jinchao.express.webservice.CompareAsyncTask;
import com.jinchao.express.webservice.CompareResult;
import com.jinchao.express.widget.IDCardView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Created by OfferJiShu01 on 2016/7/5.
 */
@ContentView(R.layout.fragment_caiji)
public class CaiJiFragment extends BaseFragment implements DeviceListDialogFragment.DeviceListDialogFragmentListener {

    @ViewInject(R.id.btn_scan) ImageButton btn_scan;
    @ViewInject(R.id.idcard) IDCardView idCardView;
    @ViewInject(R.id.edt_yundanhao) EditText edt_yundanhao;
    @ViewInject(R.id.ib_addcustom) ImageButton ib_addcustom;
    @ViewInject(R.id.root) LinearLayout root;
    @ViewInject(R.id.facematch) TextView facematch;
    @ViewInject(R.id.iv_kuaijian) ImageView iv_kuaijian;
    @ViewInject(R.id.iv_kuaididan) ImageView iv_kuaididan;
    public static final int BAR_SCAN_RESULT=1000;
    public static final int KYAIJIAN_PIC=1001;
    public static final int KUIDIDAN_PIC=1002;
    public static final int REQUEST_CODE_CAMERA = 2001;
    private ContactsPop pop;
    private IDReader idReader;
    protected boolean isReading = false;// NFC用
    private ProgressDialog dialog;
    protected DeviceListDialogFragment deviceListDialogFragment;
    private byte[] imgA,imgB;
    public File photofile,tempFile,kuaiJianFile,kuaididanFile;
    private String kuaijianpic=null,kuadidanipic=null,personpic=null;
    private User muser;
    private FacePop facePop;
    ImageOptions options=new ImageOptions.Builder()
            .setImageScaleType(ImageView.ScaleType.FIT_CENTER).build();
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) idCardView.getLayoutParams();
        params.width= CommonUtils.getWindowWidth(getActivity())-CommonUtils.dip2px(getActivity(),35);
        params.height=(CommonUtils.getWindowWidth(getActivity())-CommonUtils.dip2px(getActivity(),35))*377/600;
        idCardView.setLayoutParams(params);
        idReader = new IDReader(getActivity(), mHandler);
        deviceListDialogFragment = DeviceListDialogFragment.newInstance();
        deviceListDialogFragment.setDeviceListDialogFragmentListener(CaiJiFragment.this);

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
//                        Toast.makeText(getActivity(),"连接失败：" + idReader.strErrorMsg,Toast.LENGTH_SHORT).show();
                        String str="未响应，请将身份证紧贴手机背部重试!";
                        if (idReader.getConnectType() == ConnectType.BLUETOOTH) {
                            str="未响应，请将身份证紧贴读卡器重试!";
                        }else if(idReader.getConnectType() == ConnectType.NFC){
                            str="未响应，请将身份证紧贴手机背部重试!";
                        }else{
                            str="读卡失败，"+idReader.strErrorMsg;
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(str);
                        builder.setTitle("提示");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                              dialog.dismiss();
                              }
                           });
                        builder.create().show();
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
        muser=null;
        imgA = null;
        if (isClear){
            facematch.setVisibility(View.GONE);
            idCardView.clearIDCard();
            return;
        }
        if (user==null){
            facematch.setVisibility(View.GONE);
            String str="未响应，请将身份证紧贴手机背部重试!";
            if (idReader.getConnectType() == ConnectType.BLUETOOTH) {
                str="未响应，请将身份证紧贴读卡器重试!";
            }else if(idReader.getConnectType() == ConnectType.NFC){
                str="未响应，请将身份证紧贴手机背部重试!";
            }else{
                str="读卡失败，"+idReader.strErrorMsg;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(str);
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }else{
            facematch.setVisibility(View.VISIBLE);
            imgA=user.headImg;
            muser=user;
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
    @Event(value = R.id.btn_kuaididan)
    private void kuaididanClick(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        kuaididanFile=CommonUtils.getTempImage();
        kuadidanipic=kuaididanFile.getAbsolutePath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(kuaididanFile));
//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent,KUIDIDAN_PIC);
    }
    @Event(value = R.id.btn_kuaijian)
    private void kuaijianClick(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        kuaiJianFile=CommonUtils.getTempImage();
        kuaijianpic=kuaiJianFile.getAbsolutePath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(kuaiJianFile));
//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent,KYAIJIAN_PIC);
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
        pop=new ContactsPop(getActivity(), CommonUtils.getWindowWidth(getActivity()) - CommonUtils.dip2px(getActivity(), 6) - width, CommonUtils.getWindowHeight(getActivity()) - CommonUtils.getStatusBarHeight(getActivity()),
                location[1] - CommonUtils.getStatusBarHeight(getActivity()), ib_addcustom.getMeasuredHeight(), new ContactsPop.OnContactClickListener() {
            @Override
            public void onClick(String str) {
                muser =new User();
                muser.sexL="女";
                muser.address="江苏省苏州市吴中区测试地址名称222号";
                muser.name=str;
                muser.brithday="19910630";
                muser.nationL="汉";
                muser.id="320623199106305113";
                Bitmap bmp = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.test);
                byte[] b=CommonUtils.Bitmap2Bytes(bmp);
                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);// 将原图片转换成bitmap，方便后面转换
                muser.headImg=CommonUtils.Bitmap2Bytes(bitmap);
                showIDCardInfo(false,muser);
            }
        });
        pop.showPopupWindow(root,width+CommonUtils.dip2px(getActivity(),6),CommonUtils.getStatusBarHeight(getActivity()));
    }
    @Event(value = R.id.facematch)
    private void imgCompareClick(View view){
//        if (facePop!=null){
//            showfacepop();
//            return;
//        }
        photofile=CommonUtils.getCompareTempImage();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photofile));
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }
    @Event(value = R.id.btn_readcard)
    private void readCardClick(View view){
        showIDCardInfo(true,null);
        String net = SharePrefUtil.getString(getActivity(),Constants.DEVICE_WAY,"自动");
        switch (net) {

            case "自动":
                int a = HasOTGDeviceConnected();
                if (a == 0) {
                    showProcessDialog("正在读卡中，请稍后");
                    idReader.connect(ConnectType.OTG);
                } else if (a == 1) {
                    showProcessDialog("正在读卡中，请稍后");
                    idReader.connect(ConnectType.OTGAccessory);
                } else {
                    String mac=SharePrefUtil.getString(getActivity(),"mac",null);
                    if (mac == null) {
                        deviceListDialogFragment.show(getChildFragmentManager(), "");
                    } else {
                        showProcessDialog("正在读卡中，请稍后");
                        int delayMillis = SharePrefUtil.getInt(getActivity(), Constants.BluetoothSetting_long_time,15);
                        idReader.connect(ConnectType.BLUETOOTH, mac, delayMillis);
                    }
                }
                break;
            case "蓝牙":
                String mac=SharePrefUtil.getString(getActivity(),"mac",null);
                if (mac == null) {
                    deviceListDialogFragment.show(CaiJiFragment.this.getChildFragmentManager(), "");
                } else {
                    showProcessDialog("正在读卡中，请稍后");
                    int delayMillis = SharePrefUtil.getInt(getActivity(), Constants.BluetoothSetting_long_time,15);
                    idReader.connect(ConnectType.BLUETOOTH, mac, delayMillis);
                }
                break;
            case "OTG":
                int a2 = HasOTGDeviceConnected();
                if (a2 == 0) {
                    showProcessDialog("正在读卡中，请稍后");
                    idReader.connect(ConnectType.OTG);
                } else if (a2 == 1) {
                    showProcessDialog("正在读卡中，请稍后");
                    idReader.connect(ConnectType.OTGAccessory);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                    builder.setMessage("找不到OTG设备");
                    builder.setPositiveButton("确定", null);
                    builder.show();
                }
                break;

            case "NFC":
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                builder.setMessage("当前是NFC模式，请将身份证贴在手机背面");
                builder.setPositiveButton("确定", null);
                builder.show();
                break;
        }

    }
    @Event(value = R.id.btn_ensure)
    private void ensureClick(View view){
        DbManager dbManager =CommonUtils.getDbManager();
        String yundanhao=edt_yundanhao.getText().toString().trim();
        if (kuadidanipic==null){
            Toast.makeText(getActivity(),"请拍摄快递单",Toast.LENGTH_SHORT).show();
            return;
        }
        if (kuaijianpic==null){
            Toast.makeText(getActivity(),"请拍摄快递包裹",Toast.LENGTH_SHORT).show();
            return;
        }
        if (muser==null){
            Toast.makeText(getActivity(),"请先读取身份证",Toast.LENGTH_SHORT).show();
            return;
        }
        if (yundanhao.equals("")){
            Toast.makeText(getActivity(),"请先扫描快递单号",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date =sDateFormat.format(new java.util.Date());
            ExpressPackage expressPackage =new ExpressPackage();
            expressPackage.setIdcard(muser.id.trim());
            expressPackage.setName(muser.name.trim());
            expressPackage.setGender( muser.sexL.trim());
            expressPackage.setBirth(muser.brithday.trim());
            expressPackage.setNation( muser.nationL.trim());
            expressPackage.setPackagepic(kuaijianpic);
            expressPackage.setExpresspic(kuadidanipic);
            expressPackage.setPersonpic(new String(Base64Coder.encodeLines(muser.headImg)));
            expressPackage.setAddress(muser.address.trim());
            expressPackage.setYundanhao(yundanhao);
            expressPackage.setTime(date);
            dbManager.save(expressPackage);
            clear();
        } catch (DbException e) {
            e.printStackTrace();
        }


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
                case REQUEST_CODE_CAMERA :
                    setPhoto(photofile);
                    showfacepop();
                    break;
                case KUIDIDAN_PIC :
                    x.image().bind(iv_kuaididan,kuaididanFile.getAbsolutePath(),options);
                    break;
                case KYAIJIAN_PIC :
                    x.image().bind(iv_kuaijian,kuaiJianFile.getAbsolutePath(),options);
                    break;
                default:
                    break;
            }
        }
    }

    private void showfacepop() {
        facePop =new FacePop(getActivity(), BitmapFactory.decodeByteArray(muser.headImg, 0, muser.headImg.length),photofile.getAbsolutePath(),this, new FacePop.OnCompareClickListener() {
                @Override
                public void onClick() {
                    if (imgB != null && imgA != null) {
                        new MyCompareAsyncTask(imgA, imgB).execute();
                    } else if (imgA == null) {
                        showDialog("请读取身份证");
                    } else if (imgB == null) {
                        showDialog("请拍照");
                    }
                }
         });
        facePop.showPopupWindow(root,0,0);
        backgroundAlpha(0.3f);
        facePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
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
    protected int HasOTGDeviceConnected() {
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
    public void onConnect(String mac) {

        if (SharePrefUtil.getBoolean(getActivity(),Constants.REMEMBER_BLUETOOTH,false)) {
            SharePrefUtil.saveString(getActivity(), "mac", mac);
        }
        if (mac != null) {
            showProcessDialog("正在读卡中，请稍后");
            int delayMillis = SharePrefUtil.getInt(getActivity(),Constants.BluetoothSetting_long_time,15);
            idReader.connect(ConnectType.BLUETOOTH, mac, delayMillis);
        }
    }
    public void setPhoto(File photofile) {
        imgB = null;
        if (photofile != null && photofile.exists() && photofile.length() > 10) {
            byte[] img = CommonUtils.getByte(photofile);// 获得源图片
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);// 将原图片转换成bitmap，方便后面转换
            imgB = CommonUtils.Bitmap2Bytes(bitmap);// 得到有损图
        } else {
            Toast.makeText(getActivity(), "拍摄照片失败", Toast.LENGTH_SHORT).show();
        }
    }
    private void clear(){
        imgA=null;imgB=null;kuaijianpic=null;kuadidanipic=null;
        facePop=null;
        edt_yundanhao.setText("");
        iv_kuaijian.setImageResource(R.drawable.camera_large);
        iv_kuaididan.setImageResource(R.drawable.camera_large);
        kuaididanFile=null;kuaiJianFile=null;tempFile=null;photofile=null;
        facematch.setVisibility(View.GONE);
        showIDCardInfo(true,null);
    }
    private class MyCompareAsyncTask extends CompareAsyncTask {
        public MyCompareAsyncTask(byte[] imgA, byte[] imgB) {
            super(imgA, imgB);
        }
        @Override
        protected void onPreExecute() {}
        @Override
        protected void onPostExecute(CompareResult result) {
            facematch.setEnabled(true);
            if (result == null) {
                if (facePop!=null){
                    facePop.setResult("人脸对比失败");
                }
                return;
            }
            StringBuilder builder = new StringBuilder();
            double score = result.getScore();
            builder.append("相似度：" + (score / 100) + "%,");
            builder.append(score >= 7000 ? "可以判断为同一人" : "可以判断不为同一个人");
            if (facePop!=null){
                facePop.setResult(builder.toString());
            }
        }
    }
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }
}
