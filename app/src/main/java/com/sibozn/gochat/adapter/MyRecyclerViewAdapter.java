package com.sibozn.gochat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sibozn.gochat.R;
import com.sibozn.gochat.bean.UserInfoBean;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/8/5.
 */
public class MyRecyclerViewAdapter extends SwipeMenuAdapter<MyRecyclerViewAdapter.MyHolder> {

    private static final String TAG = "MyRecyclerViewAdapter";
    private List<UserInfoBean> userInfoList;
    private Context mContext;
    private RecyclerItemLongClickListener recyclerItemLongClickListener;
    private RecyclerItemClickListener mOnItemClickListener;


    public MyRecyclerViewAdapter(List<UserInfoBean> userInfoList, Context mContext) {
        this.userInfoList = userInfoList;
        this.mContext = mContext;
    }

    // item设置点击事件
    public void setOnItemClickListener(RecyclerItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    // item设置长按事件
    public void setRecyclerItemLongClickListener(RecyclerItemLongClickListener recyclerItemLongClickListener) {
        this.recyclerItemLongClickListener = recyclerItemLongClickListener;
    }

    // 移除数据
    public void removeData(int position) {
        userInfoList.remove(position);
        notifyItemRemoved(position);
    }

    // 新增数据
    public void addData(int position, UserInfoBean userInfoBean) {
        userInfoList.add(position, userInfoBean);
        notifyItemInserted(position);
    }

    // 更改某个位置的数据
    public void changeData(int position, UserInfoBean userInfoBean) {
        userInfoList.set(position, userInfoBean);
        notifyItemChanged(position);
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_recycler_view, parent, false);
    }

    @Override
    public MyHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new MyHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(MyRecyclerViewAdapter.MyHolder holder, final int position) {
        holder.mTitleTv.setText(userInfoList.get(position).getUid().trim());
        holder.tv_data.setText(userInfoList.get(position).getData().trim());
        Glide.with(holder.iv_header.getContext())
                .load(userInfoList.get(position).getPhoto())
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                // .transform(new GlideCircleTransform(mContext))
                .crossFade()
                .into(holder.iv_header);
        holder.setOnItemClickListener(mOnItemClickListener);
        holder.setRecyclerItemLongClickListener(recyclerItemLongClickListener);
    }

    @Override
    public int getItemCount() {
        return userInfoList == null ? 0 : userInfoList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView mTitleTv, tv_data;
        private ImageView iv_header;
        private RecyclerItemClickListener mOnItemClickListener;
        private RecyclerItemLongClickListener recyclerItemLongClickListener;

        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mTitleTv = (TextView) itemView.findViewById(R.id.tv_uid);
            tv_data = (TextView) itemView.findViewById(R.id.tv_data);
            iv_header = (ImageView) itemView.findViewById(R.id.iv_header);
        }

        // 设置每个item点击事件
        public void setOnItemClickListener(RecyclerItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        // 设置每个item长按事件
        public void setRecyclerItemLongClickListener(RecyclerItemLongClickListener recyclerItemLongClickListener) {
            this.recyclerItemLongClickListener = recyclerItemLongClickListener;
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (recyclerItemLongClickListener != null) {
                recyclerItemLongClickListener.onItemLongClick(getAdapterPosition());
            }
            return true;
        }
    }

    public interface RecyclerItemLongClickListener {
        void onItemLongClick(int position);
    }

    public interface RecyclerItemClickListener {
        void onItemClick(int position);
    }

}
