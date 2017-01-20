package com.sibozn.gochat.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.List;

/**
 * Created by Administrator on 2016/7/25.
 */
public class TabAdaper extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private List<String> titles;

    public TabAdaper(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        this.titles = titles;
        this.fragments = fragments;

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    // 添加自定义的view到tab
//    public View getTabView(int position) {
//        View view = LayoutInflater.from(context).inflate(R.layout.tab_item, null);
//        TextView tv = (TextView) view.findViewById(R.id.textView);
//        tv.setText(tabTitles[position]);
//        ImageView img = (ImageView) view.findViewById(R.id.imageView);
//        img.setImageResource(imageResId[position]);
//        return view;
//    }

}
