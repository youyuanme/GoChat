package com.sibozn.gochat.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sibozn.gochat.R;
import com.sibozn.gochat.bean.HistoryInfoBean;

import java.util.List;

/**
 * Created by Administrator on 2016/8/16.
 */
public class HistoryLivstViewAdapter extends BaseAdapter {

    private static final String TAG = "HistoryLivstViewAdapter";
    private Context mContext;
    private List<HistoryInfoBean> datas;
    private SharedPreferences sp;
    private String mEmail;

    public HistoryLivstViewAdapter(Context mContext, List<HistoryInfoBean> datas) {
        this.mContext = mContext;
        this.datas = datas;
        this.sp = mContext.getSharedPreferences("user_info",Context.MODE_PRIVATE);
        mEmail = sp.getString("email","");
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_history_view, null);
            viewHolder.iv_his = (ImageView) convertView.findViewById(R.id.iv_his);
            viewHolder.tv_sex = (TextView) convertView.findViewById(R.id.tv_sex);
            viewHolder.tv_his_age = (TextView) convertView.findViewById(R.id.tv_his_age);
            viewHolder.tv_sex = (TextView) convertView.findViewById(R.id.tv_sex);
            viewHolder.tv_his_uid = (TextView) convertView.findViewById(R.id.tv_his_uid);
            viewHolder.tv_his_data = (TextView) convertView.findViewById(R.id.tv_his_data);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        HistoryInfoBean historyInfoBean = datas.get(position);
        Glide.with(mContext)
                .load(historyInfoBean.getPhoto())
                .centerCrop()
                // .transform(new GlideCircleTransform(mContext))
                .placeholder(R.mipmap.ic_launcher)
                .crossFade()
                .into(viewHolder.iv_his);
        viewHolder.tv_his_age.setText(historyInfoBean.getAge());
        viewHolder.tv_sex.setText(historyInfoBean.getSex());
        viewHolder.tv_his_uid.setText(historyInfoBean.getUid());
        viewHolder.tv_his_data.setText(sp.getString(historyInfoBean.getFrom(),""));
        return convertView;
    }

    class ViewHolder {
        private ImageView iv_his;
        private TextView tv_his_uid, tv_his_age, tv_sex, tv_his_data;
    }
}
