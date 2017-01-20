package com.yibingding.haolaiwu;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.yibingding.haolaiwu.tools.LocalImageHelper;
import com.yibingding.haolaiwu.tools.LocalImageHelper.LoadFinishListener;
import com.yibingding.haolaiwu.tools.LocalImageHelper.LocalFile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;

public class Album_MultiImgActivity extends Activity implements LocalImageHelper.LoadFinishListener,OnCheckedChangeListener{
	View progressVIew;
	GridView gridView;
	MyAdapter adapter;
	Handler hanlder = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				progressVIew.setVisibility(View.GONE);
				if(adapter==null){
					adapter = new MyAdapter(Album_MultiImgActivity.this, LocalImageHelper.getInstance().paths);
					
				}
				gridView.setAdapter(adapter);
				break;

			default:
				break;
			}
		};
	};
	  List<LocalImageHelper.LocalFile> checkedItems ;
	  ImageLoader imageLoader;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.album_multi);
	gridView = (GridView) findViewById(R.id.gridview);
	progressVIew = findViewById(R.id.progressview);
	LocalImageHelper.init(getApplication());
	LocalImageHelper.getInstance().setListener(this);
	   checkedItems=LocalImageHelper.getInstance().getCheckedItems();
	   imageLoader=ImageLoader.getInstance();
//	   gridView.setOnItemClickListener(new OnItemClickListener() {
//		   
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position,
//				long id) {
//			// TODO Auto-generated method stub
//			CheckBox box = (CheckBox) view.findViewById(R.id.checkbox);	
//			ImageView  imageView = (ImageView)view.findViewById(R.id.imageView);
//			box.setChecked(!box.isChecked());
//			if(box.isChecked()){
//				checkedItems.add((LocalImageHelper.LocalFile)imageView.getTag());
//			}else{
//				if(checkedItems.contains(imageView.getTag())){
//				checkedItems.remove(imageView.getTag());
//				}
//			}
//		}
//	});
}
@Override
public void onfinish() {
	// TODO Auto-generated method stub
	hanlder.sendEmptyMessage(1);
}
public class MyAdapter extends BaseAdapter{
    private Context m_context;
    private LayoutInflater miInflater;
    DisplayImageOptions options;
    List<LocalImageHelper.LocalFile> paths;

    public MyAdapter(Context context, List<LocalImageHelper.LocalFile> paths) {
        m_context = context;
        this.paths = paths;
        options=new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new SimpleBitmapDisplayer()).build();
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public LocalImageHelper.LocalFile getItem(int i) {
        return paths.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null || convertView.getTag() == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = getLayoutInflater();
            convertView = inflater.inflate(R.layout.img_selected, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            viewHolder.checkBox.setOnCheckedChangeListener(Album_MultiImgActivity.this);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageView imageView = viewHolder.imageView;
        LocalImageHelper.LocalFile localFile = paths.get(i);
//        FrescoLoader.getInstance().localDisplay(localFile.getThumbnailUri(), imageView, options);
//        ImageLoader.getInstance().displayImage(localFile.getThumbnailUri(), new ImageViewAware(viewHolder.imageView), options,
//                loadingListener, null, localFile.getOrientation());
        imageLoader.displayImage(localFile.getThumbnailUri(), viewHolder.imageView, options, loadingListener);
        viewHolder.checkBox.setTag(localFile);
        viewHolder.checkBox.setChecked(checkedItems.contains(localFile));
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        CheckBox checkBox;
    }
}

com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener loadingListener=new   com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener() {
    @Override
    public void onLoadingComplete(String imageUri, View view, final Bitmap bm) {
        if (TextUtils.isEmpty(imageUri)) {
            return;
        }
        //由于很多图片是白色背景，在此处加一个#eeeeee的滤镜，防止checkbox看不清
        try {
            ((ImageView) view).getDrawable().setColorFilter(Color.argb(0xff, 0xee, 0xee, 0xee), PorterDuff.Mode.MULTIPLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
};
@Override
public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	// TODO Auto-generated method stub
	if(checkedItems.size()==5&&isChecked){
		Toast.makeText(this, "最多能选择5张图片！", Toast.LENGTH_SHORT).show();
		buttonView.setChecked(false);
		
//		return;
	}else{
	if(isChecked){
		checkedItems.add((LocalFile)buttonView.getTag());
	}else{
		checkedItems.remove(buttonView.getTag());
	}
	}
}
public void back(View v){
	finish();
}
public void submit(View v){
	ArrayList<String> uri = new ArrayList<String>();
	for(LocalFile file :checkedItems){
		
		uri.add(file.getOriginalUri());
	}
	Intent intent = getIntent();
	intent.putStringArrayListExtra("uri", uri);
	setResult(1, intent);
	finish();
}
}
