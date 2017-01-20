/*
 * Copyright (c) 2015, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kymjs.chat;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.sibozn.gochat.R;
import com.sibozn.gochat.nohttp.CallServer;
import com.sibozn.gochat.nohttp.HttpListener;
import com.sibozn.gochat.utils.Constants;
import com.sibozn.gochat.utils.DBHelper;
import com.sibozn.gochat.utils.DataBaseManager;
import com.sibozn.gochat.utils.PreferenceHelper;
import com.sibozn.gochat.utils.Tools;
import com.sibozn.gochat.websocket.MyWebSocketClient;
import com.umeng.analytics.MobclickAgent;
import com.yolanda.nohttp.BasicBinary;
import com.yolanda.nohttp.FileBinary;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.chat.adapter.ChatAdapter;
import org.kymjs.chat.bean.Emojicon;
import org.kymjs.chat.bean.Faceicon;
import org.kymjs.chat.bean.Message;
import org.kymjs.chat.emoji.DisplayRules;
import org.kymjs.chat.widget.KJChatKeyboard;
import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.FileUtils;
import org.kymjs.kjframe.utils.KJLoger;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 聊天主界面
 */
public class ChatActivity extends KJActivity implements MyWebSocketClient.MyWebSocketMessageListener
        , HttpListener<String> {

    public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0x1;
    private static final int IMAGE_CAPTURE_CODE = 0x300;
    public static final int UP_DATA_HPOTO = 0x2;
    private static final String TAG = "ChatActivity";

    private KJChatKeyboard box;
    private ListView mRealListView;
    List<Message> datas = new ArrayList<Message>();
    private ChatAdapter adapter;
    private String userEmail, userUid, mEmail, mphoto, user_photo, mUid, user_age, user_sex;
    private TextView tv_to_uid;
    private File fileImage, tempFile;
    private String lastContent, tempFileString;
    private MyWebSocketClient myWebSocketClient;

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_chat);
        userEmail = getIntent().getStringExtra("userEmail");
        userUid = getIntent().getStringExtra("user_uid");
        user_photo = getIntent().getStringExtra("user_photo");
        user_age = getIntent().getStringExtra("user_age");
        user_sex = getIntent().getStringExtra("user_sex");
        mphoto = getIntent().getStringExtra("photo");
        mEmail = PreferenceHelper.readString(this, "user_info", "email", "");
        mUid = PreferenceHelper.readString(this, "user_info", "uid", "");
        if (Tools.checkSDcard()) {
            tempFileString = getExternalCacheDir().getAbsolutePath();
        } else {
            tempFileString = getCacheDir().getAbsolutePath();
        }
        myWebSocketClient = MyWebSocketClient.getInstance();
        myWebSocketClient.setMyWebSocketMessageListener(this);
    }

    public void onBack(View view) {
        finish();
    }// 标题左边返回点击事件方法

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "------------onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "---------------onResume: ");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        if (!TextUtils.isEmpty(lastContent)) {
            PreferenceHelper.write(this, "user_info", userEmail, lastContent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "-------------onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "-------------onDestroy: ");
    }

    @Override
    public void initWidget() {
        super.initWidget();
        box = (KJChatKeyboard) findViewById(R.id.chat_msg_input_box);
        mRealListView = (ListView) findViewById(R.id.chat_listview);
        tv_to_uid = (TextView) findViewById(R.id.tv_to_uid);
        tv_to_uid.setText(userUid);
        mRealListView.setSelector(android.R.color.transparent);
        initMessageInputToolBox();
        adapter = new ChatAdapter(this, datas, getOnChatItemClickListener());
        mRealListView.setAdapter(adapter);
        queryDB();
        insetDb();
        // initListView();
    }

    /**
     * 初始化聊天页面底部栏点击事件
     */
    private void initMessageInputToolBox() {
        box.setOnOperationListener(new OnOperationListener() {
            // 发送文本内容点击事件
            @Override
            public void send(String content) {// 发送文本内容
                if (TextUtils.isEmpty(content)) {
                    Tools.showToast(ChatActivity.this, "请输入内容!");
                    return;
                }
                //发送json {"cmd":"chat","to":"对方email","from":"自己email","data":"内容","type":"类型"}
                myWebSocketClient.sendMyWebSocket(Tools.sendChatContentString(userEmail, mEmail, content));
                lastContent = content;
                ContentValues contentValues = new ContentValues();
                contentValues.put("fromUserName", PreferenceHelper.readString(ChatActivity.this, "user_info", "uid",
                        ""));// 发送者uid
                contentValues.put("toUserName", userUid);// 接收者uid
                contentValues.put("fromUserEmail", mEmail);// 发送者邮箱
                contentValues.put("toUserEmail", userEmail);// 接收者邮箱
                contentValues.put("date", System.currentTimeMillis() + "");// 发送接收时间
                contentValues.put("content", content);// 消息内容
                contentValues.put("type", "0");//判断内容类型，0是文字，1是图片
                contentValues.put("fromWho", "me");//判断消息来自me 还是you
                contentValues.put("icon", mphoto);// 接收者或发送者头像图片
                try {
                    DataBaseManager.getInstance(ChatActivity.this)
                            .insertData(DBHelper.CHAT_CONTENT_TABLE, contentValues);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message sendMessage = new Message(Message.MSG_TYPE_TEXT, Message.MSG_STATE_SUCCESS,
                        mEmail, mphoto, userEmail, user_photo, content, true, true, new Date());
                datas.add(sendMessage);
                adapter.refresh(datas);
                mRealListView.setSelection(datas.size());
                // createReplayMsg(sendMessage);
            }

            @Override
            public void selectedFace(Faceicon content) {
                // 表情包
//                Message message = new Message(Message.MSG_TYPE_FACE, Message.MSG_STATE_SUCCESS,
//                        "Tom", "avatar", "Jerry", "avatar", content.getPath(), true, true, new
//                        Date());
//                datas.add(message);
//                adapter.refresh(datas);
//                createReplayMsg(message);
            }

            @Override
            public void selectedEmoji(Emojicon emoji) {
                box.getEditTextBox().append(emoji.getValue());
            }

            @Override
            public void selectedBackSpace(Emojicon back) {
                DisplayRules.backspace(box.getEditTextBox());
            }

            // 选择图片和相机拍照点击事件
            @Override
            public void selectedFunction(int index) {
                switch (index) {
                    case 0:
                        goToAlbum();
                        break;
                    case 1:
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        tempFile = new File(tempFileString + "/capture.jpg");
//                        if (tempFile.exists()) {
//                            tempFile.delete();
//                        }
                        // intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                        startActivityForResult(intent, IMAGE_CAPTURE_CODE);
                        //ViewInject.toast("跳转相机");
                        break;
                }
            }
        });

        List<String> faceCagegory = new ArrayList<>();
//        File faceList = FileUtils.getSaveFolder("chat");
        File faceList = new File("");
        if (faceList.isDirectory()) {
            File[] faceFolderArray = faceList.listFiles();
            for (File folder : faceFolderArray) {
                if (!folder.isHidden()) {
                    faceCagegory.add(folder.getAbsolutePath());
                }
            }
        }
        box.setFaceData(faceCagegory);
        mRealListView.setOnTouchListener(getOnTouchListener());
    }//初始化聊天页面底部栏点击事件s

    /**
     * 进入聊天页面往历史记录表插数据
     */
    private void insetDb() {
        try {
            Cursor cursor = DataBaseManager.getInstance(this)
                    .queryData2Cursor("select * from " + DBHelper.CHAT_HISTORY_TABLE + " where from_his = '"
                            + userEmail + "';", null);
            if (!cursor.moveToNext()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("id", "0");
                contentValues.put("from_his", userEmail);
                contentValues.put("to_his", mEmail);
                contentValues.put("data_his", "00000000");
                contentValues.put("type", "000000000");
                contentValues.put("time", "000000000000");
                contentValues.put("uid", userUid);
                contentValues.put("photo", user_photo);
                contentValues.put("age", user_age);
                contentValues.put("sex", user_sex);
                contentValues.put("country", "00000000000");
                DataBaseManager.getInstance(this).insertData(DBHelper
                        .CHAT_HISTORY_TABLE, contentValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// 先查询聊天记录表中是否有记录，有记录则不往聊天记录表中插数据
    // ，否则往聊天记录表中插入一条数据

    /**
     * 查询聊天内容表中数据
     */
    private void queryDB() {
        try {
            Cursor cursor = DataBaseManager.getInstance(this).queryData2Cursor("select * from " + DBHelper
                    .CHAT_CONTENT_TABLE
                    + " where toUserEmail = '" + userEmail + "';", null);
            while (cursor.moveToNext()) {
                String fromUserName = cursor.getString(cursor.getColumnIndex("fromUserName"));
                String toUserName = cursor.getString(cursor.getColumnIndex("toUserName"));
                String fromUserEmail = cursor.getString(cursor.getColumnIndex("fromUserEmail"));
                String toUserEmail = cursor.getString(cursor.getColumnIndex("toUserEmail"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String icon = cursor.getString(cursor.getColumnIndex("fromWho"));
                String fromWho = cursor.getString(cursor.getColumnIndex("fromWho"));
                if (TextUtils.equals(fromWho, "me")) {
                    if (TextUtils.equals("0", type)) {//判断内容类型，0是文字，1是图片
                        Message fromMessage = new Message(Message.MSG_TYPE_TEXT, Message.MSG_STATE_SUCCESS,
                                mUid, mphoto, userUid, user_photo, content, true, true, new Date());
                        datas.add(fromMessage);
                    } else {
                        Message fromMessage = new Message(Message.MSG_TYPE_PHOTO, Message.MSG_STATE_SUCCESS,
                                mUid, mphoto, userUid, user_photo, content, true, true, new Date());
                        datas.add(fromMessage);
                    }
                } else {// you
                    if (TextUtils.equals("0", type)) {
                        Message fromMessage = new Message(Message.MSG_TYPE_TEXT, Message.MSG_STATE_SUCCESS,
                                userUid, user_photo, mUid, mphoto, content, false, true, new Date());
                        datas.add(fromMessage);
                    } else {
                        Message fromMessage = new Message(Message.MSG_TYPE_PHOTO, Message.MSG_STATE_SUCCESS,
                                userUid, user_photo, mUid, mphoto, content, false, true, new Date());
                        datas.add(fromMessage);
                    }
                }
                adapter.refresh(datas);
                mRealListView.setSelection(datas.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// 查询聊天内容表

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && box.isShow()) {
            box.hideLayout();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 跳转到选择相册界面
     */
    private void goToAlbum() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    REQUEST_CODE_GETIMAGE_BYSDCARD);
        } else {
            intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    REQUEST_CODE_GETIMAGE_BYSDCARD);
        }

    }// 跳转到选择相册界面

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_GETIMAGE_BYSDCARD || requestCode == IMAGE_CAPTURE_CODE) {
            Uri dataUri = data.getData();
            if (dataUri != null) {
                fileImage = FileUtils.uri2File(aty, dataUri);
                // 上传图片
                Request<String> request = NoHttp.createStringRequest(Constants.UP_LOAD_URL, RequestMethod.POST);// 上传图片
                if (request != null) {
                    BasicBinary binary = new FileBinary(fileImage);
                    request.add("userfile", binary);// 添加1个文件
                    // 添加到请求队列
                    CallServer.getRequestInstance().add(this, UP_DATA_HPOTO, request, this, false, true);
                }
            }
        }
//        if (requestCode == IMAGE_CAPTURE_CODE) {
//            // 上传图片
//            Request<String> request = NoHttp.createStringRequest(Constants.UP_LOAD_URL, RequestMethod.POST);// 上传图片
//            if (request != null) {
//                BasicBinary binary = new FileBinary(tempFile);
//                request.add("userfile", binary);// 添加1个文件
//                // 添加到请求队列
//                CallServer.getRequestInstance().add(this, UP_DATA_HPOTO, request, this, false, true);
//            }
//        }
    }

    /**
     * 若软键盘或表情键盘弹起，点击上端空白处应该隐藏输入法键盘
     *
     * @return 会隐藏输入法键盘的触摸事件监听器
     */
    private View.OnTouchListener getOnTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                box.hideLayout();
                box.hideKeyboard(aty);
                return false;
            }
        };
    }

    @Override
    public void onMessage(final String content) { //
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 收到json {"cmd":"fromMsg","to":"我的email","from":"发送人的email","data":"内容","type":"类型"}
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    switch (jsonObject.getString("cmd")) {
                        case "fromMsg":
                            switch (jsonObject.getString("type")) {
                                case "text":
                                    lastContent = jsonObject.getString("data");
                                    // 接收聊天内容之后插入本地聊天内容表中
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("fromUserName", userUid);// 发送者uid
                                    contentValues.put("toUserName", PreferenceHelper.readString(ChatActivity.this,
                                            "user_info", "uid",
                                            ""));// 接收者uid
                                    contentValues.put("fromUserEmail", jsonObject.getString("to"));// 发送者邮箱
                                    contentValues.put("toUserEmail", jsonObject.getString("from"));// 接收者邮箱
                                    contentValues.put("date", System.currentTimeMillis() + "");// 发送接收时间
                                    contentValues.put("content", jsonObject.getString("data"));// 消息内容
                                    contentValues.put("type", "0");//判断内容类型，0是文字，1是图片
                                    contentValues.put("fromWho", "you");//判断消息来自me 还是you
                                    contentValues.put("icon", user_photo);// 接收者或发送者头像图片
                                    try {
                                        DataBaseManager.getInstance(ChatActivity.this)
                                                .insertData(DBHelper.CHAT_CONTENT_TABLE, contentValues);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    // 显示在聊天listview中
                                    Message fromMessage = new Message(Message.MSG_TYPE_TEXT, Message.MSG_STATE_SUCCESS,
                                            userUid, user_photo, userEmail, user_photo, jsonObject.getString("data"),
                                            false,
                                            true, new Date());
                                    datas.add(fromMessage);
                                    adapter.refresh(datas);
                                    // adapter.notifyDataSetChanged();
                                    //mRealListView.setFocusable(true);
                                    mRealListView.setSelection(datas.size());
                                    break;
                                case "image":// 接收聊天图片类型内容
                                    String imageUrl = jsonObject.getString("data");
                                    Message iamgeMessage = new Message(Message.MSG_TYPE_PHOTO, Message
                                            .MSG_STATE_SUCCESS,
                                            userUid, user_photo, mUid, mphoto, imageUrl, false, true, new Date());
                                    datas.add(iamgeMessage);
                                    adapter.refresh(datas);
                                    //mRealListView.setFocusable(true);
                                    mRealListView.setSelection(datas.size());
                                    break;
                            }

                            break;
                        case "":// 其他。。

                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }// webSocket请求成功回调

    @Override
    public void onSucceed(int what, Response<String> response) {
        int responseCode = response.getHeaders().getResponseCode();// 服务器响应码
        if (responseCode == 200) {
            switch (what) {
                case UP_DATA_HPOTO:// 上传图片成功
                    Log.e(TAG, "----------上传图片成功-------》》》" + response.get());
                    String imageString = response.get();
                    Message message = new Message(Message.MSG_TYPE_PHOTO, Message.MSG_STATE_SUCCESS,
                            mEmail, mphoto, userEmail, user_photo, fileImage.getAbsolutePath(), true, true, new Date());
                    datas.add(message);
                    adapter.refresh(datas);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("fromUserName", PreferenceHelper.readString(ChatActivity.this, "user_info", "uid",
                            ""));// 发送者uid
                    contentValues.put("toUserName", userUid);// 接收者uid
                    contentValues.put("fromUserEmail", mEmail);// 发送者邮箱
                    contentValues.put("toUserEmail", userEmail);// 接收者邮箱
                    contentValues.put("date", System.currentTimeMillis() + "");// 发送接收时间
                    contentValues.put("content", imageString);// 消息内容
                    contentValues.put("type", "1");//判断内容类型，0是文字，1是图片
                    contentValues.put("fromWho", "me");//判断消息来自me 还是you
                    contentValues.put("icon", mphoto);// 接收者或发送者头像图片
                    try {
                        DataBaseManager.getInstance(ChatActivity.this)
                                .insertData(DBHelper.CHAT_CONTENT_TABLE, contentValues);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2222222: // 其他情况

                    break;
            }
        }
    }// noHttp请求成功回调

    @Override
    public void onFailed(int what, Response<String> response) {
        if (what == 1) {
            Tools.showToast(this, getString(R.string.update_avatar_error));
            return;
        }
    }// noHttp请求失败回调

    /**
     * 聊天列表中对内容的点击事件监听接口
     */
    public interface OnChatItemClickListener {
        void onPhotoClick(int position);

        void onTextClick(int position);

        void onFaceClick(int position);
    }// 聊天列表中对内容的点击事件接口

    /**
     * @return 聊天列表内存点击事件监听器
     */
    private OnChatItemClickListener getOnChatItemClickListener() {
        return new OnChatItemClickListener() {
            @Override
            public void onPhotoClick(int position) {
                KJLoger.debug(datas.get(position).getContent() + "点击图片的");
                ViewInject.toast(aty, datas.get(position).getContent() + "点击图片的");
            }

            @Override
            public void onTextClick(int position) {
            }

            @Override
            public void onFaceClick(int position) {
            }
        };
    }// 点击聊天列表中对内容的点击事件

    private void initListView() {
        byte[] emoji = new byte[]{
                (byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x81
        };
        Message message = new Message(Message.MSG_TYPE_TEXT,
                Message.MSG_STATE_SUCCESS, "\ue415", "avatar", "Jerry", "avatar",
                new String(emoji), false, true, new Date(System.currentTimeMillis()
                - (1000 * 60 * 60 * 24) * 8));
        Message message1 = new Message(Message.MSG_TYPE_TEXT,
                Message.MSG_STATE_SUCCESS, "Tom", "avatar", "Jerry", "avatar",
                "以后的版本支持链接高亮喔:http://www.kymjs.com支持http、https、svn、ftp开头的链接",
                true, true, new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24) * 8));
        Message message2 = new Message(Message.MSG_TYPE_PHOTO,
                Message.MSG_STATE_SUCCESS, "Tom", "avatar", "Jerry", "avatar",
                "http://static.oschina.net/uploads/space/2015/0611/103706_rpPc_1157342.png",
                false, true, new Date(
                System.currentTimeMillis() - (1000 * 60 * 60 * 24) * 7));
        Message message6 = new Message(Message.MSG_TYPE_TEXT,
                Message.MSG_STATE_FAIL, "Tom", "avatar", "Jerry", "avatar",
                "test send fail", true, false, new Date(
                System.currentTimeMillis() - (1000 * 60 * 60 * 24) * 6));
        Message message7 = new Message(Message.MSG_TYPE_TEXT,
                Message.MSG_STATE_SENDING, "Tom", "avatar", "Jerry", "avatar",
                "<a href=\"http://kymjs.com\">自定义链接</a>也是支持的", true, true, new Date(System.currentTimeMillis()
                - (1000 * 60 * 60 * 24) * 6));

        datas.add(message);
        datas.add(message1);
        datas.add(message2);
        datas.add(message6);
        datas.add(message7);

    }

    private void createReplayMsg(Message message) {
        final Message reMessage = new Message(message.getType(), Message.MSG_STATE_SUCCESS, "Tom",
                "avatar", "Jerry", "avatar", message.getType() == Message.MSG_TYPE_TEXT ? "返回:"
                + message.getContent() : message.getContent(), false,
                true, new Date());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * (new Random().nextInt(3) + 1));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            datas.add(reMessage);
                            adapter.refresh(datas);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
