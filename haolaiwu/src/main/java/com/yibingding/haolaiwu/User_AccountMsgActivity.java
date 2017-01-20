package com.yibingding.haolaiwu;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ybd.app.BaseActivity;
import com.ybd.app.tools.PreferenceHelper;
import com.ybd.app.tools.Tools;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.tools.MyUtils;

public class User_AccountMsgActivity extends BaseActivity implements
		OnClickListener {
	private TextView option;
	private EditText nickname;
	private EditText name;
	private EditText birth;
	private TextView sexview;
	private TextView user_type;
	private EditText phone;
	private RadioGroup sexRadioGroup;
	private RadioButton nan, nv;
	boolean edit = false;
	private ImageView avatar;
	private Dialog sharedialog;
	private File tempFile;
	private File outFile;
	private File copyFile;
	private ProgressDialog dialog;
	private boolean editok = false;
	private String savepath;
	private String mobile;
	private String userid;
	private boolean isEditPic = false;

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.user_accountmsg);
		savepath = PreferenceHelper.readString(this, "userinfo", "picsavepath");
		mobile = PreferenceHelper.readString(this, "userinfo", "mobile");
		if (savepath == null || savepath.equals("")
				|| (!new File(savepath).exists())) {
			File path = getExternalCacheDir();
			if (path == null) {
				path = getCacheDir();
			}
			savepath = path.getAbsolutePath() + "/";
			PreferenceHelper.write(this, "userinfo", "picsavepath", savepath);
		}
	}

	@Override
	public void initViews() {
		option = (TextView) findViewById(R.id.option);
		option.setOnClickListener(this);
		nickname = (EditText) findViewById(R.id.nickname);
		name = (EditText) findViewById(R.id.name);
		birth = (EditText) findViewById(R.id.birth);
		phone = (EditText) findViewById(R.id.phone);
		findViewById(R.id.avatar1).setOnClickListener(this);
		sexview = (TextView) findViewById(R.id.sextextview);
		user_type = (TextView) findViewById(R.id.user_type);
		nan = (RadioButton) findViewById(R.id.radioButton1);
		nv = (RadioButton) findViewById(R.id.radioButton2);
		sexRadioGroup = (RadioGroup) findViewById(R.id.sex);
		avatar = (ImageView) findViewById(R.id.avatar);
		avatar.setOnClickListener(this);
		sexRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				if (arg1 == R.id.radioButton1) {
					sexview.setText("男");
				} else {
					sexview.setText("女");
				}
			}
		});
	}

	@Override
	public void initData() {
		SharedPreferences preference = this.getSharedPreferences("userinfo",
				Context.MODE_PRIVATE);
		nickname.setText(preference.getString("nickname", ""));
		name.setText(preference.getString("realname", ""));
		user_type.setText(preference.getString("style", ""));
		String birthstr = preference.getString("birth", "");
		if (!birthstr.equals("")) {
			// SimpleDateFormat format = new
			// SimpleDateFormat("yy/MM/dd hh:mm:ss");
			// try {
			// Date date = format.parse(birthstr);
			// birth.setText(date.getYear()+"/"+date.getMonth()+"/"+date.getDay());
			// } catch (ParseException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			int index = birthstr.indexOf(" ");
			birthstr = birthstr.substring(0, index);
			birth.setText(birthstr);
		}
		sexview.setText(preference.getString("sex", "男"));
		if (sexview.getText().equals("男")) {
			// sexview.setText("女");
			nan.setChecked(true);
		} else {
			nv.setChecked(true);
			sexview.setText("女");
		}
		phone.setText(preference.getString("mobile", ""));
		userid = PreferenceHelper.readString(this, "userinfo", "userid");
		File file = new File(savepath + userid + "_pic.jpg");
		String picurl = PreferenceHelper.readString(this, "userinfo", "avatar");
		if ((!picurl.equals("")) && file.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			avatar.setImageBitmap(bitmap);
		} else if (picurl.equals("") && file.exists()) {
			file.delete();
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.option:
			if (option.getText().toString().equals("编辑")) {
				edit = true;
				option.setText("完成");
				sexview.setVisibility(View.GONE);
				sexRadioGroup.setVisibility(View.VISIBLE);
				nickname.setEnabled(true);
				name.setEnabled(true);
				phone.setEnabled(true);
				birth.setEnabled(true);
				avatar.setEnabled(true);
			} else {
				setData();
			}
			break;
		case R.id.avatar:
		case R.id.avatar1:
			if (TextUtils.equals("完成", option.getText().toString().trim())) {
				selectimg();
			}
			break;
		case R.id.selectimg_option1:// 拍照
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			tempFile = new File(savepath + "tempavatar.jpg");
			if (tempFile.exists()) {
				tempFile.delete();
			}
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
			sharedialog.dismiss();
			startActivityForResult(intent, 1);
			break;
		case R.id.selectimg_option2:// 相册
			// 打开相册并返回指定url
			Intent intent1 = new Intent();
			intent1.setType("image/*");
			intent1.setAction(Intent.ACTION_GET_CONTENT);
			sharedialog.dismiss();
			startActivityForResult(intent1, 2);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case 1:// 照相
			startPhotoZoom(Uri.fromFile(tempFile), 1);
			break;
		case 2:// 相册
			Uri uri = data.getData();
			startPhotoZoom(uri, 1);
			break;
		case 3:
			sentPicToNext(data);
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		if (sharedialog != null && sharedialog.isShowing()) {
			sharedialog.dismiss();
		}
		super.onDestroy();
	}

	private void startPhotoZoom(Uri uri, int type) {
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
		// intent.putExtra("return-data", true);
		// intent.putExtra("noFaceDetection", true);

		// String path= Environment.getExternalStorageDirectory()+"123.jpg";
		if (outFile != null) {
			copyFile = outFile;
		}
		outFile = new File(savepath + new Date().getTime() + ".jpg");
		// if(outFile.exists()){
		// outFile.delete();
		// }
		// Uri outUri = Uri.parse(Environment.getExternalStorageDirectory()+
		// new Date().getTime()+".jpg")
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, 3);
	}

	private void sentPicToNext(Intent picdata) {
		// Bundle bundle = picdata.getExtras();
		// if (bundle != null) {
		// Bitmap photo = bundle.getParcelable("data");
		// String filePath = bundle.getString("filePath");
		// if (photo == null) {
		// photo = BitmapFactory.decodeFile(filePath);
		// if (photo != null) {
		// avatar.setImageBitmap(photo);;
		// }
		// } else {
		// avatar.setImageBitmap(photo);
		// }
		// // TODO 上传logo
		// }
		if (outFile != null && outFile.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(outFile.getAbsolutePath());
			avatar.setImageBitmap(bitmap);
			isEditPic = true;
		} else {
			outFile = copyFile;
		}
	}

	public void back(View v) {
		finish();
	}

	public void selectimg() {
		// AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Dialog dialog = builder.setMessage("确定要删除么").setTitle("标题").create();
		// android.R.style.Animation_InputMethod
		sharedialog = new Dialog(this, R.style.simpledialog);
		Window window = sharedialog.getWindow();
		View view = getLayoutInflater().inflate(R.layout.selectimg, null);
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

	private void setData() {
		CharSequence nicknamestr = nickname.getText();
		CharSequence namestr = name.getText();
		if (nicknamestr == null) {
			nickname.setText("");
		}
		if (namestr == null) {
			name.setText("");
		}
		CharSequence birthstr = birth.getText();
		if (birthstr == null) {
			birth.setText("");
		}
		CharSequence phonestr = phone.getText();
		if (phonestr == null) {
			phone.setText("");
		}
		String sex = sexview.getText().toString();
		String img = "";
		if (isEditPic) {
			if (outFile != null && outFile.exists()) {
				try {
					img = Tools.encodeBase64File(outFile.getAbsolutePath());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				File file = new File(savepath + mobile + "_pic.jpg");
				if (file.exists()) {
					// file.delete();
					try {
						img = Tools.encodeBase64File(file.getAbsolutePath());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		String phoneString = phonestr.toString();
		if (!TextUtils.isEmpty(phoneString)
				&& !MyUtils.isPhoneNumber(phoneString)) {
			MyUtils.showToast(this, "请输入正确的手机号!");
			return;
		}
		String birthString = birth.getText().toString().replace("/", "-");
		if (!isDateStringValid(birthString)) {
			MyUtils.showToast(this, "请输入正确的生日格式!");
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("userGuid",
				PreferenceHelper.readString(this, "userinfo", "guid"));
		params.put("realName", name.getText().toString());
		params.put("nickName", nickname.getText().toString());
		params.put("Sex", sex);
		params.put("Birth", birth.getText().toString());
		params.put("Phone", phone.getText().toString());
		params.put("base64Pic", img);
		params.put("Token", AESUtils.encode("userGuid"));
		if (dialog == null) {
			dialog = new ProgressDialog(this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("正在努力加载中...");
		}
		dialog.show();
		VolleyPost request = new VolleyPost(this, Constants.SERVER_IP
				+ Constants.User_CompleteUser_URL, params) {
			@Override
			public void pullJson(String json) {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				if (json == null || json.equals("")) {
					Toast.makeText(User_AccountMsgActivity.this, "网络连接有问题！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				JSONArray jsonArray = JSON.parseArray(json);
				JSONObject obj = jsonArray.getJSONObject(0);
				String state = obj.getString("state");
				if (state.equals("true")) {
					jsonArray = obj.getJSONArray("result");
					obj = jsonArray.getJSONObject(0);
					SharedPreferences preference = User_AccountMsgActivity.this
							.getSharedPreferences("userinfo",
									Context.MODE_PRIVATE);
					Editor editor = preference.edit();
					editor.putString("guid", obj.getString("Guid"));
					editor.putString("userid", obj.getString("t_User_LoginId"));
					editor.putString("realname",
							obj.getString("t_User_RealName"));
					editor.putString("nickname",
							obj.getString("t_User_NickName"));
					editor.putString("sex", obj.getString("t_User_Sex"));
					editor.putString("birth", obj.getString("t_User_Birth"));
					editor.putString("mobile", obj.getString("t_User_Mobile"));
					editor.putString("avatar", obj.getString("t_User_Pic"));
					editor.putString("tStyle", obj.getString("t_User_Style"));
					editor.putString("style", obj.getString("UserStyle"));
					editor.putString("date", obj.getString("t_User_Date"));
					editor.putString("leader", obj.getString("t_User_Leader"));
					editor.putString("leaderid", obj.getString("LeaderLoginId"));
					editor.putString("leaderrealname",
							obj.getString("LeaderRealName"));
					editor.putString("accunt", obj.getString("Account"));
					editor.putString("Integral", obj.getString("Integral"));
					editor.putString("accountmsgcomplete",
							obj.getString("t_User_Complete"));
					editor.commit();
					edit = false;
					editok = true;
					option.setText("编辑");
					sexview.setVisibility(View.VISIBLE);
					sexRadioGroup.setVisibility(View.GONE);
					if (sexRadioGroup.getCheckedRadioButtonId() == R.id.radioButton0) {

					}
					nickname.setEnabled(false);
					name.setEnabled(false);
					phone.setEnabled(false);
					birth.setEnabled(false);
					avatar.setEnabled(false);
					Toast.makeText(User_AccountMsgActivity.this, "信息保存成功！",
							Toast.LENGTH_SHORT).show();
					if (outFile != null && outFile.exists()) {
						File file = new File(savepath + userid + "_pic.jpg");
						if (file.exists()) {
							file.delete();
						}
						outFile.renameTo(file);
					}
				} else {
					Toast.makeText(User_AccountMsgActivity.this,
							obj.getString("result"), Toast.LENGTH_SHORT).show();
					editok = false;
				}
			}

			@Override
			public String getPageIndex() {
				return null;
			}
		};
	}

	/**
	 * 判断字符串是否为日期格式
	 * 
	 * @param date
	 * @return
	 */
	public boolean isDateStringValid(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");
		// 输入对象不为空
		try {
			sdf.parse(date);
			return true;
		} catch (java.text.ParseException e) {
			return false;
		}
	}
}
