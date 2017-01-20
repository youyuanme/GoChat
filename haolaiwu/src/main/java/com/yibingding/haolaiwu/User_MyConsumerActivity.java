package com.yibingding.haolaiwu;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.ybd.app.BaseActivity;
import com.ybd.app.tools.PreferenceHelper;
import com.ybd.app.tools.Tools;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.adapter.User_CustomerAdapter;
import com.yibingding.haolaiwu.dialog.User_SimpleDialog;
import com.yibingding.haolaiwu.domian.Customer;
import com.yibingding.haolaiwu.fragment.PullToRefreshFragment;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.tools.MyApplication;
import com.yibingding.haolaiwu.view.Dialog_Custom;
import com.yibingding.haolaiwu.view.Dialog_Custom.DialogClickListener;

@SuppressWarnings("rawtypes")
public class User_MyConsumerActivity extends BaseActivity implements
		OnClickListener {

	private TextView tab0, tab1, tab2, sum;
	private View tabimg0, tabimg1, tabimg2;
	private ViewPager viewPager;
	private List<Customer> list0 = new ArrayList<Customer>();
	private List<Customer> list1 = new ArrayList<Customer>();
	private List<Customer> list2;
	// BaseAdapter adapter0,adapter1,adapter2;
	private PullToRefreshFragment fragment0, fragment1, fragment2;
	private ProgressDialog dialog;
	private int countpage;
	private JSONArray managerjson;
	private Dialog DowndDialog;
	private int pagePosition = 1;
	private boolean isInitData = true;

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.user_myconsumer);
	}

	@Override
	public void initViews() {
		tab0 = (TextView) findViewById(R.id.tab0);
		tab1 = (TextView) findViewById(R.id.tab1);
		tab2 = (TextView) findViewById(R.id.tab2);
		tab0.setOnClickListener(this);
		tab1.setOnClickListener(this);
		tab2.setOnClickListener(this);
		tabimg0 = findViewById(R.id.img0);
		tabimg1 = findViewById(R.id.img1);
		tabimg2 = findViewById(R.id.img2);
		sum = (TextView) findViewById(R.id.sum);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
	}

	@Override
	public void initData() {
		final String usertype = PreferenceHelper.readString(this, "userinfo",
				"tStyle");
		if (usertype.equals("1")) {
			// tab2.setVisibility(View.GONE);
			tab1.setText("已审核");
			sum.setVisibility(View.GONE);
			((View) tab2.getParent()).setVisibility(View.GONE);
			countpage = 2;
		} else if (TextUtils.equals("2", usertype)) {
			tab0.setText("到访");
			tab1.setText("大定");
			tab2.setText("签约");
			list2 = new ArrayList<Customer>();
			countpage = 3;
		}
		fragment0 = new PullToRefreshFragment();
		fragment0.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				pagePosition = 1;
				list0.clear();
				list1.clear();
				if (list2 != null) {
					list2.clear();
				}
				if (countpage != 3) {
					getData("0", "");
				} else {
					getData("", "到访");
				}
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				pagePosition++;
				if (countpage != 3) {
					getData("0", "");
				} else {
					getData("", "到访");
				}
			}
		});
		User_CustomerAdapter adapter0 = new User_CustomerAdapter(
				User_MyConsumerActivity.this, list0, countpage, true);
		adapter0.setListener(this);
		fragment0.setAdapter(adapter0);

		fragment1 = new PullToRefreshFragment();
		fragment1.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				pagePosition = 1;
				list0.clear();
				list1.clear();
				if (list2 != null) {
					list2.clear();
				}
				if (countpage != 3) {
					getData("1", "");
				} else {
					getData("", "大定");
				}
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				pagePosition++;
				if (countpage != 3) {
					getData("1", "");
				} else {
					getData("", "大定");
				}
			}
		});
		User_CustomerAdapter adapter1 = new User_CustomerAdapter(
				User_MyConsumerActivity.this, list1, countpage);
		adapter1.setListener(this);
		fragment1.setAdapter(adapter1);

		if (countpage == 3) {
			fragment2 = new PullToRefreshFragment();
			fragment2.setOnRefreshListener(new OnRefreshListener2<ListView>() {
				@Override
				public void onPullDownToRefresh(
						PullToRefreshBase<ListView> refreshView) {
					pagePosition = 1;
					list0.clear();
					list1.clear();
					if (list2 != null) {
						list2.clear();
					}
					getData("", "签订协议");
				}

				@Override
				public void onPullUpToRefresh(
						PullToRefreshBase<ListView> refreshView) {
					pagePosition++;
					getData("", "签订协议");
				}
			});
			User_CustomerAdapter adapter2 = new User_CustomerAdapter(
					User_MyConsumerActivity.this, list2, 3); // 这个地方3就表示该界面有三个tab
			adapter2.setListener(this);
			fragment2.setAdapter(adapter2);
		}
		List<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(fragment0);
		fragments.add(fragment1);
		if (countpage == 3) {
			fragments.add(fragment2);
		}
		viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),
				fragments));
		// viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					if (sum.getVisibility() == View.VISIBLE) {
						sum.setText("共加载" + list0.size() + "个客户");
					}
					list0.clear();
					list1.clear();
					if (list2 != null) {
						list2.clear();
					}
					if (countpage != 3) {
						getData("0", "");
					} else {
						getData("", "到访");
					}
					tabimg0.setVisibility(View.VISIBLE);
					tabimg1.setVisibility(View.INVISIBLE);
					tabimg2.setVisibility(View.INVISIBLE);
					break;
				case 1:
					if (sum.getVisibility() == View.VISIBLE) {
						sum.setText("共加载" + list1.size() + "个客户");
					}
					list0.clear();
					list1.clear();
					if (list2 != null) {
						list2.clear();
					}
					if (countpage != 3) {
						getData("1", "");
					} else {
						getData("", "大定");
					}
					tabimg0.setVisibility(View.INVISIBLE);
					tabimg1.setVisibility(View.VISIBLE);
					tabimg2.setVisibility(View.INVISIBLE);
					break;
				case 2:
					if (sum.getVisibility() == View.VISIBLE) {
						sum.setText("共加载" + list2.size() + "个客户");
					}
					list0.clear();
					list1.clear();
					if (list2 != null) {
						list2.clear();
					}
					getData("", "签订协议");
					tabimg0.setVisibility(View.INVISIBLE);
					tabimg1.setVisibility(View.INVISIBLE);
					tabimg2.setVisibility(View.VISIBLE);
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
		if (countpage != 3) {
			getData("0", "");
		} else {
			getData("", "到访");
		}
	}

	public void back(View v) {
		finish();
	}

	@Override
	protected void onDestroy() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		if (DowndDialog != null && DowndDialog.isShowing()) {
			DowndDialog.dismiss();
		}
		super.onDestroy();
	}

	private void getData(String dstate, String statetype) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("dstate", dstate);
		params.put("statetype", statetype);
		params.put("usertype",
				PreferenceHelper.readString(this, "userinfo", "tStyle"));
		params.put("userGuid",
				PreferenceHelper.readString(this, "userinfo", "guid"));
		params.put("page", pagePosition + "");
		params.put("pagesize", MyApplication.pageSize);
		params.put("Token", AESUtils.encode("userGuid"));
		if (isInitData) {
			if (dialog == null) {
				dialog = new ProgressDialog(this);
				dialog.setCanceledOnTouchOutside(false);
				dialog.setMessage("正在努力加载中...");
			}
			dialog.show();
		}
		VolleyPost request = new VolleyPost(this, Constants.SERVER_IP
				+ Constants.User_GetClientListByPage_URL, params) {
			@Override
			public void pullJson(String json) {
				if (dialog != null && dialog.isShowing() && isInitData) {
					dialog.dismiss();
					isInitData = false;
				}
				if (json == null || json.equals("")) {
					Toast.makeText(User_MyConsumerActivity.this, "网络连接有问题！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				JSONArray array = JSONArray.parseArray(json);
				JSONObject obj = array.getJSONObject(0);
				if (obj.getString("state").equals("true")) {
					String v = obj.getString("result");
					List<Customer> total = JSON.parseArray(v, Customer.class);
					for (Customer c : total) {
						if (countpage == 2) {
							if (c.getT_Client_dState().equals("0")) {
								list0.add(c);
							} else {
								list1.add(c);
							}
						} else {
							if (c.getT_Client_dState().equals("0")
							/* || c.getT_Client_eState().equals("1") */) {
								continue;
							}
							if (c.getT_Client_sState().equals("0")) {
								list0.add(c);
							} else if (c.getT_Client_mState().equals("0")) {
								list1.add(c);
							} else /* if (c.getT_Client_eState().equals("0")) */{
								list2.add(c);
							} /*
							 * else if (c.getT_Client_eState().equals("1")) {
							 * list3.add(c);
							 * System.out.println("=======list3.toString()===" +
							 * list3.toString()); }
							 */
						}
					}
					System.out.println("===list0.toString()="
							+ list0.toString());
					System.out.println("=======list0.size()===" + list0.size());
					System.out.println("=======list1.toString()==="
							+ list1.toString());
					System.out.println("=======list1.size()===" + list1.size());
					if (countpage == 3) {
						System.out.println("=======list2.toString()==="
								+ list2.toString());
						System.out.println("=======list2.size()==="
								+ list2.size());
					}
					switch (viewPager.getCurrentItem()) {
					case 0:
						sum.setText("共加载" + list0.size() + "个客户");
						break;
					case 1:
						sum.setText("共加载" + list1.size() + "个客户");
						break;
					case 2:
						sum.setText("共加载" + list2.size() + "个客户");
						break;
					}
				} else {
					Tools.showToast(User_MyConsumerActivity.this,
							obj.getString("result"));
				}
				fragment0.datachange();
				fragment1.datachange();
				if (fragment2 != null) {
					fragment2.datachange();
				}
			}

			@Override
			public String getPageIndex() {
				return null;
			}
		};
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tab0:
			viewPager.setCurrentItem(0);
			break;
		case R.id.tab1:
			viewPager.setCurrentItem(1);
			break;
		case R.id.tab2:
			viewPager.setCurrentItem(2);
			break;
		case R.id.btn1:
			TextView v = (TextView) arg0;
			final Customer c1 = (Customer) v.getTag();
			if (v.getText().toString().contains("首访")) {
				final User_SimpleDialog dialog = new User_SimpleDialog(this);
				dialog.setTitleText("是否确认首访?");
				dialog.setConfirmListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Map<String, String> params1 = new HashMap<String, String>();
						params1.put("guid", c1.getGuid());
						params1.put("state", 0 + "");
						params1.put("Token", AESUtils.encode("guid"));
						editData(params1, Constants.SERVER_IP
								+ Constants.User_EditClientState_URL, "首访");
						dialog.dismiss();
					}
				});
				dialog.show();
			} else if (v.getText().toString().contains("查看")) {
				Map<String, String> params1 = new HashMap<String, String>();
				Intent intent = new Intent(this,
						User_MyRecommedDetailsActivity.class);
				intent.putExtra("customer", c1);
				startActivity(intent);
			}
			break;
		case R.id.btn2:
			TextView v1 = (TextView) arg0;
			final Customer c = (Customer) v1.getTag();
			if (v1.getText().toString().equals("未分配")) {
				// final Map<String, String> params = new HashMap<String,
				// String>();
				// params.put("guid", c.getGuid());
				getManager(c.getGuid());

			} else if (v1.getText().toString().equals("已分配")) {
				Dialog_Custom dialog_Custom = new Dialog_Custom();
				// dialog_Custom.showSelectDialog(this, "账号：" +
				// c.getConsultantLoginId() + "\n" + "姓名："
				// + c.getConsultantRealName(), new );

				// final User_SimpleDialog dialog = new User_SimpleDialog(this);
				// dialog.setTitleText("顾问信息");
				// dialog.setCancelText("账号：" + c.getConsultantLoginId() + "\n"
				// + "姓名："
				// + c.getConsultantRealName());
				// dialog.setConfirmListener(new OnClickListener() {
				// @Override
				// public void onClick(View arg0) {
				// // TODO Auto-generated method stub
				// dialog.dismiss();
				// }
				// });
				// dialog.show();

				dialog_Custom.showSelectDialog(
						this,
						"顾问信息",
						"账号：" + c.getConsultantLoginId() + "\n" + "姓名："
								+ c.getConsultantRealName(),
						new DialogClickListener() {

							@Override
							public void confirm(String result) {
								// TODO Auto-generated method stub
							}

							@Override
							public void cancel() {
								// TODO Auto-generated method stub
							}
						});
			} else if (v1.getText().toString().equals("客户到访")) {
				final User_SimpleDialog dialog = new User_SimpleDialog(this);
				dialog.setTitleText("是否确认客户到访?");
				dialog.setConfirmListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						TextView v = (TextView) arg0;
						final Map<String, String> params = new HashMap<String, String>();
						params.put("guid", c.getGuid());
						params.put("state", 1 + "");
						params.put("Token", AESUtils.encode("guid"));
						editData(params, Constants.SERVER_IP
								+ Constants.User_EditClientState_URL, "客户到访");
						dialog.dismiss();
					}
				});
				dialog.show();
			} else if (v1.getText().toString().equals("客户大定")) {
				final User_SimpleDialog dialog = new User_SimpleDialog(this);
				dialog.setTitleText("是否确认客户大定?");
				dialog.setConfirmListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						TextView v = (TextView) arg0;
						final Map<String, String> params = new HashMap<String, String>();
						params.put("guid", c.getGuid());
						params.put("state", 2 + "");
						params.put("Token", AESUtils.encode("guid"));
						editData(params, Constants.SERVER_IP
								+ Constants.User_EditClientState_URL, "客户大定");
						dialog.dismiss();
					}
				});
				dialog.show();
			} else if (v1.getText().toString().equals("客户签约")) {
				final User_SimpleDialog dialog = new User_SimpleDialog(this);
				dialog.setTitleText("是否确认客户签约?");
				dialog.setConfirmListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						final Map<String, String> params = new HashMap<String, String>();
						params.put("guid", c.getGuid());
						params.put("state", 3 + "");
						params.put("Token", AESUtils.encode("guid"));
						editData(params, Constants.SERVER_IP
								+ Constants.User_EditClientState_URL, "客户签约");
						dialog.dismiss();
					}
				});
				dialog.show();
			} else if (v1.getText().toString().equals("非首访")) {
				final User_SimpleDialog dialog = new User_SimpleDialog(this);
				dialog.setTitleText("是否确认非首访?");
				dialog.setConfirmListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Map<String, String> params1 = new HashMap<String, String>();
						params1.put("guid", c.getGuid());
						params1.put("state", 4 + "");
						params1.put("Token", AESUtils.encode("guid"));
						editData(params1, Constants.SERVER_IP
								+ Constants.User_EditClientState_URL, "非首访");
						dialog.dismiss();
					}
				});
				dialog.show();
			}
			break;
		}
	}

	public void getManager(final String guid) {
		if (managerjson != null) {
			selectManager(guid, managerjson);
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("userGuid",
				PreferenceHelper.readString(this, "userinfo", "guid"));
		params.put("Token", AESUtils.encode("userGuid"));
		if (dialog == null) {
			dialog = new ProgressDialog(this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("正在努力加载中...");
		}
		dialog.show();
		VolleyPost post = new VolleyPost(this, Constants.GetUserByManager_URL,
				params) {
			@Override
			public void pullJson(String json) {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				if (json == null || json.equals("")) {
					Tools.showToast(context, "获取业务顾问失败!");
					return;
				}
				JSONArray array = JSONArray.parseArray(json);
				JSONObject obj = array.getJSONObject(0);
				if (obj.getString("state").equals("true")) {
					managerjson = obj.getJSONArray("result");
					selectManager(guid, managerjson);
				} else {
					Tools.showToast(context, "获取业务顾问失败!");
				}
			}

			@Override
			public String getPageIndex() {
				return null;
			}
		};
	}

	public void selectManager(final String guid, JSONArray managerjson) {
		DowndDialog = new Dialog(this, R.style.DialogStyle);
		Window window = DowndDialog.getWindow();
		View v = getLayoutInflater().inflate(R.layout.dialog_selectmanager,
				null);
		v.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DowndDialog.dismiss();
			}
		});
		final View okview = v.findViewById(R.id.ok);
		okview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String guida = (String) v.getTag();
				if (TextUtils.isEmpty(guida)) {
					Tools.showToast(User_MyConsumerActivity.this, "请选择置业顾问");
					return;
				}
				DowndDialog.dismiss();
				final Map<String, String> params = new HashMap<String, String>();
				params.put("guid", guid);
				params.put("consultant", guida);
				params.put("Token", AESUtils.encode("guid"));
				editData(params, Constants.SERVER_IP
						+ Constants.User_EditClientConsultant_URL, "未分配");
			}
		});
		LinearLayout linearLayout = (LinearLayout) v
				.findViewById(R.id.itemcontainer);

		linearLayout.setOrientation(LinearLayout.VERTICAL);
		final int dim = (int) getResources().getDimension(R.dimen.padding10);
		LinearLayout.LayoutParams paramss = new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < managerjson.size(); i++) {
			JSONObject objt = managerjson.getJSONObject(i);
			TextView textView = new TextView(this);

			textView.setText(objt.getString("t_User_RealName")
					+ objt.getString("t_User_LoginId"));
			textView.setTag(objt.getString("Guid"));
			linearLayout.addView(textView, paramss);
			textView.setBackgroundResource(R.drawable.bg_bottom_divider);
			textView.setPadding(dim, (int) (dim * 1.8), dim, (int) (dim * 1.8));
			textView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// DowndDialog.dismiss();
					ViewGroup parent = (ViewGroup) v.getParent();
					for (int i = 0; i < parent.getChildCount(); i++) {
						View s = parent.getChildAt(i);
						if (v == s) {
							s.setBackgroundResource(R.color.manager_selected_color);
							s.setPadding(dim, (int) (dim * 1.8), dim,
									(int) (dim * 1.8));
						} else {
							s.setBackgroundResource(R.drawable.bg_bottom_divider);
							s.setPadding(dim, (int) (dim * 1.8), dim,
									(int) (dim * 1.8));
						}
					}
					okview.setTag(v.getTag());
				}
			});
		}
		// View b =
		// LayoutInflater.from(this).inflate(R.layout.allotdialog,null);
		// final EditText a = (EditText) v.findViewById(R.id.guid);
		// b.findViewById(R.id.submit).setOnClickListener(new OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// if(a.getText()==null||a.getText().toString().equals("")){
		// Toast.makeText(User_MyConsumerActivity.this, "请输入分配的账号",
		// Toast.LENGTH_SHORT).show();
		// return;
		// }
		// editData(params,
		// Constants.SERVER_IP+Constants.User_EditClientConsultant_URL);
		// }
		// });
		DowndDialog.setContentView(v);
		window.setWindowAnimations(R.style.inputanimation);
		window.setGravity(Gravity.BOTTOM);
		window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		DowndDialog.show();
	}

	public void editData(Map params, String url, final String type) {
		if (dialog == null) {
			dialog = new ProgressDialog(this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("正在努力加载中...");
		}
		dialog.show();
		VolleyPost request = new VolleyPost(this, url, params) {
			@Override
			public void pullJson(String json) {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				if (json == null || json.equals("")) {
					Toast.makeText(User_MyConsumerActivity.this, "网络连接有问题！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				JSONArray array = JSONArray.parseArray(json);
				JSONObject obj = array.getJSONObject(0);
				if (obj.getString("state").equals("true")) {
					pagePosition = 1;
					list1.clear();
					list0.clear();
					if (list2 != null) {
						list2.clear();
					}
					switch (type) {
					case "首访":
					case "非首访":
						getData("0", "");
						break;
					case "未分配":
						getData("1", "");
						break;
					case "客户到访":
						getData("", "到访");
						break;
					case "客户大定":
						getData("", "大定");
						break;
					case "客户签约":
						getData("", "签订协议");
						break;
					}
				} else {
					System.out.println("更新失败!!!!");
				}
				Toast.makeText(User_MyConsumerActivity.this,
						obj.getString("result"), Toast.LENGTH_SHORT).show();
			}

			@Override
			public String getPageIndex() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	class FragmentAdapter extends FragmentPagerAdapter {
		List<Fragment> fragments;

		public FragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragments.get(arg0);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

	}
}
