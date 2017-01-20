package com.yibingding.haolaiwu;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.app.BaseActivity;
import com.ybd.app.tools.PreferenceHelper;
import com.ybd.app.tools.SystemTool;
import com.ybd.app.tools.Tools;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.activity.BuildingInfoActivity;
import com.yibingding.haolaiwu.dialog.User_SimpleDialog;
import com.yibingding.haolaiwu.domian.Customer;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.view.CornerImageView;

public class User_MyRecommedDetailsActivity extends BaseActivity implements
		OnClickListener {

	private View step0, step1, step2, step3, part;
	private LinearLayout ll_big_content;
	private CheckBox radioButton0, radioButton1, radioButton2, radioButton3;
	private TextView time0, time1, time2, time3;
	private TextView customername, customerphone, customerremark;
	private TextView name, value, address, tv_tishi;
	private CornerImageView imageView;
	private Customer customer;
	private TextView title;
	private TextView link;
	private List<String> tempImgUri;
	private ProgressDialog dialog;
	private TextView payvalue;
	private Context context;

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.user_myrecommeddetails);
		this.context = this;
	}

	@Override
	public void initViews() {
		ll_big_content = (LinearLayout) findViewById(R.id.ll_big_content);
		step0 = findViewById(R.id.step0);
		step1 = findViewById(R.id.step1);
		step2 = findViewById(R.id.step2);
		step3 = findViewById(R.id.step3);
		part = findViewById(R.id.part);
		time0 = (TextView) findViewById(R.id.time0);
		time1 = (TextView) findViewById(R.id.time1);
		time2 = (TextView) findViewById(R.id.time2);
		time3 = (TextView) findViewById(R.id.time3);
		link = (TextView) findViewById(R.id.link);
		link.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TextUtils.equals(customer.getStyle(), "楼盘")) {
					Intent intent = new Intent(context,
							BuildingInfoActivity.class);
					intent.putExtra("id", customer.getT_Assicoate_Guid());
					startActivity(intent);
				} else if (TextUtils.equals(customer.getStyle(), "租赁")) {
					Intent intent = new Intent(context,
							HouseDetailsActivity.class);
					intent.putExtra("id", customer.getT_Assicoate_Guid());
					intent.putExtra("whereFrom", Constants.HOUSE_RENT);
					startActivity(intent);
				} else if (TextUtils.equals(customer.getStyle(), "二手房")) {
					Intent intent = new Intent(context,
							HouseDetailsActivity.class);
					intent.putExtra("id", customer.getT_Assicoate_Guid());
					intent.putExtra("whereFrom", Constants.OLD_HOUSE);
					startActivity(intent);
				}
			}
		});
		radioButton0 = (CheckBox) findViewById(R.id.radioButton0);
		radioButton1 = (CheckBox) findViewById(R.id.radioButton1);
		radioButton2 = (CheckBox) findViewById(R.id.radioButton2);
		radioButton3 = (CheckBox) findViewById(R.id.radioButton3);
		customername = (TextView) findViewById(R.id.customername);
		customerphone = (TextView) findViewById(R.id.customerphone);
		customerremark = (TextView) findViewById(R.id.customerremark);
		tv_tishi = (TextView) findViewById(R.id.tv_tishi);
		name = (TextView) findViewById(R.id.name);
		value = (TextView) findViewById(R.id.value);
		address = (TextView) findViewById(R.id.address);
		imageView = (CornerImageView) findViewById(R.id.image);
		imageView.setRadius(14);
		title = (TextView) findViewById(R.id.title);
		payvalue = (TextView) findViewById(R.id.payvalue);
	}

	@Override
	public void initData() {
		customer = getIntent().getParcelableExtra("customer");
		link.setText(customer.getStyle());
		if (TextUtils.isEmpty(customer.getPic())) {
			ImageLoader.getInstance()
					.displayImage(customer.getPic(), imageView);
		} else {
			ImageLoader.getInstance().displayImage(
					Constants.IMAGE_URL + customer.getPic(), imageView);
		}
		name.setText(customer.getNames());
		value.setText(customer.getAveragePrice());
		address.setText("地址:" + customer.getCityName() + customer.getStreet());
		customername.setText(customer.getT_Client_Name());
		customerphone.setText(customer.getT_Client_Phone());
		customerphone.setTag(customer.getT_Client_Phone());
		customerphone.setOnClickListener(this);
		customerremark.setText(customer.getT_Client_Remark());
		time0.setText(customer.getT_Client_dDate());
		time1.setText(customer.getT_Client_sDate());
		time2.setText(customer.getT_Client_mDate());
		time3.setText(customer.getT_Client_eDate());
		String t_Client_dState = customer.getT_Client_dState();
		if (TextUtils.equals("2", t_Client_dState)) {// 1是2是0是
			step0.setVisibility(View.GONE);
			step1.setVisibility(View.GONE);
			step2.setVisibility(View.GONE);
			step3.setVisibility(View.GONE);
		} else if (TextUtils.equals("1", customer.getT_Client_eState())) {
			ll_big_content.removeView(tv_tishi);
			radioButton3.setChecked(true);
		} else if (TextUtils.equals("1", customer.getT_Client_mState())) {
			ll_big_content.removeView(tv_tishi);
			radioButton2.setChecked(true);
		} else if (TextUtils.equals("1", customer.getT_Client_sState())) {
			ll_big_content.removeView(tv_tishi);
			radioButton1.setChecked(true);
		} else if (TextUtils.equals("1", customer.getT_Client_dState())) {
			ll_big_content.removeView(tv_tishi);
			radioButton0.setChecked(true);
		} else {
			ll_big_content.removeView(tv_tishi);
		}
	}

	public void back(View v) {
		finish();
	}

	public void link(View v) {

	}

	public void submit(View v) {
		CharSequence payvaluestr = payvalue.getText();
		if (payvaluestr == null || payvaluestr.equals("")) {
			Toast.makeText(this, "请输入支付金额", Toast.LENGTH_SHORT).show();
			return;
		}
		if (dialog == null) {
			dialog = new ProgressDialog(this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("正在努力加载中...");
		}
		dialog.show();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("Token", AESUtils.encode("clientGuid"));
		params.put("clientGuid",
				PreferenceHelper.readString(this, "userinfo", "guid"));
		StringBuffer imgs = new StringBuffer("");
		if (tempImgUri != null) {
			for (String uristr : tempImgUri) {
				Uri uri = Uri.parse(uristr);
				try {
					Bitmap bitmap = getimage(uri2File(
							User_MyRecommedDetailsActivity.this, uri)
							.getAbsolutePath());
					// bitmap=compressImage(bitmap);
					imgs.append(Tools.Bitmap2StrByBase64(bitmap));
					imgs.append(",");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		if (imgs.length() > 0) {
			imgs.deleteCharAt(imgs.length() - 1);
		}
		params.put("base64Pic", imgs.toString());
		VolleyPost post = new VolleyPost(this, Constants.SERVER_IP
				+ Constants.UploadImg_URL, params) {

			@Override
			public void pullJson(String json) {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				if (json == null || json.equals("")) {
					Toast.makeText(User_MyRecommedDetailsActivity.this,
							"网络连接有问题！", Toast.LENGTH_SHORT).show();
					return;
				}
				// Log.v("app", "上传图片请求"+json);

				try {
					JSONArray array = new JSONArray(json);
					JSONObject jsonObject = array.getJSONObject(0);
					if (jsonObject.getString("state").equals("true")) {
						Intent intent = new Intent(
								User_MyRecommedDetailsActivity.this,
								AgreementActivity.class);
						String from = PreferenceHelper.readString(
								User_MyRecommedDetailsActivity.this,
								"userinfo", "mobile");
						String totel = customer.getT_Client_Phone();
						intent.putExtra("totel", totel);
						intent.putExtra("fromtel", from);
						intent.putExtra("value", payvalue.getText().toString());
						Log.v("ap", "tel" + totel + "from" + from + "value"
								+ value);
						startActivityForResult(intent, 2);
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

	public void selectimg(View v) {
		Intent intent = new Intent(this, Album_MultiImgActivity.class);
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg0 == 1 && arg1 == 1) {
			tempImgUri = arg2.getStringArrayListExtra("uri");
			Log.v("app", tempImgUri.toString());
		} else if (arg0 == 2 & arg1 == 1) {
			radioButton2.setChecked(true);
			radioButton1.setChecked(false);
			step2.setVisibility(View.VISIBLE);
			part.setVisibility(View.GONE);

		} else if (arg0 == 2 && arg1 == 2) {
			Toast.makeText(this, "注意：支付结果正在审核中!!!", Toast.LENGTH_LONG).show();
		}
		super.onActivityResult(arg0, arg1, arg2);
	}

	private Bitmap getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return imageZoom(bitmap, 50);// 压缩好比例大小后再进行质量压缩
	}

	private Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		Log.v("app", "图片的大小" + baos.toByteArray().length / 1024);
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	public static Bitmap imageZoom(Bitmap bitmap, double maxSize) {
		// 将bitmap放至数组中，意在获得bitmap的大小（与实际读取的原文件要大）
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 格式、质量、输出流
		bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
		byte[] b = baos.toByteArray();
		// 将字节换成KB
		double mid = b.length / 1024;
		// 获取bitmap大小 是允许最大大小的多少倍
		double i = mid / maxSize;
		// 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
		if (i > 1) {
			// 缩放图片 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
			// （保持宽高不变，缩放后也达到了最大占用空间的大小）
			bitmap = scaleWithWH(bitmap, bitmap.getWidth() / Math.sqrt(i),
					bitmap.getHeight() / Math.sqrt(i));
		}
		return bitmap;
	}

	/***
	 * 图片的缩放方法,如果参数宽高为0,则不处理<br>
	 * 
	 * <b>注意</b> src实际并没有被回收，如果你不需要，请手动置空
	 * 
	 * @param src
	 *            ：源图片资源
	 * @param w
	 *            ：缩放后宽度
	 * @param h
	 *            ：缩放后高度
	 */
	public static Bitmap scaleWithWH(Bitmap src, double w, double h) {
		if (w == 0 || h == 0 || src == null) {
			return src;
		} else {
			// 记录src的宽高
			int width = src.getWidth();
			int height = src.getHeight();
			// 创建一个matrix容器
			Matrix matrix = new Matrix();
			// 计算缩放比例
			float scaleWidth = (float) (w / width);
			float scaleHeight = (float) (h / height);
			// 开始缩放
			matrix.postScale(scaleWidth, scaleHeight);
			// 创建缩放后的图片
			return Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
		}
	}

	@SuppressLint("NewApi")
	public static File uri2File(Activity aty, Uri uri) {
		if (SystemTool.getSDKVersion() < 11) {
			// 在API11以下可以使用：managedQuery
			String[] proj = { MediaStore.Images.Media.DATA };
			@SuppressWarnings("deprecation")
			Cursor actualimagecursor = aty.managedQuery(uri, proj, null, null,
					null);
			int actual_image_column_index = actualimagecursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			actualimagecursor.moveToFirst();
			String img_path = actualimagecursor
					.getString(actual_image_column_index);
			return new File(img_path);
		} else {
			// 在API11以上：要转为使用CursorLoader,并使用loadInBackground来返回
			String[] projection = { MediaStore.Images.Media.DATA };
			CursorLoader loader = new CursorLoader(aty, uri, projection, null,
					null, null);
			Cursor cursor = loader.loadInBackground();
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return new File(cursor.getString(column_index));
		}
	}

	public static String encodeBase64File(String path) throws Exception {
		File file = new File(path);
		FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];
		inputFile.read(buffer);
		inputFile.close();
		return Base64.encodeToString(buffer, Base64.DEFAULT);
	}

	@Override
	public void onClick(View v) {
		final String phoneString = (String) v.getTag();
		final User_SimpleDialog dialogCallPhone = new User_SimpleDialog(this);
		dialogCallPhone.setTitleText("是否呼叫：" + phoneString);
		dialogCallPhone.setConfirmListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ phoneString));
				startActivity(intent);
				dialogCallPhone.dismiss();
			}
		});
		dialogCallPhone.show();

	}
}
