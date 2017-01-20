package com.sibozn.gochat.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.sibozn.gochat.receiver.NetWorkChangeBroadcastReceiver;
import com.sibozn.gochat.service.WebSocketClientService;
import com.sibozn.gochat.utils.Constants;
import com.umeng.analytics.MobclickAgent;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

//  ┏┓　　　┏┓
//┏┛┻━━━┛┻┓
//┃　　　　　　　┃
//┃　　　━　　　┃
//┃　┳┛　┗┳　┃
//┃　　　　　　　┃
//┃　　　┻　　　┃
//┃　　　　　　　┃
//┗━┓　　　┏━┛
//    ┃　　　┃   神兽保佑
//    ┃　　　┃   代码无BUG！
//    ┃　　　┗━━━┓
//    ┃　　　　　　　┣┓
//    ┃　　　　　　　┏┛
//    ┗┓┓┏━┳┓┏┛
//      ┃┫┫　┃┫┫
//      ┗┻┛　┗┻┛

public abstract class BaseWebSockteActivity extends AppCompatActivity
        implements NetWorkChangeBroadcastReceiver.onLoginWebSocketListener,
        NetWorkChangeBroadcastReceiver.onLoginOutWebSocketListener {

    protected String TAG = this.getClass().getSimpleName();

    //子类具体实现处理逻辑
    protected abstract int getContentViewId();

    protected abstract void initAllMembersView(Bundle savedInstanceState);

    protected abstract void loginWebSocketSuccessed(String message);

    protected abstract void loginOutWebSocket();


    protected interface OnActionResponse {
        void onResponse(Intent intent);
    }

    private BroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;
    // 用来记录需要处理的action和响应函数
    private Map<String, List<OnActionResponse>> mCallbacks;
    protected NetWorkChangeBroadcastReceiver netWorkChangeBroadcastReceiver;
    protected WebSocketClientService webSocketClientService;
    protected Context mContext;
    private IntentFilter mFilter;
    // protected View ll_no_net_work;
    // 在Activity中，我们通过ServiceConnection接口来取得建立连接与连接意外丢失的回调
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 建立连接
            // 获取服务的操作对象
            WebSocketClientService.MyBinder binder = (WebSocketClientService.MyBinder) service;
            webSocketClientService = binder.getService();// 获取到的Service即PlayerService
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 连接断开
            Log.e(TAG, "-----服务断开连接---onServiceDisconnected: ");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        mContext = this;
        //注册网络状态的广播，绑定到mReceiver
        mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        netWorkChangeBroadcastReceiver = new NetWorkChangeBroadcastReceiver();
        netWorkChangeBroadcastReceiver.setOnLoginWebSocketListener(this);
        netWorkChangeBroadcastReceiver.setOnLoginOutWebSocketListener(this);
        //Intent intent = new Intent(this, WebSocketClientService.class);
        ///bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);//绑定目标Service
        initAllMembersView(savedInstanceState);
        // ll_no_net_work = View.inflate(this, R.layout.layout_no_net_work, null);
    }

    @Override
    public void onFirstLoginSucceed(String message) {
        loginWebSocketSuccessed(message);
    }

    @Override
    public void onLoginOut() {
        loginOutWebSocket();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        registerReceiver(netWorkChangeBroadcastReceiver, mFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //注销接收
        unregisterReceiver(netWorkChangeBroadcastReceiver);
        //unbindService(serviceConnection);// 解除绑定，断开连接
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销接收
        //unregisterReceiver(netWorkChangeBroadcastReceiver);
    }
    //子类调用该方法，注册所要处理的广播action
//    /**
//     * if subclass need response BroadcastReceiver, need invoke this method to
//     * add can Receive Action
//     *
//     * @param intent
//     * @param callback
//     */
//    public void addCanReceiveAction(Intent intent, OnActionResponse callback) {
//        final String action = intent.getAction();
//        if (!mIntentFilter.hasAction(action)) {
//            mIntentFilter.addAction(action);
//            registerReceiver(mReceiver, mIntentFilter);
//        }
//        if (!mCallbacks.containsKey(action)) {
//            mCallbacks.put(action, Collections.synchronizedList(new ArrayList<OnActionResponse>()));
//        }
//        mCallbacks.get(action).add(callback);
//        intent.putExtra(Constants.EXTRA_ACTION_CALLBACK_HASH_CODE, callback.hashCode());
//    }

    private class CommonReceiver extends BroadcastReceiver {
        // 子类收到广播后的逻辑
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "CommonReceiver receiver intent:" + intent.getAction());
            final String action = intent.getAction();
            if (mCallbacks != null && mCallbacks.containsKey(action)) {
                int hashCode = intent.getIntExtra(Constants.EXTRA_ACTION_CALLBACK_HASH_CODE, -1);
                List<OnActionResponse> list = mCallbacks.get(action);
                if (list != null) {
                    int index = -1;
                    int count = list.size();

                    for (int i = 0; i < count; i++) {
                        if (hashCode == list.get(i).hashCode()) {
                            index = i;
                            break;
                        }
                    }

                    if (index >= 0) {
                        list.get(index).onResponse(intent);
                    } else {
                        list.get(count - 1).onResponse(intent);
                    }

                    if (list.isEmpty()) {
                        mCallbacks.remove(action);
                    }
                }
            }
        }
    }

}
