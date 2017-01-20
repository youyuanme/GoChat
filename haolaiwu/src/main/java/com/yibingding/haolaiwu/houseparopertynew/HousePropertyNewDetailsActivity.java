package com.yibingding.haolaiwu.houseparopertynew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.ybd.app.BaseActivity;
import com.ybd.app.interf.GetDataSuccessListener;
import com.ybd.app.tools.Tools;
import com.ybd.app.views.MyListView;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.User_LoginActivity;
import com.yibingding.haolaiwu.adapter.NewsDetails_Adapter;
import com.yibingding.haolaiwu.dialog.Dialognetwork;
import com.yibingding.haolaiwu.domian.CommentBean;
import com.yibingding.haolaiwu.internet.Comment_Volley;
import com.yibingding.haolaiwu.internet.GetNewsDetails_Volley;
import com.yibingding.haolaiwu.internet.Return_result_Volley;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.AutoShareSDK;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.tools.MyApplication;
import com.yibingding.haolaiwu.view.NewPopuwindow;
import com.yibingding.haolaiwu.view.NewPopuwindow.NewPopuwindowOnClickListener;

public class HousePropertyNewDetailsActivity extends BaseActivity implements
		OnClickListener, GetDataSuccessListener, NewPopuwindowOnClickListener {

	private ImageView iv_new_back, iv_new_right;
	private NewPopuwindow popu;
	public final static int popuShoucang = 0;
	public final static int popuFenxinag = 1;
	private WebView wv_new;
	private PullToRefreshScrollView new_details_scrollView;
	private MyListView new_details_myListView;
	private int pageIndex = 1;
	private boolean loadingWeb = true;
	private String guidString;
	private List<CommentBean> list_newsCommentBeans;
	private NewsDetails_Adapter newsDetails_Adapter;
	private View webView;
	private String isshoucang;
	private String shoucangguid;
	private EditText et_news_send_content;
	private TextView tv_news_send;
	private String uerGuid;
	private Dialog sharedialog;
	private String downLoadAppURL = "";
	private Dialognetwork dialog;

	@Override
	public void onCreateThisActivity() {
		ShareSDK.initSDK(this);
		setContentView(R.layout.activity_house_property_new_details);
		list_newsCommentBeans = new ArrayList<CommentBean>();
		downLoadAppURL = getSharedPreferences("userinfo", Context.MODE_PRIVATE)
				.getString("downLoadAppURL", "");
		if (TextUtils.isDigitsOnly(downLoadAppURL)) {
			downLoadAppURL = "www.baidu.com";
		}
	}

	@Override
	public void initViews() {
		iv_new_back = (ImageView) findViewById(R.id.iv_new_back);
		iv_new_right = (ImageView) findViewById(R.id.iv_new_right);
		et_news_send_content = (EditText) findViewById(R.id.et_news_send_content);
		tv_news_send = (TextView) findViewById(R.id.tv_news_send);

		wv_new = (WebView) findViewById(R.id.wv_new);
		new_details_scrollView = (PullToRefreshScrollView) findViewById(R.id.new_details_scrollView);
		new_details_myListView = (MyListView) findViewById(R.id.new_details_myListView);
		new_details_scrollView.setMode(Mode.BOTH);
		new_details_scrollView
				.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						pageIndex = 1;
						if (list_newsCommentBeans != null
								&& !list_newsCommentBeans.isEmpty()) {
							list_newsCommentBeans.clear();
						}
						onResume();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						pageIndex++;
						onResume();
					}
				});

		guidString = getIntent().getStringExtra("Guid");

		popu = new NewPopuwindow(this);
		popu.setNewPopuwindowOnClickListener(this);
		iv_new_back.setOnClickListener(this);
		tv_news_send.setOnClickListener(this);
		iv_new_right.setOnClickListener(this);

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		getDownLoadUrl();
	}

	private void show() {
		if (dialog == null) {
			dialog = new Dialognetwork(this);
		}
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

	private void dismiss() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		uerGuid = getSharedPreferences("userinfo", Context.MODE_PRIVATE)
				.getString("guid", "");
		Map<String, String> map = new HashMap<String, String>();
		map.put("page", pageIndex + "");
		map.put("pageSize", MyApplication.pageSize);
		map.put("associateGuid", guidString);
		map.put("token", AESUtils.encode("associateGuid").replaceAll("\n", ""));
		GetNewsDetails_Volley list_Volley = new GetNewsDetails_Volley(this,
				Constants.NEW_COMMENT_URL, map, "1", pageIndex);
		list_Volley.setOnGetDataSuccessListener(this);

		if (loadingWeb) {
			show();
			loadingWeb = false;
			wv_new.loadUrl(Constants.NEW_DETAILS_URL + guidString);
			Log.e("==========", "====url===" + Constants.NEW_DETAILS_URL
					+ guidString);
			wv_new.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					// view.loadUrl(url);
					return true;
				}

				@Override
				public void onPageFinished(WebView view, String url) {
					// TODO Auto-generated method stub
					dismiss();
					super.onPageFinished(view, url);
				}

				@Override
				public void onReceivedError(WebView view, int errorCode,
						String description, String failingUrl) {
					// TODO Auto-generated method stub
					Log.e("-----", "---------" + description + "========="
							+ failingUrl);
					dismiss();
					super.onReceivedError(view, errorCode, description,
							failingUrl);
				}

			});
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		pageIndex = 1;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (popu.isShowing()) {
			popu.dismiss();
		}
		if (sharedialog != null && sharedialog.isShowing()) {
			sharedialog.dismiss();
		}
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		switch (v.getId()) {
		case R.id.iv_new_back:
			finish();
			break;
		case R.id.tv_news_send:
			if (TextUtils.isEmpty(uerGuid)) {
				// Tools.showToast(this, "请登录!");
				Intent intent = new Intent(this, User_LoginActivity.class);
				startActivity(intent);
				break;
			}
			String sendCentent = et_news_send_content.getText().toString()
					.trim();
			if (!TextUtils.isEmpty(sendCentent)) {
				map.put("fromUserGuid", uerGuid);
				map.put("fromContent", sendCentent);
				map.put("associateGuid", guidString);
				map.put("Token",
						AESUtils.encode("fromUserGuid").replaceAll("\n", ""));
				Comment_Volley comment_Volley = new Comment_Volley(this,
						Constants.NEW_COMMENTADD_URL, map, "4");
				comment_Volley.setOnGetDataSuccessListener(this);
			} else {
				Tools.showToast(this, getString(R.string.input_comment_content));
			}
			break;
		case R.id.iv_new_right:
			popu.showAsDropDown(iv_new_right, 0, -12);
			if (TextUtils.isEmpty(uerGuid)) {
				break;
			}
			map.put("collectionGuid", guidString);
			map.put("userGuid", uerGuid);
			map.put("Token",
					AESUtils.encode("collectionGuid").replaceAll("\n", ""));
			Return_result_Volley result_Volley = new Return_result_Volley(this,
					Constants.NEW_ISCOLLECTION_URL, map, "3");
			result_Volley.setOnGetDataSuccessListener(this);
			break;

		case R.id.selectimg_option1:
			// String imageUrlString = Constants.IMAGE_URL +
			// getIntent().getStringExtra("T_News_Pic");
			// System.out.println("==========imageUrlString===="+imageUrlString);
			// String titleString = getIntent().getStringExtra("T_News_Title");
			// System.out.println("=========titleString==="+titleString);
			AutoShareSDK.wechat2(
					this,
					getIntent().getStringExtra("T_News_Title"),
					Constants.NEW_SHARD + guidString,
					getIntent().getStringExtra("T_News_Contents"),
					Constants.IMAGE_URL
							+ getIntent().getStringExtra("T_News_Pic"),
					 R.drawable.ic_launcher , Wechat.NAME);
			if (sharedialog != null && sharedialog.isShowing()) {
				sharedialog.dismiss();
			}
			break;
		case R.id.selectimg_option2:
			AutoShareSDK.wechat2(
					this,
					getIntent().getStringExtra("T_News_Title"),
					Constants.NEW_SHARD + guidString,
					getIntent().getStringExtra("T_News_Contents"),
					Constants.IMAGE_URL
							+ getIntent().getStringExtra("T_News_Pic"),
					/* R.drawable.ic_launcher */-1, WechatMoments.NAME);
			if (sharedialog != null && sharedialog.isShowing()) {
				sharedialog.dismiss();
			}
			break;
		}
	}

	@Override
	public void onPopuwindowOnClick(int type) {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(uerGuid)) {
			// Tools.showToast(this, "请登录!");
			Intent intent = new Intent(this, User_LoginActivity.class);
			startActivity(intent);
			return;
		}
		switch (type) {
		case 0:// 收藏
			if ("是".equals(isshoucang)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("collectionGuid", shoucangguid);
				map.put("Token",
						AESUtils.encode("collectionGuid").replaceAll("\n", ""));
				Return_result_Volley result_Volley = new Return_result_Volley(
						this, Constants.NEW_DELISCOLLECTION_URL, map, "2");
				result_Volley.setOnGetDataSuccessListener(this);
			} else {
				Map<String, String> map = new HashMap<String, String>();
				map.put("collectionGuid", guidString);
				map.put("userGuid", uerGuid);
				map.put("remark", "新闻");
				map.put("Token",
						AESUtils.encode("collectionGuid").replaceAll("\n", ""));
				Return_result_Volley result_Volley = new Return_result_Volley(
						this, Constants.NEW_ADDISCOLLECTION_URL, map, "2");
				result_Volley.setOnGetDataSuccessListener(this);
			}
			break;

		case 1:
			share();
			break;

		}
	}

	@Override
	public void onGetDataSuccess(String tag, Object obj) {
		new_details_scrollView.onRefreshComplete();
		if (obj != null) {
			if (TextUtils.equals("1", tag)) {
				if (pageIndex == 1) {
					list_newsCommentBeans = (List<CommentBean>) obj;
					newsDetails_Adapter = new NewsDetails_Adapter<CommentBean>(
							this, list_newsCommentBeans, null);
					new_details_myListView.setAdapter(newsDetails_Adapter);
				} else {
					List<CommentBean> arrayList = (List<CommentBean>) obj;
					list_newsCommentBeans.addAll(arrayList);
				}
				newsDetails_Adapter.notifyDataSetChanged();
			} else if (TextUtils.equals("3", tag)) {// 判断是否收藏
				Map<String, String> map = (Map<String, String>) obj;
				if ("true".equals(map.get("state"))) {
					isshoucang = map.get("IfCollection");
					shoucangguid = map.get("Guid");
					popu.setShoucangImage(isshoucang);
				} else {
					String result = map.get("result");
					Tools.showToast(this, result);
				}
			} else if (TextUtils.equals("2", tag)) {// 取消/收藏
				Map<String, String> map = (Map<String, String>) obj;
				if ("true".equals(map.get("state"))) {
					if ("否".equals(map.get("IfCollection"))) {
						Tools.showToast(this, this
								.getString(R.string.news_quxaioshouchagncheng));
					} else {
						Tools.showToast(this,
								this.getString(R.string.news_shoucangchenggong));
					}
				} else {
					String result = map.get("result");
					Tools.showToast(this, result);
				}
			} else if (TextUtils.equals("4", tag)) {// 评论
				Map<String, String> map = (Map<String, String>) obj;
				if (TextUtils.equals(map.get("state"), "true")) {
					et_news_send_content.setText("");
					onResume();
				}
				Tools.showToast(this, map.get("result"));
			}
		}
	}

	public void share() {
		// AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Dialog dialog = builder.setMessage("确定要删除么").setTitle("标题").create();
		// android.R.style.Animation_InputMethod
		sharedialog = new Dialog(this, R.style.simpledialog);
		Window window = sharedialog.getWindow();
		View view = this.getLayoutInflater()
				.inflate(R.layout.sharedialog, null);

		sharedialog.setContentView(view);
		view.findViewById(R.id.selectimg_option1).setOnClickListener(this);
		view.findViewById(R.id.selectimg_option2).setOnClickListener(this);

		view.findViewById(R.id.cancel).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						sharedialog.dismiss();
					}
				});
		sharedialog.setContentView(view);
		window.setWindowAnimations(R.style.inputanimation);
		window.setGravity(Gravity.BOTTOM);
		window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		sharedialog.show();

	}

	private void getDownLoadUrl() {
		Map<String, String> params1 = new HashMap<String, String>();
		params1.put("Token", AESUtils.encode("Token"));
		VolleyPost post1 = new com.ybd.app.volley.VolleyPost(this,
				Constants.GET_DOWN_LOAD_SRC, params1) {
			@Override
			public void pullJson(String json) {
				if (TextUtils.isEmpty(json)) {
					return;
				}
				try {
					downLoadAppURL = new JSONArray(json).getJSONObject(0)
							.getString("result");
					SharedPreferences preference = context
							.getSharedPreferences("userinfo",
									Context.MODE_PRIVATE);
					preference.edit()
							.putString("downLoadAppURL", downLoadAppURL)
							.commit();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public String getPageIndex() {
				return null;
			}
		};
	}
}
