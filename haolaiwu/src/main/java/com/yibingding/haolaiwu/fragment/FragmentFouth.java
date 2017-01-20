package com.yibingding.haolaiwu.fragment;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.ybd.app.BaseFragment;
import com.ybd.app.interf.GetDataSuccessListener;
import com.ybd.app.tools.DensityUtils;
import com.ybd.app.tools.PreferenceHelper;
import com.ybd.app.tools.Tools;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.FangdaijisuanqiActivity;
import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.User_AccountMsgActivity;
import com.yibingding.haolaiwu.User_LoginActivity;
import com.yibingding.haolaiwu.User_MsgCenterActivity;
import com.yibingding.haolaiwu.User_MyAppraisalActivity;
import com.yibingding.haolaiwu.User_MyBankActivity;
import com.yibingding.haolaiwu.User_MyCommissionActivity;
import com.yibingding.haolaiwu.User_MyConsumerActivity;
import com.yibingding.haolaiwu.User_MyFollowActivity;
import com.yibingding.haolaiwu.User_MyRecommedActivity;
import com.yibingding.haolaiwu.User_MyScoreActivity;
import com.yibingding.haolaiwu.User_MyTeamActivity;
import com.yibingding.haolaiwu.User_RegistActivity;
import com.yibingding.haolaiwu.User_SysSettingsActivity;
import com.yibingding.haolaiwu.WoyaotuijianActivity;
import com.yibingding.haolaiwu.internet.Comment_Volley;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.AutoShareSDK;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.tools.MyUtils;
import com.yibingding.haolaiwu.tools.UserImageTools;

/*我的*/
public class FragmentFouth extends BaseFragment implements OnClickListener {
	private String userid = "";
	private String avatarurl = "";
	private String downLoadAppURL = "";
	private TextView name;
	private View unlogin;
	private ImageView avatar;
	private TextView recommed;
	private View team, bankno;
	private Dialog sharedialog;
	private View scorecommission, line, a;
	private LinearLayout ll_woyaotuijian;
	private ImageView image_view_red_round;

	@Override
	public View onCreateThisFragment(LayoutInflater inflater,
			ViewGroup container) {
		ShareSDK.initSDK(getActivity());
		String savepath = PreferenceHelper.readString(getActivity(),
				"userinfo", "picsavepath");
		downLoadAppURL = getActivity().getSharedPreferences("userinfo",
				Context.MODE_PRIVATE).getString("downLoadAppURL", "");
		if (savepath == null || savepath.equals("")
				|| (!new File(savepath).exists())) {
			File path = getActivity().getExternalCacheDir();
			if (path == null) {
				path = getActivity().getCacheDir();
			}
			savepath = path.getAbsolutePath() + "/";
			PreferenceHelper.write(getActivity(), "userinfo", "picsavepath",
					savepath);
		}
		return inflater.inflate(R.layout.fragment_fouth, null);

	}

	@Override
	public void initViews(View view) {
		view.findViewById(R.id.login).setOnClickListener(this);
		view.findViewById(R.id.regist).setOnClickListener(this);
		view.findViewById(R.id.my_score).setOnClickListener(this);
		view.findViewById(R.id.my_commission).setOnClickListener(this);
		view.findViewById(R.id.account_msg).setOnClickListener(this);
		image_view_red_round = (ImageView) view
				.findViewById(R.id.image_view_red_round);
		ll_woyaotuijian = (LinearLayout) view
				.findViewById(R.id.ll_woyaotuijian);
		ll_woyaotuijian.setOnClickListener(this);
		view.findViewById(R.id.msg_center).setOnClickListener(this);
		view.findViewById(R.id.my_recommed).setOnClickListener(this);
		view.findViewById(R.id.bankno).setOnClickListener(this);
		view.findViewById(R.id.my_team).setOnClickListener(this);
		view.findViewById(R.id.my_follow).setOnClickListener(this);
		view.findViewById(R.id.my_appraisal).setOnClickListener(this);
		view.findViewById(R.id.onkeyshare).setOnClickListener(this);
		view.findViewById(R.id.sys_settings).setOnClickListener(this);
		view.findViewById(R.id.onfangdaijisuan).setOnClickListener(this);
		team = view.findViewById(R.id.my_team);
		bankno = view.findViewById(R.id.bankno);
		recommed = (TextView) view.findViewById(R.id.my_recommedtextview);
		avatar = (ImageView) view.findViewById(R.id.avatar);
		unlogin = view.findViewById(R.id.unloginview);
		name = (TextView) view.findViewById(R.id.name);
		scorecommission = view.findViewById(R.id.layout2);
		a = view.findViewById(R.id.layout3);
		line = view.findViewById(R.id.line);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.user_msgback);
		int h = (int) (DensityUtils.getScreenW(getActivity()) * (bitmap
				.getHeight() * 1.0 / bitmap.getWidth()));
		View s = view.findViewById(R.id.account_msg);
		ViewGroup.LayoutParams params = s.getLayoutParams();
		params.height = h;
		s.setLayoutParams(params);
	}

	@Override
	public void initData() {
	}

	@Override
	public void onResume() {
		getDownLoadUrl();
		String guid = PreferenceHelper.readString(getActivity(), "userinfo",
				"guid");
		if (!TextUtils.isEmpty(guid)) {
			getIsRead(guid);
		}
		if (TextUtils.isEmpty(guid)) {
			avatar.setImageResource(R.drawable.user_avatar);
		}
		String id = PreferenceHelper.readString(getActivity(), "userinfo",
				"userid");

		if (id == null || id.equals("")) {
			name.setVisibility(View.GONE);
			unlogin.setVisibility(View.VISIBLE);
			recommed.setText("我的推荐");
			team.setVisibility(View.VISIBLE);
			userid = "";
			avatarurl = "";
			avatar.setImageResource(R.drawable.user_avatar);
			super.onResume();
			return;
		}
		if (!TextUtils.isEmpty(PreferenceHelper.readString(getActivity(),
				"userinfo", "guid"))) {
			name.setVisibility(View.VISIBLE);
			unlogin.setVisibility(View.GONE);
			// recommed.setText("我的推荐");
			userid = id;
			String type = PreferenceHelper.readString(getActivity(),
					"userinfo", "tStyle");
			int t = Integer.parseInt(type);
			if (t == 1 || t == 2) {
				ll_woyaotuijian.setVisibility(View.GONE);
				team.setVisibility(View.GONE);
				bankno.setVisibility(View.GONE);
				recommed.setText("我的客户");
				scorecommission.setVisibility(View.GONE);
				RelativeLayout.LayoutParams params = (LayoutParams) a
						.getLayoutParams();
				params.addRule(RelativeLayout.CENTER_VERTICAL);
				a.setLayoutParams(params);
				line.setVisibility(View.GONE);
			} else {
				ll_woyaotuijian.setVisibility(View.VISIBLE);
				bankno.setVisibility(View.VISIBLE);
				team.setVisibility(View.VISIBLE);
				recommed.setText("我的推荐");
				scorecommission.setVisibility(View.VISIBLE);
				line.setVisibility(View.VISIBLE);
				RelativeLayout.LayoutParams params = (LayoutParams) a
						.getLayoutParams();
				params.addRule(RelativeLayout.CENTER_VERTICAL, 0);
				a.setLayoutParams(params);
			}
		}
		String n = PreferenceHelper.readString(getActivity(), "userinfo",
				"nickname");
		if (n != null && n.length() > 0) {
			name.setText(n);
		} else {
			name.setText(id);
		}
		String picurl = PreferenceHelper.readString(getActivity(), "userinfo",
				"avatar");
		picurl = (picurl == null ? "" : picurl);
		if (!avatarurl.equals(picurl)) {
			String savepath = PreferenceHelper.readString(getActivity(),
					"userinfo", "picsavepath");
			String userid = PreferenceHelper.readString(getActivity(),
					"userinfo", "userid");
			File file = new File(savepath + userid + "_pic.jpg");
			if (savepath != null && (!savepath.equals(""))) {
				if (file.exists()) {
					Bitmap bitmap = BitmapFactory.decodeFile(file
							.getAbsolutePath());
					avatar.setImageBitmap(UserImageTools.createCircleImage(
							bitmap, bitmap.getWidth()));
					if (!bitmap.isRecycled()) {
						bitmap.recycle();
					}
				} else if (!picurl.equals("")) {
					loadAvatar(Constants.IMAGE_URL + picurl, file);
				}
			} else if (!picurl.equals("")) {
				loadAvatar(Constants.IMAGE_URL + picurl, file);
			}
			avatarurl = picurl;
		}
		super.onResume();
	}

	private void getIsRead(String userGuid) {
		Map<String, String> params1 = new HashMap<String, String>();
		params1.put("userGuid", userGuid);
		params1.put("Token", AESUtils.encode("userGuid"));
		VolleyPost post1 = new com.ybd.app.volley.VolleyPost(getActivity(),
				Constants.GET_IS_READ_SRC, params1) {
			@Override
			public void pullJson(String json) {
				if (TextUtils.isEmpty(json)) {
					return;
				}
				try {
					String state = new JSONArray(json).getJSONObject(0)
							.getString("state");
					String result = new JSONArray(json).getJSONObject(0)
							.getString("result");
					if (TextUtils.equals("0", result)) {
						image_view_red_round.setVisibility(View.GONE);
					} else if (TextUtils.equals("1", result)) {
						image_view_red_round.setVisibility(View.VISIBLE);
					}
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

	private void getDownLoadUrl() {
		Map<String, String> params1 = new HashMap<String, String>();
		params1.put("Token", AESUtils.encode("Token"));
		VolleyPost post1 = new com.ybd.app.volley.VolleyPost(getActivity(),
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

	@Override
	public void onDestroyView() {
		if (sharedialog != null && sharedialog.isShowing()) {
			sharedialog.dismiss();
		}
		super.onDestroyView();
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.login:
			intent = new Intent(getActivity(), User_LoginActivity.class);
			break;
		case R.id.regist:
			intent = new Intent(getActivity(), User_RegistActivity.class);
			break;
		case R.id.onfangdaijisuan:
			Map<String, String> map = new HashMap<String, String>();
			map.put("Token", AESUtils.encode("Token"));
			Comment_Volley comment_Volley = new Comment_Volley(getActivity(),
					Constants.GET_Counter, map);
			comment_Volley
					.setOnGetDataSuccessListener(new GetDataSuccessListener() {
						@Override
						public void onGetDataSuccess(String tag, Object obj) {
							if (obj != null) {
								Map<String, String> map = (Map<String, String>) obj;
								if (TextUtils.equals(map.get("state"), "true")) {
									Intent intent = new Intent(getActivity(),
											FangdaijisuanqiActivity.class);
									intent.putExtra("url", map.get("result"));
									startActivity(intent);
								} else {
									Tools.showToast(getActivity(),
											map.get("result"));
									System.out.println("获取房贷计算器url出错");
								}
							}
						}
					});
			break;
		case R.id.sys_settings:
			intent = new Intent(getActivity(), User_SysSettingsActivity.class);
			break;
		case R.id.onkeyshare:
			share();
			break;
		case R.id.selectimg_option1:
			if (TextUtils.isEmpty(downLoadAppURL)) {
				MyUtils.showToast(getActivity(), "暂无法获取分享地址，请稍后重试!");
				return;
			}
			AutoShareSDK.wechat2(getActivity(), this
					.getString(R.string.app_name), downLoadAppURL, this
					.getResources().getString(R.string.please_use),
					Environment.getExternalStorageDirectory()
							+ "/haolaiwu/logo", R.drawable.ic_launcher,
					Wechat.NAME);
			if (sharedialog != null && sharedialog.isShowing()) {
				sharedialog.dismiss();
			}
			break;
		case R.id.selectimg_option2:
			if (TextUtils.isEmpty(downLoadAppURL)) {
				MyUtils.showToast(getActivity(), "暂无法获取分享地址，请稍后重试!");
				return;
			}
			AutoShareSDK.wechat2(getActivity(), this
					.getString(R.string.app_name), downLoadAppURL, this
					.getResources().getString(R.string.please_use),
					Environment.getExternalStorageDirectory()
							+ "/haolaiwu/logo", R.drawable.ic_launcher,
					WechatMoments.NAME);
			if (sharedialog != null && sharedialog.isShowing()) {
				sharedialog.dismiss();
			}
			break;
		case R.id.ll_woyaotuijian:
			if (TextUtils.isEmpty(userid)) {
				intent = new Intent(getActivity(), User_LoginActivity.class);
				break;
			}
			intent = new Intent(getActivity(), WoyaotuijianActivity.class);
			break;
		case R.id.my_score:
			if (TextUtils.isEmpty(userid)) {
				intent = new Intent(getActivity(), User_LoginActivity.class);
				break;
			}
			intent = new Intent(getActivity(), User_MyScoreActivity.class);
			break;
		case R.id.my_commission:
			if (TextUtils.isEmpty(userid)) {
				intent = new Intent(getActivity(), User_LoginActivity.class);
				break;
			}
			intent = new Intent(getActivity(), User_MyCommissionActivity.class);
			break;
		case R.id.account_msg:
			if (TextUtils.isEmpty(userid)) {
				intent = new Intent(getActivity(), User_LoginActivity.class);
				break;
			}
			intent = new Intent(getActivity(), User_AccountMsgActivity.class);
			break;
		case R.id.msg_center:
			if (TextUtils.isEmpty(userid)) {
				intent = new Intent(getActivity(), User_LoginActivity.class);
				break;
			}
			intent = new Intent(getActivity(), User_MsgCenterActivity.class);
			break;
		case R.id.my_recommed:
			if (TextUtils.isEmpty(userid)) {
				intent = new Intent(getActivity(), User_LoginActivity.class);
				break;
			}
			if (recommed.getText().toString().contains("客户")) {
				intent = new Intent(getActivity(),
						User_MyConsumerActivity.class);
			} else {
				intent = new Intent(getActivity(),
						User_MyRecommedActivity.class);
			}
			break;
		case R.id.bankno:
			if (TextUtils.isEmpty(userid)) {
				intent = new Intent(getActivity(), User_LoginActivity.class);
				break;
			}
			intent = new Intent(getActivity(), User_MyBankActivity.class);
			break;
		case R.id.my_team:
			if (TextUtils.isEmpty(userid)) {
				intent = new Intent(getActivity(), User_LoginActivity.class);
				break;
			}
			intent = new Intent(getActivity(), User_MyTeamActivity.class);
			break;
		case R.id.my_follow:
			if (TextUtils.isEmpty(userid)) {
				intent = new Intent(getActivity(), User_LoginActivity.class);
				break;
			}
			intent = new Intent(getActivity(), User_MyFollowActivity.class);
			break;
		case R.id.my_appraisal:
			if (TextUtils.isEmpty(userid)) {
				intent = new Intent(getActivity(), User_LoginActivity.class);
				break;
			}
			intent = new Intent(getActivity(), User_MyAppraisalActivity.class);
			break;
		}
		if (intent != null) {
			startActivity(intent);
			return;
		}
	}

	public void loadAvatar(String urls, final File savafile) {
		// URL url =new URL(urls);
		// URI uri = url.toURI();
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(urls, avatar, new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				Tools.showToast(getActivity(), "头像加载失败!");
				avatar.setImageResource(R.drawable.user_avatar);
			}

			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				if (!UserImageTools.saveBitmap(savafile, arg2)) {
					Tools.showToast(getActivity(), "头像保存失败!");
				}
				avatar.setImageBitmap(UserImageTools.createCircleImage(arg2,
						arg2.getWidth()));
			}

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
			}
		});
	}

	public void share() {
		// AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Dialog dialog = builder.setMessage("确定要删除么").setTitle("标题").create();
		// android.R.style.Animation_InputMethod
		sharedialog = new Dialog(getActivity(), R.style.simpledialog);
		Window window = sharedialog.getWindow();
		View view = getActivity().getLayoutInflater().inflate(
				R.layout.sharedialog, null);

		sharedialog.setContentView(view);
		view.findViewById(R.id.selectimg_option1).setOnClickListener(this);
		view.findViewById(R.id.selectimg_option2).setOnClickListener(this);

		view.findViewById(R.id.cancel).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
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
}
