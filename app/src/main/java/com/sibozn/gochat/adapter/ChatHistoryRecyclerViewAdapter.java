package com.sibozn.gochat.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sibozn.gochat.R;
import com.sibozn.gochat.bean.HistoryInfoBean;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/8/5.
 */
public class ChatHistoryRecyclerViewAdapter extends SwipeMenuAdapter<ChatHistoryRecyclerViewAdapter.MyHolder> {

    private static final String TAG = "ChatHistoryRecyclerViewAdapter";
    private List<HistoryInfoBean> datas;
    private Context mContext;
    private SharedPreferences sp;
    private RecyclerItemLongClickListener recyclerItemLongClickListener;
    private RecyclerItemClickListener mOnItemClickListener;


    public ChatHistoryRecyclerViewAdapter(List<HistoryInfoBean> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
        this.sp = mContext.getSharedPreferences("user_info", Context.MODE_PRIVATE);
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
        datas.remove(position);
        notifyItemRemoved(position);
    }

    // 新增数据
    public void addData(int position, HistoryInfoBean historyInfoBean) {
        datas.add(position, historyInfoBean);
        notifyItemInserted(position);
    }

    // 更改某个位置的数据
    public void changeData(int position, HistoryInfoBean historyInfoBean) {
        datas.set(position, historyInfoBean);
        notifyItemChanged(position);
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_history_view, parent, false);
    }


    @Override
    public MyHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new MyHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(ChatHistoryRecyclerViewAdapter.MyHolder holder, final int position) {
        HistoryInfoBean historyInfoBean = datas.get(position);
        holder.tv_his_uid.setText(historyInfoBean.getUid().trim());
        holder.tv_his_age.setText(historyInfoBean.getAge().trim());
        holder.tv_sex.setText(historyInfoBean.getSex().trim());
        holder.tv_his_data.setText(sp.getString(historyInfoBean.getFrom(), ""));
        Glide.with(holder.iv_his.getContext())
                .load(historyInfoBean.getPhoto())
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .crossFade()
                .dontAnimate()
                .into(holder.iv_his);
        holder.setOnItemClickListener(mOnItemClickListener);
        //holder.setRecyclerItemLongClickListener(recyclerItemLongClickListener);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView iv_his;
        private TextView tv_his_uid, tv_his_age, tv_sex, tv_his_data;

        private RecyclerItemClickListener mOnItemClickListener;
        private RecyclerItemLongClickListener recyclerItemLongClickListener;

        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //itemView.setOnLongClickListener(this);
            iv_his = (ImageView) itemView.findViewById(R.id.iv_his);
            tv_his_uid = (TextView) itemView.findViewById(R.id.tv_his_uid);
            tv_his_age = (TextView) itemView.findViewById(R.id.tv_his_age);
            tv_sex = (TextView) itemView.findViewById(R.id.tv_sex);
            tv_his_data = (TextView) itemView.findViewById(R.id.tv_his_data);
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
