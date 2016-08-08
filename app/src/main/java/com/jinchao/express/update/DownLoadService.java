package com.jinchao.express.update;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.caihua.cloud.common.util.FileUtils;
import com.jinchao.express.R;
import com.jinchao.express.utils.CommonUtils;

import org.xutils.common.Callback;
import org.xutils.common.util.FileUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created by OfferJiShu01 on 2016/8/5.
 */
public class DownLoadService extends Service {
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;

    public int onStartCommand(Intent intent, int flags, int startId) {
        String url="";
        if (intent!=null) {
            url=intent.getStringExtra("url");
        }else{
//            url=SharePrefUtil.getString(this, "url", "");
        }
        download(url);
        return super.onStartCommand(intent, START_REDELIVER_INTENT, startId);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void download(String url){
        RequestParams params =new RequestParams(url);
        params.setAutoRename(true);
        params.setSaveFilePath(CommonUtils.getSdPath());
        x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File file) {

            }
            @Override
            public void onError(Throwable throwable, boolean b) {

            }
            @Override
            public void onCancelled(CancelledException e) {}
            @Override
            public void onFinished() {}
            @Override
            public void onWaiting() {}
            @Override
            public void onStarted() {}
            @Override
            public void onLoading(long total, long current, boolean isDownLoading) {

            }
        });
    }
    private void createNotification(){
        builder=new NotificationCompat.Builder(this);
        RemoteViews remoteView =new RemoteViews(getPackageName(), R.layout.layout_download_notification);
        remoteView.setProgressBar(R.id.pb,100,0,true);
    }
}
