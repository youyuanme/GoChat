package com.sibozn.gochat.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.sibozn.gochat.R;
import com.sibozn.gochat.adapter.MyGifRecyclerViewAdapter;
import com.sibozn.gochat.bean.GifsBean;
import com.sibozn.gochat.loadmore.DefaultFootItem;
import com.sibozn.gochat.loadmore.OnLoadMoreListener;
import com.sibozn.gochat.loadmore.RecyclerViewWithFooter;
import com.sibozn.gochat.nohttp.CallServer;
import com.sibozn.gochat.nohttp.HttpListener;
import com.sibozn.gochat.utils.Constants;
import com.sibozn.gochat.utils.Tools;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.CacheMode;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchGIFSActivity extends BaseActivity implements View.OnClickListener, HttpListener<String>
        , TextView.OnEditorActionListener, MyGifRecyclerViewAdapter.MyItemOnClickListener {

    private static final int GET_GIFS_CODE = 100;
    private static final int SEND_GIF_URL_CODE = 101;

    @BindView(R.id.tv_return)
    TextView tvReturn;
    @BindView(R.id.tv_pick)
    TextView tvPick;
    @BindView(R.id.ed_search_content)
    EditText edSearchContent;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.tv_refresh)
    TextView tv_refresh;
    @BindView(R.id.rv_load_more)
    RecyclerViewWithFooter rv_load_more;

    private List<GifsBean> gifsBeens;
    private MyGifRecyclerViewAdapter myGifRecyclerViewAdapter;
    private int pageSize = 48;
    private int pagePositon = 1;
    private String search_content;
    private SharedPreferences sp;
    private String gifUrl;

    @Override
    public int getContentViewId() {
        return R.layout.activity_search_gifs;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        sp = getSharedPreferences("user_info", MODE_PRIVATE);
        initView();
        rv_load_more.setVisibility(View.INVISIBLE);
        SwipeBackHelper.onCreate(this);
//        pagePositon = 1;
//        toSearch(search_content, pageSize, pageSize * pagePositon, true, true);
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
        CallServer.getRequestInstance().cancelBySign(GET_GIFS_CODE);
        CallServer.getRequestInstance().cancelBySign(SEND_GIF_URL_CODE);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        edSearchContent.setSelection(edSearchContent.getText().length());
        rv_load_more.setHasFixedSize(true);
        //rv_load_more.setLayoutManager(new GridLayoutManager(this, 3));
        //rv_load_more.addItemDecoration(new DividerGridItemDecoration(this));
        rv_load_more.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
        rv_load_more.setItemAnimator(new DefaultItemAnimator());
        rv_load_more.setFootItem(new DefaultFootItem());//默认是这种
        rv_load_more.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                pagePositon++;
                toSearch(search_content, pageSize, pageSize * pagePositon, false, false);
            }
        });
        gifsBeens = new ArrayList<GifsBean>();
        myGifRecyclerViewAdapter = new MyGifRecyclerViewAdapter(gifsBeens, this);
        myGifRecyclerViewAdapter.setItemOnClickListener(this);
        rv_load_more.setAdapter(myGifRecyclerViewAdapter);

        tvReturn.setOnClickListener(this);
        tv_refresh.setOnClickListener(this);
        tvPick.setOnClickListener(this);
        ivClear.setOnClickListener(this);
        edSearchContent.setOnEditorActionListener(this);
        edSearchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    ivClear.setVisibility(View.VISIBLE);
                } else {
                    ivClear.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_return:
                finish();
                break;
            case R.id.tv_refresh:
                pagePositon = 1;
                toSearch(search_content, pageSize, pageSize * pagePositon, true, true);
                break;
            case R.id.tv_pick:
                Tools.showToast(this, "点击pick");
                break;
            case R.id.iv_clear:
                edSearchContent.setText("");
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND
                || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            ((InputMethodManager) edSearchContent.getContext().getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
            search_content = edSearchContent.getText().toString().trim();
            if (!"".equals(search_content)) {
                // TODO 搜索
                pagePositon = 1;
                toSearch(search_content, pageSize, pageSize * pagePositon, true, true);
            }
            return false;
        }
        return false;
    }

    //搜索内容
    private void toSearch(String search_content, int limit, int offset, boolean canCancel,
                          boolean isLoading) {
        Request<String> request = NoHttp.createStringRequest(Constants.GET_GIFS_URL + "api_key=13CjKAs11e6O1G"
                + "&q=" + search_content + "&limit=" + limit + "&offset=" + offset, RequestMethod.GET);
        if (request != null) {
            request.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);
            // 添加到请求队列
            CallServer.getRequestInstance().add(this, GET_GIFS_CODE, request, this, canCancel, isLoading);
        }
    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        int responseCode = response.getHeaders().getResponseCode();// 服务器响应码
        if (responseCode == 200) {
            switch (what) {
                case GET_GIFS_CODE:
                    Log.d(TAG, "onSucceed: --获取gifs-->>" + response.get());
                    rv_load_more.setVisibility(View.VISIBLE);
                    try {
                        JSONObject jsonObject = new JSONObject(response.get());
                        JSONObject jsonMeta = new JSONObject(jsonObject.getString("meta"));
                        String status = jsonMeta.getString("status");
                        if (TextUtils.equals("200", status)) {
                            JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                            if (jsonArray.length() == 0) {
                                rv_load_more.setEnd(getString(R.string.no_gifs_data));
                                return;
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json = jsonArray.getJSONObject(i);
                                JSONObject jsonFixed_height = new JSONObject(new JSONObject(json.getString("images"))
                                        .getString("downsized"));//original
                                GifsBean gifsBean = new GifsBean(json.getString("id")
                                        , jsonFixed_height.getString("width")
                                        , jsonFixed_height.getString("url")
                                        , jsonFixed_height.getString("height")
                                        , jsonFixed_height.getString("size"));
                                gifsBeens.add(gifsBean);
                            }
                            if (gifsBeens.size() == 0) {
                                tv_refresh.setVisibility(View.VISIBLE);
                                rv_load_more.setVisibility(View.INVISIBLE);
                            } else {
                                tv_refresh.setVisibility(View.INVISIBLE);
                                rv_load_more.setVisibility(View.VISIBLE);
                            }
                            Log.d(TAG, "downsized--gif图片数据大小---->>" + gifsBeens.size());
                            myGifRecyclerViewAdapter.notifyDataSetChanged();

                        }
                        // Toast.makeText(this, jsonMeta.getString("msg"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case SEND_GIF_URL_CODE:// 上传gif图片成功
                    Log.d(TAG, "onSucceed: --gif图片上传成功->>" + response.get());
                    try {
                        JSONObject jsonObject = new JSONObject(response.get());
                        Toast.makeText(this, jsonObject.getString("desc"), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.putExtra("head_gif_url", gifUrl);
                        setResult(RESULT_OK, intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }

        }
    }

    @Override
    public void onFailed(int what, Response<String> response) {
        Toast.makeText(this, "登录失败！", Toast.LENGTH_LONG).show();
        Log.e(TAG, "onFailed:" + response.getException().getMessage());
    }

    @Override
    public void onItemClickListenerPositon(int postion) {// 发送gif地址给服务器
        Log.e(TAG, "onItemClickListenerPositon:---------- " + postion);
        gifUrl = gifsBeens.get(postion).getFixed_height_url();
        Request<String> request = NoHttp.createStringRequest(Constants.RET_HEAD_URL, RequestMethod.POST);
        request.add("email", sp.getString("email", ""));
        request.add("token", sp.getString("token", ""));
        request.add("head", gifUrl);
        CallServer.getRequestInstance().add(this, SEND_GIF_URL_CODE, request, this, true, true);
    }

}
