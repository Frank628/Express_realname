package com.jinchao.express.activity;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.Common;
import com.jinchao.express.R;
import com.jinchao.express.base.BaseActivity;
import com.jinchao.express.fragment.CaiJiFragment;
import com.jinchao.express.fragment.SendDataFragment;
import com.jinchao.express.fragment.SettingFragment;
import com.jinchao.express.utils.CommonUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Locale;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @ViewInject(R.id.toolbar) private Toolbar toolbar;
    @ViewInject(R.id.title)private TextView title;
    private Fragment currentFragment;
    private NfcAdapter mAdapter;
    protected PendingIntent mPendingIntent;
    protected NdefMessage mNdefPushMessage;
    private USBBroadcastReceiver receiveBroadCast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        title.setText(getResources().getString(R.string.realname));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        currentFragment=new CaiJiFragment();
        changeFragment(currentFragment);
        if (!Common.init(this)){
            Toast.makeText(this, "身份证云终端开发包初始化失败！", Toast.LENGTH_SHORT).show();
            finish();
        }
        //注册usb广播
        receiveBroadCast = new USBBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(receiveBroadCast, filter);
        initNFC();
    }
    private void initNFC(){
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
            builder.setMessage("手机不支持NFC,部分功能无法使用");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.create().show();
        }
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mNdefPushMessage = new NdefMessage(
                new NdefRecord[] {CommonUtils.newTextRecord("Message from NFC Reader :-)", Locale.ENGLISH, true) });
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (currentFragment!=null){
            if (currentFragment instanceof  CaiJiFragment){
                ((CaiJiFragment)currentFragment).onNewIntent(intent);
            }
        }
    }
    public class USBBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
//                headPanel.setRightTitle(R.drawable.usb);
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
//                headPanel.setRightTitle(R.drawable.bt);
            }
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void changeFragment(Fragment fragment){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.container,fragment);
        ft.commitAllowingStateLoss();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage) {
            title.setText(getResources().getString(R.string.setting));
            currentFragment=new SettingFragment();
            changeFragment(currentFragment);
        } else if (id == R.id.nav_send) {
            title.setText(getResources().getString(R.string.senddata));
            currentFragment=new SendDataFragment();
            changeFragment(currentFragment);
        }else if(id==R.id.nav_operation){
            title.setText(getResources().getString(R.string.realname));
            currentFragment=new CaiJiFragment();
            changeFragment(currentFragment);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        UsbManager mUsbManager = (UsbManager) this.getSystemService(Context.USB_SERVICE);
        if (!mUsbManager.getDeviceList().isEmpty()) {
//            headPanel.setRightTitle(R.drawable.usb);
        }
        if (mAdapter != null) {
            // 显示activity的时候开始nfc的监控
            if (!mAdapter.isEnabled()) {
                // showNfcSettingsDialog();
            } else {
                mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
                mAdapter.enableForegroundNdefPush(this, mNdefPushMessage);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            // activity暂停的时候，暂停nfc的监控
            mAdapter.disableForegroundDispatch(this);
            mAdapter.disableForegroundNdefPush(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiveBroadCast != null) {
            unregisterReceiver(receiveBroadCast);
            receiveBroadCast = null;
        }
    }
}
