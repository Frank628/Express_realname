package com.jinchao.express.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.jinchao.express.R;
import com.jinchao.express.base.BaseActivity;
import com.jinchao.express.fragment.CaiJiFragment;
import com.jinchao.express.widget.CaptureImageView;
import com.jinchao.express.zxing.ScanListener;
import com.jinchao.express.zxing.ScanManager;
import com.jinchao.express.zxing.decode.DecodeThread;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by OfferJiShu on 2016/7/7.
 */
@ContentView(R.layout.activity_scan)
public class ScanActivity extends BaseActivity implements ScanListener{
    public static final int ALL_MODE = 0X300;
    @ViewInject(R.id.capture_preview) SurfaceView scanPreview = null;
    @ViewInject(R.id.capture_container) View scanContainer;
    @ViewInject(R.id.capture_crop_view) View scanCropView;
    @ViewInject(R.id.capture_scan_line) ImageView scanLine;
    @ViewInject(R.id.tv_light) TextView tv_light;
    @ViewInject(R.id.tv_back) TextView tv_back;
    @ViewInject(R.id.tv_scan_result) TextView tv_scan_result;
    @ViewInject(R.id.scan_image)CaptureImageView scan_image;
    @ViewInject(R.id.btn_rescan) Button btn_rescan;
    @ViewInject(R.id.btn_ensure) Button btn_ensure;

    ScanManager scanManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        scanManager = new ScanManager(this, scanPreview, scanContainer, scanCropView, scanLine, ALL_MODE,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        scanManager.onResume();
        btn_rescan.setVisibility(View.GONE);
        btn_ensure.setVisibility(View.GONE);
        scan_image.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanManager.onPause();
    }

    @Override
    public void scanResult(Result rawResult, Bundle bundle) {
        if (!scanManager.isScanning()) { //如果当前不是在扫描状态
            //设置再次扫描按钮出现
            btn_rescan.setVisibility(View.VISIBLE);
            btn_ensure.setVisibility(View.VISIBLE);
            scan_image.setVisibility(View.VISIBLE);
            Bitmap barcode = null;
            byte[] compressedBitmap = bundle.getByteArray(DecodeThread.BARCODE_BITMAP);
            if (compressedBitmap != null) {
                barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
                barcode = barcode.copy(Bitmap.Config.ARGB_8888, true);
            }
            scan_image.setImageBitmap(barcode);
        }
        btn_ensure.setVisibility(View.VISIBLE);
        btn_rescan.setVisibility(View.VISIBLE);
        scan_image.setVisibility(View.VISIBLE);
        tv_scan_result.setVisibility(View.VISIBLE);
        tv_scan_result.setText("快递单号："+rawResult.getText());

    }

    @Override
    public void scanError(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        //相机扫描出错时
        if(e.getMessage()!=null&&e.getMessage().startsWith("相机")){
            scanPreview.setVisibility(View.INVISIBLE);
        }
    }

    void startScan() {
        if (btn_rescan.getVisibility() == View.VISIBLE) {
            btn_rescan.setVisibility(View.GONE);
            btn_ensure.setVisibility(View.GONE);
            scan_image.setVisibility(View.GONE);
            scanManager.reScan();
        }
    }
    @Event(value = R.id.tv_back)
    private void backClick(View view){
        onBackPressed();
    }
    @Event(value = R.id.tv_light)
    private void swichLightClick(View view){
        scanManager.switchLight();
    }
    @Event(value = R.id.btn_rescan)
    private void rescanClick(View view){
        startScan();
    }
    @Event(value = R.id.btn_ensure)
    private void ensureClick(View view){
        ScanActivity.this.finishActivity(CaiJiFragment.BAR_SCAN_RESULT);
    }
}
