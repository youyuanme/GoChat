package com.sibozn.gochat.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.sibozn.gochat.R;
import com.sibozn.gochat.bean.HistoryInfoBean;
import com.sibozn.gochat.recycler.Divider;
import com.sibozn.gochat.adapter.ChatHistoryRecyclerViewAdapter;
import com.sibozn.gochat.utils.DBHelper;
import com.sibozn.gochat.utils.DataBaseManager;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.ResCompat;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;

import org.kymjs.chat.ChatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

public class ChatHistoryActivity extends BaseWebSockteActivity implements ChatHistoryRecyclerViewAdapter
        .RecyclerItemClickListener {

    private List<HistoryInfoBean> datas;
    @BindView(R.id.ll_no_net_work)
    LinearLayout ll_no_net_work;
    @BindView(R.id.recycler_view)
    SwipeMenuRecyclerView recyclerView;
    private ChatHistoryRecyclerViewAdapter mAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_chat_history;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        datas = new ArrayList<>();
        initView();
        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this)//获取当前页面
                .setDisallowInterceptTouchEvent(true);
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

    // Websocket登录成功回调
    @Override
    protected void loginWebSocketSuccessed(String message) {
        ll_no_net_work.setVisibility(View.GONE);
        Log.e(TAG, "--------websocket登录成功回调-----------");
    }

    // websocket连接失败回调
    @Override
    protected void loginOutWebSocket() {
        ll_no_net_work.setVisibility(View.VISIBLE);
        Log.e(TAG, "--------websocket连接失败回调---------");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "----------------onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "--------------onResume: ");
        quryDb();
    }

    private void initView() {
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
                            .setBackgroundDrawable(R.drawable.selector_red)
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
                    swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。

                    SwipeMenuItem closeItem = new SwipeMenuItem(mContext)
                            .setBackgroundDrawable(R.drawable.selector_purple)
                            .setImage(R.mipmap.ic_action_close)
                            .setWidth(width)
                            .setHeight(height);
                    //swipeRightMenu.addMenuItem(closeItem); // 添加一个按钮到右侧菜单。

                    SwipeMenuItem addItem = new SwipeMenuItem(mContext)
                            .setBackgroundDrawable(R.drawable.selector_green)
                            .setText("添加")
                            .setTextColor(Color.WHITE)
                            .setWidth(width)
                            .setHeight(height);
                    //swipeRightMenu.addMenuItem(addItem); // 添加一个按钮到右侧菜单。
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
                            try {
                                deleteDate(datas.get(adapterPosition).getUid());
                            } catch (Exception e) {
                                Log.e(TAG, "onItemClick: 删除失败");
                                e.printStackTrace();
                            }
                            datas.remove(adapterPosition);
                            mAdapter.notifyItemRemoved(adapterPosition);
                            break;
                    }
                } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
                    Toast.makeText(mContext, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast
                            .LENGTH_SHORT).show();
                }
            }
        });
        recyclerView.setLongPressDragEnabled(true);// 开启拖拽，就这么简单一句话。
        recyclerView.setOnItemMoveListener(new OnItemMoveListener() {
            @Override
            public boolean onItemMove(int fromPosition, int toPosition) {
                // 当Item被拖拽的时候。
                Collections.swap(datas, fromPosition, toPosition);
                mAdapter.notifyItemMoved(fromPosition, toPosition);
                return true;// 返回true表示处理了，返回false表示你没有处理。
            }

            @Override
            public void onItemDismiss(int position) {

            }
        });// 监听拖拽，更新UI。
        mAdapter = new ChatHistoryRecyclerViewAdapter(datas, this);
        //mAdapter.setRecyclerItemLongClickListener(this);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ChatHistoryActivity.this, ChatActivity.class);
        HistoryInfoBean historyInfoBean = datas.get(position);
        intent.putExtra("userEmail", historyInfoBean.getFrom());
        intent.putExtra("user_uid", historyInfoBean.getUid());
        intent.putExtra("user_photo", historyInfoBean.getPhoto());
        intent.putExtra("user_age", historyInfoBean.getAge());
        intent.putExtra("user_sex", historyInfoBean.getSex());
        intent.putExtra("mphoto", ChatHistoryActivity.this.getSharedPreferences("user_info", MODE_PRIVATE).getString
                ("head_url", ""));
        startActivity(intent);
    }

    public void onBack(View view) {
        finish();
    }

    private void deleteDate(String id) throws Exception {
        DataBaseManager.getInstance(this).deleteData(DBHelper.CHAT_HISTORY_TABLE, "uid=?", new String[]{id});
    }

    private void quryDb() {
        try {
            Cursor cursor = DataBaseManager.getInstance(this)
                    .queryData2Cursor("select * from " + DBHelper.CHAT_HISTORY_TABLE, null);
            if (datas.size() != 0) {
                datas.clear();
            }
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String from = cursor.getString(cursor.getColumnIndex("from_his"));
                String to = cursor.getString(cursor.getColumnIndex("to_his"));
                String data = cursor.getString(cursor.getColumnIndex("data_his"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String uid = cursor.getString(cursor.getColumnIndex("uid"));
                String photo = cursor.getString(cursor.getColumnIndex("photo"));
                String sex = cursor.getString(cursor.getColumnIndex("sex"));
                String age = cursor.getString(cursor.getColumnIndex("age"));
                String country = cursor.getString(cursor.getColumnIndex("country"));
                String city = cursor.getString(cursor.getColumnIndex("city"));
                HistoryInfoBean historyInfoBean = new HistoryInfoBean(id, from, to, data, type, time, uid, photo,
                        sex, age, country, city);
                datas.add(historyInfoBean);
            }
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// 查询本地聊天记录数据库

}
