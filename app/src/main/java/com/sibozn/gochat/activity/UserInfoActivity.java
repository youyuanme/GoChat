package com.sibozn.gochat.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.sibozn.gochat.R;
import com.sibozn.gochat.application.MyApplication;
import com.sibozn.gochat.dialog.WaitDialog;
import com.sibozn.gochat.nohttp.CallServer;
import com.sibozn.gochat.nohttp.HttpListener;
import com.sibozn.gochat.utils.Constants;
import com.sibozn.gochat.utils.PreferenceHelper;
import com.sibozn.gochat.utils.Tools;
import com.sibozn.gochat.websocket.MyWebSocketClient;
import com.sibozn.gochat.widget.FilterImageView;
import com.sibozn.gochat.widget.GlideCircleTransform;
import com.yolanda.nohttp.BasicBinary;
import com.yolanda.nohttp.BitmapBinary;
import com.yolanda.nohttp.FileBinary;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;

public class UserInfoActivity extends BaseWebSockteActivity implements View.OnClickListener,
        HttpListener<String>, RadioGroup.OnCheckedChangeListener, MyWebSocketClient.MyWebSocketMessageListener {

    private static final int UP_DATA_PHOTO = 100;
    private static final int UP_DATA_PHOTO_URL = 200;
    private static final int IMAGE_CAPTURE_CODE = 300;
    private static final int IMAGE_PhOTO_CODE = 400;
    private static final int PHOTO_ZOOM_CODE = 500;
    private static final int GIFS_RESULT_CODE = 600;
    @BindView(R.id.tv_return)
    TextView tvReturn;
    @BindView(R.id.tv_save)
    TextView tv_save;
    @BindView(R.id.head_portrait1)
    FilterImageView head_portrait1;
    @BindView(R.id.et_nickname)
    EditText etNickname;
    @BindView(R.id.et_age)
    EditText et_age;
    @BindView(R.id.rb_nan)
    RadioButton rbNan;
    @BindView(R.id.rb_nv)
    RadioButton rbNv;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.bt_logout)
    Button bt_logout;
    private String sex = "female";
    private File tempFile, tempAvatarFile;
    private String tempFileString;
    private String mEmail, mToken;
    private Dialog photoImageDialog;
    private WaitDialog mWaitDialog;

    @Override
    public int getContentViewId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        mToken = PreferenceHelper.readString(this, "user_info", "token", "");
        mEmail = PreferenceHelper.readString(this, "user_info", "email", "");
        MyWebSocketClient.getInstance().setMyWebSocketMessageListener(this);
        if (Tools.checkSDcard()) {
            tempFileString = getExternalCacheDir().getAbsolutePath();
        } else {
            tempFileString = getCacheDir().getAbsolutePath();
        }
        initView();
        Tools.showDialog(this, false);
        MyWebSocketClient.getInstance().send(Tools.getMyInfoWebSocketString(mEmail, mToken));
        SwipeBackHelper.onCreate(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    // websocket登录成功回调
    @Override
    protected void loginWebSocketSuccessed(String message) {
        MyWebSocketClient.getInstance().setMyWebSocketMessageListener(this);
    }

    @Override
    protected void loginOutWebSocket() {

    }

    private void initView() {
        tvReturn.setOnClickListener(this);
        head_portrait1.setOnClickListener(this);
        bt_logout.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "------------------onResume: ");
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_nan:
                sex = "male";
                break;
            case R.id.rb_nv:
                sex = "female";
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_return:
                finish();
                break;
            case R.id.bt_logout:
                MyWebSocketClient.getInstance().close();
                MyWebSocketClient.myWebSocketClient = null;
                MyApplication.isLoginWebSocket = false;
                PreferenceHelper.clean(this, "user_info");
                startActivity(new Intent(this, LoginActivity.class));
                for (Activity activity : MyApplication.activiys) {
                    activity.finish();
                }
                finish();
                break;
            case R.id.head_portrait:
            case R.id.head_portrait1:
                // showPhotoDialog(this);
                photoImageDialog = Tools.showPhotoDialog(this, this);
                break;
            case R.id.search_gif_online:// 在线gif图片
                startActivityForResult(new Intent(this, SearchGIFSActivity.class), GIFS_RESULT_CODE);
                // startActivity(new Intent(this, SearchGIFSActivity.class));
                photoImageDialog.dismiss();
                break;
            case R.id.photograph_option1://拍照
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                tempFile = new File(tempFileString + "/avatar.jpg");
                if (tempFile.exists()) {
                    tempFile.delete();
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                startActivityForResult(intent, IMAGE_CAPTURE_CODE);
                photoImageDialog.dismiss();
                break;
            case R.id.images_option2:// 相册
                // 打开相册并返回指定url
                Intent intent1 = new Intent();
                intent1.setType("image/*");
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent1, IMAGE_PhOTO_CODE);
                photoImageDialog.dismiss();
                break;
            case R.id.tv_save:
                Request<String> request = NoHttp.createStringRequest(Constants.RET_INFO_URL, RequestMethod.POST);
                if (request != null) {
                    request.add("email", mEmail);
                    request.add("token", mToken);
                    request.add("uid", etNickname.getText().toString());
                    request.add("age", et_age.getText().toString());
                    request.add("sex", sex);
                    // 添加到请求队列
                    CallServer.getRequestInstance().add(this, 0, request, this, true, true);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case IMAGE_CAPTURE_CODE:// 拍照
                startPhotoZoom(Uri.fromFile(tempFile));
                break;
            case GIFS_RESULT_CODE:// head_gif_url
                String head_gif_url = data.getStringExtra("head_gif_url");
                Glide.with(this)
                        .load(head_gif_url)
                        .centerCrop()
                        .transform(new GlideCircleTransform(this))
                        .placeholder(R.mipmap.ic_launcher)
                        .crossFade()
                        .into(head_portrait1);
                break;
            case IMAGE_PhOTO_CODE:// 相册
                Uri uri = data.getData();
                startPhotoZoom(uri);
                break;
            case PHOTO_ZOOM_CODE:// 剪切
                mWaitDialog = new WaitDialog(this);
                mWaitDialog.setMessage(getText(R.string.wait_dialog_title));
                mWaitDialog.show();
                Request<String> request = NoHttp.createStringRequest(Constants.UP_LOAD_URL, RequestMethod.POST);
                if (request != null) {
                    BasicBinary binary = new FileBinary(tempAvatarFile);
                    request.add("userfile", binary);// 添加1个文件
                    // 添加到请求队列
                    CallServer.getRequestInstance().add(this, UP_DATA_PHOTO, request, this, false, false);
                }
                PreferenceHelper.write(this, "user_info", "avatarFile", tempAvatarFile.getAbsolutePath());
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        int responseCode = response.getHeaders().getResponseCode();// 服务器响应码
        String headURL = null;
        if (responseCode == 200) {
            switch (what) {
                case UP_DATA_PHOTO:// 上传头像成功之后返回来的URL地址
                    headURL = response.get();
                    Request<String> request = NoHttp.createStringRequest(Constants.RET_HEAD_URL, RequestMethod.POST);
                    request.add("email", mEmail);
                    request.add("token", mToken);
                    request.add("head", headURL);
                    CallServer.getRequestInstance().add(this, UP_DATA_PHOTO_URL, request, this, true, false);
                    return;
                case UP_DATA_PHOTO_URL:// 发送头像url地址成功返回的数据
                    try {
                        if (mWaitDialog != null && mWaitDialog.isShowing()) {
                            mWaitDialog.dismiss();
                        }
                        Glide.with(this)
                                .load(headURL)
                                .centerCrop()
                                .transform(new GlideCircleTransform(this))
                                .placeholder(R.mipmap.ic_launcher)
                                .crossFade()
                                .into(head_portrait1);
                        JSONObject jsonObject = new JSONObject(response.get());
                        Toast.makeText(this, jsonObject.getString("desc"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return;
            }
            if (RequestMethod.HEAD == response.request().getRequestMethod())// 请求方法为HEAD时没有响应内容
                // showMessageDialog(R.string.request_succeed, R.string.request_method_head);
                Toast.makeText(this, "request_method_head", Toast.LENGTH_SHORT).show();
            else {
                Log.d(TAG, response.get());
                try {
                    JSONObject jsonObject = new JSONObject(response.get());
                    String ret = jsonObject.getString("ret");
                    if (TextUtils.equals("1", ret)) {
                        getSharedPreferences("user_info", MODE_PRIVATE).edit()
                                .putString("uid", etNickname.getText().toString())
                                .putString("age", et_age.getText().toString())
                                .putString("sex", sex)
                                .commit();
                        finish();
                    }
                    Toast.makeText(this, jsonObject.getString("desc"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }// nohttp 请求成功回调

    @Override
    public void onFailed(int what, Response<String> response) {
        if (what == 1) {
            Tools.showToast(this, getString(R.string.update_avatar_error));
            return;
        }
        Toast.makeText(this, R.string.save_error, Toast.LENGTH_LONG).show();
        Log.e(TAG, "onFailed:" + response.getException().getMessage());
    }// nohttp 请求失败回调


    @Override
    public void onMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Tools.closeDialog();
                    JSONObject jsonObject = new JSONObject(message);
                    switch (jsonObject.getString("cmd")) {
                        case "getinfo":
                            String listInfo = jsonObject.getString("list");
                            if (!TextUtils.equals("null", listInfo)) {
                                JSONObject jsonObject1 = new JSONObject(listInfo);
                                etNickname.setText(jsonObject1.getString("uid"));
                                et_age.setText(jsonObject1.getString("age"));
                                if (TextUtils.equals(jsonObject1.getString("sex"), "female")) {
                                    rbNv.setChecked(true);
                                } else {
                                    rbNan.setChecked(true);
                                }
                                String photo = jsonObject1.getString("photo");
                                if (!TextUtils.isEmpty(photo)) {
                                    // ImageLoader.getInstance().displayImage(photo, headPortrait);
                                    Glide.with(UserInfoActivity.this)
                                            .load(photo)
                                            // .centerCrop()
                                            .transform(new GlideCircleTransform(UserInfoActivity.this))
                                            .placeholder(R.mipmap.ic_launcher)
                                            .crossFade()
                                            .into(head_portrait1);
                                    // headPortrait.setImageResource(R.raw.cat);
                                }
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }// webSocket 接收回调方法

    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        tempAvatarFile = new File(tempFileString + "/temp_avatar.jpg");
        if (tempAvatarFile.exists()) {
            tempAvatarFile.delete();
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempAvatarFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, PHOTO_ZOOM_CODE);
    }// 跳转剪切图片

}
