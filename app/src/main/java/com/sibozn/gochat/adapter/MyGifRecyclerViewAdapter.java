package com.sibozn.gochat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sibozn.gochat.R;
import com.sibozn.gochat.bean.GifsBean;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Random;

/**
 * Created by wjk on 2016/8/5.
 */
public class MyGifRecyclerViewAdapter extends RecyclerView.Adapter<MyGifRecyclerViewAdapter.MyHolder> {

    private static final String TAG = "MyGifRecyclerViewAdapter";
    private List<GifsBean> gifsBeens;
    private Context mContext;
    private MyItemOnClickListener myItemOnClickListener;
    private Random random;
    private int[] gif_bg = {R.drawable.rectangle_blue, R.drawable.rectangle_green, R.drawable.rectangle_pink};

    public void setItemOnClickListener(MyItemOnClickListener myItemOnClickListener) {
        this.myItemOnClickListener = myItemOnClickListener;
    }

    public MyGifRecyclerViewAdapter(List<GifsBean> gifsBeens, Context mContext) {
        this.gifsBeens = gifsBeens;
        this.mContext = mContext;
        random = new Random();
    }

    @Override
    public MyGifRecyclerViewAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_gifs_image_view, parent, false);
        return new MyHolder(view);
    }


    @Override
    public void onBindViewHolder(final MyGifRecyclerViewAdapter.MyHolder holder, final int position) {
        final GifsBean gifsBean = gifsBeens.get(position);
        ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
        layoutParams.height = Integer.parseInt(gifsBean.getFixed_height_height());
        holder.imageView.setLayoutParams(layoutParams);
        holder.imageView.setFocusable(false);
        WeakReference<ImageView> imageViewWeakReference = new WeakReference<ImageView>(holder.imageView);
        ImageView target = imageViewWeakReference.get();
        if (target != null) {
            Glide.with(holder.imageView.getContext())
                    .load(gifsBean.getFixed_height_url())
                    .asGif()
                    .placeholder(gif_bg[random.nextInt(3)])
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate()
                    .crossFade()
                    .override(800, 800)
                    //.animate(R.anim.glide_anim)
                    // .error(R.mipmap.ic_launcher)
                    // .skipMemoryCache(true)
                    // .centerCrop()
                    //.fitCenter()
                    // .thumbnail(0.1f)
                    .into(holder.imageView);

        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GifsBean gifsBean = gifsBeens.get(position);
                Log.e(TAG, "----点击地址---" + gifsBean.getFixed_height_url());
                Log.e(TAG, "-点击地址--gifsBean.getFixed_height_height()-" + gifsBean.getFixed_height_height());
                Log.e(TAG, "-点击地址-gifsBean.getFixed_height_width()-" + gifsBean.getFixed_height_width());
                Log.e(TAG, "-点击地址-gifsBean.getFixed_height_size()-" + gifsBean.getFixed_height_size());
                myItemOnClickListener.onItemClickListenerPositon(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gifsBeens == null ? 0 : gifsBeens.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public MyHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_gif);
        }
    }

    public interface MyItemOnClickListener {
        void onItemClickListenerPositon(int postion);
    }
}
