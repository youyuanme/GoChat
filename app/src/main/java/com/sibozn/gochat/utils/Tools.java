package com.sibozn.gochat.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.sibozn.gochat.R;
import com.sibozn.gochat.dialog.WaitDialog;

import org.kymjs.chat.bean.Message;
import org.kymjs.kjframe.utils.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by Administrator on 2016/8/1.
 */
public class Tools {

    private static WaitDialog mWaitDialog;

    /**
     * 解压缩一个文件
     */
    public static void Unzip(String zipFile, String targetDir) {
        int BUFFER = 4096; // 这里缓冲区我们使用4KB，
        String strEntry; // 保存每个zip的条目名称
        try {
            BufferedOutputStream dest = null; // 缓冲输出流
            FileInputStream fis = new FileInputStream(zipFile);
            ZipInputStream zis = new ZipInputStream(
                    new BufferedInputStream(fis));
            ZipEntry entry; // 每个zip条目的实例
            while ((entry = zis.getNextEntry()) != null) {
                try {
                    int count;
                    byte data[] = new byte[BUFFER];
                    strEntry = entry.getName();
                    File entryFile = new File(targetDir + strEntry);
                    File entryDir = new File(entryFile.getParent());
                    if (!entryDir.exists()) {
                        entryDir.mkdirs();
                    }
                    FileOutputStream fos = new FileOutputStream(entryFile);
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            zis.close();
        } catch (Exception cwj) {
            cwj.printStackTrace();
        }
    }

    /* * 解压缩一个文件
     * @param zipFile
     *            压缩文件
     * @param folderPath
     *            解压缩的目标目录
     * @throws IOException
     *             当解压缩过程出错时抛出
     */
    public static void upZipFile(File zipFile, String folderPath)
            throws ZipException, IOException {
        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            desDir.mkdirs();
        }
        ZipFile zf = new ZipFile(zipFile);
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            InputStream in = zf.getInputStream(entry);
            String str = folderPath;
            // str = new String(str.getBytes("8859_1"), "GB2312");
            File desFile = new File(str, java.net.URLEncoder.encode(
                    entry.getName(), "UTF-8"));

            if (!desFile.exists()) {
                File fileParentDir = desFile.getParentFile();
                if (!fileParentDir.exists()) {
                    fileParentDir.mkdirs();
                }
            }
            OutputStream out = new FileOutputStream(desFile);
            byte buffer[] = new byte[1024 * 1024];
            int realLength = in.read(buffer);
            while (realLength != -1) {
                out.write(buffer, 0, realLength);
                realLength = in.read(buffer);
            }
            out.close();
            in.close();
        }
    }


    public static boolean isLoginNohttp(Context context) {
        return !TextUtils.isEmpty(context.getSharedPreferences("user_info", Context.MODE_PRIVATE).getString("token",
                ""));
    }

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
     */

    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;

    public static int getNetworkType(Context context) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!StringUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }


    // 登录websocket
    public static String loginWebSocketString(String mEmail, String mToken) {
        return "{\"cmd\":\"login\",\"email\":\"" + mEmail + "\",\"token\":\"" + mToken + "\"}";
    }

    // 根据邮箱获取用户信息
    public static String getMyInfoWebSocketString(String mEmail, String mToken) {
        // {"cmd":"getinfo","email":自己的email,"token":token,"getemail":所要获取信息的email}
        return "{\"cmd\":\"getinfo\",\"email\":\"" + mEmail + "\",\"token\":\""
                + mToken + "\",\"getemail\":\"" + mEmail + "\"}";
    }

    // 获取post信息
    public static String getPostWebSocketString(String mEmail, String mToken, double lat, double lng) {
        return "{\"cmd\":\"getpost\",\"email\":\"" + mEmail + "\",\"token\":\""
                + mToken + "\",\"lng\":\"" + lng + "\",\"lat\":\"" + lat + "\"}";
    }

    //获取未读消息信息
    public static String getUnreadWebSocketString(String mEmail, String mToken) {
        return "{\"cmd\":\"getunread\",\"email\":\"" + mEmail + "\",\"token\":\""
                + mToken + "\",\"mid\":0}";
    }

    // 发布post信息
    public static String sendPostWebSocketString(String mEmail, String mToken, double lat, double lng, String content) {
        // {"cmd":"post","email":email,"token":token,"lng":"经度","lat":"纬度","data":"内容","type":"类型"}
        return "{\"cmd\":\"post\",\"email\":\"" + mEmail + "\",\"token\":\""
                + mToken + "\",\"lng\":\"" + lng + "\",\"lat\":\"" + lat + "\",\"data\":\"" + content + "\"," +
                "\"type\":\"txt\"}";
    }

    // 发送聊天内容
    public static String sendChatContentString(String userEmail, String mEmail, String content) {
        //发送json {"cmd":"chat","to":"对方email","from":"自己email","data":"内容","type":"类型"}
        return "{\"cmd\":\"chat\",\"to\":\"" + userEmail + "\",\"from\":\""
                + mEmail + "\",\"data\":\"" + content + "\"," + "\"type\":\"text\"}";
    }

    /**
     * 从底部显示相册和相机dialog
     *
     * @param ctx
     * @param onClickListener
     * @return
     */
    public static Dialog showPhotoDialog(Context ctx, View.OnClickListener onClickListener) {
        final Dialog photoImageDialog = new Dialog(ctx, R.style.simpledialog);
        View view = View.inflate(ctx, R.layout.photo_dialog, null);
        photoImageDialog.setContentView(view);
        view.findViewById(R.id.photograph_option1).setOnClickListener(onClickListener);
        view.findViewById(R.id.search_gif_online).setOnClickListener(onClickListener);
        view.findViewById(R.id.images_option2).setOnClickListener(onClickListener);
        view.findViewById(R.id.cancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        photoImageDialog.dismiss();
                    }
                });
        photoImageDialog.setContentView(view);
        Window window = photoImageDialog.getWindow();
        window.setWindowAnimations(R.style.inputanimation);
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        photoImageDialog.show();
        return photoImageDialog;
    }

    /**
     * 显示一个加载对话框
     *
     * @param context
     * @param canCancel 是否可以取消对话框
     */
    public static void showDialog(Context context, boolean canCancel) {
        mWaitDialog = new WaitDialog(context);
        // mWaitDialog.setCancelable(canCancel);
        mWaitDialog.setMessage(context.getText(R.string.wait_dialog_title));
        mWaitDialog.show();
    }

    public static void showWebSocketDialog(Context context, boolean canCancel, String content) {
        mWaitDialog = new WaitDialog(context);
        mWaitDialog.setMessage(content);
        mWaitDialog.show();
    }

    /**
     * 关闭一个加载对话框
     */
    public static void closeDialog() {
        if (mWaitDialog != null && mWaitDialog.isShowing()) {
            mWaitDialog.dismiss();
        }
    }

    /**
     * 检测SD卡是否存在
     */
    public static boolean checkSDcard() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    /**
     * 4.4-5.0的处理着色模式::
     *
     * @param activity
     */
    public static void noFullScreen(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        int statusBarHeight = getStatusBarHeight(activity);

        View mTopView = mContentView.getChildAt(0);
        if (mTopView != null && mTopView.getLayoutParams() != null && mTopView.getLayoutParams().height ==
                statusBarHeight) {
            //避免重复添加 View
            mTopView.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
            return;
        }
        //使 ChildView 预留空间
        if (mTopView != null) {
            ViewCompat.setFitsSystemWindows(mTopView, true);
        }

        //添加假 View
        mTopView = new View(activity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        mTopView.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        mContentView.addView(mTopView, 0, lp);
    }

    /**
     * 4.4-5.0的处理全屏模式:
     *
     * @param activity
     */
    public static void fullScreen(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        View statusBarView = mContentView.getChildAt(0);
        //移除假的 View
        if (statusBarView != null && statusBarView.getLayoutParams() != null && statusBarView.getLayoutParams()
                .height == Tools.getStatusBarHeight(activity)) {
            mContentView.removeView(statusBarView);
        }
        //不预留空间
        if (mContentView.getChildAt(0) != null) {
            ViewCompat.setFitsSystemWindows(mContentView.getChildAt(0), false);
        }
    }

    /**
     * 获状态栏高度
     *
     * @param context 上下文
     * @return 通知栏高度
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
     * 获状态栏高度
     *
     * @param context 上下文
     * @return 通知栏高度
     */
    public static int getStatusBarHeight1(Context context) {
        int statusBarHeight = 0;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object obj = clazz.newInstance();
            Field field = clazz.getField("status_bar_height");
            int temp = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 默认显示toast位置
     *
     * @param context
     * @param words
     */
    public static void showToast(Context context, String words) {
        Toast.makeText(context, words, Toast.LENGTH_SHORT).show();
    }

    /**
     * toast显示在屏幕中间
     *
     * @param context
     * @param words
     */
    public static void showCenterToast(Context context, String words) {
        Toast toast = Toast.makeText(context, words, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * toast显示在屏幕顶部
     *
     * @param context
     * @param words
     */
    public static void showTopToast(Context context, String words) {
        Toast toast = Toast.makeText(context, words, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    /**
     * 判断email格式是否正确
     *
     * @param emailString
     * @return
     */
    public static boolean isEmailform(String emailString) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))" +
                "([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(emailString);
        return m.matches();
    }
}
