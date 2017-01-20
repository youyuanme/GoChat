package com.yibingding.haolaiwu.houseparopertynew;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.ybd.app.BaseActivity;
import com.ybd.app.interf.GetDataSuccessListener;
import com.ybd.app.tools.Tools;
import com.ybd.app.views.MyListView;
import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.adapter.Comment_Adapter;
import com.yibingding.haolaiwu.adapter.NewsDetails_Adapter;
import com.yibingding.haolaiwu.domian.CommentBean;
import com.yibingding.haolaiwu.internet.Comment_Volley;
import com.yibingding.haolaiwu.internet.GetNewsDetails_Volley;
import com.yibingding.haolaiwu.internet.GetNews_Volley;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.tools.MyApplication;

public class CommentListActivity extends BaseActivity implements
		OnClickListener, GetDataSuccessListener {

	private ImageView iv_new_back;
	private PullToRefreshScrollView news_comment_scrollView;
	private MyListView news_comment_myListView;
	private int pageIndex = 1;
	private List<CommentBean> list_newsCommentBeans;
	private Comment_Adapter newsDetails_Adapter;
	private String associateGuid;
	private EditText et_news_comment_send_content;
	private TextView tv_news_comment_send;

	@Override
	public void onCreateThisActivity() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_comment_list);
		associateGuid = getIntent().getStringExtra("associateGuid");
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		iv_new_back = (ImageView) findViewById(R.id.iv_new_back);
		news_comment_scrollView = (PullToRefreshScrollView) findViewById(R.id.news_comment_scrollView);
		news_comment_myListView = (MyListView) findViewById(R.id.news_comment_myListView);
		et_news_comment_send_content = (EditText) findViewById(R.id.et_news_comment_send_content);
		tv_news_comment_send = (TextView) findViewById(R.id.tv_news_comment_send);
		news_comment_scrollView.setMode(Mode.BOTH);
		news_comment_scrollView
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
		iv_new_back.setOnClickListener(this);
		tv_news_comment_send.setOnClickListener(this);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		map.put("page", pageIndex + "");
		map.put("pageSize", MyApplication.pageSize);
		map.put("associateGuid", associateGuid);
		map.put("Token", AESUtils.encode("associateGuid").replaceAll("\n", ""));
		GetNewsDetails_Volley list_Volley = new GetNewsDetails_Volley(this,
				Constants.NEW_ABOUTCOMMENT_URL, map, "0");
		list_Volley.setOnGetDataSuccessListener(this);
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		pageIndex = 1;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_new_back:
			finish();
			break;
		case R.id.tv_news_comment_send:
			String contentsString = et_news_comment_send_content.getText()
					.toString().trim();
			if (TextUtils.isEmpty(contentsString)) {
				Tools.showToast(this, getString(R.string.input_comment_content));
				break;
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("fromUserGuid",
					getSharedPreferences("userinfo", Context.MODE_PRIVATE)
							.getString("guid", ""));
			map.put("fromContent", contentsString);
			map.put("associateGuid", associateGuid);
			map.put("Token",
					AESUtils.encode("fromUserGuid").replaceAll("\n", ""));
			Comment_Volley comment_Volley = new Comment_Volley(this,
					Constants.NEW_COMMENTADD_URL, map, "1");
			comment_Volley.setOnGetDataSuccessListener(this);
			break;
		}
	}

	@Override
	public void onGetDataSuccess(String tag, Object obj) {
		// TODO Auto-generated method stub
		news_comment_scrollView.onRefreshComplete();
		if (obj != null) {
			if ("0".equals(tag)) {
				if (pageIndex == 1) {
					list_newsCommentBeans = (List<CommentBean>) obj;
					if (!list_newsCommentBeans.isEmpty()) {
						newsDetails_Adapter = new Comment_Adapter<CommentBean>(
								this, list_newsCommentBeans, null);
						news_comment_myListView.setAdapter(newsDetails_Adapter);
					}
				} else {
					List<CommentBean> arrayList = (List<CommentBean>) obj;
					list_newsCommentBeans.addAll(arrayList);
				}
				newsDetails_Adapter.notifyDataSetChanged();
			} else if ("1".equals(tag)) {
				Map<String, String> map = (Map<String, String>) obj;
				Tools.showToast(this, map.get("result"));
				onResume();
			}
		}
	}

}
