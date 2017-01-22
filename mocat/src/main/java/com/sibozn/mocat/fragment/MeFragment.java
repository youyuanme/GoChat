package com.sibozn.mocat.fragment;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sibozn.mochat.R;
import com.sibozn.mochat.activity.MainActivity;
import com.sibozn.mochat.domain.MyinfoBean;
import com.sibozn.mochat.interf.GetDataSuccessListener;
import com.sibozn.mochat.internet.MeInfoDate_Volley;
import com.sibozn.mochat.internet.UploadFile;
import com.sibozn.mochat.internet.VolleyGet;
import com.sibozn.mochat.tools.AppStorageUtils;
import com.sibozn.mochat.tools.BitmapCreate;
import com.sibozn.mochat.tools.FileUtils;
import com.sibozn.mochat.tools.SystemTool;
import com.sibozn.mochat.tools.Tools;
import com.sibozn.mochat.view.RoundImageView;

public class MeFragment extends BaseFragment implements /*GetDataSuccessListener,*/
		OnClickListener {

	private final String GET_ME_INFO_URL = "http://moliao.sibozn.com/api/user.php?device_id=";
	private static final int OPEN_CAMERA_CODE = 10;
	private static final int OPEN_GALLERY_CODE = 11;
	private static final int CROP_PHOTO_CODE = 12;

	private Context ctx;
	private TextView tv_uid, tv_signature, tv_sex, tv_age, tv_vip, tv_vip_time,
			tv_score, tv_rose;
	private RoundImageView iv_photo;
	private File tempFile;

	@Override
	public View onCreateThisFragment(LayoutInflater inflater,
			ViewGroup container) {
		ctx = getActivity();
		return View.inflate(ctx, R.layout.fragment_me, null);
	}

	@Override
	public void initViews(View view) {
		tv_uid = (TextView) view.findViewById(R.id.tv_uid);
		tv_signature = (TextView) view.findViewById(R.id.tv_signature);
		tv_sex = (TextView) view.findViewById(R.id.tv_sex);
		tv_age = (TextView) view.findViewById(R.id.tv_age);
		tv_vip = (TextView) view.findViewById(R.id.tv_vip);
		tv_vip_time = (TextView) view.findViewById(R.id.tv_vip_time);
		tv_score = (TextView) view.findViewById(R.id.tv_score);
		tv_rose = (TextView) view.findViewById(R.id.tv_rose);
		iv_photo = (RoundImageView) view.findViewById(R.id.iv_photo);
		iv_photo.setOnClickListener(this);
		view.findViewById(R.id.bt_submit).setOnClickListener(this);
	}

	@Override
	public void initData() {
		System.out.println("-=-=-=-=-=-=-=-=-" + SystemTool.getPhoneIMEI(ctx));
		new MeInfoDate_Volley(ctx, GET_ME_INFO_URL
				+ SystemTool.getPhoneIMEI(ctx), "1")
				.setOnGetDataSuccessListener(this);
		// new StringRequest(Method.GET, url, listener, errorListener)
	}

	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		System.out.println("==========onResume()======");
		super.onResume();
	}

	@Override
	public void onGetDataSuccess(String tag, Object obj) {
		if (obj != null) {
			if (TextUtils.equals("1", tag)) {
				MyinfoBean myinfoBean = (MyinfoBean) obj;
				tv_uid.setText(myinfoBean.getUid());
				tv_signature.setText(myinfoBean.getSignature());
				tv_sex.setText(myinfoBean.getSex());
				tv_age.setText(myinfoBean.getAge());
				tv_vip.setText(myinfoBean.getVip());
				tv_vip_time.setText(myinfoBean.getVip_time());
				tv_score.setText(myinfoBean.getScore());
				tv_rose.setText(myinfoBean.getRose());
				if (!TextUtils.isEmpty(myinfoBean.getPhopo())) {
					ImageLoader.getInstance().displayImage(
							myinfoBean.getPhopo(), iv_photo);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_submit:
			Tools.showToast(ctx, "提交数据！");
			break;
		case R.id.iv_photo:
			showPhotoDialog();
			break;
		case R.id.selectimg_option1:

			break;

		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, final Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case CROP_PHOTO_CODE:
//			File imageFile = FileUtils.uri2File(getActivity(), data.getData());
//			System.out.println("============imageFile.getAbsolutePath()==="
//					+ imageFile.getAbsolutePath());
			System.out.println("--------"+data.getData());
			// final File  file = FileUtils.uri2File(getActivity(), data.getData());
			final File  file = new File(FileUtils.getRealFilePath(getActivity(), data.getData()));
			new Thread(new Runnable() {
				@Override
				public void run() {
					final String phpotoUrl = UploadFile.uploadFile("http://moliao.sibozn.com/api/upload.php", file.getAbsolutePath());
					new VolleyGet(ctx,"http://moliao.sibozn.com/api/photo.php?device_id="+SystemTool.getPhoneIMEI(ctx)+"&photo="+phpotoUrl) {
						@Override
						public void pullJson(String json) {
							if (TextUtils.equals("ok", json)) {
								((MainActivity)ctx).runOnUiThread(new Runnable() {
									@Override
									public void run() {
										if (!TextUtils.isEmpty(phpotoUrl)) {
											System.out.println("--------photo----"+phpotoUrl);
											iv_photo.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
										}
									}
								});
							}
						}
					};
				}
			}).start();
			iv_photo.setImageURI(data.getData());
			break;

		case OPEN_CAMERA_CODE:
			new Thread(new Runnable() {
				@Override
				public void run() {
					final String phpotoUrl = UploadFile.uploadFile("http://moliao.sibozn.com/api/upload.php", tempFile.getAbsolutePath());
					new VolleyGet(ctx,"http://moliao.sibozn.com/api/photo.php?device_id="+SystemTool.getPhoneIMEI(ctx)+"&photo="+phpotoUrl) {
						@Override
						public void pullJson(String json) {
							if (TextUtils.equals("ok", json)) {
								((MainActivity)ctx).runOnUiThread(new Runnable() {
									@Override
									public void run() {
										if (!TextUtils.isEmpty(phpotoUrl)) {
											System.out.println("--------photo----"+phpotoUrl);
											iv_photo.setImageBitmap(BitmapFactory.decodeFile(tempFile.getAbsolutePath()));
										}
									}
								});
							}
						}
					};
				}
			}).start();
			// UploadFile.uploadFile("http://moliao.sibozn.com/upload.html", tempFile.getAbsolutePath(), "temp.jpg");
			//iv_photo.setImageBitmap(BitmapCreate.bitmapFromFile(tempFile.getAbsolutePath(), 40, 40));
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 从底部显示相册和相机dialog
	 */
	private void showPhotoDialog() {
		final Dialog photoImageDialog = new Dialog(ctx, R.style.simpledialog);
		Window window = photoImageDialog.getWindow();
		View view = View.inflate(ctx, R.layout.photo_dialog, null);
		photoImageDialog.setContentView(view);
		view.findViewById(R.id.selectimg_option1).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Tools.showToast(ctx, "点击相机！");
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						File file = ctx.getExternalCacheDir();
						if (file == null) {
							file = ctx.getCacheDir();
						}
						tempFile = new File(file.getAbsolutePath()+"/" + "tempavatar.jpg");
						//tempFile = FileUtils.getSaveFile(ctx, file.getAbsolutePath()+File.separator, "temp.jpg");
						if (tempFile.exists()) {
							tempFile.delete();
						}
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
						photoImageDialog.dismiss();
						startActivityForResult(intent, OPEN_CAMERA_CODE);
					}
				});
		view.findViewById(R.id.selectimg_option2).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						Tools.showToast(ctx, "点击相册！");
						
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.setType("image/*");
						
						System.out.println("DIRECTORY_DCIM:"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));;
						System.out.println(";;;;;;;;;"+SystemTool.getDeviceUsableMemory(ctx));
						System.out.println("=Environment.getDataDirectory().getAbsolutePath()=="+Environment.getDataDirectory().getAbsolutePath());
						System.out.println("AppStorageUtils.getInternalStorageTotalSpace():"+AppStorageUtils.getInternalStorageTotalSpace());
						System.out.println("内置总内存："+AppStorageUtils.getInternalStorageTotalSpace()/(1024*1024));
						System.out.println("-----ctx.getFilesDir().getAbsolutePath()--"+ctx.getFilesDir().getAbsolutePath());
						System.out.println("AppStorageUtils.getInternalStorageAvailableSpace():"+AppStorageUtils.getInternalStorageAvailableSpace(ctx));
						System.out.println("内置可用"+AppStorageUtils.getInternalStorageAvailableSpace(ctx)/(1024*1024));
//						System.out.println("----ctx.getDatabasePath(null).getAbsolutePath()----"+ctx.getDatabasePath(null).getAbsolutePath());
						System.out.println("----ctx.getExternalFilesDir(null).getAbsolutePath()---"+ctx.getExternalFilesDir(null).getAbsolutePath());
						System.out.println("-ctx.getExternalFilesDir(/123).getAbsolutePath()-"+ctx.getExternalFilesDir("/123").getAbsolutePath());
						System.out.println("====ctx.getCacheDir().getAbsolutePath()==="+ctx.getCacheDir().getAbsolutePath());

						// Log.e("---", ctx.getCodeCacheDir().getAbsolutePath());
						System.out.println("-----ctx.getExternalCacheDir().getAbsolutePath()---"+ctx.getExternalCacheDir().getAbsolutePath());
						System.out.println("-----Environment.getExternalStorageState()--"+Environment.getExternalStorageState());
						// System.out.println(""+Environment.getExternalStoragePublicDirectory(type));
						System.out.println("--Environment.getExternalStorageDirectory().getAbsolutePath()-"+Environment.getExternalStorageDirectory().getAbsolutePath());
						System.out.println("-Environment.getDownloadCacheDirectory().getAbsolutePath()----"+Environment.getDownloadCacheDirectory().getAbsolutePath());
						System.out.println("-Environment.getRootDirectory().getAbsolutePath()-"+Environment.getRootDirectory().getAbsolutePath());
//						
						photoImageDialog.dismiss();
						startActivityForResult(intent, CROP_PHOTO_CODE);
					}
				});
		view.findViewById(R.id.cancel).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						photoImageDialog.dismiss();
					}
				});
		photoImageDialog.setContentView(view);
		window.setWindowAnimations(R.style.inputanimation);
		window.setGravity(Gravity.BOTTOM);
		window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		photoImageDialog.show();
	}
	
	private void startPhotoZoom(Uri uri) {
		
	}
}
