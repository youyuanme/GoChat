package com.sibozn.gochat.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sibozn.gochat.R;
import com.sibozn.gochat.nohttp.CallServer;
import com.sibozn.gochat.nohttp.HttpListener;
import com.sibozn.gochat.utils.Tools;
import com.sibozn.gochat.utils.ZipUtils;
import com.sibozn.gochat.view.CarRecorderView;
import com.sibozn.gochat.view.CustomImageView;
import com.sibozn.gochat.view.LooperTextView;
import com.sibozn.gochat.view.TipView;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DemoActivity extends AppCompatActivity {

    private static final String TAG = "DemoActivity";
    private static final String TIP_PREFIX = "this is tip No.";

    private CarRecorderView carview;
    private SeekBar seekbar;
    private LooperTextView looperview;
    private TipView tip_view;
    private EditText et_weizhi;
    private Button bt_jeiya, bt_upData;
    private ImageView originalIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ButterKnife.bind(this);
        et_weizhi = (EditText) findViewById(R.id.et_weizhi);
        bt_jeiya = (Button) findViewById(R.id.bt_jeiya);
        bt_upData = (Button) findViewById(R.id.bt_upData);
        originalIv = (ImageView) findViewById(R.id.originalIv);
        ((CustomImageView) findViewById(R.id.customIV)).setImageRes(R.mipmap.ic_launcher);
        //originalIv.setImageResource(R.mipmap.slider_bg);
        Bitmap bitmap = compoundBitmap(R.mipmap.slider_bg);
        originalIv.setImageBitmap(bitmap);

        final String string = Environment.getExternalStorageDirectory() + "/Download/2.zip";
        final String folderPath = Environment.getExternalStorageDirectory() + "/Download/";
        final String stringe = Environment.getExternalStorageDirectory() + "/Download/e05.zip";
        final String folderPath1 = Environment.getExternalStorageDirectory() + "/Download/";
        bt_jeiya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ZipUtils.unzip(stringe, folderPath1 + "/e05");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Tools.Unzip(string, folderPath);
//                try {
//                    ZipUtils.upZipFile(new File(string),folderPath);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    Tools.upZipFile(new File(string), folderPath);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });
        bt_upData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.Unzip(stringe, folderPath);
//                try {
//                    ZipUtils.upZipFile(new File(stringe),folderPath);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    Tools.upZipFile(new File(stringe), folderPath1);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

//                final Request<String> request = NoHttp.createStringRequest("http://photomania.net/upload/iostream",
//                        RequestMethod.POST);
//                String path = Environment.getExternalStorageDirectory() + "/Download/1.jpg";
//                File file = new File(path);
//                Log.e(TAG, "onClick:----path-->>> " + file.getAbsolutePath());
//                request.add("file", file);
//                CallServer.getRequestInstance().add(DemoActivity.this, 10, request, new HttpListener<String>() {
//                    @Override
//                    public void onSucceed(int what, Response<String> response) {
//                        Log.e(TAG, "----------->onSucceed: " + response.get());
//                    }
//
//                    @Override
//                    public void onFailed(int what, Response<String> response) {
//                        Log.e(TAG, "----------->onFailed: " + response.get() + "-what--" + what);
//                    }
//                }, false, false);
            }
        });

        looperview = (LooperTextView) findViewById(R.id.looperview);
        looperview.setTipList(generateTips());

        tip_view = (TipView) findViewById(R.id.tip_view);
        tip_view.setTipList(generateTips());
        tip_view.setOnMyLooperClickListener(new TipView.OnMyLooperClickListener() {
            @Override
            public void onMyLooperClickListener(int index) {
                Log.e(TAG, "onMyLooperClickListener----->: " + index);
            }
        });

        // 汽车表盘
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        carview = (CarRecorderView) findViewById(R.id.carview);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.e(TAG, "onProgressChanged: " + progress);
                carview.startAnim(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @OnClick({R.id.textView1, R.id.textView2, R.id.textView3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView1:
                Log.e(TAG, "onClick1: ");
                Toast.makeText(this, "I can be dragged !", Toast.LENGTH_LONG).show();
                break;
            case R.id.textView2:
                Toast.makeText(this, "I will back to origin pos!", Toast.LENGTH_LONG).show();
                Log.e(TAG, "onClick2: ");

                break;
            case R.id.textView3:
                Toast.makeText(this, "Use edge tracker to operate me!", Toast.LENGTH_LONG).show();
                Log.e(TAG, "onClick3: ");

                break;

        }
    }

    /**
     * 合成图片
     *
     * @param resId 图片 id
     */
    private Bitmap compoundBitmap(int resId) {
        //把资源图片变成一个Bitmap对象
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), resId);
        // 生成下面的一半图片
        Matrix matrix = new Matrix();
        matrix.setScale(1, -1);
        Bitmap invertBitmap = Bitmap.createBitmap(originalBitmap, 0, originalBitmap.getHeight() / 2, originalBitmap
                .getWidth(), originalBitmap.getHeight() / 2, matrix, false);
        // 创建一个空的位图
        Bitmap compoundBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight() +
                invertBitmap.getHeight() + 10, Bitmap.Config.ARGB_8888);// +10是为了2张图片之间有空隙
        Canvas canvas = new Canvas(compoundBitmap);
        canvas.drawBitmap(originalBitmap, 0, 0, null);
        canvas.drawBitmap(invertBitmap, 0, originalBitmap.getHeight() + 10, null);
        Paint paint = new Paint();
        // 设置渐变颜色
        LinearGradient shader = new LinearGradient(0, originalBitmap.getHeight() + 10, 0, compoundBitmap.getHeight(),
                0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0, originalBitmap.getHeight() + 5, originalBitmap.getWidth(), compoundBitmap.getHeight(),
                paint);
        return compoundBitmap;
    }

    private List<String> generateTips() {
        List<String> tips = new ArrayList<>();
        for (int i = 100; i < 120; i++) {
            tips.add(TIP_PREFIX + i);
        }
        return tips;
    }

    private List<String> generateTips1() {
        List<String> tips = new ArrayList<>();
        tips.add("赵丽颖");
        tips.add("杨颖");
        tips.add("郑爽");
        tips.add("杨幂");
        tips.add("刘诗诗");
        tips.add("迪丽热巴");
        tips.add("李沁");
        tips.add("唐嫣");
        tips.add("林心如");
        tips.add("陈乔恩");
        tips.add("范冰冰");
        tips.add("刘亦菲");
        tips.add("李小璐");
        tips.add("佟丽娅");
        return tips;
    }
}
