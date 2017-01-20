package com.sibozn.gochat.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sibozn.gochat.R;
import com.sibozn.gochat.application.MyApplication;
import com.sibozn.gochat.bean.UserInfoBean;
import com.sibozn.gochat.nohttp.CallServer;
import com.sibozn.gochat.recycler.Divider;
import com.sibozn.gochat.adapter.MyRecyclerViewAdapter;
import com.sibozn.gochat.utils.DBHelper;
import com.sibozn.gochat.utils.DataBaseManager;
import com.sibozn.gochat.utils.PreferenceHelper;
import com.sibozn.gochat.utils.Tools;
import com.sibozn.gochat.websocket.MyWebSocketClient;
import com.sibozn.gochat.widget.FilterImageView;
import com.sibozn.gochat.widget.GlideCircleTransform;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.ResCompat;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.chat.ChatActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseWebSockteActivity
        implements View.OnClickListener, MyWebSocketClient.MyWebSocketMessageListener
        , OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
        , SwipeRefreshLayout.OnRefreshListener, MyRecyclerViewAdapter.RecyclerItemLongClickListener
        , MyRecyclerViewAdapter.RecyclerItemClickListener {

    @BindView(R.id.tv_round_white)
    TextView tv_round_white;
    @BindView(R.id.my_frame_layout_map)
    FrameLayout myFrameLayoutMap;
    //@BindView(R.id.recyler_view)
    //RecyclerView recyclerView;
    @BindView(R.id.swipe_recycler_view)
    SwipeMenuRecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.ll_no_net_work)
    LinearLayout ll_no_net_work;
    private FilterImageView head_portrait;
    private TextView id_link, id_username;
    private GoogleApiClient mGoogleApiClient;
    //private LatLng lastLocationLatLng;
    private Location lastLocation;
    // 默认定位在悉尼
    private double lat = -33.867, lng = 151.206;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private MyRecyclerViewAdapter mAdapter;
    //private boolean isOneAddCircle;
    private List<UserInfoBean> userInfoList;
    private String mEmail, mToken, photo, lastContent;
    private long waitTime = 2000, touchTime = 0;
    private Circle circle;

    @Override
    public int getContentViewId() {
        return R.layout.activity_maps;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        MyApplication.activiys.add(this);
        mEmail = PreferenceHelper.readString(this, "user_info", "email", "");
        mToken = PreferenceHelper.readString(this, "user_info", "token", "");
        userInfoList = new ArrayList<UserInfoBean>();
        initView();
        // google定位
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        // Google地图
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.my_frame_layout_map);
        mapFragment.getMapAsync(this);
    }

    // websocket登录成功回调
    @Override
    protected void loginWebSocketSuccessed(String message) {
        Log.e(TAG, "----websocket登录成功回调----");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ll_no_net_work.setVisibility(View.GONE);
                // webSocket获取消息的监听
                MyWebSocketClient.getInstance().setMyWebSocketMessageListener(MainActivity.this);
                getInfo();
                getPost();
            }
        });

    }

    // websocket连接失败回调
    @Override
    protected void loginOutWebSocket() {
        Log.e(TAG, "----websocket连接失败回调-----");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ll_no_net_work.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initView() {
        View headerView = navigationView.getHeaderView(0);
        id_link = (TextView) headerView.findViewById(R.id.id_link);
        id_username = (TextView) headerView.findViewById(R.id.id_username);
        head_portrait = (FilterImageView) headerView.findViewById(R.id.head_portrait1);
        head_portrait.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            private MenuItem mPreMenuItem;

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (mPreMenuItem != null) mPreMenuItem.setChecked(false);
                item.setChecked(true);
                drawerLayout.closeDrawers();
                mPreMenuItem = item;
                switch (item.getItemId()) {
                    case R.id.navigation_item_home:
                        Log.d(TAG, "首页");
                        Toast.makeText(mContext, "显示首页!", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.navigation_item_blog:
                        // Snackbar.make(mainContent, "我的博客--滑动删除snackbar提示", Snackbar.LENGTH_SHORT).show();
                        Log.d(TAG, "我的博客");
                        break;
                    case R.id.navigation_item_about:
                        // Snackbar.make(mainContent, "关于--滑动删除snackbar提示", Snackbar.LENGTH_SHORT).show();
                        Log.d(TAG, "关于");
                        break;
                    case R.id.navigation_sub_item1:
                        // Snackbar.make(mainContent, "01--滑动删除snackbar提示", Snackbar.LENGTH_SHORT).show();
                        Log.d(TAG, "01");
                        break;
                    case R.id.navigation_sub_item2:
                        // Snackbar.make(mainContent, "02--滑动删除snackbar提示", Snackbar.LENGTH_SHORT).show();
                        Log.d(TAG, "02");
                        break;
                }
                return false;
            }
        });
        recyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new Divider(ResCompat.getDrawable(this, R.drawable.divider_recycler)
                , LinearLayoutManager.VERTICAL));
        // 设置菜单创建器。
        recyclerView.setSwipeMenuCreator(new SwipeMenuCreator() {
            //菜单创建器。在Item要创建菜单的时候调用。
            @Override
            public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
                int width = getResources().getDimensionPixelSize(R.dimen.item_height);
                // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
                int height = ViewGroup.LayoutParams.MATCH_PARENT;

                // 添加左侧的，如果不添加，则左侧不会出现菜单。
                {
                    SwipeMenuItem addItem = new SwipeMenuItem(mContext)
                            .setBackgroundDrawable(R.drawable.selector_green)// 点击的背景。
                            .setImage(R.mipmap.ic_action_add) // 图标。
                            .setWidth(width) // 宽度。
                            .setHeight(height); // 高度。
                    //swipeLeftMenu.addMenuItem(addItem); // 添加一个按钮到左侧菜单。

                    SwipeMenuItem closeItem = new SwipeMenuItem(mContext)
                            .setBackgroundDrawable(R.drawable.selector_green)
                            .setImage(R.mipmap.ic_action_close)
                            .setWidth(width)
                            .setHeight(height);

                    //swipeLeftMenu.addMenuItem(closeItem); // 添加一个按钮到左侧菜单。
                }

                // 添加右侧的，如果不添加，则右侧不会出现菜单。
                {
                    SwipeMenuItem deleteItem = new SwipeMenuItem(mContext)
                            .setBackgroundDrawable(R.drawable.selector_red)
                            .setImage(R.mipmap.ic_action_delete)
                            .setText("删除") // 文字，还可以设置文字颜色，大小等。。
                            .setTextColor(Color.WHITE)
                            .setWidth(width)
                            .setHeight(height);
                    //swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。

                    SwipeMenuItem closeItem = new SwipeMenuItem(mContext)
                            .setBackgroundDrawable(R.drawable.selector_purple)
                            .setImage(R.mipmap.ic_action_close)
                            .setWidth(width)
                            .setHeight(height);
                    //swipeRightMenu.addMenuItem(closeItem); // 添加一个按钮到右侧菜单。

                    SwipeMenuItem addItem = new SwipeMenuItem(mContext)
                            .setBackgroundDrawable(R.drawable.selector_red)
                            .setText(getString(R.string.chat))
                            .setTextColor(Color.WHITE)
                            .setWidth(width)
                            .setHeight(height);
                    swipeRightMenu.addMenuItem(addItem); // 添加一个按钮到右侧菜单。
                }
            }
        });
        // 设置菜单Item点击监听。
        recyclerView.setSwipeMenuItemClickListener(new OnSwipeMenuItemClickListener() {
            /**
             * Item的菜单被点击的时候调用。
             * @param closeable       closeable. 用来关闭菜单。
             * @param adapterPosition adapterPosition. 这个菜单所在的item在Adapter中position。
             * @param menuPosition    menuPosition. 这个菜单的position。比如你为某个Item创建了2个MenuItem，那么这个position可能是是 0、1，
             * @param direction
             * 如果是左侧菜单，值是：SwipeMenuRecyclerView#LEFT_DIRECTION，如果是右侧菜单，值是：SwipeMenuRecyclerView#RIGHT_DIRECTION.
             */
            @Override
            public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
                closeable.smoothCloseMenu();// 关闭被点击的菜单。
                if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                    switch (menuPosition) {
                        case 0:
                            onItemLongClick(adapterPosition);
                            break;
                    }
                    //Toast.makeText(mContext, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast
                    //.LENGTH_SHORT).show();zgz
                } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
                    Toast.makeText(mContext, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast
                            .LENGTH_SHORT).show();
                }
                // TODO 如果是删除：推荐调用Adapter.notifyItemRemoved(position)，不推荐Adapter.notifyDataSetChanged();
                //if (menuPosition == 0) {// 删除按钮被点击。
                //Tools.showTopToast(mContext, "删除按钮");
                //mStrings.remove(adapterPosition);
                //mMenuAdapter.notifyItemRemoved(adapterPosition);
                // }
            }
        });
        mAdapter = new MyRecyclerViewAdapter(userInfoList, this);
        mAdapter.setRecyclerItemLongClickListener(this);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.e(TAG, "onNewIntent: ");
        if (TextUtils.isEmpty(mToken)) {
            finish();
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        Log.e(TAG, "---------onStart: ");
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "--------onResume: ");
        if (MyApplication.isLoginWebSocket) {
            MyWebSocketClient.getInstance().setMyWebSocketMessageListener(this);// webSocket获取消息的监听
            getInfo();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "----------onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "-------------nStop: ");
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "---------onDestroy: ");
        Tools.closeDialog();
        CallServer.getRequestInstance().cancelAll();
    }

    @Override
    public void onRefresh() {
        getPost();
    } // 下拉刷新回调

    @OnClick({R.id.tv_xiaoxi, R.id.fab_post, R.id.tv_home, R.id.ll_no_net_work})
    public void onMyClick(View view) {
        switch (view.getId()) {
            case R.id.ll_no_net_work:// 跳转设置页面
                startActivity(new Intent("android.settings.WIFI_SETTINGS"));
                break;
            case R.id.tv_xiaoxi:// 跳转聊天记录点击事件
                if (!Tools.isNetworkConnected(this)) {
                    Tools.showToast(this, getString(R.string.qing_lian_jei_wang_luo));
                    return;
                }
                startActivity(new Intent(this, ChatHistoryActivity.class));
                break;
            case R.id.tv_home:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.fab_post:// 发布post点击事件
                if (!Tools.isNetworkConnected(this)) {
                    Tools.showToast(this, getString(R.string.qing_lian_jei_wang_luo));
                    return;
                }
                Intent intent = new Intent(this, CreatePostctivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_portrait1:// 跳转用户详情点击事件
                if (!Tools.isNetworkConnected(this)) {
                    Tools.showToast(this, getString(R.string.qing_lian_jei_wang_luo));
                    return;
                }
                if (!MyApplication.isLoginWebSocket) {
                    Log.e(TAG, "webSocket没有连接成功。。。。");
                    return;
                }
                drawerLayout.closeDrawers();
                startActivity(new Intent(this, UserInfoActivity.class));
                break;
        }
    }

    //google定位成功回调,位置每次有变化都会回调该方法
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e(TAG, "-------------定位成功--->>>onConnected");
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastLocation == null) {
            Tools.showToast(this, "定位失败....");
            return;
        }
        LocationServices.FusedLocationApi
                .requestLocationUpdates(mGoogleApiClient, new LocationRequest(), new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.e(TAG, "----onLocationChanged: " + location.getLatitude() + "-lng:" + lastLocation
                                .getLongitude());
                        lng = location.getLongitude();
                        lat = location.getLatitude();
                        circle.setCenter(new LatLng(lat, lng));
                    }
                });
        lat = lastLocation.getLatitude();
        lng = lastLocation.getLongitude();
        mMap.clear();
        circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(lat, lng))
                .strokeWidth(2)
                // .strokeColor(R.color.colorPrimary)
                .fillColor(R.color.gray)
                .radius(2600));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12.6f));
        if (lastLocation != null && MyApplication.isLoginWebSocket) {
            getPost();
        }
    }

    //google定位暂停回调
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "---onConnectionSuspended");
    }

    // google定位失败回调
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "--google定位-onConnectionFailed");
    }

    //google地图就绪回调方法
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e(TAG, "--------------------------onMapReady: ");
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
//        mMap.setBuildingsEnabled(true);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);// 缩放比例控件
        uiSettings.setZoomGesturesEnabled(true);//手势
        //uiSettings.setIndoorLevelPickerEnabled();//层级选取器
        uiSettings.setMapToolbarEnabled(false);// 地图工具栏
//        uiSettings.setMyLocationButtonEnabled(false);//完全禁止定位按钮出现
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {// My Location 按钮事件
            @Override
            public boolean onMyLocationButtonClick() {
                Log.d(TAG, "点击mylocaltion");
                return false;
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {// 地图标记事件
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d(TAG, "onMarkerClick: --getTitle->>>" + marker.getTitle());
                //marker.remove();
                //LatLng latLng = marker.getPosition();
                //该方法返回的布尔值表示您是否使用了该事件 如果它返回 false，则除了发生自定义行为外，还会发生默认行为。
                return false;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {//信息窗口事件
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.d(TAG, "onInfoWindowClick: --点击信息窗口事件-->>>" + marker.getTitle());
            }
        });
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {//摄像机改变监听
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d(TAG, "onCameraChange---cameraPosition.zoom->>" + cameraPosition.zoom);
            }
        });

    }

    // websocket收到服务器消息回调
    @Override
    public void onMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    switch (jsonObject.getString("cmd")) {
                        case "getpost":
                            Log.e(TAG, "---getpost---" + message);
                            String list = jsonObject.getString("list");
                            if (!TextUtils.equals("null", list)) {
                                JSONObject jsonObject1 = new JSONObject(list);
                                if (userInfoList.size() != 0) {
                                    userInfoList.clear();
                                }
                                for (Iterator keys = jsonObject1.keys(); keys.hasNext(); ) {
                                    String type = (String) keys.next();
                                    JSONObject jsonObjec2 = jsonObject1.getJSONObject(type);
                                    double lat = Double.parseDouble(jsonObjec2.getString("lat"));
                                    double lng = Double.parseDouble(jsonObjec2.getString("lng"));
                                    String email = jsonObjec2.getString("email");
                                    String uid = jsonObjec2.getString("uid");
                                    String data = jsonObjec2.getString("data");
                                    Marker marker = mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(lat, lng))
                                            .title(uid)
                                            .alpha(0.7f)
                                            // .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory
                                            // .HUE_AZURE))
                                            // .draggable(true)
                                            .snippet(data));
                                    userInfoList.add(new UserInfoBean(email, lng + "", lat + ""
                                            , jsonObjec2.getString("data"), jsonObjec2.getString("type")
                                            , uid, jsonObjec2.getString("photo")
                                            , jsonObjec2.getString("sex"), jsonObjec2.getString("age")
                                            , jsonObjec2.getString("country"), jsonObjec2.getString("city"), marker));

                                    if (TextUtils.equals(email, mEmail)) {
                                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory
                                                .HUE_RED));
                                    } else {
                                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory
                                                .HUE_AZURE));
                                    }
                                }
                                mAdapter.notifyDataSetChanged();
                            } else {
                                Tools.showToast(mContext, getString(R.string.rv_with_footer_empty));
                            }
                            if (swipeRefreshLayout.isRefreshing()) {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            break;
                        case "getinfo":
                            Log.e(TAG, "---getinfo---" + message);
                            String listInfo = jsonObject.getString("list");
                            if (!TextUtils.equals("null", listInfo)) {
                                JSONObject jsonObject1 = new JSONObject(listInfo);
                                if (TextUtils.equals(jsonObject1.getString("email"), mEmail)) {
                                    id_link.setText(jsonObject1.getString("email"));
                                    id_username.setText(jsonObject1.getString("uid"));
                                    photo = jsonObject1.getString("photo");
                                    Glide.with(head_portrait.getContext())
                                            .load(photo)
                                            .centerCrop()
                                            .transform(new GlideCircleTransform(mContext))
                                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                            .placeholder(R.mipmap.ic_launcher)
                                            .error(R.mipmap.ic_launcher)
                                            .crossFade()
                                            .into(head_portrait);
                                    PreferenceHelper.write(mContext, "user_info", "head_url", photo);
                                } else {
                                    try {
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("id", jsonObject1.getString("uid"));
                                        contentValues.put("from_his", mEmail);
                                        contentValues.put("to_his", jsonObject1.getString("email"));
                                        contentValues.put("data_his", lastContent);
                                        contentValues.put("type", "text");
                                        contentValues.put("time", System.currentTimeMillis());
                                        contentValues.put("uid", jsonObject1.getString("uid"));
                                        contentValues.put("photo", jsonObject1.getString("photo"));
                                        contentValues.put("age", jsonObject1.getString("age"));
                                        contentValues.put("sex", jsonObject1.getString("sex"));
                                        contentValues.put("country", jsonObject1.getString("country"));
                                        contentValues.put("country", jsonObject1.getString("country"));
                                        DataBaseManager.getInstance(MainActivity.this).insertData(DBHelper
                                                        .CHAT_HISTORY_TABLE
                                                , contentValues);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                Log.e(TAG, "-------getinfo为空------");
                            }
                            break;
                        case "getunread":
                            Log.e(TAG, "---getunread---" + message);
                            String unreadList = jsonObject.getString("list");
                            if (!TextUtils.equals("null", unreadList)) {
                                tv_round_white.setVisibility(View.VISIBLE);
                                JSONObject jsonObject1 = new JSONObject(unreadList);
                                boolean b = true;
                                for (Iterator keys = jsonObject1.keys(); keys.hasNext(); ) {
                                    String type = (String) keys.next();
                                    JSONObject jsonObjec2 = jsonObject1.getJSONObject(type);
                                    if (b) {
                                        PreferenceHelper.write(mContext, "user_info", jsonObjec2.getString("from"),
                                                jsonObjec2.getString("data"));
                                        b = false;
                                    }
                                    try {
                                        Cursor cursor = DataBaseManager.getInstance(MainActivity.this)
                                                .queryData2Cursor("select * from_his " + DBHelper.CHAT_HISTORY_TABLE
                                                        + " where from_his = '" + jsonObjec2.getString("from")
                                                        + "';", null);
                                        if (!cursor.moveToNext()) {
                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put("id", jsonObjec2.getString("id"));
                                            contentValues.put("from_his", jsonObjec2.getString("from"));
                                            contentValues.put("to_his", jsonObjec2.getString("to"));
                                            contentValues.put("data_his", jsonObjec2.getString("data"));
                                            contentValues.put("type", jsonObjec2.getString("type"));
                                            contentValues.put("time", jsonObjec2.getString("time"));
                                            contentValues.put("uid", jsonObjec2.getString("uid"));
                                            contentValues.put("photo", jsonObjec2.getString("photo"));
                                            contentValues.put("age", jsonObjec2.getString("age"));
                                            contentValues.put("sex", jsonObjec2.getString("sex"));
                                            contentValues.put("country", jsonObjec2.getString("country"));
                                            contentValues.put("country", jsonObjec2.getString("country"));
                                            DataBaseManager.getInstance(MainActivity.this).insertData(DBHelper
                                                            .CHAT_HISTORY_TABLE
                                                    , contentValues);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                tv_round_white.setVisibility(View.INVISIBLE);
                            }
                            break;
                        case "fromMsg":
                            tv_round_white.setVisibility(View.VISIBLE);
                            String fromEmail = null;
                            switch (jsonObject.getString("type")) {
                                case "text":
                                    fromEmail = jsonObject.getString("from");
                                    lastContent = jsonObject.getString("data");
                                    PreferenceHelper.write(mContext, "user_info", fromEmail, lastContent);
                                    try {
                                        Cursor cursor = DataBaseManager.getInstance(mContext).queryData2Cursor
                                                ("select * from"
                                                        + DBHelper.CHAT_HISTORY_TABLE
                                                        + " where from_his = '" + fromEmail + "';", null);
                                        if (!cursor.isFirst()) {
                                            MyWebSocketClient.getInstance().sendMyWebSocket(Tools
                                                    .getMyInfoWebSocketString(fromEmail, mToken));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case "image":// 接收聊天图片类型内容
                                    Log.e(TAG, "---------首页收到图片的消息------");
                                    PreferenceHelper.write(mContext, "user_info", fromEmail, "iamge");
                                    break;
                            }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //通过webSocket获取post列表数据
    private void getPost() {
        if (Tools.isNetworkConnected(this)) {
            swipeRefreshLayout.setRefreshing(true);
            MyWebSocketClient.getInstance().sendMyWebSocket(Tools.getPostWebSocketString(mEmail, mToken, lat, lng));
        } else {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            Tools.showToast(this, getString(R.string.qing_lian_jei_wang_luo));
        }

    }

    //通过webSocket发送getinfo、getunread
    private void getInfo() {
        if (Tools.isNetworkConnected(this)) {
            MyWebSocketClient.getInstance().sendMyWebSocket(Tools.getMyInfoWebSocketString(mEmail, mToken));
            MyWebSocketClient.getInstance().sendMyWebSocket(Tools.getUnreadWebSocketString(mEmail, mToken));
        } else {
            Tools.showToast(this, "请连接网络");
        }
    }

    /**
     * 监听[返回]键事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 返回键
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= waitTime) {
                Tools.showToast(getApplicationContext(), getString(R.string.exit_string));
                touchTime = currentTime;
            } else {
                MyWebSocketClient.getInstance().close();
                finish();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onItemClick(int position) {
        UserInfoBean userInfoBean = userInfoList.get(position);
        Marker marker = userInfoBean.getMarker();
        double lat = Double.parseDouble(userInfoBean.getLat());
        double lng = Double.parseDouble(userInfoBean.getLng());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 14.6f));
        marker.showInfoWindow();
    }

    @Override
    public void onItemLongClick(final int position) {
        final String userEmail = userInfoList.get(position).getEmail();
        if (TextUtils.equals(mEmail, userEmail)) {
            showDialog();
            return;
        }
        if (!MyApplication.isLoginWebSocket) {
            Log.e(TAG, "webSocket没有连接成功。。。。");
            return;
        }
        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        intent.putExtra("userEmail", userEmail);
        intent.putExtra("user_uid", userInfoList.get(position).getUid());
        intent.putExtra("user_photo", userInfoList.get(position).getPhoto());
        intent.putExtra("user_age", userInfoList.get(position).getAge());
        intent.putExtra("user_sex", userInfoList.get(position).getSex());
        intent.putExtra("mphoto", photo);
        startActivity(intent);
    }// 长按跳转聊天页面

    /**
     * 这里使用了 android.support.v7.app.AlertDialog.Builder
     * 可以直接在头部写 import android.support.v7.app.AlertDialog
     * 那么下面就可以写成 AlertDialog.Builder
     */
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle("Material Design Dialog");
        builder.setMessage(getString(R.string.no_send_post_message));
        // builder.setNegativeButton("取消", null);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}

/*--------------------------------------------------------------------------------------------------------------*/

//        List<Fragment> fragments = new ArrayList<Fragment>();
//        fragments.add(MapFragment.newInstance(latString, lngString));
//        fragments.add(ChatFragment.newInstance("2222222222", "2222222222"));
//        List<String> tiles = new ArrayList<String>();
//        tiles.add("地图");
//        tiles.add("聊天");
//        TabAdaper tabAdaper = new TabAdaper(getSupportFragmentManager(), fragments, tiles);
//        view_pager.setAdapter(tabAdaper);
//        tab_layout.setupWithViewPager(view_pager);// setupWithViewPager必须在ViewPager.setAdapter()之后调用
//        tab_layout.setTabMode(TabLayout.MODE_SCROLLABLE);
//        Log.d(TAG, "lat/lng:" + latString + lngString);
//        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.d(TAG, "onPageScrolled" + position);
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                Log.d(TAG, "onPageSelected:" + position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                Log.d(TAG, "onPageScrollStateChanged" + state);
//            }
//        });
// view_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));
//        fabSearch.setOnClickListener(this);
//  frameContent = (FrameLayout) findViewById(R.id.frame_content);

//        final ActionBar actionBar = getSupportActionBar();
//        actionBar.setHomeAsUpIndicator(android.R.drawable.checkbox_off_background);
//        actionBar.setHomeButtonEnabled(true);//决定左上角的图标是否可以点击
//        actionBar.setDisplayHomeAsUpEnabled(false);//决定左上角图标的右侧是否有向左的小箭头
//        actionBar.setDisplayHomeAsUpEnabled(true);
//设置导航图标、添加菜单点击事件要在setSupportActionBar方法之后
//        idToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
////                    case R.id.action_settings:
////                        Toast.makeText(MainActivity.this, "action_settings", 0).show();
////                        break;
////                    case R.id.action_share:
////                        Toast.makeText(MainActivity.this, "action_share", 0).show();
////                        break;
////                    default:
////                        break;
//                }
//                return true;
//            }
//        });
//                Snackbar.make(mainContent, "SnackbarClicked", Snackbar.LENGTH_SHORT).setAction("Action", new View
//                        .OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(MainActivity.this, "I'm a Toast", Toast.LENGTH_SHORT).show();
//                    }
//                }).setActionTextColor(Color.RED).show();