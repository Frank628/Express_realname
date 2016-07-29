package com.jinchao.express.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.NdefRecord;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.WindowManager;

import com.jinchao.express.Constants;

import org.xutils.DbManager;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by OfferJiShu01 on 2016/7/5.
 */
public class CommonUtils {

    public static DbManager getDbManager(){
        DbManager.DaoConfig daoConfig =new DbManager.DaoConfig()
                .setDbName(Constants.DB_NAME)
                .setDbVersion(1)
                .setDbDir(new File(Constants.DB_PATH))
                .setAllowTransaction(true);
        DbManager dbManager = x.getDb(daoConfig);
        return  dbManager;
    }
    public static  boolean isMobile(String mobile){
        if (mobile.length()==11) {
            Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,0-9])|(17[0,0-9]))\\d{8}$");
            Matcher m = p.matcher(mobile);
            return m.matches();
        }else{
            return false;
        }
    }
    public static boolean isPassword(String password){
        if (password.length()>=6&&password.length()<=16){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 获取屏幕宽度
     */
    public static int getWindowWidth(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }
    /**
     * 获取屏幕g高度
     */
    public static int getWindowHeight(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * 获取状态栏高度
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将文件变成byte数组
     *
     * @param file
     * @return
     */
    public static byte[] getByte(File file) {
        byte[] buffer = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer;
    }
    public static byte[] Bitmap2Bytes(Bitmap bm, Bitmap.CompressFormat format, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(format, quality, baos);
        return baos.toByteArray();
    }
    /**
     * 把jpg转换成尺寸小的byte数组
     *
     * @param bm
     *
     * @return
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        return Bitmap2Bytes(bm, Bitmap.CompressFormat.JPEG, 50);
    }
    public static NdefRecord newTextRecord(String text, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));

        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);

        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }
    @SuppressLint("SimpleDateFormat")
    public static File getTempImage() {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "IMG_face.jpg");
    }
    public static String  getSdPath() {
        String path="";
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
        }else{
            path="/data"+ Environment.getDataDirectory().getAbsolutePath() + "/com.jinchao.express/databases";
        }
        return path;
    }

    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static String getPicPath(String type){
        String path="";
        String name =GenerateGUID();
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/express/temp/"+name+".jpg";
        }else{
            path="/data"+ Environment.getDataDirectory().getAbsolutePath() + "/com.jinchao.express/databases/temp/"+name+".jpg";
        }
        return path;
    }

    public static final String GenerateGUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
